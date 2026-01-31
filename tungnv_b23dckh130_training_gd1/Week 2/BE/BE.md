# Tìm hiểu Framework và ORM
## Phần 1: Framework & xây dựng RESTful API
### 1. Lựa chọn Framework
- Em chọn Framework FastAPI.
### 2. Tạo Project & cấu trúc
#### Khởi tạo
    - Chạy dev server:
    ```Bash
    # Cách 1:
    uvicorn app.main:app --reload
    # Cách 2:
    fastapi dev app/main.py
    ```
#### Cấu trúc thư mục
```pgsql
fastapi-app/
├─ app/
│  ├─ main.py
│  ├─ core/
│  │  └─ config.py
│  ├─ api/
│  │  ├─ deps.py
│  │  └─ routers/     
│  ├─ controllers/
│  ├─ models/
│  ├─ schemas/
│  ├─ repositories/
│  ├─ services/
│  ├─ db/
│  │  ├─ base.py
│  │  └─ session.py
│  ├─ migrations/
│  │  ├─ env.py
│  └─ common/
│     └─ exceptions.py
├─ alembic.ini
├─ requirements.txt
└─ .env
```
##### Cấu trúc bao gồm:
- `fastapi-app/`:
  + `requirements.txt`: Danh sách thư viện cần thiết để chạy project
  + `.env`: File chứa các biến môi trường bảo mật: DB_URL, SECRET_KEY,...
  + `alembic.ini`: File cấu hình của Alembic. Đây là một migration tool cho SQLAlchemy.
- `app/`:
  + `main.py`: File main để khởi động cho ứng dụng
  + `core`: Chứa file `config.py` để chứa các cấu hình thường dùng
  + `api`:
    + `deps.py`: Chứa các **Dependency Injection**
    + `routers`: Mỗi file định nghĩa một endpoint theo entity
  + `controllers`: Chứa logic điều phối giữa router và service
  + `models/`: Chứa SQLAlchemy model dùng để định nghĩa cấu trúc bảng trong DB
  + `schemas`: Chứa pydantic models cho request/response validation
  + `repositories/`: Chứa các hàm thao tác trực tiếp với DB
  + `db`:
    + `base.py`: Chứa các base class cho các SQLAlchemy models
    + `session.py`: Tạo **SessionLocal** để thao tác với DB
  + `migrations`: Quản lý thay đổi cấu trúc DB với Alembic
  + `common`: Định nghĩa các lỗi tùy chỉnh

### 3. RESTful API & CRUD Operations
#### Giới thiệu
Practice Part 1 xây dựng một ứng dụng CRUD đơn giản để quản lý sản phẩm sử dụng FastAPI.

#### Cấu trúc
Ứng dụng được tổ chức theo mô hình 3 tầng:
1. **Router**: Tiếp nhận HTTP requests, định nghĩa endpoints
2. **Controller**: Xử lý logic nghiệp vụ, validation, error handling
3. **Service**: Thao tác trực tiếp với dữ liệu

#### Các thành phần chính

##### 1. Models (`item_model.py`)
- Định nghĩa cấu trúc dữ liệu của Item sử dụng Pydantic BaseModel
- Các trường: `id`, `name`, `price`, `description`
- Model này dùng để đại diện cho dữ liệu trong database

##### 2. Schemas/DTOs (`item_dto.py`)
- **ItemRequest**: Schema cho dữ liệu đầu vào
- **ItemResponse**: Schema cho dữ liệu đầu ra

##### 3. Database (`db.py`)
- `items_db = []`: Lưu trữ tạm thời các items trong bộ nhớ

##### 4. Service (`item_service.py`)
Chứa các hàm thao tác CRUD cơ bản:
- `get_all()`: Lấy tất cả items
- `get_by_id(item_id)`: Tìm item theo id
- `create(item)`: Tạo item mới với id tự động tăng
- `update(item_id, item_data)`: Cập nhật item theo id
- `delete(item_id)`: Xóa item theo id

##### 5. Controller (`item_controller.py`)
- Gọi các hàm từ service
- Xử lý exception và trả về HTTPException khi cần:
  - Status 404: Không tìm thấy item

