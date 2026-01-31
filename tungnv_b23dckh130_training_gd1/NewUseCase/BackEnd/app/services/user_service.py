from sqlalchemy.orm import Session
import bcrypt
from app.models.user import User
from app.schemas.user import UserCreate

class UserService:
    @staticmethod
    def get_user_by_username(db: Session, username: str):
        return db.query(User).filter(User.username == username).first()

    @staticmethod
    def create_user(db: Session, user: UserCreate):
        # 1. Mã hóa mật khẩu
        hashed = bcrypt.hashpw(user.password.encode('utf-8'), bcrypt.gensalt())

        # 2. Tạo đối tượng User
        db_user = User(
            username=user.username,
            password=hashed.decode('utf-8'),
            full_name=user.full_name,
            email=user.email
        )

        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        return db_user

    @staticmethod
    def verify_password(plain_password, hashed_password):
        return bcrypt.checkpw(
            plain_password.encode('utf-8'),
            hashed_password.encode('utf-8')
        )