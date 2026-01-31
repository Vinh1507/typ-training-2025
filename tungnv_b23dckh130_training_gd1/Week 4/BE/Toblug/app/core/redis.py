import redis
import os

REDIS_HOST = os.getenv("REDIS_HOST")
REDIS_PORT = os.getenv("REDIS_PORT")
REDIS_DB = os.getenv("REDIS_DB_BLACKLIST")

pool = redis.ConnectionPool(
    host=REDIS_HOST,
    port=REDIS_PORT,
    db=REDIS_DB,
    decode_responses=True
    # pass
)

def get_redis_client():
    return redis.Redis(connection_pool=pool)

def is_token_blacklisted(token: str) -> bool:
    r = get_redis_client()

    is_blacklisted = r.exists(f"blacklist:{token}")
    return is_blacklisted > 0

def add_token_to_blacklist(token: str, ttl: int):
    r = get_redis_client()

    r.setex(f"blacklist:{token}", ttl, "true")