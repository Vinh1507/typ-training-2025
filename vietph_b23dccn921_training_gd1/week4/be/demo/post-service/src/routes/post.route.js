import express from "express"
import { authMiddleware } from "../middleware/auth.middleware.js";
import { createPostController, deletePostByIdController, getAllPostsController, getPostByIdController, updatePostByIdController } from "../controllers/post.controller.js";
import { rateLimitForCreatePost } from "../utils/rateLimit.js";
const router = express.Router();

router.use(authMiddleware);
router.post("/",rateLimitForCreatePost,createPostController);
router.get("/",getAllPostsController);
router.get("/:id",getPostByIdController)
router.delete("/:id",deletePostByIdController);
router.put("/:id",updatePostByIdController);
export default router;