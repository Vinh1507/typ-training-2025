require("dotenv").config();
const { MongoClient } = require("mongodb");
const MONGODB_URI = process.env.MONGODB_URI;


let db;
MongoClient.connect(process.env.MONGODB_URI, {maxPoolSize: 10}).then((client) => {
    db = client.db();
    console.log(`Connected to MongoDB at ${process.env.MONGODB_URI}`);
}).catch((err) => console.error("Failed to connect to MongoDB", err));