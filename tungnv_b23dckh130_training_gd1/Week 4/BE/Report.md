# Tìm hiểu về Message Queue

## 1. Message Queue là gì ?

### Khái niệm

Message Queue là một kiến trúc cung cấp giao tiếp không đồng bộ. Ý nghĩa của `queue` ở đây là một hàng đợi chứa `message` chờ để được xử lý tuần tự theo cơ chế FIFO. Một `message` là một dữ liệu cần vận chuyển giữa người gửi và người nhận. Vậy có thể hiểu đơn giản, message queue giống như một hòm thư email

### Các thành phần của Message Queue

Message rất đơn giản, bao gồm các thành phần như sau:

- `Message`: Thông tin được gửi (Có thể là text, binary, JSON,...)
- `Producer`: Service tạo ra thông tin, đưa thông tin vào message queue
- `Message Queue`: Nơi chứa những message này cho phép producer và consumer có thể trao đổi với nhau
- `Consumer`: Service nhận message từ message queue và xử lý
- Một service có thể vừa là producer và consumer

### Những lý do cần sử dụng Message Queue

#### 1. Tăng tốc độ phản hồi:

- Giúp API trả về kết quả ngay lập tức cho người dùng mà không cần chờ các việc nặng nhọc (gửi mail, xử lý ảnh, xuất báo cáo, ...).

#### 2. Đảm bảo an toàn dữ liệu

- Khi hệ thống đang hoạt động nhưng bỗng nhiên gặp sự cố, message queue giúp đảm bảo dữ liệu được lưu trữ và không bị mất.

#### 3. Dễ dàng mở rộng

- Nếu trong một hệ thống lượng người dùng đột ngột tăng lên, ta có thể tạo ra thêm nhiều consumer để xử lý message.

### Các mô hình Message Queue

#### 1. Mô hình Point-to-Point

- Mỗi message được gửi đến một và chỉ một consumer duy nhất
- Khi consumer nhận được message, nó sẽ xử lý message và không trả lại message cho producer

#### 2. Mô hình Publisher-Subscriber

- Mỗi message được gửi đến tất cả các consumer
- Khi subscriber nhận được message, nó sẽ xử lý message và trả lại message cho publisher

### Các message queue thông dụng

#### 1. RabbitMQ

- RabbitMQ khác với các loại MQ khác ở Exchange type

- Kiến trúc luồng đi của tin nhắn:
    - `Producer`: Gửi tin nhắn đến `Exchange`
    - `Exchange`: Đóng vai trò là người điều phối. Nó xem xét tin nhắn và quyết định đẩy tin nhắn đó vào queue nào
    - `Queue`: Lưu trữ tin nhắn chờ được xử lý
    - `Consumer`: Lấy tin nhắn từ queue và xử lý

#### 2. Kafka

- Kafka được thiết kế để lưu trữ dòng dữ liệu liên tục và cho phép nhiều người đọc lại bất cứ lúc nào 

- Thay vì dùng `queue`, Kafka sử dụng `log`

- Cơ chế như sau:
    - Producer viết tin vào cuối file log (append-only)
    - Tin nhắn đó nằm vĩnh viễn (hoặc tùy theo thời gian cài đặt)
    - `Consumer` muốn đọc thì chỉ cần biết vị trí (offset) mà mình muốn đọc

- Các thành phần chính:
    - `Topic`: Giống như một "folder" chứa dữ liệu. Ví dụ: `website-clicks`, `order-events`, `payment-notifications`, ...
    - `Partition`: Giống như một "file" trong "folder". Ví dụ: `website-clicks-1`, `website-clicks-2`, `order-events-1`, `order-events-2`, `payment-notifications-1`, `payment-notifications-2`, ...
    - `Offset`: Giống như một "vị trí" trong "file". Ví dụ: 1, 2, 3, ...
    - `Consumer`: Lấy tin nhắn từ topic và partition và xử lý

#### 3. Redis

- Redis là một In-Memory Database (Cơ sở dữ liệu trên RAM). Vì lưu trữ trên RAM nên tốc độ nó là nhanh nhất trong cả 3 cái tên.

- Bởi vì tốc độ quá nhanh và hỗ trợ các cấu trúc dữ liệu danh sách, người ta tận dụng nó làm message queue cho các hệ thống có tốc độ tức thì nhưng không quá khắt khe về độ tin cậy

##### Mô hình dữ liệu

A. Redis List (Point-to-Point):

- Cách hoạt động: Dùng cấu trúc dữ liệu "Danh sách liên kết"
    - `Producer` dùng lệnh `LPUSH` (đẩy vào trái)
    - `Consumer` dùng lệnh `RPOP` (lấy ra từ phải)
