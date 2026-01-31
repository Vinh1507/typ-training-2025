from sqlalchemy import Column, Integer, Enum, ForeignKey
from sqlalchemy.orm import relationship
from app.db.base_class import Base

from .enums import VoteType

class Vote(Base):
    __tablename__ = "votes"

    user_id = Column(Integer, ForeignKey("users.id"), primary_key=True)
    post_id = Column(Integer, ForeignKey("posts.id"), primary_key=True)

    vote_type = Column(Enum(VoteType, name="vote_type"), nullable=False)

    # Rela
    user = relationship("User", back_populates="votes")
    post = relationship("Post", back_populates="votes")