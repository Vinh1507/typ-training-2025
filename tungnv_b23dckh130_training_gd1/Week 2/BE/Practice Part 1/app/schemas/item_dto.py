from pydantic import BaseModel

class ItemRequest(BaseModel):
    name: str
    price: float
    description: str

class ItemResponse(ItemRequest):
    id: int