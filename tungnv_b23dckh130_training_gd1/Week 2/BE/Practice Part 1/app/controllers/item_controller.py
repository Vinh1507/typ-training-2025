from fastapi import HTTPException

from app.schemas.item_dto import ItemRequest
from app.services import item_service

def get_all_item():
    return item_service.get_all()

def get_item(item_id: int):
    item = item_service.get_by_id(item_id)
    if not item:
        raise HTTPException(status_code=404, detail="Item not found")
    return item

def create_item(item: ItemRequest):
    return item_service.create(item)

def update_item(item_id: int, item_to_update: ItemRequest):
    updated = item_service.update(item_id, item_to_update)
    if not updated:
        raise HTTPException(status_code=404, detail="Item not found")
    return updated

def delete_item(item_id: int):
    ok = item_service.delete(item_id)
    if not ok:
        raise HTTPException(status_code=404, detail="Item not found")