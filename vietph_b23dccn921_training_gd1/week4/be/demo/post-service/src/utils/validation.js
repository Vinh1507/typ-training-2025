import joi from "joi";
export const validationCreatePost = (data) => {
  const schema = joi.object({
    title: joi.string().required().min(3).max(100).trim(),
    content: joi.string().required().min(1).max(2000).trim(),
    media: joi.string().optional().trim(),
  });
  return schema.validate(data);
};