##### 6. Router (`item_router.py`)
Định nghĩa các RESTful API endpoints:
- `GET /items/`: Lấy danh sách tất cả items
- `GET /items/{item_id}`: Lấy chi tiết một item
- `POST /items/`: Tạo item mới
- `PUT /items/{item_id}`: Cập nhật toàn bộ thông tin item
- `DELETE /items/{item_id}`: Xóa item

##### 7. Main (`main.py`)
- Khởi tạo FastAPI app
- Register router với prefix `/items` và tag `Items`
- Entry point của ứng dụng

#### Dependencies cần thiết
```txt
fastapi
uvicorn
```

#### Link demo: https://youtu.be/onw6k5eyzTk

## Phần 2: Tích hợp Database (ORM)

### 1. Khái niệm ORM

**ORM** viết tắt của **Object-Relational Mapping** (Ánh xạ Quan hệ Đối tượng).

- Khi làm việc với database, ta thường phải viết các câu lệnh SQL như `SELECT`, `INSERT`, `UPDATE`, `DELETE`...
- Nhưng với ORM, thay vì viết SQL, ta có thể làm việc với database bằng cách sử dụng các **class** và **object**.

**Ví dụ:**

Không dùng ORM (viết SQL thuần):
```python
# Phải viết câu SQL thủ công
cursor.execute("SELECT * FROM users WHERE id = 1")
user = cursor.fetchone()
```

Có dùng ORM (như SQLAlchemy):
```python
# Làm việc với object như Python bình thường
user = session.query(User).filter(User.id == 1).first()
```

### 2. Lợi ích của ORM

Qua quá trình tìm hiểu, em thấy ORM có những lợi ích sau:

#### 2.1. Dễ học và dễ sử dụng hơn
- Không cần phải học nhiều về SQL, chỉ cần biết Python
- Code dễ đọc, dễ hiểu hơn vì gần với ngôn ngữ lập trình hàng ngày
- Ví dụ: `user.name` thay vì phải nhớ tên cột trong database

#### 2.2. Giảm thiểu lỗi
- ORM tự động xử lý các vấn đề về bảo mật như **SQL Injection**
- Khi viết SQL thủ công, dễ quên validate dữ liệu đầu vào
- ORM đã có sẵn các cơ chế bảo vệ

#### 2.3. Tiết kiệm thời gian
- Không cần viết nhiều câu SQL lặp đi lặp lại
- ORM tự động generate các câu SQL phức tạp
- Ví dụ: Join nhiều bảng, ORM sẽ tự xử lý

#### 2.4. Dễ chuyển đổi database
- Nếu muốn đổi từ MySQL sang PostgreSQL chẳng hạn

#### 2.5. Làm việc theo hướng đối tượng (OOP)
- Một bảng trong database = một class Python
- Một dòng dữ liệu = một object
- Dễ áp dụng các nguyên tắc OOP: kế thừa, đóng gói...

#### 2.6. Tự động quản lý quan hệ giữa các bảng
- Dễ dàng làm việc với relationship: one-to-many, many-to-many
- Ví dụ: Một User có nhiều Posts, chỉ cần `user.posts` là lấy được tất cả bài viết

### 3. Tìm hiểu về SQLAlchemy

#### 3.1. SQLAlchemy là gì?

**SQLAlchemy** là một thư viện ORM phổ biến nhất cho Python. Em chọn SQLAlchemy vì:
- Được sử dụng rộng rãi trong cộng đồng Python
- Tài liệu đầy đủ, dễ tìm kiếm khi gặp lỗi
- Tích hợp tốt với FastAPI
- Hỗ trợ nhiều loại database: PostgreSQL, MySQL, SQLite, Oracle...

#### 3.2. Các thành phần chính của SQLAlchemy

SQLAlchemy có 2 phần chính:

##### a) SQLAlchemy Core
- Là phần cấp thấp, gần với SQL hơn
- Dùng để viết các câu query phức tạp
- Em chưa sử dụng nhiều phần này

##### b) SQLAlchemy ORM
- Là phần cấp cao, làm việc với các class và object
- Đây là phần em sử dụng chính
- Bao gồm các thành phần:

**1. Engine (Bộ máy)**
- Là cầu nối giữa Python và database
- Quản lý connection đến database

```python
from sqlalchemy import create_engine

# Tạo engine kết nối đến SQLite
engine = create_engine('sqlite:///./test.db')

# Hoặc PostgreSQL
engine = create_engine('postgresql://user:password@localhost/dbname')
```

