from sqlalchemy import Column, Integer, String, ForeignKey, BigInteger, Boolean, DateTime
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from app.db.base_class import Base

from .tag import post_tags

class Post(Base):
    __tablename__ = "posts"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    title = Column(String(70), nullable=False)
    slug = Column(String, nullable=False)

    content_raw = Column(String, nullable=False)
    content_html = Column(String, nullable=False)

    view_count = Column(BigInteger, default=0)
    is_published = Column(Boolean, default=False)

    created_at = Column(DateTime(timezone=True), server_default=func.now())
    deleted_at = Column(DateTime(timezone=True), nullable=True)
    # Relations

    #User
    owner = relationship("User", back_populates="posts")
    #Tags
    tags = relationship("Tag", secondary=post_tags, back_populates="posts")
    # Series
    series_links = relationship("SeriesPost", back_populates="post")
    # Comment & Vote
    comments = relationship("Comment", back_populates="post")
    votes = relationship("Vote", back_populates="post")