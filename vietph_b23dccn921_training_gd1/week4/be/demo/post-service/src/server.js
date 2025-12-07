import express from "express"
import dotenv from "dotenv"
import helmet from "helmet"
import { corsConfig } from "./config/corsConfig.js"
import logger from "./utils/logger.js"
import { errorHandler } from "./middleware/errorHandler.js"
import rateLimit from "express-rate-limit"
import Redis from "ioredis"
import RedisStore from "rate-limit-redis"
import postRoute from "./routes/post.route.js";
import { connectDB } from "./config/connectDb.js"
import { connectToRabbitMQ } from "./utils/rabbitmq.js"
dotenv.config()
const redisClient= new Redis(process.env.REDIS_URL);
const PORT = process.env.PORT || 4003 ;
const app = express()
app.use(express.json())
app.use(helmet())
app.use(corsConfig())

 
//route 
app.use("/api/posts",(req,res,next)=>{
  req.redisClient=redisClient
  next()
},postRoute);

app.use(errorHandler)
await connectToRabbitMQ()
await connectDB()
app.listen(PORT, () => {
    logger.info("Server is rinning!")
  console.log("Server is running on: ", PORT);
});