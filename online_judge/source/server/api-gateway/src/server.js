const express = require("express");
const Redis = require("ioredis");
const cors = require("cors");

const app = express();
app.set("trust proxy", 1);
app.use(cors());
app.use(express.json());

const REDIS_URL = process.env.REDIS_URL;
const PROBLEM_SERVICE_URL = process.env.PROBLEM_SERVICE_URL;
const SUBMISSION_SERVICE_URL = process.env.SUBMISSION_SERVICE_URL;
const USER_SERVICE_URL = process.env.USER_SERVICE_URL;
const PORT = process.env.PORT;

const redis = new Redis(REDIS_URL, {
  retryStrategy: (times) => Math.min(times * 50, 2000),
});

redis.on("connect", () => console.log("Redis connected"));
redis.on("error", (err) => console.error("Redis error:", err));

const MAX_LOAD = parseInt(process.env.MAX_LOAD);

app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
  // console.log(`Rate limit: ${process.env.RATE_LIMIT_MAX || 100} req/min`);
  console.log(`Max load: ${MAX_LOAD} concurrent requests`);
});
