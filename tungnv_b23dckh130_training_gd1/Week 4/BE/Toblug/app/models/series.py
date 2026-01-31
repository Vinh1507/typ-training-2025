from sqlalchemy import Column, Integer, String, Text, ForeignKey
from sqlalchemy.orm import relationship

from app.db.base_class import Base

class Series(Base):
    __tablename__ = "series"
    
    id = Column("series_id", Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    description = Column(Text)

    owner_id = Column("user_id", Integer, ForeignKey("users.id"))
    owner = relationship("User", back_populates="series")
    post_links = relationship("SeriesPost", back_populates="series", order_by="SeriesPost.order")