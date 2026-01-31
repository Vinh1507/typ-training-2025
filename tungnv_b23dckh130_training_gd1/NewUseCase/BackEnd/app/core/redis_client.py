import redis

from app.core.config import settings

# Kết nối cổng 6379
redis_client = redis.Redis(
    port=settings.REDIS_PORT,
    host=settings.REDIS_HOST,
    db=0,
    decode_responses=True # để nó trả về string thay vì là bytes
)

def check_redis():
    try:
        redis_client.ping()
    except Exception as e:
        print("Lỗi kết nối redis", e)
        return False