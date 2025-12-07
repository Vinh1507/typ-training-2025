import cors from "cors"

const whitelist = ["http://localhost:3000", "https://yourdomain.com"];
export const corsConfig=()=>{
    return cors({
        origin: function (origin, callback) {
            if (!origin || whitelist.includes(origin)) {
                callback(null, true);
            } else {
                callback(new Error("Not allowed by CORS"));
            }
        },
        credentials: true,
        methods: ['POST', 'GET', 'DELETE', 'PUT', 'OPTIONS'],
        allowedHeaders: ['Content-Type', 'Authorization'],
        exposedHeaders: ['set-cookie']
    })
}