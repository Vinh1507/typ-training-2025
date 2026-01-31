from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.schemas.user_dto import (
    UserRegister, 
    UserLogin, 
    Token, 
    UserResponse,
    RefreshTokenRequest,
    TokenVerifyResponse
)
from app.services.user_service import UserService
from app.utils.jwt_handler import verify_token


class UserController:
    
    @staticmethod
    def register(db: Session, user_data: UserRegister) -> UserResponse:
        try:
            user = UserService.register_user(db, user_data)
            return UserResponse.model_validate(user)
        
        except ValueError as e:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=str(e)
            )
        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Lỗi khi đăng ký user: {str(e)}"
            )
    
    @staticmethod
    def login(db: Session, login_data: UserLogin) -> Token:
        try:
            user = UserService.authenticate_user(db, login_data)
            
            if not user:
                raise HTTPException(
                    status_code=status.HTTP_401_UNAUTHORIZED,
                    detail="Username hoặc password không đúng",
                    headers={"WWW-Authenticate": "Bearer"}
                )
            
            tokens = UserService.create_tokens(user)
            
            UserService.save_refresh_token(db, user.id, tokens.refresh_token)
            
            return tokens
        
        except HTTPException:
            raise
        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Lỗi khi đăng nhập: {str(e)}"
            )
    
    @staticmethod
    def verify_token_endpoint(token: str) -> TokenVerifyResponse:
        try:
            token_data = verify_token(token)
            
            if token_data:
                return TokenVerifyResponse(
                    valid=True,
                    user_id=token_data.user_id,
                    username=token_data.username,
                    role=token_data.role,
                    message="Token hợp lệ"
                )
            else:
                return TokenVerifyResponse(
                    valid=False,
                    message="Token không hợp lệ hoặc đã hết hạn"
                )
        
        except Exception as e:
            return TokenVerifyResponse(
                valid=False,
                message=f"Lỗi khi xác thực token: {str(e)}"
            )
    
    @staticmethod
    def refresh_access_token(db: Session, refresh_request: RefreshTokenRequest) -> Token:
        try:
            new_tokens = UserService.verify_and_refresh_token(
                db, 
                refresh_request.refresh_token
            )
            
            if not new_tokens:
                raise HTTPException(
                    status_code=status.HTTP_401_UNAUTHORIZED,
                    detail="Refresh token không hợp lệ hoặc đã hết hạn",
                    headers={"WWW-Authenticate": "Bearer"}
                )
            
            return new_tokens
        
        except HTTPException:
            raise
        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Lỗi khi làm mới token: {str(e)}"
            )
    
    @staticmethod
    def get_current_user_info(db: Session, user_id: int) -> UserResponse:
        try:
            user = UserService.get_user_by_id(db, user_id)
            
            if not user:
                raise HTTPException(
                    status_code=status.HTTP_404_NOT_FOUND,
                    detail="Không tìm thấy user"
                )
            
            return UserResponse.model_validate(user)
        
        except HTTPException:
            raise
        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Lỗi khi lấy thông tin user: {str(e)}"
            )

