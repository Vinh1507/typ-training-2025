from fastapi import APIRouter
from app.schemas.item_dto import ItemRequest, ItemResponse
from app.controllers import item_controller
from typing import List

router = APIRouter()

@router.get("/", response_model=List[ItemResponse])
def get_all_items():
    return item_controller.get_all_item()

@router.get("/{item_id}", response_model=ItemResponse)
def get_item(item_id: int):
    return item_controller.get_item(item_id)

@router.post("/", response_model=ItemResponse, status_code=201)
def create_item(item: ItemRequest):
    return item_controller.create_item(item)

@router.put("/{item_id}", response_model=ItemResponse)
def update_item(item_id: int, item: ItemRequest):
    return item_controller.update_item(item_id, item)

@router.delete("/{item_id}", status_code=204)
def delete_item(item_id: int):
    item_controller.delete_item(item_id)