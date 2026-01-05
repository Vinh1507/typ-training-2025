const redis = require('redis');

const REDIS_URL = process.env.REDIS_URL || 'redis://localhost:6379';

const client = redis.createClient({
  url: REDIS_URL
});

client.on('error', err => console.log('[Redis] Error:', err.message));
client.on('connect', () => console.log('[Redis] Connected'));
client.on('reconnecting', () => console.log('[Redis] Reconnecting...'));

async function connect() {
  try {
    await client.connect();
  } catch (err) {
    console.log('[Redis] Connection failed:', err.message);
    console.log('Make sure Redis is running: docker run -d -p 6379:6379 redis');
  }
}

module.exports = { client, connect };

