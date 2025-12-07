import mongoose from "mongoose";
import logger from "../utils/logger.js";
import dotenv from "dotenv";
dotenv.config();
export const connectDB=async()=>{
    try {
        await mongoose.connect(process.env.DB_URL).then(()=>{
            console.log("Connected to MongoDB");
        }).catch((e)=>{
            console.log("Error connecting to MongoDB", e);
        });
        logger.info("Connected to MongoDB");
    } catch (error) {
        logger.error("Error connecting to MongoDB", error);
        process.exit(1);
    }
}