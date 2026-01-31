from sqlalchemy import Column, Integer, String, Boolean
from sqlalchemy.orm import relationship

from app.core.database import Base

class Product(Base):
    __tablename__ = "products"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    image = Column(String)
    price = Column(Integer)
    original_price = Column(Integer)
    total_stock = Column(Integer)
    is_hot = Column(Boolean)
    sold = Column(Integer)

    orders = relationship("Order", back_populates="product")