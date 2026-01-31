import json

from fastapi.encoders import jsonable_encoder
from sqlalchemy.orm import Session
from app.models.product import Product
from app.core.redis_client import redis_client


class ProductService:
    @staticmethod
    def get_all_products(db: Session):
        # Lấy toàn bộ danh sách sản phẩm
        redis_key = "all_products"
        cached_data = redis_client.get(redis_key)

        if cached_data:
            return json.loads(cached_data)

        products = db.query(Product).all()

        products_json = jsonable_encoder(products)
        redis_client.set(redis_key, json.dumps(products_json), ex=3600)

        return products

    @staticmethod
    def get_product_by_id(db: Session, product_id: int):
        redis_key = f"product:{product_id}"
        cached_data = redis_client.get(redis_key)

        if cached_data:
            return json.loads(cached_data)

        product = db.query(Product).filter(Product.id == product_id).first()

        if product:
            product_json = jsonable_encoder(product)
            redis_client.set(redis_key, json.dumps(product_json), ex=3600)

        return product