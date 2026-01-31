from pydantic import BaseModel, EmailStr, Field

class UserBase(BaseModel):
    user_name: str
    email: EmailStr
    bio: str | None = None
    avatar_url: str | None = None

class UserCreate(UserBase):
    password: str = Field(..., min_length=8, description="Mật khẩu phải có ít nhất 8 kí tự")

class UserUpdate(UserBase):
    password: str | None = Field(None, min_length=8, description="Mật khẩu phải có ít nhất 8 kí tự")

class UserResponse(UserBase):
    id: int
    reputation_score: int
    role: str

    class Config:
        from_attributes = True