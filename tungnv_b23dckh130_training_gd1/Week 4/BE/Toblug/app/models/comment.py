from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.orm import relationship
from app.db.base_class import Base

class Comment(Base):
    __tablename__ = "comments"

    id = Column("comment_id", Integer, primary_key=True, index=True)
    content = Column(String, nullable=False)

    # User
    user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    user = relationship("User", back_populates="comments")

    # Post
    post_id = Column(Integer, ForeignKey("posts.id"), nullable=False)
    post = relationship("Post", back_populates="comments")

    # Self Refer
    parent_id = Column(Integer, ForeignKey("comments.comment_id"), nullable=True)

    replies = relationship("Comment", back_populates="parent", cascade="all, delete-orphan")
    parent = relationship("Comment", back_populates="replies", remote_side=[id])