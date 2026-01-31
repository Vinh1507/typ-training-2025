from fastapi import APIRouter

from app.api.v1.endpoints import login, users, posts
api_router = APIRouter()

api_router.include_router(login.router, tags=["login"])
api_router.include_router(users.router, prefix="/user", tags=["Users"])
api_router.include_router(posts.router, prefix="/posts", tags=["Posts"])