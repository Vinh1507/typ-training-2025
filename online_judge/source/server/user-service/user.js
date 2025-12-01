require("dotenv").config();
const { MongoClient } = require("mongodb");

const REDIS_URL = process.env.REDIS_URL;
const MONGODB_URI = process.env.MONGODB_URI;
const PORT = process.env.PORT;
const SERVICE_ID = process.env.SERVICE_ID;
const JWT_SECRET = process.env.JWT_SECRET;

let db;
MongoClient.connect(MONGODB_URI, { maxPoolSize: 10 }).then((client) => {
    db = client.db();
    console.log(`Connected to MongoDB at ${MONGODB_URI}`);
}).catch((err) => console.error("Failed to connect to MongoDB", err));