- Đặc điểm: Giống hệt một hàng đợi đơn giảnm ai lấy trước thì được, lấy xong thì mất khỏi hàng đợi
- Ứng dụng: Làm hàng đợi cho các job đơn giản

B. Redis Pub/Sub (Publisher-Subscriber):

- Cách hoạt động: Dùng như một đài phát thanh
    - `Publisher` gửi vào `Channel`
    - Tất cả `Subcriber` đang lắng nghe `Channel` đó sẽ nhận được tin nhắn

- Đặc điểm: Fire and Forget
    - Nếu lúc `Publisher` gửi tin nhắn mà `Subscriber` không lắng nghe, thì tin nhắn sẽ bị mất
- Ứng dụng: Chat real-time, Notification, ...

C. Redis Stream:

- Nó khắc phục điểm yếu của Pub/Sub

- Cách hoạt động: Nó học hỏi kiến trúc của Kafka. Lưu lại tin nhắn và hỗ trợ `Consumer Group`

- Đặc điểm: Cho phép `Consumer` xác nhận đã gửi xong. Nếu crash có thể đọc lại

## Implement Message Queue

### Vấn đề cần giải quyết

Trong dự án Toblug, có các tác vụ tốn thời gian như:
- **Gửi email chào mừng** khi người dùng đăng ký (3-10 giây)

Nếu xử lý **đồng bộ**, API sẽ:
- Phản hồi chậm (user phải đợi 5-10s)
- Tăng load server
- Trải nghiệm người dùng kém

### Giải pháp: Message Queue

Sử dụng **Message Queue** với **Celery + Redis** để:
- Xử lý **bất đồng bộ**
- API phản hồi nhanh (< 100ms)
- Tách biệt xử lý nặng ra worker riêng
- Retry tự động khi có lỗi

### Kiến trúc Message Queue

FastAPI App (User đăng ký)
->
Redis (Message Broker)
->
Celery Worker (Consumer)
->
Gmail SMTP

### Luồng hoạt động

1. **Client** → Gọi API `/api/v1/user/` để đăng ký
2. **FastAPI** → Tạo user trong DB
3. **FastAPI** → Đẩy task `send_email_welcome` vào Redis queue
4. **FastAPI** → Trả về response ngay lập tức
5. **Celery Worker** → Lấy task từ queue
6. **Celery Worker** → Xử lý gửi email qua SMTP
7. **Celery Worker** → Lưu kết quả vào Redis

**Timeline:**
- Bước 1-4: **< 100ms** (user nhận response)
- Bước 5-7: **2-5s** (chạy background, user không cần đợi)

### Các thành phần chính

**File**: `app/core/celery_app.py`

```python
from celery import Celery
import os
from dotenv import load_dotenv

load_dotenv()

REDIS_URL = os.getenv("REDIS_CELERY_URL")

celery_app = Celery(
    "worker",
    broker=REDIS_URL,
    backend=REDIS_URL
)

celery_app.conf.update(
    task_serializer="json",
    accept_content=["json"],
    result_serializer="json",
    timezone="Asia/Ho_Chi_Minh",
    enable_utc=True,
)

celery_app.conf.imports = [
    "app.tasks.email_tasks"
]
```

### Task: Gửi Email

**File**: `app/tasks/email_tasks.py`

```python
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from app.core.celery_app import celery_app
from app.core.config import settings

@celery_app.task(
    name="send_email_welcome",
    bind=True,
    max_retries=3,
    default_retry_delay=30
)
def send_email_welcome(self, email_to: str, username: str):
    """
    Task gửi email chào mừng khi user đăng ký
    """
    
    html_content = f"""
    <html>
        <body style="font-family: Arial, sans-serif;">
            <div style="background-color: #f4f4f4; padding: 20px;">
                <h2>Chào mừng {username} đến với Toblug!</h2>
                <p>Cảm ơn bạn đã đăng ký tài khoản.</p>
            </div>
        </body>
    </html>
    """

    message = MIMEMultipart("alternative")
    message["Subject"] = "Chào mừng đến với Toblug"
    message["From"] = f"{settings.MAIL_FROM_NAME} <{settings.MAIL_FROM}>"
    message["To"] = email_to
    message.attach(MIMEText(html_content, "html"))

    try:
        with smtplib.SMTP(settings.MAIL_SERVER, 587, timeout=30) as server:
            server.ehlo()
            server.starttls()
            server.ehlo()
            server.login(settings.MAIL_USERNAME, settings.MAIL_PASSWORD)
            server.send_message(message)
        
        return f"Gửi mail thành công tới {email_to}"
        
    except smtplib.SMTPException as e:
        try:
            raise self.retry(exc=e, countdown=30)
        except self.MaxRetriesExceededError:
            return f"Lỗi sau {self.max_retries} lần thử: {e}"
```

### Producer (FastAPI Endpoint)

**File**: `app/api/v1/endpoints/users.py`

