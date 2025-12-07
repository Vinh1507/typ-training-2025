import rateLimit from "express-rate-limit";
import logger from "./logger.js";

export const rateLimitForCreatePost=rateLimit({
    windowMs:60*1000,
    max:6,
    handler:(req,res)=>{
        logger.warn(`Rate limit exceeded for IP: ${req.ip}`);
        return res.status(429).json({
            success:false,
            message:"You can only create 6 posts per minute. Try again later!"
        })
    }
})