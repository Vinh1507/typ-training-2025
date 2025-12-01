const express = require("express");
const Redis = require("ioredis");
const amqp = require("amqplib");
const { MongoClient } = require("mongodb");

const app = express();
app.use(express.json());

const REDIS_URL = process.env.REDIS_URL;
const RABBITMQ_URL = process.env.RABBITMQ_URL;
const MONGODB_URI = process.env.MONGODB_URI;
const PORT = process.env.PORT;
const QUEUE_NAME = process.env.QUEUE_NAME;
const SERVICE_ID = process.env.HOSTNAME || "submission-" + Math.random().toString(36).substr(2, 9);

const redis = new Redis(REDIS_URL);
redis.on("connect", () => console.log("[Submission Service] Redis connected"));
redis.on("error", (err) => console.error("[Submission Service] Redis error:", err));

let db;
MongoClient.connect(MONGODB_URI, { maxPoolSize: 10 })
  .then((client) => {
      db = client.db();
      console.log("[Submission Service] MongoDB connected");

  })
  .catch((err) => console.error("MongoDB error:", err));

let channel;
async function connectRabbitMQ() {
  try {
    const connection = await amqp.connect(RABBITMQ_URL);
    channel = await connection.createChannel();
    await channel.assertQueue(QUEUE_NAME, {
      durable: true,
    });
    console.log("[Submission Service] RabbitMQ connected");
  } catch (error) {
    console.error("RabbitMQ error:", error);
    setTimeout(connectRabbitMQ, 5000);
  }
}

connectRabbitMQ();

app.listen(PORT, () => {
  console.log(`[${SERVICE_ID}] Submission Service running on port ${PORT}`);
});