```python
from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from app.api import deps
from app.crud import user as crud_user
from app.schemas.user import UserCreate, UserResponse
from app.tasks.email_tasks import send_email_welcome

router = APIRouter()

@router.post("/", response_model=UserResponse)
def create_user(
    user_in: UserCreate,
    db: Session = Depends(deps.get_db)
):
    user = crud_user.get_user_by_email(db, email=user_in.email)
    if user:
        raise HTTPException(status_code=400, detail="Email đã được đăng ký.")

    user = crud_user.get_user_by_username(db, username=user_in.user_name)
    if user:
        raise HTTPException(status_code=400, detail="Tên người dùng đã tồn tại.")

    user = crud_user.create_user(db, user=user_in)
    
    send_email_welcome.delay(email_to=user.email, username=user.user_name)
    
    return user
```

## Các vấn đề quan trọng khi sử dụng Message Queue

### 1. Idempotency

#### Khái niệm

**Idempotent** nghĩa là: Thực hiện một thao tác **nhiều lần** cho kết quả **giống hệt như thực hiện 1 lần**.

**Công thức:**
```
f(x) = f(f(x)) = f(f(f(x)))
```

#### Tại sao cần Idempotent trong Message Queue ?

**Vấn đề:** Message có thể được xử lý **nhiều lần** do:
- Worker crash giữa chừng -> Task retry
- Network timeout -> Task được gửi lại
- Acknowledgment bị mất -> Broker gửi lại message

**Hậu quả nếu không Idempotent:**

#### Ví dụ 1: Gửi email không Idempotent

**Kịch bản:**
1. Task gửi email thành công
2. **Nhưng worker crash trước khi ACK**
3. Broker tưởng task chưa xử lý → **Gửi lại**
4. Worker mới nhận task → **Gửi email lần 2**
5. **User nhận 2 email giống nhau**

#### Giải pháp: Làm Idempotent

**Cách 1: Lưu trạng thái đã gửi**

```python
import redis
from app.core.celery_app import celery_app

redis_client = redis.Redis(host='localhost', port=6379, db=2)

@celery_app.task(bind=True)
def send_email_welcome(self, email_to: str, username: str):
    # Tạo unique key cho email này
    task_key = f"email_sent:{self.request.id}"
    
    # Kiểm tra đã gửi chưa
    if redis_client.exists(task_key):
        return f"Email đã được gửi trước đó (task_id: {self.request.id})"
    
    # Gửi email
    smtp.send_email(
        to=email_to,
        subject="Chào mừng đến Toblug",
        body=f"Xin chào {username}"
    )
    redis_client.setex(task_key, 7 * 24 * 3600, "sent")
    
    return f"Gửi mail thành công tới {email_to}"
```

**Cách 2: Sử dụng database flag**

```python
from sqlalchemy.orm import Session
from app.models import User

@celery_app.task
def send_email_welcome(user_id: int):
    db = Session()
    user = db.query(User).filter(User.id == user_id).first()
    
    # Kiểm tra đã gửi email chưa
    if user.welcome_email_sent:
        return f"Email đã được gửi cho user {user_id}"
    
    # Gửi email
    smtp.send_email(to=user.email, ...)
    
    # Cập nhật flag
    user.welcome_email_sent = True
    db.commit()
    
    return f"Gửi email thành công cho user {user_id}"
```

#### Ví dụ 2: Trừ tiền không Idempotent

```python
@celery_app.task
def process_payment(user_id: int, amount: float):
    db = Session()
    user = db.query(User).filter(User.id == user_id).first()
    
    # Trừ tiền (không idempotent)
    user.balance -= amount  # Nếu retry → trừ 2 lần!
    db.commit()
    
    return f"Đã trừ {amount} từ user {user_id}"
```

**Hậu quả:**
- Lần 1: Balance = 100 - 50 = **50**
- Lần 2 (retry): Balance = 50 - 50 = **0**

#### Giải pháp: Sử dụng Transaction ID

```python
@celery_app.task(bind=True)
def process_payment(self, user_id: int, amount: float, transaction_id: str):
    # Idempotent: Dùng transaction_id để tránh trùng lặp
    db = Session()
    
    # Kiểm tra transaction đã xử lý chưa
    existing_tx = db.query(Transaction).filter(
        Transaction.id == transaction_id
    ).first()
    
    if existing_tx:
        return f"Transaction {transaction_id} đã được xử lý"
    
    # Xử lý payment
    user = db.query(User).filter(User.id == user_id).first()
    user.balance -= amount
    
    # Lưu transaction
    tx = Transaction(
        id=transaction_id,
        user_id=user_id,
        amount=amount,
        status="completed"
    )
    db.add(tx)
    db.commit()
    
    return f"Transaction {transaction_id} thành công"
```
