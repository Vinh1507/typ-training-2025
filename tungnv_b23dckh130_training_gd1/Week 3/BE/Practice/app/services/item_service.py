from sqlalchemy.orm import Session
from app.models.item_model import Item
from app.schemas.item_dto import ItemRequest

def get_all(db: Session):
    return db.query(Item).all()

def get_by_id(db: Session, item_id: int):
    return db.query(Item).filter(Item.id == item_id).first()

def create(db: Session, item: ItemRequest):
    new_item = Item(
        name=item.name,
        price=item.price,
        description=item.description
    )

    db.add(new_item)
    
    db.commit()
    
    db.refresh(new_item)
    
    return new_item

def update(db: Session, item_id: int, item_data: ItemRequest):
    item = db.query(Item).filter(Item.id == item_id).first()
    
    if not item:
        return None
    
    item.name = item_data.name
    item.price = item_data.price
    item.description = item_data.description
    
    db.commit()
    
    db.refresh(item)
    
    return item

def delete(db: Session, item_id: int):
    item = db.query(Item).filter(Item.id == item_id).first()
    
    if not item:
        return False
    
    db.delete(item)
    db.commit()
    
    return True
