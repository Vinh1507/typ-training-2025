# -*- coding: utf-8 -*-
from sqlalchemy.orm import Session
from app.core.database import SessionLocal, engine, Base
from app.models.product import Product
from app.models.user import User
from app.schemas.user import UserCreate
from app.services.user_service import UserService

# 1. Reset lại bảng (Xóa đi tạo lại để cập nhật cột is_hot mới thêm)
print("Dang lam moi Database...")
Base.metadata.drop_all(bind=engine)
Base.metadata.create_all(bind=engine)

def init_data():
    db: Session = SessionLocal()
    
    try:
        # --- TẠO USER ADMIN ---
        if not db.query(User).filter(User.username == "admin").first():
            print("Dang tao User admin...")
            admin_user = UserCreate(
                username="admin",
                password="123", # Password này sẽ được mã hóa tự động
                full_name="Admin Dep Trai",
                email="admin@gmail.com"
            )
            UserService.create_user(db, admin_user)

        # --- TẠO SẢN PHẨM MẪU ---
        if db.query(Product).count() == 0:
            print("Dang nhap kho san pham...")
            products = [
                Product(
                    name="iPhone 15 Pro Max 256GB",
                    image="https://apple.ngocnguyen.vn/cdn/images/202409/goods_img/iphone-15-pro-max-chinh-hang--like-new-99-G15597-1726997326183.png",
                    price=29990000,
                    original_price=34990000,
                    total_stock=10,
                    is_hot=True,
                    sold=5
                ),
                Product(
                    name="MacBook Air M2 2023",
                    image="https://bizweb.dktcdn.net/100/071/198/products/a-nh-chu-p-ma-n-hi-nh-2025-09-27-lu-c-21-50-57.png?v=1758984709040",
                    price=26500000,
                    original_price=28900000,
                    total_stock=50,
                    is_hot=False,
                    sold=12
                ),
                Product(
                    name="Samsung Galaxy S24 Ultra",
                    image="https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcSGEJk3XIRn4jrG8VWbPRM5Tndtrs_9yFu_yUFDHfjT8E9hSj9gj6Q5jeCXM2xASSayxafIO_evwViXNNv5XDy-n9aEzGjZ6Xc5lZMSXt5ttsUjN6UzNzBWR6Y",
                    price=23990000,
                    original_price=33990000,
                    total_stock=5,
                    is_hot=True,
                    sold=4
                ),
                Product(
                    name="Tai nghe AirPods Pro 2",
                    image="https://www.techone.vn/wp-content/uploads/2023/11/apple-airpods-pro-2-usb-c_8_.webp",
                    price=5990000,
                    original_price=6990000,
                    total_stock=100,
                    is_hot=False,
                    sold=2
                ),
            ]
            
            db.add_all(products)
            db.commit()
            print("Da them xong 4 san pham mau!")
            
    except Exception as e:
        print(f"Co loi xay ra: {e}")
    finally:
        db.close()

if __name__ == "__main__":
    init_data()