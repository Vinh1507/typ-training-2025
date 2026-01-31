from app.core.database import SessionLocal
from app.models.product import Product
from app.core.redis_client import redis_client

def init_cache():
    db = SessionLocal()
    try:
        products = db.query(Product).all()

        for p in products:
            redis_key = f"product_stock:{p.id}"
            redis_client.set(redis_key, p.total_stock)

        print("ok")

    except Exception as e:
        print("Ko on roi")
    finally:
        db.close()

init_cache()