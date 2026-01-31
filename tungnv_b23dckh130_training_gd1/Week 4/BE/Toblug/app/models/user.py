from sqlalchemy import Column, Integer, String, Enum
from sqlalchemy.orm import relationship

from .enums import UserRole
from app.db.base_class import Base

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    user_name = Column(String, unique=True, index=True, nullable=False)
    email = Column(String, unique=True, index=True, nullable=False)
    hashed_password = Column(String, nullable=False)
    avatar_url = Column(String)
    bio = Column(String)
    reputation_score = Column(Integer, default=0, server_default="0", nullable=False)
    role = Column(Enum(UserRole), default=UserRole.USER, nullable=False)

    posts = relationship("Post", back_populates="owner")
    comments = relationship("Comment", back_populates="user")
    votes = relationship("Vote", back_populates="user")
    series = relationship("Series", back_populates="owner")