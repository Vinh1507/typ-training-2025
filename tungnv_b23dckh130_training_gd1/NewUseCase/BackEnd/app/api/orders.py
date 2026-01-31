from fastapi import APIRouter, HTTPException, Depends
from app.schemas.order import BuyRequest
from app.services.order_service import OrderService
from app.core.security import decode_token, oauth2_scheme

router = APIRouter()

@router.post("/buy")
def buy_product(
    request: BuyRequest,
    token: str = Depends(oauth2_scheme),
):
    token = token.replace("Bearer ", "")
    payload = decode_token(token)
    if not payload or "user_id" not in payload:
        raise HTTPException(status_code=401, detail="Người dùng không hợp lệ")

    user_id = payload["user_id"]
    # Gọi Service xử lý toàn bộ logic mua hàng
    return OrderService.process_order_by_id(user_id, request.product_id)