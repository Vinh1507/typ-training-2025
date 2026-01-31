from fastapi import FastAPI
from app.api import item_router
from app.database.base import Base
from app.database.session import engine

from app.models import item_model

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Items API with SQLAlchemy",
    description="RESTful API quản lý sản phẩm sử dụng FastAPI + SQLAlchemy ORM",
    version="2.0.0"
)

app.include_router(item_router.router, prefix="/items", tags=["Items"])

@app.get("/")
def root():
    return {
        "message": "Welcome to Items API with SQLAlchemy!",
        "docs": "/docs",
        "version": "2.0.0"
    }
