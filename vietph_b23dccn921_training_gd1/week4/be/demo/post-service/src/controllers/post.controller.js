import postModel from "../models/Post.js";
import logger from "../utils/logger.js";
import { publishPostEvent } from "../utils/rabbitmq.js";
import { validationCreatePost } from "../utils/validation.js";
const invalidatePostCache = async (req, id = "", userId = "") => {
  const cache = await req.redisClient;
  if (id && userId) {
    const cacheKey = `post:id:${id}:user:${userId}`;
    await cache.del(cacheKey);
  }
  const keys = await cache.keys("posts:*");
  if (keys.length > 0) {
    await cache.del(keys);
  }
};
export const createPostController = async (req, res) => {
  logger.info("Create post running!");
  try {
    const userId = req.user?.userId;
    const { title, content, media } = req.body ?? {};
    const { error } = validationCreatePost({ title, content, media });
    if (error) {
      logger.warn("Validation error: ", error.details[0].message);
      return res.status(400).json({
        success: false,
        message: error.details[0].message,
      });
    }
    const post = await postModel.create({ user: userId, title, content, media });
    await publishPostEvent("post.created",{postId:post._id.toString(),userId:userId,content:post.content});
    logger.info(`Post created with id: ${post._id}`);
    await invalidatePostCache(req);
    return res.status(201).json({
      success: true,
      data: post,
      message: "Post created successfully",
    });
  } catch (error) {
    logger.error("Create post error", error);
    return res.status(500).json({
      success: false,
      message: "Internal server error!",
    });
  }
};
// export const getPostsController = async (req, res) => {
//   logger.info("Get posts running!");
//   try {
//     const { userId } = req.user;
//     const posts = await postModel.find({ user: userId });
//     return res.status(200).json({
//       success: true,
//       data: posts,
//     });
//   } catch (error) {
//     logger.error("Get posts error", error);
//     return res.status(500).json({
//       success: false,
//       message: "Internal server error!",
//     });
//   }
// };
export const getPostByIdController = async (req, res) => {
  logger.info("Get post by id running!");
  try {
    const { id } = req.params ?? {};
    const userId = req.user?.userId;
    const cache = await req.redisClient;
    const cacheKey = `post:id:${id}:user:${userId}`;
    if (!id) {
      return res.status(400).json({
        success: false,
        message: "Post id is required!",
      });
    }
    const cachedPost = await cache.get(cacheKey);
    if (cachedPost) {
      return res.status(200).json({
        success: true,
        data: JSON.parse(cachedPost),
        message: "Post fetched from cache",
      });
    }
    const post = await postModel.findOne({ _id: id, user: userId });
    if (!post) {
      return res.status(404).json({
        success: false,
        message: "Post not found!",
      });
    }
    await cache.setex(cacheKey, 120, JSON.stringify(post));
    return res.status(200).json({
      success: true,
      data: post,
    });
  } catch (error) {
    logger.error("Get post by id error", error);
    return res.status(500).json({
      success: false,
      message: "Internal server error!",
    });
  }
};

export const deletePostByIdController = async (req, res) => {
  logger.info("Delete post by id running!");
  try {
    const { userId } = req.user;
    const { id } = req.params ?? {};
    if (!id) {
      return res.status(400).json({
        success: false,
        message: "Post id is required!",
      });
    }
    const post = await postModel.findOneAndDelete({ _id: id, user: userId });
    if (!post) {
      return res.status(404).json({
        success: false,
        message: "Post not found!",
      });
    }
    // publish post deleted event 
    await publishPostEvent('post.deleted',{postId:post._id.toString(),userId:userId,media:post.media});
    await invalidatePostCache(req, post._id, userId);
    return res.status(200).json({
      success: true,
      message: "Post deleted successfully",
    });
  } catch (error) {
    logger.error("Delete post by id error", error);
    return res.status(500).json({
      success: false,
      message: "Internal server error!",
    });
  }
};
export const getAllPostsController = async (req, res) => {
  logger.info("Get all posts running!");
  try {
    const { page = 1, limit = 10 } = req.query ?? {};
    const skip = (page - 1) * limit;
    const cache = await req.redisClient;
    const cacheKey = `posts:page:${page}:limit:${limit}`;
    const cachedPosts = await cache.get(cacheKey);
    if (cachedPosts) {
      return res.status(200).json({
        success: true,
        data: JSON.parse(cachedPosts),
        message: "Posts fetched from cache",
      });
    }
    const posts = await postModel
      .find()
      .limit(limit)
      .skip(skip)
      .sort({ updatedAt: -1 })
     ;
    const totalPosts = await postModel.countDocuments();
    const totalPages = Math.ceil(totalPosts / limit);
    const data = {
      posts,
      totalPosts,
      totalPages,
    };
    await cache.setex(cacheKey, 120, JSON.stringify(data));
    return res.status(200).json({
      success: true,
      data,
    });
  } catch (error) {
    logger.error("Get all posts error", error);
    return res.status(500).json({
      success: false,
      message: "Internal server error!",
    });
  }
};
export const updatePostByIdController = async (req, res) => {
  logger.info("Update post by id running!");
  try {
    const { userId } = req.user;
    const { id } = req.params ?? {};
    const cache = await req.redisClient;
    const cachedKey = `post:id:${id}:user:${userId}`;
    const { title, content, url } = req.body ?? {};
    if (!id) {
      return res.status(400).json({
        success: false,
        message: "Post id is required!",
      });
    }
    const post = await postModel.findOneAndUpdate(
      { _id: id, user: userId },
      { title, content, url, updatedAt: new Date() },
      { new: true }
    );
    if (!post) {
      return res.status(404).json({
        success: false,
        message: "Post not found!",
      });
    }
    await invalidatePostCache(req);
    await cache.setex(cachedKey, 120, JSON.stringify(post));
    return res.status(200).json({
      success: true,
      data: post,
      message: "Post updated successfully",
    });
  } catch (error) {
    logger.error("Update post by id error", error);
    return res.status(500).json({
      success: false,
      message: "Internal server error!",
    });
  }
};
