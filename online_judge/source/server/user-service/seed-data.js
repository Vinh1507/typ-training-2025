const { MongoClient } = require('mongodb');
const bcrypt = require('bcryptjs');
const Redis = require('ioredis');
require("dotenv").config();
const fs = require('fs');
const path = require('path');

const MONGODB_URI = process.env.MONGODB_URI;
const REDIS_URL = process.env.REDIS_URL;
const redis = new Redis(REDIS_URL);

const INSTANCE_ID = process.env.INSTANCE_ID || 'default';
const LOG_FILE = path.join(__dirname, `seed-log-${INSTANCE_ID}.csv`);

const TOTAL_USERS = parseInt(process.env.TOTAL_USERS);
const BATCH_SIZE = parseInt(process.env.BATCH_SIZE);
const BATCH_COUNTER_KEY = process.env.BATCH_COUNTER_KEY;
const PROGRESS_KEY = process.env.PROGRESS_KEY;
const INIT_LOCK_KEY = process.env.INIT_LOCK_KEY;
const LOCK_TTL = parseInt(process.env.LOCK_TTL);

const passwordCache = new Map();

redis.on('connect', () => console.log(`[${INSTANCE_ID}] Connected to Redis at ${REDIS_URL}`));
redis.on('error', (err) => console.error(`[${INSTANCE_ID}] Redis connection error:`, err));

// CSV LOGGING
function initLogFile() {
    if (!fs.existsSync(LOG_FILE)) {
        fs.writeFileSync(LOG_FILE, 'timestamp,userId,username,email,status,message\n');
    }
}

function writeLog(userId, username, email, status, error = '') {
    const timestamp = new Date().toISOString();
    const line = `${timestamp},${userId},${username},${email},${status},"${error.replace(/"/g, '""')}"\n`;
    fs.appendFileSync(LOG_FILE, line, { encoding: 'utf8' });
}

// PASSWORD CACHE
function getPasswordHash(password) {
    if (!passwordCache.has(password)) {
        passwordCache.set(password, bcrypt.hashSync(password, 10));
    }
    return passwordCache.get(password);
}

// USER GENERATOR
function generateUsers(startIndex, count) {
    const users = [];
    const endIndex = Math.min(startIndex + count, TOTAL_USERS);

    for (let i = startIndex; i < endIndex; i++) {
        users.push({
            userId: `user${i}`,
            username: `username${i}`,
            email: `email${i}@example.com`,
            password: getPasswordHash(`password${i}`),
            score: 0,
            solved: 0,
            submission: 0,
            rank: i,
            createdAt: new Date(),
        });
    }

    return users;
}

// INSERT BATCH
async function insertBatch(db, users, stats) {
    try {
        const result = await db.collection('users').insertMany(users, { ordered: false });
        users.forEach(user => writeLog(user.userId, user.username, user.email, 'SUCCESS', ''));
        stats.succeeded += result.insertedCount;
    } catch (err) {
        if (err.code === 11000) {
            const errors = err.writeErrors || [];
            stats.succeeded += users.length - errors.length;
            stats.failed += errors.length;
            errors.forEach(error => {
                const user = users[error.index];
                writeLog(user.userId, user.username, user.email, 'FAILURE', "Duplicate key");
            });
        } else {
            stats.failed += users.length;
            console.error(`[${INSTANCE_ID}] Batch insert error:`, err.message);
        }
    }
}

async function seedDistributed() {
    let client;

    try {
        initLogFile();

        console.log(`[${INSTANCE_ID}] Connecting to MongoDB at ${MONGODB_URI}...`);
        client = await MongoClient.connect(MONGODB_URI);
        const db = client.db("users");

        const initSuccess = await redis.set(INIT_LOCK_KEY, INSTANCE_ID, "NX", "EX", LOCK_TTL);
        if (initSuccess) {
            console.log(`[${INSTANCE_ID}] Initializing database (first instance)...`);
            await db.collection("users").deleteMany({});
            await db.collection("users").createIndex({ userId: 1 }, { unique: true });
            await db.collection("users").createIndex({ username: 1 }, { unique: true });
            await db.collection("users").createIndex({ email: 1 }, { unique: true });
            await db.collection("users").createIndex({ score: -1 });
            await redis.set(INIT_LOCK_KEY, "DONE");
        } else {
            console.log(`[${INSTANCE_ID}] Waiting for initialization to finish...`);
            while ((await redis.get(INIT_LOCK_KEY)) !== "DONE") {
                await new Promise(r => setTimeout(r, 1000));
            }
        }

        const stats = { succeeded: 0, failed: 0 };

        while (true) {
            const batchId = await redis.incr(BATCH_COUNTER_KEY);
            const start = (batchId - 1) * BATCH_SIZE;
            const end = start + BATCH_SIZE;

            if (start >= TOTAL_USERS) {
                console.log(`[${INSTANCE_ID}] All batches completed. Exiting...`);
                break;
            }

            console.log(`[${INSTANCE_ID}] Seeding batch ${batchId}: users ${start}–${end - 1}`);

            const users = generateUsers(start, BATCH_SIZE);
            await insertBatch(db, users, stats);

            await redis.set(PROGRESS_KEY, stats.succeeded);
        }

        console.log(`[${INSTANCE_ID}] Done. Succeeded: ${stats.succeeded}, Failed: ${stats.failed}`);

    } catch (err) {
        console.error(`[${INSTANCE_ID}] Fatal error:`, err);
        process.exit(1);
    } finally {
        if (client) await client.close();
        redis.quit();
        console.log(`[${INSTANCE_ID}] Redis connection closed.`);
    }
}

// CLEANUP SIGNALS
process.on('SIGINT', async () => {
    console.log(`\n[${INSTANCE_ID}] Received SIGINT. Cleaning up...`);
    redis.quit();
    process.exit(0);
});

process.on('SIGTERM', async () => {
    console.log(`\n[${INSTANCE_ID}] Received SIGTERM. Cleaning up...`);
    redis.quit();
    process.exit(0);
});

seedDistributed().catch(console.error);
