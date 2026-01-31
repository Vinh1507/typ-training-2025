# BÁO CÁO TUẦN 3: AUTHENTICATION & AUTHORIZATION

## MỤC LỤC

1. [Giới thiệu](#1-giới-thiệu)
2. [Lý thuyết](#2-lý-thuyết)
   - 2.1. [Authentication vs Authorization](#21-authentication-vs-authorization)
   - 2.2. [JWT (JSON Web Token)](#22-jwt-json-web-token)
   - 2.3. [Password Hashing](#23-password-hashing)
   - 2.4. [Refresh Token](#24-refresh-token)
   - 2.5. [Role-based Access Control (RBAC)](#25-role-based-access-control-rbac)
   - 2.6. [Middleware Pattern](#26-middleware-pattern)
3. [Thiết kế hệ thống](#3-thiết-kế-hệ-thống)
4. [Triển khai](#4-triển-khai)
5. [Kết quả](#5-kết-quả)
6. [Kết luận](#6-kết-luận)
7. [Tài liệu tham khảo](#7-tài-liệu-tham-khảo)

---

## 1. GIỚI THIỆU

### 1.1. Bối cảnh

Trong phát triển ứng dụng web hiện đại, bảo mật là một yếu tố quan trọng hàng đầu. Việc xác thực người dùng (Authentication) và phân quyền truy cập (Authorization) là hai thành phần cốt lõi để đảm bảo chỉ những người dùng hợp lệ mới có thể truy cập vào các tài nguyên của hệ thống.

### 1.2. Mục tiêu

Project này nhằm xây dựng một hệ thống Authentication và Authorization hoàn chỉnh cho RESTful API sử dụng:
- JWT (JSON Web Token) để xác thực
- bcrypt để hash mật khẩu
- Role-based Access Control để phân quyền
- Refresh Token để quản lý phiên đăng nhập

### 1.3. Phạm vi

- Xây dựng API đăng ký và đăng nhập
- Implement JWT Token (Access Token + Refresh Token)
- Hash mật khẩu an toàn
- Xây dựng middleware xác thực
- Phân quyền theo vai trò (admin/user)
- Bảo vệ các API endpoints

---

## 2. LÝ THUYẾT

### 2.1. Authentication vs Authorization

#### 2.1.1. Authentication (Xác thực)

**Định nghĩa:** Authentication là quá trình xác minh danh tính của người dùng - "Bạn là ai?"

**Cách thức hoạt động:**
1. User cung cấp credentials (username/password)
2. Server kiểm tra credentials với database
3. Nếu đúng, server cấp phát token để xác nhận danh tính
4. User sử dụng token này cho các request tiếp theo

**Ví dụ:** Khi đăng nhập vào Facebook bằng email và mật khẩu, Facebook xác thực bạn là chủ sở hữu của tài khoản đó.

#### 2.1.2. Authorization (Phân quyền)

**Định nghĩa:** Authorization là quá trình xác định quyền truy cập của người dùng - "Bạn được phép làm gì?"

**Cách thức hoạt động:**
1. Sau khi đã xác thực, server kiểm tra quyền của user
2. Dựa trên role/permissions, cho phép hoặc từ chối truy cập
3. User chỉ có thể thực hiện các hành động được phép

**Ví dụ:** Trên Facebook, bạn (user thông thường) chỉ có thể xóa bài viết của mình, còn admin có thể xóa bất kỳ bài viết nào.

#### 2.1.3. So sánh

| Tiêu chí | Authentication | Authorization |
|----------|----------------|---------------|
| Mục đích | Xác minh danh tính | Kiểm tra quyền truy cập |
| Câu hỏi | "Bạn là ai?" | "Bạn được làm gì?" |
| Thời điểm | Trước Authorization | Sau Authentication |
| Thông tin | Username, Password, Token | Role, Permissions |
| Kết quả | Token | Access Granted/Denied |
| HTTP Status | 401 Unauthorized | 403 Forbidden |

---

### 2.2. JWT (JSON Web Token)

#### 2.2.1. Khái niệm

JWT là một chuẩn mở (RFC 7519) định nghĩa cách truyền thông tin an toàn giữa các bên dưới dạng JSON object. Thông tin này có thể được xác thực và tin cậy vì nó được ký số.

#### 2.2.2. Cấu trúc JWT

JWT gồm 3 phần, được phân tách bởi dấu chấm (.):

```
xxxxx.yyyyy.zzzzz
```

**1. Header (Phần đầu):**
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
- `alg`: Thuật toán mã hóa (HS256, RS256, ...)
- `typ`: Loại token (JWT)

**2. Payload (Phần dữ liệu):**
```json
{
  "user_id": 1,
  "username": "admin",
  "role": "admin",
  "exp": 1699531200,
  "type": "access"
}
```
- Chứa thông tin về user (claims)
- `exp`: Thời gian hết hạn (timestamp)
- Có thể thêm custom claims

**3. Signature (Chữ ký):**
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret_key
)
```
- Đảm bảo token không bị thay đổi
- Được tạo bằng header + payload + secret key

#### 2.2.3. Luồng hoạt động JWT

```
1. Login Request
   ┌─────────┐                     ┌─────────┐
   │ Client  │──username/password─▶│ Server  │
   └─────────┘                     └─────────┘

2. Verify & Generate JWT
                                   ┌─────────┐
                                   │ Verify  │
                                   │Password │
                                   └────┬────┘
                                        │
                                        ▼
                                   ┌─────────┐
                                   │Generate │
                                   │  JWT    │
                                   └────┬────┘

3. Return JWT
   ┌─────────┐                     ┌─────────┐
   │ Client  │◀────JWT Token───────│ Server  │
   └─────────┘                     └─────────┘

4. Authenticated Request
   ┌─────────┐                     ┌─────────┐
   │ Client  │──Header: Bearer JWT▶│ Server  │
   └─────────┘                     └─────────┘

5. Verify JWT & Process
                                   ┌─────────┐
                                   │ Verify  │
                                   │  JWT    │
                                   └────┬────┘
                                        │
                                        ▼
                                   ┌─────────┐
                                   │Process  │
                                   │Request  │
                                   └────┬────┘

6. Return Response
   ┌─────────┐                     ┌─────────┐
   │ Client  │◀────Response────────│ Server  │
   └─────────┘                     └─────────┘
```

#### 2.2.4. Ưu điểm của JWT

1. **Stateless:** Server không cần lưu trữ session
2. **Scalable:** Dễ dàng scale horizontal
3. **Cross-domain:** Có thể dùng cho nhiều domain
4. **Mobile-friendly:** Dễ implement trên mobile
5. **Self-contained:** Chứa tất cả thông tin cần thiết

#### 2.2.5. Nhược điểm của JWT

1. **Không thể revoke:** Không thể hủy token trước khi hết hạn
2. **Token size:** Lớn hơn session ID
3. **Security:** Phải bảo vệ secret key cẩn thận

---

### 2.3. Password Hashing

#### 2.3.1. Khái niệm

Password hashing là quá trình biến đổi mật khẩu gốc thành một chuỗi ký tự ngẫu nhiên không thể đảo ngược.

#### 2.3.2. Tại sao phải Hash Password?

**Vấn đề:** Nếu lưu mật khẩu dạng plaintext:
```
username: admin
password: admin123  ← Nguy hiểm!
```

**Hậu quả nếu database bị hack:**
- Hacker biết chính xác mật khẩu
- User dùng password trên nhiều website khác cũng bị
- Mất lòng tin của người dùng

**Giải pháp:** Hash mật khẩu
```
username: admin
password: $2b$12$KIXx6V9dpGx8j.PNxJt.4u...  ← An toàn!
```

#### 2.3.3. bcrypt - Thuật toán Hash

**Đặc điểm:**
1. **One-way hash:** Không thể đảo ngược từ hash về plaintext
2. **Salt:** Thêm chuỗi ngẫu nhiên vào mỗi password
3. **Cost factor:** Tốc độ hash có thể điều chỉnh
4. **Slow by design:** Chậm để chống brute-force

**Cách hoạt động:**

```
1. Khi user đăng ký:
   plaintext: "admin123"
        ↓
   Add salt: "admin123" + "random_salt"
        ↓
   bcrypt hash: "$2b$12$KIXx6V9dpGx8j.PNxJt.4u..."
        ↓
   Save to DB: hashed_password

2. Khi user đăng nhập:
   Input: "admin123"
        ↓
   Get stored hash from DB
        ↓
   bcrypt.verify(input, stored_hash)
        ↓
   Extract salt from hash
        ↓
   Hash input with same salt
        ↓
   Compare: new_hash == stored_hash
        ↓
   Return: True/False
```

**Ví dụ:**

```python
# Hash password
password = "admin123"
hashed = bcrypt.hash(password)
# Result: "$2b$12$KIXx6V9dpGx8j.PNxJt.4uGZvL3FnEqV7j.0xJQ5r9k"

# Verify password
is_valid = bcrypt.verify("admin123", hashed)  # True
is_valid = bcrypt.verify("wrong", hashed)     # False
```

#### 2.3.4. Salt là gì?

**Định nghĩa:** Salt là một chuỗi ngẫu nhiên được thêm vào password trước khi hash.

**Tại sao cần Salt?**

Không có salt:
```
user1: password123 → hash1: 5f4dcc3b5aa765d61d8327deb882cf99
user2: password123 → hash2: 5f4dcc3b5aa765d61d8327deb882cf99
                            ← Giống nhau! Nguy hiểm!
```

Có salt:
```
user1: password123 + salt1 → hash1: a3d9f8e7c6b5a4d3...
user2: password123 + salt2 → hash2: 7k2m9n8p1q5r3t6...
                                    ← Khác nhau! An toàn!
```

**Lợi ích của Salt:**
1. Cùng password nhưng hash khác nhau
2. Chống rainbow table attack
3. Buộc hacker phải brute-force từng account

---

### 2.4. Refresh Token

#### 2.4.1. Vấn đề với Access Token

**Scenario:**
- Access token hết hạn sau 1 giờ
- User phải đăng nhập lại sau mỗi giờ
- Trải nghiệm người dùng kém

**Giải pháp đơn giản (không tốt):**
- Tăng thời gian hết hạn lên vài ngày
- Không an toàn nếu token bị đánh cắp

#### 2.4.2. Refresh Token là gì?

**Định nghĩa:** Refresh token là một loại token đặc biệt được dùng để lấy access token mới mà không cần đăng nhập lại.

**Đặc điểm:**
- Thời gian hết hạn dài (7-30 ngày)
- Được lưu trong database
- Có thể revoke (thu hồi) bất cứ lúc nào
- Chỉ dùng để lấy access token mới

#### 2.4.3. Luồng hoạt động Refresh Token

```
1. Initial Login
   ┌────────┐                        ┌────────┐
   │ Client │──username/password────▶│ Server │
   └────────┘                        └────────┘
                                          │
                                          ▼
                                     ┌─────────────┐
                                     │Generate:    │
                                     │- Access     │
                                     │- Refresh    │
                                     └──────┬──────┘
                                            │
                                            ▼
   ┌────────┐                         ┌────────┐
   │ Client │◀─Access + Refresh───────│ Server │
   │        │  Token                  │        │
   │ Store  │                         │Save    │
   │Tokens  │                         │Refresh │
   └────────┘                         │to DB   │
                                      └────────┘

2. Use Access Token (1 hour)
   ┌────────┐                         ┌────────┐
   │ Client │──Header: Bearer Access─▶│ Server │
   └────────┘                         └────────┘
                                          │
                                          ▼
   ┌────────┐                         ┌────────┐
   │ Client │◀────Response────────────│ Server │
   └────────┘                         └────────┘

3. Access Token Expired
   ┌────────┐                         ┌────────┐
   │ Client │──Header: Bearer Access─▶│ Server │
   └────────┘                         └────────┘
                                          │
                                          ▼
   ┌────────┐                         ┌────────┐
   │ Client │◀─401 Unauthorized───────│ Server │
   └────────┘                         └────────┘

4. Refresh Access Token
   ┌────────┐                        ┌────────┐
   │ Client │──Refresh Token────────▶│ Server │
   └────────┘                        └────────┘
                                          │
                                          ▼
                                     ┌─────────────┐
                                     │Verify       │
                                     │Refresh      │
                                     │in DB        │
                                     └──────┬──────┘
                                            │
                                            ▼
                                     ┌─────────────┐
                                     │Generate:    │
                                     │- New Access │
                                     │- New Refresh│
                                     │             │
                                     │Revoke old   │
                                     │Refresh      │
                                     └──────┬──────┘
                                            │
                                            ▼
   ┌────────┐                         ┌────────┐
   │ Client │◀─New Tokens─────────────│ Server │
   └────────┘                         └────────┘

5. Continue using new Access Token
   Repeat from step 2
```

#### 2.4.4. Tại sao lưu Refresh Token trong Database?

**Lý do:**

1. **Có thể revoke:** Thu hồi token khi cần
   ```
   - User đăng xuất → revoke refresh token
   - Phát hiện tài khoản bị hack → revoke tất cả tokens
   - User đổi password → revoke tất cả tokens
   ```

2. **Tracking:** Theo dõi phiên đăng nhập
   ```
   - Xem user đang đăng nhập ở đâu
   - Xem lịch sử đăng nhập
   - Giới hạn số thiết bị đăng nhập
   ```

3. **Security:** An toàn hơn
   ```
   - Kiểm tra token có bị revoke chưa
   - Kiểm tra token có trong database không
   - Phát hiện token bị đánh cắp
   ```

#### 2.4.5. Token Rotation

**Khái niệm:** Mỗi lần dùng refresh token để lấy access token mới, refresh token cũ sẽ bị vô hiệu hóa và trả về refresh token mới.

**Lợi ích:**
- Giảm thiểu rủi ro nếu refresh token bị đánh cắp
- Hacker chỉ có một cơ hội dùng token
- Phát hiện token bị đánh cắp sớm hơn

**Cách hoạt động:**
```
1. Client dùng Refresh Token A
2. Server tạo mới Access Token + Refresh Token B
3. Server revoke Refresh Token A
4. Client lưu Refresh Token B
5. Lần sau dùng Refresh Token B (không dùng A được nữa)
```

---

### 2.5. Role-based Access Control (RBAC)

#### 2.5.1. Khái niệm

RBAC là phương pháp quản lý quyền truy cập dựa trên vai trò (role) của người dùng trong hệ thống.

#### 2.5.2. Các thành phần

**1. User (Người dùng):**
- Đại diện cho người sử dụng hệ thống
- Mỗi user được gán một hoặc nhiều roles

**2. Role (Vai trò):**
- Tập hợp các permissions
- Ví dụ: admin, user, moderator, guest

**3. Permission (Quyền):**
- Hành động cụ thể trên tài nguyên
- Ví dụ: create_item, read_item, update_item, delete_item

**4. Resource (Tài nguyên):**
- Đối tượng cần được bảo vệ
- Ví dụ: items, users, posts

#### 2.5.3. Mô hình RBAC

```
┌──────────┐         ┌──────────┐         ┌──────────┐
│   User   │────────▶│   Role   │────────▶│Permission│
└──────────┘  has    └──────────┘  has    └──────────┘
                                                │
                                                │ on
                                                ▼
                                          ┌──────────┐
                                          │ Resource │
                                          └──────────┘
```

#### 2.5.5. Ưu điểm của RBAC

1. **Dễ quản lý:** Gán role thay vì từng permission
2. **Scalable:** Dễ thêm role/permission mới
3. **Bảo mật:** Principle of least privilege
4. **Audit:** Dễ theo dõi ai làm gì

---

### 2.6. Middleware Pattern

#### 2.6.1. Khái niệm

Middleware là một lớp trung gian nằm giữa request và response, cho phép xử lý request trước khi đến endpoint và xử lý response trước khi trả về client.

#### 2.6.2. Luồng xử lý với Middleware

```
┌────────┐         ┌────────────┐         ┌────────┐         ┌─────────┐
│ Client │────────▶│ Middleware │────────▶│ Router │────────▶│ Handler │
└────────┘ Request └────────────┘         └────────┘         └─────────┘
                           │                                        │
                           │ Verify JWT                             │
                           │ Check Role                             │
                           │                                        │
                           ▼                                        │
                    ┌────────────┐                                  │
                    │ Continue   │                                  │
                    │ or Reject  │                                  │
                    └────────────┘                                  │
                                                                    │
┌────────┐         ┌────────────┐         ┌────────┐         ┌─────────┐
│ Client │◀────────│ Middleware │◀────────│ Router │◀────────│ Handler │
└────────┘Response └────────────┘         └────────┘         └─────────┘
```

#### 2.6.3. Middleware cho Authentication

**Chức năng:**
1. Lấy token từ header `Authorization: Bearer <token>`
2. Verify token (kiểm tra chữ ký, expiration)
3. Decode token để lấy thông tin user
4. Inject thông tin user vào request
5. Cho phép request tiếp tục hoặc reject với 401

**Code minh họa:**

```python
def get_current_user(credentials: HTTPAuthorizationCredentials):
    # 1. Extract token
    token = credentials.credentials
    
    # 2. Verify & decode
    token_data = verify_token(token)
    
    # 3. Check validity
    if token_data is None:
        raise HTTPException(401, "Invalid token")
    
    # 4. Return user info
    return token_data
```

#### 2.6.4. Middleware cho Authorization

**Chức năng:**
1. Lấy thông tin user từ authentication middleware
2. Kiểm tra role của user
3. So sánh với role yêu cầu
4. Cho phép hoặc từ chối với 403

**Code minh họa:**

```python
def require_admin(current_user: TokenData = Depends(get_current_user)):
    # 1. User đã được authenticate
    # 2. Check role
    if current_user.role != "admin":
        raise HTTPException(403, "Forbidden: Admin only")
    
    # 3. Return user nếu có quyền
    return current_user
```

#### 2.6.5. Dependency Injection trong FastAPI

FastAPI sử dụng Dependency Injection để implement middleware:

```python
# Define dependency
def get_current_user() -> TokenData:
    # ... verify token ...
    return token_data

# Use as dependency
@router.get("/items/")
def get_items(
    current_user: TokenData = Depends(get_current_user)  # ← Inject
):
    # current_user tự động có giá trị
    return items
```

**Ưu điểm:**
- Code sạch, dễ đọc
- Reusable
- Testable
- Type-safe

---

## 3. THIẾT KẾ HỆ THỐNG

### 3.1. Kiến trúc Tổng thể

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                         │
│              (Browser, Mobile App, Postman)                 │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP Request (JSON + JWT)
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                     API Layer (FastAPI)                     │
├─────────────────────────────────────────────────────────────┤
│  Auth Routes         │  Item Routes        │  Other Routes  │
│  /auth/register      │  /items/            │                │
│  /auth/login         │  /items/{id}        │                │
│  /auth/verify        │                     │                │
│  /auth/refresh       │                     │                │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    Middleware Layer                         │
├─────────────────────────────────────────────────────────────┤
│  - JWT Authentication (get_current_user)                    │
│  - Role Authorization (require_admin, require_role)         │
│  - Error Handling                                           │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                   Controller Layer                          │
├─────────────────────────────────────────────────────────────┤
│  - Request Validation                                       │
│  - Response Formatting                                      │
│  - Exception Handling                                       │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                            │
├─────────────────────────────────────────────────────────────┤
│  - Business Logic                                           │
│  - Token Generation/Verification                            │
│  - Password Hashing/Verification                            │
│  - User Authentication                                      │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                    Database Layer                           │
├─────────────────────────────────────────────────────────────┤
│  Users Table        │  RefreshTokens Table  │  Items Table  │
│  - id               │  - id                 │  - id         │
│  - username         │  - user_id            │  - name       │
│  - email            │  - token              │  - price      │
│  - hashed_password  │  - expires_at         │  - desc       │
│  - role             │  - is_revoked         │               │
└─────────────────────────────────────────────────────────────┘
```

### 3.2. Database Schema

```sql
-- Users Table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    role VARCHAR(50) NOT NULL DEFAULT 'user',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Refresh Tokens Table
CREATE TABLE refresh_tokens (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    token VARCHAR(500) UNIQUE NOT NULL,
    expires_at DATETIME NOT NULL,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Items Table (existing)
CREATE TABLE items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    description VARCHAR(500) NOT NULL
);
```

### 3.3. API Endpoints Design

#### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Đăng ký tài khoản | ❌ |
| POST | `/auth/login` | Đăng nhập | ❌ |
| GET | `/auth/verify` | Xác thực token | ❌ |
| POST | `/auth/refresh` | Refresh access token | ❌ |
| GET | `/auth/me` | Thông tin user hiện tại | ✅ |

#### Items Endpoints (Protected)

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| GET | `/items/` | Lấy danh sách items | User/Admin |
| GET | `/items/{id}` | Lấy chi tiết item | User/Admin |
| POST | `/items/` | Tạo item mới | **Admin** |
| PUT | `/items/{id}` | Cập nhật item | **Admin** |
| DELETE | `/items/{id}` | Xóa item | **Admin** |

---

## 4. TRIỂN KHAI

### 4.1. Công nghệ sử dụng

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | FastAPI | Latest |
| ORM | SQLAlchemy | Latest |
| Database | SQLite | 3.x |
| JWT | python-jose | Latest |
| Password Hashing | passlib + bcrypt | Latest |
| Validation | Pydantic | Latest |

### 4.2. Cấu trúc thư mục

```
Practice/
├── app/
│   ├── api/                    # API Routes
│   ├── controllers/            # Controllers
│   ├── services/               # Business Logic
│   ├── models/                 # Database Models
│   ├── schemas/                # Pydantic Schemas
│   ├── middleware/             # Middleware
│   ├── utils/                  # Utilities (JWT, Password)
│   ├── database/               # Database config
│   ├── config.py               # Configuration
│   └── main.py                 # Entry point
├── requirements.txt
└── README.md
```

### 4.3. Các module chính

#### 4.3.1. JWT Handler (`utils/jwt_handler.py`)

**Chức năng:**
- Tạo Access Token
- Tạo Refresh Token
- Verify Token
- Decode Token

**Key Functions:**
```python
def create_access_token(data: Dict) -> str
def create_refresh_token(data: Dict) -> str
def verify_token(token: str) -> TokenData
def verify_refresh_token(token: str) -> TokenData
```

#### 4.3.2. Password Handler (`utils/password_handler.py`)

**Chức năng:**
- Hash password với bcrypt
- Verify password

**Key Functions:**
```python
def hash_password(password: str) -> str
def verify_password(plain: str, hashed: str) -> bool
```

#### 4.3.3. User Service (`services/user_service.py`)

**Chức năng:**
- Đăng ký user
- Xác thực user
- Tạo tokens
- Refresh tokens
- Quản lý refresh tokens

**Key Functions:**
```python
def register_user(db, user_data) -> User
def authenticate_user(db, login_data) -> User
def create_tokens(user) -> Token
def verify_and_refresh_token(db, refresh_token) -> Token
def save_refresh_token(db, user_id, token)
def revoke_refresh_token(db, token) -> bool
```

#### 4.3.4. Auth Middleware (`middleware/auth_middleware.py`)

**Chức năng:**
- Xác thực JWT
- Phân quyền theo role

**Key Functions:**
```python
def get_current_user(credentials) -> TokenData
def require_role(role: str)
def require_roles(roles: List[str])

# Pre-defined
require_admin = require_role("admin")
require_user = require_role("user")
```

---

## 5. KẾT QUẢ

### 5.1. Tính năng đã hoàn thành

**Authentication:**
- Đăng ký tài khoản với validation
- Đăng nhập trả về JWT (Access + Refresh Token)
- Xác thực JWT Token
- Hash mật khẩu với bcrypt

**Authorization:**
- Middleware xác thực tự động
- Phân quyền theo role (admin/user)
- Protected API routes
- HTTP 401/403 error handling

**Token Management:**
- Access Token (1 giờ)
- Refresh Token (7 ngày)
- Token rotation khi refresh
- Lưu refresh token trong database
- Có thể revoke tokens

**Security:**
- Password hashing với bcrypt
- JWT với signature verification
- Token expiration
- Role-based access control
- Secure secret key management

