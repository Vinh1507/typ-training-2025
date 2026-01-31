from fastapi import APIRouter, Depends, HTTPException
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
import traceback  # Thư viện để in dấu vết lỗi

# Import chuẩn
from app.core.database import get_db
from app.schemas.user import UserResponse, UserCreate, LoginRequest, Token
from app.services.user_service import UserService
from app.core.security import create_access_token

router = APIRouter()


@router.post("/register", response_model=UserResponse)
def register(user: UserCreate, db: Session = Depends(get_db)):
    try:
        exist_user = UserService.get_user_by_username(db, user.username)
        if exist_user:
            raise HTTPException(status_code=400, detail="Người dùng đã tồn tại")
        return UserService.create_user(db, user)
    except Exception as e:
        traceback.print_exc()
        raise e


@router.post("/login", response_model=Token)
def login(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    try:
        user = UserService.get_user_by_username(db, form_data.username)

        if not user:
            raise HTTPException(status_code=401, detail="Sai tài khoản hoặc mật khẩu")

        is_valid_pass = UserService.verify_password(form_data.password, user.password)

        if not is_valid_pass:
            raise HTTPException(status_code=401, detail="Sai tài khoản hoặc mật khẩu")

        access_token = create_access_token(data={
            "sub": user.username,
            "email": user.email,
            "full_name": user.full_name,
            "user_id": user.id
        })

        return {
            "access_token": access_token,
            "token_type": "bearer"
        }

    except HTTPException as he:
        raise he
    except Exception as e:
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=f"Lỗi Server: {str(e)}")