from fastapi import APIRouter, Depends, HTTPException, status, Body
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from typing import Any
from jose import jwt, JWTError
from datetime import datetime

from app.core import security
from app.api import deps
from app.core.redis import add_token_to_blacklist
from app.models import User
from app.schemas.token import Token

router = APIRouter()

@router.post("/login/access-token", response_model=Token)
def login_access_token(
    db: Session = Depends(deps.get_db),
    form_data: OAuth2PasswordRequestForm = Depends()
) -> Any:
    user = db.query(User).filter(User.user_name == form_data.username).first()

    if not user or not security.verify_password(form_data.password, user.hashed_password):
        raise HTTPException(status_code=400, detail="Sai tài khoản hoặc mật khẩu")

    return {
        "access_token": security.create_access_token(subject=user.id),
        "refresh_token": security.create_refresh_token(subject=user.id),
        "token_type": "bearer"
    }

@router.post("/login/refresh-token", response_model=Token)
def refresh_token(
    db: Session = Depends(deps.get_db),
    refresh_token_to_decode: str = Body(..., embed=True)
):
    try:
        payload = jwt.decode(refresh_token_to_decode, security.SECRET_KEY, algorithms=[security.ALGORITHM])
        token_data = payload.get("sub")
        token_type = payload.get("type")

        if token_type != "refresh":
            raise HTTPException(status_code=400, detail="Token không hợp lệ")

    except JWTError:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Token không hợp lệ"
        )

    user = db.query(User).filter(User.id == int(token_data)).first()
    if not user:
        raise HTTPException(status_code=404, detail="Người dùng không tồn tại")

    return {
        "access_token": security.create_access_token(subject=user.id),
        "refresh_token": security.create_refresh_token(subject=user.id),
        "token_type": "bearer"
    }

@router.post("/logout")
def logout(
    token: str = Depends(deps.oauth2_schemas)
):
    try:
        payload = jwt.decode(token, security.SECRET_KEY, algorithms=[security.ALGORITHM])

        expire_timestamp = payload.get("exp")
        cur_timestamp = datetime.utcnow().timestamp()

        ttl = int(expire_timestamp - cur_timestamp)

        if ttl > 0:
            add_token_to_blacklist(token, ttl)

        return {"message": "Đăng xuất thành công"}

    except JWTError:
        return {"message": "Đăng xuất thành công"}