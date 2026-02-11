const { MongoClient } = require('mongodb');
require("dotenv").config();
const MONGODB_URI = process.env.MONGODB_URI;


const dataProblem = [
    {
        problemId: 1,
        title: "Two Sum",
        difficulty: "Easy",
        description:
            "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice.",
        acceptanceRate: 0,
        tags: ["Array", "Hash Table"],
        examples: [
            { input: "nums = [2,7,11,15], target = 9", output: "[0,1]" },
            { input: "nums = [3,2,4], target = 6", output: "[1,2]" },
        ],
        testCases: [
            {
                input: { nums: [2, 7, 11, 15], target: 9 },
                expected: [0, 1],
            },
            {
                input: { nums: [3, 2, 4], target: 6 },
                expected: [1, 2],
            },
            {
                input: { nums: [3, 3], target: 6 },
                expected: [0, 1],
            },
        ],
        createdAt: new Date(),
    }
];


async function seedDatabase() {
    let client;

    try {

        console.log("Connecting to MongoDB (Problems)...");
        client = await MongoClient.connect(MONGODB_URI);

        const db = client.db("problems");

        await db.collection("problems").deleteMany({});
        await db.collection("problems").createIndex({ problemId: 1 }, { unique : true });
        await db.collection("problems").createIndex({ difficulty: 1 });
        await db.collection("problems").createIndex({ tags: 1 });
        
        await db.collection("problems").insertMany(dataProblem);

    } catch (err) {
        console.error("Error seeding database:", err);
        process.exit(1);
    } finally {
        if (client) await client.close();
        console.log("Database connection closed");
    }

}

seedDatabase().catch(console.error);