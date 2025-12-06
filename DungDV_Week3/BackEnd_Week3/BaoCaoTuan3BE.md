# Authentication và JSON Web Token (JWT)

## 1. Tìm hiểu các khái niệm

### Authentication (Xác thực) là gì?

Authentication là quá trình xác minh danh tính của người dùng. Nó trả lời câu hỏi: **"Bạn là ai?"**

**Ví dụ:**
- Đăng nhập bằng username/password
- Xác thực bằng email và mã OTP
- Đăng nhập bằng Google, Facebook

Khi đăng nhập thành công, hệ thống biết được bạn là ai và cho phép bạn truy cập.

### Authorization (Phân quyền) là gì?

Authorization là quá trình xác định quyền truy cập của người dùng. Nó trả lời câu hỏi: **"Bạn được phép làm gì?"**

**Ví dụ:**
- Admin có thể xóa bài viết, người dùng thường thì không
- Chỉ chủ sở hữu mới có thể chỉnh sửa bài viết của mình
- Một số trang chỉ dành cho thành viên VIP

**Sự khác biệt:**
- **Authentication**: Xác định bạn là ai
- **Authorization**: Xác định bạn được làm gì

---

## 2. Phân biệt cơ chế xác thực

Có 2 cách chính để xác thực người dùng:

### 2.1. Session-based Authentication

**Cách hoạt động:**
1. Người dùng đăng nhập với username/password
2. Server tạo một session ID và lưu vào cookie
3. Mỗi request tiếp theo, browser tự động gửi cookie chứa session ID
4. Server kiểm tra session ID để biết người dùng đã đăng nhập

**Ưu điểm:**
- Dễ triển khai
- Server có thể hủy session bất cứ lúc nào
- An toàn vì session ID được lưu trên server

**Nhược điểm:**
- Cần lưu trữ session trên server (tốn bộ nhớ)
- Khó scale khi có nhiều server (cần shared session storage)
- Không phù hợp với mobile app

### 2.2. Token-based Authentication

**Cách hoạt động:**
1. Người dùng đăng nhập với username/password
2. Server tạo một token (thường là JWT) và trả về cho client
3. Client lưu token (localStorage, cookie, hoặc memory)
4. Mỗi request tiếp theo, client gửi token trong header
5. Server kiểm tra token để xác thực

**Ưu điểm:**
- Không cần lưu trữ trên server (stateless)
- Dễ scale (không cần shared storage)
- Phù hợp với mobile app và API
- Có thể chứa thông tin người dùng trong token

**Nhược điểm:**
- Khó hủy token trước khi hết hạn
- Token lớn hơn session ID
- Cần xử lý refresh token để bảo mật tốt hơn

---

## 3. Tìm hiểu JWT (JSON Web Token)

### 3.1. JWT là gì?

JWT (JSON Web Token) là một chuẩn mở để truyền thông tin an toàn giữa các bên dưới dạng JSON object.

**Đặc điểm:**
- Được mã hóa và ký số
- Có thể chứa thông tin về người dùng (claims)
- Không cần lưu trữ trên server (stateless)
- Có thể tự hết hạn (expiration)

### 3.2. Lợi ích của JWT?

**1. Stateless (Không trạng thái)**
- Server không cần lưu session
- Giảm tải cho server
- Dễ scale horizontal

**2. Bảo mật**
- Được ký số bằng secret key hoặc public/private key
- Khó bị giả mạo
- Có thể mã hóa payload

**3. Linh hoạt**
- Có thể chứa thông tin người dùng (user ID, role, permissions)
- Giảm số lần query database
- Phù hợp với microservices

**4. Cross-domain**
- Có thể dùng cho nhiều domain khác nhau
- Phù hợp với Single Sign-On (SSO)

---

## 4. Cấu trúc của JWT

JWT gồm 3 phần, được ngăn cách bởi dấu chấm (`.`):

```
header.payload.signature
```

### 4.1. Header (Phần đầu)

Chứa thông tin về loại token và thuật toán mã hóa:

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

- `alg`: Thuật toán dùng để ký (HS256, RS256, ...)
- `typ`: Loại token (luôn là "JWT")

Sau đó được encode base64.

### 4.2. Payload (Phần thân)

Chứa thông tin về người dùng (claims):

```json
{
  "sub": "1234567890",
  "name": "Dung",
  "email": "dung@example.com",
  "iat": 1516239022,
  "exp": 1516242622
}
```

**Các claim phổ biến:**
- `sub` (subject): ID người dùng
- `name`: Tên người dùng
- `email`: Email
- `iat` (issued at): Thời gian tạo token
- `exp` (expiration): Thời gian hết hạn
- `role`: Vai trò người dùng (admin, user, ...)

Sau đó được encode base64.

### 4.3. Signature (Chữ ký)

Được tạo bằng cách:
1. Lấy header và payload đã encode
2. Kết hợp với secret key
3. Dùng thuật toán (như HS256) để tạo chữ ký

```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

**Ví dụ JWT hoàn chỉnh:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkR1bmciLCJpYXQiOjE1MTYyMzkwMjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### 4.4. Cách sử dụng JWT

**Tạo token:**
```javascript
const jwt = require('jsonwebtoken');

const token = jwt.sign(
  { userId: 123, email: 'dung@example.com' },
  'secret-key',
  { expiresIn: '1h' }
);
```

**Xác thực token:**
```javascript
const decoded = jwt.verify(token, 'secret-key');
console.log(decoded); // { userId: 123, email: 'dung@example.com', iat: ..., exp: ... }
```

**Gửi token trong request:**
```javascript
// Frontend
fetch('/api/protected', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

// Backend (Express)
const token = req.headers.authorization?.split(' ')[1];
const decoded = jwt.verify(token, 'secret-key');
```

---

## Kết luận

- **Authentication** xác định bạn là ai, **Authorization** xác định bạn được làm gì
- **Session-based** lưu trữ trên server, **Token-based** không cần lưu trữ
- **JWT** là cách phổ biến để implement token-based authentication
- JWT gồm 3 phần: header, payload, signature
- JWT giúp xây dựng hệ thống stateless, dễ scale và bảo mật

**Video demo:**https://youtu.be/rU-dOz1VbFY