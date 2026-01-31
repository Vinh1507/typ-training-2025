from pydantic import BaseModel
from typing import Optional

class ProductBase(BaseModel):
    name: str
    image: Optional[str] = None
    price: int
    original_price: int
    total_stock: int
    is_hot: bool = False
    sold: int

class ProductResponse(ProductBase):
    id: int

    class Config:
        from_attributes = True