**2. Session (Phiên làm việc)**
- Là "công cụ" để thực hiện các thao tác với database
- Mọi thao tác CRUD đều thông qua session

```python
from sqlalchemy.orm import sessionmaker

SessionLocal = sessionmaker(bind=engine)
session = SessionLocal()
```

**3. Base (Lớp cơ sở)**
- Là class cha cho tất cả các model
- Dùng `declarative_base()` để tạo

```python
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()
```

**4. Model (Mô hình dữ liệu)**
- Là các class đại diện cho bảng trong database
- Kế thừa từ Base

```python
from sqlalchemy import Column, Integer, String

class User(Base):
    __tablename__ = "users"  # Tên bảng trong DB
    
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, nullable=False)
    email = Column(String, unique=True, index=True)
```

#### 3.3. Quy trình làm việc với SQLAlchemy

Khi làm việc với SQLAlchemy, em thường làm theo các bước sau:

**Bước 1: Cài đặt**
```bash
pip install sqlalchemy
```

**Bước 2: Tạo Engine và Session**
```python
# file: db/session.py
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

DATABASE_URL = "sqlite:///./app.db"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(bind=engine)
```

**Bước 3: Tạo Base**
```python
# file: db/base.py
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()
```

**Bước 4: Định nghĩa Models**
```python
# file: models/user.py
from sqlalchemy import Column, Integer, String
from db.base import Base

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True)
    username = Column(String, unique=True)
    email = Column(String, unique=True)
```

**Bước 5: Tạo bảng trong database**
```python
# file: main.py
from db.base import Base
from db.session import engine

# Tạo tất cả các bảng
Base.metadata.create_all(bind=engine)
```

**Bước 6: Thực hiện CRUD operations**
```python
from db.session import SessionLocal
from models.user import User

# Mở session
session = SessionLocal()

new_user = User(username="tungdz", email="vinhtungnguyen2005@gmail.com")
session.add(new_user)
session.commit()

users = session.query(User).all()  # Lấy tất cả
user = session.query(User).filter(User.id == 1).first()  # Lấy 1 user

user.email = "newemail@example.com"
session.commit()

session.delete(user)
session.commit()

session.close()
```

#### 3.4. Một số kiểu dữ liệu thường dùng

```python
from sqlalchemy import Column, Integer, String, Boolean, DateTime, Float, Text

class Product(Base):
    __tablename__ = "products"
    
    id = Column(Integer, primary_key=True)           # Số nguyên
    name = Column(String(100), nullable=False)       # Chuỗi, tối đa 100 ký tự
    description = Column(Text)                       # Văn bản dài
    price = Column(Float)                            # Số thực
    in_stock = Column(Boolean, default=True)         # True/False
    created_at = Column(DateTime)                    # Ngày giờ
```

#### 3.5. Quan hệ giữa các bảng

SQLAlchemy giúp định nghĩa mối quan hệ giữa các bảng dễ dàng:

**One-to-Many (Một-nhiều):**
```python
from sqlalchemy import ForeignKey
from sqlalchemy.orm import relationship

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    name = Column(String)
    
    # Một user có nhiều posts
    posts = relationship("Post", back_populates="author")

class Post(Base):
    __tablename__ = "posts"
    id = Column(Integer, primary_key=True)
    title = Column(String)
    user_id = Column(Integer, ForeignKey("users.id"))
    
    # Một post thuộc về một user
    author = relationship("User", back_populates="posts")
```

**Sử dụng:**
```python
# Lấy tất cả bài viết của một user
user = session.query(User).first()
user_posts = user.posts
```

#### 3.6. Kết hợp SQLAlchemy với FastAPI

Trong FastAPI, em sử dụng **Dependency Injection** để quản lý session:

```python
from db.session import SessionLocal

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
```

```python
from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from api.deps import get_db

router = APIRouter()

@router.get("/users/")
def get_users(db: Session = Depends(get_db)):
    users = db.query(User).all()
    return users

@router.post("/users/")
def create_user(user_data: UserCreate, db: Session = Depends(get_db)):
    new_user = User(**user_data.dict())
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return new_user
```
### Báo cáo thực hành

- Link demo: https://youtu.be/lseGb97-H1Q