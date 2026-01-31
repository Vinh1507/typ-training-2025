from pydantic import BaseModel, EmailStr, Field
from typing import Optional
from datetime import datetime

# Schema cho đăng ký
class UserRegister(BaseModel):
    username: str = Field(..., min_length=3, max_length=50, description="Tên đăng nhập")
    email: EmailStr = Field(..., description="Email")
    password: str = Field(..., min_length=6, description="Mật khẩu (tối thiểu 6 ký tự)")
    full_name: Optional[str] = Field(None, max_length=255, description="Họ và tên")
    role: Optional[str] = Field("user", description="Vai trò (user/admin)")
    
    class Config:
        json_schema_extra = {
            "example": {
                "username": "john_doe",
                "email": "john@example.com",
                "password": "password123",
                "full_name": "John Doe",
                "role": "user"
            }
        }


# Schema cho đăng nhập
class UserLogin(BaseModel):
    username: str = Field(..., description="Tên đăng nhập")
    password: str = Field(..., description="Mật khẩu")
    
    class Config:
        json_schema_extra = {
            "example": {
                "username": "john_doe",
                "password": "password123"
            }
        }


# Schema cho response User (không bao gồm password)
class UserResponse(BaseModel):
    id: int
    username: str
    email: str
    full_name: Optional[str]
    role: str
    is_active: bool
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


# Schema cho Token response
class Token(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str = "bearer"
    expires_in: int  # seconds
    
    class Config:
        json_schema_extra = {
            "example": {
                "access_token": "eyJhbGciOiJIUzI1NiIs...",
                "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
                "token_type": "bearer",
                "expires_in": 3600
            }
        }


# Schema cho Token Data (payload trong JWT)
class TokenData(BaseModel):
    user_id: Optional[int] = None
    username: Optional[str] = None
    role: Optional[str] = None


# Schema cho Refresh Token request
class RefreshTokenRequest(BaseModel):
    refresh_token: str = Field(..., description="Refresh token")
    
    class Config:
        json_schema_extra = {
            "example": {
                "refresh_token": "eyJhbGciOiJIUzI1NiIs..."
            }
        }


# Schema cho response khi verify token
class TokenVerifyResponse(BaseModel):
    valid: bool
    user_id: Optional[int] = None
    username: Optional[str] = None
    role: Optional[str] = None
    message: Optional[str] = None

