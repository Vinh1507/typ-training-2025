from app.core.rabbitmq_client import mq_client
from app.core.redis_client import redis_client


class OrderService:
    @staticmethod
    def process_order_by_id(user_id: int, product_id: str):

        bought_set_key = f"bought_users:{product_id}"

        if redis_client.sismember(bought_set_key, user_id):
            return {"status": "fail", "msg": "Bạn đã mua món này rồi"}

        redis_key = f'product_stock:{product_id}'
        stock_left = redis_client.decr(redis_key)

        if stock_left < 0:
            redis_client.incr(redis_key)
            return {"status": "fail", "msg": "Đã hết hàng!"}

        # Xử lý mua hàng
        order_data = {
            "user_id": user_id,
            "product_id": product_id,
            "status": "pending"
        }

        try:
            # Bắn tin nhắn sang RabbitMQ
            mq_client.publish_message(order_data)

            return {
                "status": "success",
                "msg": "Đã ghi nhận đơn!",
                "queue_position": stock_left
            }
        except Exception as e:
            # Nếu RabbitMQ lỗi thì phải cộng lại kho cho Redis (Rollback)
            redis_client.incr(redis_key)
            redis_client.srem(bought_set_key, user_id)
            return {"status": "error", "msg": "Lỗi hệ thống. Vui lòng chờ"}