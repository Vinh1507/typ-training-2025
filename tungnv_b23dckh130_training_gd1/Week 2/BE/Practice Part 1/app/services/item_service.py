from app.models.item_model import ItemModel
from app.schemas.item_dto import ItemRequest
from app.database.db import items_db

def get_all():
    return items_db

def get_by_id(item_id: int):
    return next((item for item in items_db if item.id == item_id), None)

def create(item: ItemRequest):
    new_id = max([i.id for i in items_db], default=0) + 1
    new_item = ItemModel(id=new_id, **item.dict())
    items_db.append(new_item)
    return new_item

def update(item_id: int, item_data: ItemRequest):
    for idx, item in enumerate(items_db):
        if item.id == item_id:
            updated = ItemModel(id=item_id, **item_data.dict())
            items_db[idx] = updated
            return updated
    return None

def delete(item_id: int):
    for idx, item in enumerate(items_db):
        if item.id == item_id:
            items_db.pop(idx)
            return True
    return False