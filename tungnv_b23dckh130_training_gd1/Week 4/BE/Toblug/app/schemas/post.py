from pydantic import BaseModel, Field, field_validator
from datetime import datetime
from app.schemas.user import UserResponse

class PostBase(BaseModel):
    title: str
    content_raw: str
    is_published: bool = True
    descriptions: str | None = None

class PostCreate(PostBase):
    title: str = Field(..., min_length=1, max_length=100, title="Tiêu đề bài viết.")
    content_raw: str = Field(..., min_length=1, title="Nội dung bài viết.")

    @field_validator('title', 'content_raw')
    @classmethod
    def check_not_empty(cls, v: str):
        if not v.strip():
            raise ValueError("Không được để trống hoặc chỉ chứa khoảng trống")
        return v

class PostUpdate(PostBase):
    title: str = Field(None, min_length=1, max_length=100, title="Tiêu đề bài viết.")
    content_raw: str = Field(None, min_length=1, title="Nội dung bài viết.")

    @field_validator('title', 'content_raw')
    @classmethod
    def check_not_empty(cls, v: str):
        if not v.strip():
            raise ValueError("Không được để trống hoặc chỉ chứa khoảng trống")
        return v

class PostResponse(PostBase):
    id: int
    slug: str
    content_html: str
    view_count: int
    created_at: datetime
    deleted_at: datetime | None = None

    owner: UserResponse

    class Config:
        from_attributes = True