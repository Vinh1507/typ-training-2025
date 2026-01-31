
import os

SECRET_KEY = os.getenv("SECRET_KEY", "iuaegfg897gy3948gbiu3gb")
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = int(os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES", "60"))
REFRESH_TOKEN_EXPIRE_DAYS = int(os.getenv("REFRESH_TOKEN_EXPIRE_DAYS", "7"))

DATABASE_URL = os.getenv("DATABASE_URL", "sqlite:///./test.db")

APP_NAME = "Items API with Authentication & Authorization"
APP_VERSION = "3.0.0"
APP_DESCRIPTION = "RESTful API quản lý sản phẩm với xác thực JWT và phân quyền"

