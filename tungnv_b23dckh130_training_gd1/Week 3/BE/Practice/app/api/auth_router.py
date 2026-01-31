from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from app.database.deps import get_db
from app.schemas.user_dto import (
    UserRegister, 
    UserLogin, 
    Token, 
    UserResponse,
    RefreshTokenRequest,
    TokenVerifyResponse
)
from app.controllers.user_controller import UserController

router = APIRouter()


@router.post("/register", response_model=UserResponse, status_code=201)
def register(
    user_data: UserRegister,
    db: Session = Depends(get_db)
):
    return UserController.register(db, user_data)


@router.post("/login", response_model=Token)
def login(
    login_data: UserLogin,
    db: Session = Depends(get_db)
):
    return UserController.login(db, login_data)


@router.get("/verify", response_model=TokenVerifyResponse)
def verify_token(
    token: str = Query(..., description="JWT Token cần xác thực")
):
    return UserController.verify_token_endpoint(token)


@router.post("/refresh", response_model=Token)
def refresh_token(
    refresh_request: RefreshTokenRequest,
    db: Session = Depends(get_db)
):
    return UserController.refresh_access_token(db, refresh_request)


@router.get("/me", response_model=UserResponse)
def get_current_user(
    db: Session = Depends(get_db),
    user_id: int = Query(..., description="User ID từ token")
):
    return UserController.get_current_user_info(db, user_id)

