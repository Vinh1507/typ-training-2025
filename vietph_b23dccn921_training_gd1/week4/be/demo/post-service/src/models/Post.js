import mongoose from "mongoose"
const PostSchema = new mongoose.Schema({
    user:{
        type:mongoose.Schema.Types.ObjectId,
        ref:'users',
        required:true
    },
    title:{
        type:String,
        required:true
    },
    content:{
        type:String,
        required:true
    },
    media:{
        type:String,
        default:''
    },
    createdAt:{
        type:Date,
        default:Date.now()
    }
    ,
    updatedAt:{
        type:Date,
        default:Date.now()
    }
},{timestamps:true})
PostSchema.index({content:'text'})
const postModel = mongoose.models.posts || mongoose.model("posts", PostSchema);
export default postModel;