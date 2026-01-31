import json
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status, Request
from sqlalchemy.orm import Session
from fastapi.encoders import jsonable_encoder

from app.schemas.post import PostCreate, PostResponse
from app.api import deps
from app.models.user import User
from app.crud import post as crud_post
from app.core.redis import get_redis_client

router = APIRouter()

@router.post("/", response_model=PostResponse, status_code=status.HTTP_201_CREATED)
def create_post(
    post_in: PostCreate,
    db: Session = Depends(deps.get_db),
    cur_user: User = Depends(deps.get_current_user)
):
    return crud_post.create_post(db=db, post_in=post_in, user_id=cur_user.id)

@router.get("/", response_model=List[PostResponse])
def read_posts(
    skip: int = 0,
    limit: int = 10,
    db: Session = Depends(deps.get_db)
):
    posts = crud_post.get_multi(db=db, skip=skip, limit=limit)
    return posts

@router.get("/{slug}", response_model=PostResponse)
def read_post_by_slug(
    slug: str,
    request: Request, # dùng để lấy IP người dùng
    db: Session = Depends(deps.get_db)
):
    r = get_redis_client()
    cache_key = f"post:{slug}"

    # Caching
    cached_data = r.get(cache_key)
    if cached_data:
        return json.loads(cached_data)

    post = crud_post.get_by_slug(db=db, slug=slug)

    if not post:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Bài viết không tồn tại")

    client_ip = request.client.host if request.client else "unknown"

    view_key = f"post:{post.id}:unique_views"

    is_new_view = r.sadd(view_key, client_ip)

    if is_new_view:
        crud_post.update_view_count(db, post.id)
        post.view_count += 1
        r.expire(view_key, 86400)

    post_pydantic = PostResponse.model_validate(post)
    post_data = jsonable_encoder(post_pydantic)
    r.set(cache_key, json.dumps(post_data), ex=300)
    return post