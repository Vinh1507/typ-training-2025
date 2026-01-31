from fastapi import FastAPI
from app.api import item_router

app = FastAPI()

app.include_router(item_router.router, prefix="/items", tags=["Items"])