from fastapi import HTTPException
from sqlalchemy.orm import Session

from app.schemas.item_dto import ItemRequest
from app.services import item_service

def get_all_item(db: Session):
    return item_service.get_all(db)

def get_item(db: Session, item_id: int):
    item = item_service.get_by_id(db, item_id)
    if not item:
        raise HTTPException(status_code=404, detail="Item not found")
    return item

def create_item(db: Session, item: ItemRequest):
    return item_service.create(db, item)

def update_item(db: Session, item_id: int, item_to_update: ItemRequest):
    updated = item_service.update(db, item_id, item_to_update)
    if not updated:
        raise HTTPException(status_code=404, detail="Item not found")
    return updated

def delete_item(db: Session, item_id: int):
    ok = item_service.delete(db, item_id)
    if not ok:
        raise HTTPException(status_code=404, detail="Item not found")
