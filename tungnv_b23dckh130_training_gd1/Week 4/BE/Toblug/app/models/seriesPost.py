from sqlalchemy import Column, Integer, ForeignKey
from sqlalchemy.orm import relationship

from app.db.base_class import Base

class SeriesPost(Base):
    __tablename__ = "series_posts"

    series_id = Column(Integer, ForeignKey("series.series_id"), primary_key=True)
    post_id = Column(Integer, ForeignKey("posts.id"), primary_key=True)

    order = Column(Integer, default=0, nullable=False)

    series = relationship("Series", back_populates="post_links")
    post = relationship("Post", back_populates="series_links")