from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import text
from app.db.session import SessionLocal
from app.api.v1.api import api_router

app = FastAPI()

app.include_router(api_router, prefix="/api/v1")

# Dependency để lấy DB session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/health/db")
def check_database_connection(db: Session = Depends(get_db)):
    try:
        # Thử chạy lệnh SQL
        db.execute(text("SELECT 1"))
        return {"status": "ok", "message": "Database kết nối ngon lành!"}
    except Exception as e:
        # Nếu lỗi thì báo 500
        raise HTTPException(status_code=500, detail=f"Lỗi kết nối DB: {str(e)}")