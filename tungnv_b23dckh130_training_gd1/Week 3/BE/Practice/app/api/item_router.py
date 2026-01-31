from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from typing import List

from app.schemas.item_dto import ItemRequest, ItemResponse
from app.schemas.user_dto import TokenData
from app.controllers import item_controller
from app.database.deps import get_db
from app.middleware.auth_middleware import get_current_user, require_admin

router = APIRouter()

@router.get("/", response_model=List[ItemResponse])
def get_all_items(
    db: Session = Depends(get_db),
    current_user: TokenData = Depends(get_current_user)
):
    return item_controller.get_all_item(db)

@router.get("/{item_id}", response_model=ItemResponse)
def get_item(
    item_id: int, 
    db: Session = Depends(get_db),
    current_user: TokenData = Depends(get_current_user)
):
    return item_controller.get_item(db, item_id)

@router.post("/", response_model=ItemResponse, status_code=201)
def create_item(
    item: ItemRequest, 
    db: Session = Depends(get_db),
    current_user: TokenData = Depends(require_admin)
):
    return item_controller.create_item(db, item)

@router.put("/{item_id}", response_model=ItemResponse)
def update_item(
    item_id: int, 
    item: ItemRequest, 
    db: Session = Depends(get_db),
    current_user: TokenData = Depends(require_admin)
):
    return item_controller.update_item(db, item_id, item)

@router.delete("/{item_id}", status_code=204)
def delete_item(
    item_id: int, 
    db: Session = Depends(get_db),
    current_user: TokenData = Depends(require_admin)
):
    item_controller.delete_item(db, item_id)
