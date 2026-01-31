from pydantic import BaseModel, Field

class ItemRequest(BaseModel):
    name: str = Field(..., min_length=1, description="Tên sản phẩm")
    price: float = Field(..., gt=0, description="Giá sản phẩm (phải > 0)")
    description: str = Field(..., min_length=1, description="Mô tả sản phẩm")

class ItemResponse(BaseModel):
    id: int
    name: str
    price: float
    description: str
    
    class Config:
        from_attributes = True