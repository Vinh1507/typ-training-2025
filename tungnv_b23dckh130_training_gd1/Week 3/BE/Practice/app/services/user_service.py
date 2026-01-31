from sqlalchemy.orm import Session
from datetime import datetime, timedelta
from typing import Optional
from app.models.user_model import User, RefreshToken
from app.schemas.user_dto import UserRegister, UserLogin, Token
from app.utils.password_handler import hash_password, verify_password
from app.utils.jwt_handler import (
    create_access_token, 
    create_refresh_token,
    verify_refresh_token,
    ACCESS_TOKEN_EXPIRE_MINUTES,
    REFRESH_TOKEN_EXPIRE_DAYS
)


class UserService:
    
    @staticmethod
    def register_user(db: Session, user_data: UserRegister) -> User:
        existing_user = db.query(User).filter(User.username == user_data.username).first()
        if existing_user:
            raise ValueError("Username đã tồn tại")

        existing_email = db.query(User).filter(User.email == user_data.email).first()
        if existing_email:
            raise ValueError("Email đã tồn tại")
        
        hashed_password = hash_password(user_data.password)
        
        new_user = User(
            username=user_data.username,
            email=user_data.email,
            hashed_password=hashed_password,
            full_name=user_data.full_name,
            role=user_data.role if user_data.role else "user"
        )
        
        db.add(new_user)
        db.commit()
        db.refresh(new_user)
        
        return new_user
    
    @staticmethod
    def authenticate_user(db: Session, login_data: UserLogin) -> Optional[User]:
        user = db.query(User).filter(User.username == login_data.username).first()
        
        if not user:
            return None
        
        if not user.is_active:
            return None
        
        if not verify_password(login_data.password, user.hashed_password):
            return None
        
        return user
    
    @staticmethod
    def create_tokens(user: User) -> Token:
        token_data = {
            "user_id": user.id,
            "username": user.username,
            "role": user.role
        }

        access_token = create_access_token(data=token_data)
        refresh_token = create_refresh_token(data=token_data)
        
        return Token(
            access_token=access_token,
            refresh_token=refresh_token,
            token_type="bearer",
            expires_in=ACCESS_TOKEN_EXPIRE_MINUTES * 60
        )
    
    @staticmethod
    def save_refresh_token(db: Session, user_id: int, token: str) -> RefreshToken:
        expires_at = datetime.utcnow() + timedelta(days=REFRESH_TOKEN_EXPIRE_DAYS)
        
        refresh_token = RefreshToken(
            user_id=user_id,
            token=token,
            expires_at=expires_at
        )
        
        db.add(refresh_token)
        db.commit()
        db.refresh(refresh_token)
        
        return refresh_token
    
    @staticmethod
    def get_user_by_id(db: Session, user_id: int) -> Optional[User]:
        return db.query(User).filter(User.id == user_id).first()
    
    @staticmethod
    def verify_and_refresh_token(db: Session, refresh_token_str: str) -> Optional[Token]:
        token_data = verify_refresh_token(refresh_token_str)
        if not token_data:
            return None
        
        db_refresh_token = db.query(RefreshToken).filter(
            RefreshToken.token == refresh_token_str,
            RefreshToken.is_revoked == False
        ).first()
        
        if not db_refresh_token:
            return None

        if db_refresh_token.expires_at < datetime.utcnow():
            return None
        
        user = UserService.get_user_by_id(db, token_data.user_id)
        if not user or not user.is_active:
            return None
        
        new_tokens = UserService.create_tokens(user)
        
        UserService.save_refresh_token(db, user.id, new_tokens.refresh_token)
        
        db_refresh_token.is_revoked = True
        db.commit()
        
        return new_tokens
    
    @staticmethod
    def revoke_refresh_token(db: Session, refresh_token_str: str) -> bool:
        db_refresh_token = db.query(RefreshToken).filter(
            RefreshToken.token == refresh_token_str
        ).first()
        
        if not db_refresh_token:
            return False
        
        db_refresh_token.is_revoked = True
        db.commit()
        
        return True

