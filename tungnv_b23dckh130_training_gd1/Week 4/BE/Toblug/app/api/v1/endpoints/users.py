from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from app.api import deps
from app.crud import user as crud_user
from app.schemas.user import UserCreate, UserResponse
from app.tasks.email_tasks import send_email_welcome

router = APIRouter()

@router.post("/", response_model=UserResponse)
def create_user(
    user_in: UserCreate,
    db: Session = Depends(deps.get_db)
):
    user = crud_user.get_user_by_email(db, email=user_in.email)
    if user:
        raise HTTPException(
            status_code=400,
            detail="Email này đã được đăng ký."
        )

    user = crud_user.get_user_by_username(db, username=user_in.user_name)
    if user:
        raise HTTPException(
            status_code=400,
            detail="Tên người dùng đã tồn tại."
        )

    user = crud_user.create_user(db, user=user_in)
    send_email_welcome.delay(email_to=user.email, username=user.user_name)
    return user