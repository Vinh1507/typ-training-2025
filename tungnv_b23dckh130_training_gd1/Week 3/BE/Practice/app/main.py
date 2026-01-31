from fastapi import FastAPI
from app.api import item_router, auth_router
from app.database.base import Base
from app.database.session import engine

from app.models import item_model, user_model

Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Items API with Authentication & Authorization",
    description="RESTful API quản lý sản phẩm với xác thực JWT và phân quyền",
    version="3.0.0"
)

# Include routers
app.include_router(auth_router.router, prefix="/auth", tags=["Authentication"])
app.include_router(item_router.router, prefix="/items", tags=["Items"])

@app.get("/")
def root():
    return {
        "message": "Welcome to Items API with Authentication & Authorization!",
        "docs": "/docs",
        "version": "3.0.0",
        "features": [
            "JWT Authentication",
            "Password Hashing",
            "Role-based Authorization",
            "Refresh Token",
            "Protected API Routes"
        ]
    }
