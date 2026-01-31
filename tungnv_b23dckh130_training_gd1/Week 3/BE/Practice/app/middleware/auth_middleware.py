from fastapi import Request, HTTPException, status, Depends
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from typing import Optional
from app.utils.jwt_handler import verify_token
from app.schemas.user_dto import TokenData

security = HTTPBearer()


def get_current_user(
    credentials: HTTPAuthorizationCredentials = Depends(security)
) -> TokenData:
    token = credentials.credentials
    
    token_data = verify_token(token)
    
    if token_data is None:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Token không hợp lệ hoặc đã hết hạn",
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    return token_data


def require_role(required_role: str):
    def check_role(current_user: TokenData = Depends(get_current_user)) -> TokenData:
        if current_user.role != required_role:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail=f"Bạn không có quyền truy cập. Yêu cầu role: {required_role}"
            )
        return current_user
    
    return check_role


def require_roles(allowed_roles: list):
    def check_roles(current_user: TokenData = Depends(get_current_user)) -> TokenData:
        if current_user.role not in allowed_roles:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail=f"Bạn không có quyền truy cập. Yêu cầu role: {', '.join(allowed_roles)}"
            )
        return current_user
    
    return check_roles

require_admin = require_role("admin")
require_user = require_role("user")


class CurrentUser:
    def __call__(self, credentials: HTTPAuthorizationCredentials = Depends(security)) -> TokenData:
        return get_current_user(credentials)

