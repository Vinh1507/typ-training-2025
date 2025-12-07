# Tuần 3: Authentication & Authorization

## Authentication (Xác thực)

### Định nghĩa:

- Authentication là quá trình xác minh danh tính của người dùng — kiểm tra xem bạn là ai.

→ Nói cách khác: “Bạn có thực sự là người mà bạn khai báo không?”

### Ví dụ:

- Khi bạn đăng nhập bằng tên đăng nhập và mật khẩu → hệ thống kiểm tra thông tin đó trong cơ sở dữ liệu.

* Nếu đúng → bạn được xác thực (authenticated).

* Nếu sai → bị từ chối.

### Các cách xác thực phổ biến:

* Mật khẩu (password-based)
* OTP / mã xác minh qua SMS / email
* Xác thực hai lớp (2FA)
* OAuth (đăng nhập qua Google, Facebook, v.v.)
* JWT (JSON Web Token)

## Authorization (Phân quyền)
- Authorization là quá trình phân quyền truy cập, xác định xem người dùng được phép làm gì sau khi đã xác thực.
→ Xác định quyền truy cập vào các tài nguyên hoặc hành động nhất định.
###  Ví dụ
- Người dùng thường chỉ được **xem** thông tin của mình.
- Quản trị viên (Admin) có thể **thêm, sửa, xóa** thông tin của người khác.
- Nếu người dùng thường cố truy cập trang quản trị → hệ thống trả về lỗi **403 Forbidden**.

### Cơ chế phân quyền phổ biến
- **Role-Based Access Control (RBAC)** – phân quyền theo vai trò
- **Policy-Based Access Control (PBAC)** – phân quyền theo chính sách
- **Attribute-Based Access Control (ABAC)** – phân quyền theo thuộc tính

# Cơ chế xác thực: Session-based vs Token-based

## 1. Session-based Authentication

###  Khái niệm
- Là cơ chế xác thực truyền thống, thường được dùng trong các ứng dụng web dựa trên server (server-rendered).
Khi người dùng đăng nhập:
1. Server **xác thực thông tin** (username, password).
2. Server **tạo một Session (phiên làm việc)** và lưu thông tin đăng nhập (ID người dùng, vai trò, thời gian, …) trong bộ nhớ hoặc cơ sở dữ liệu.
3. Server gửi lại cho trình duyệt một **Session ID** (mã định danh phiên) và lưu nó trong **cookie**.
4. Mỗi lần người dùng gửi request tiếp theo, cookie sẽ tự động được gửi kèm → server tra lại Session tương ứng → xác định người dùng.

### Ví dụ
Client -- (username/password)-> Server
Server -> tạo session + trả về sessionID (cookie)
Client --(cookie: sessionID)--> Server --> xác thực từ session
### Ưu điểm
- Dễ triển khai.
- Phù hợp với ứng dụng web truyền thống .
- Có thể dễ dàng hủy phiên từ phía server (logout an toàn).
### Nhược điểm
- Server phải lưu trữ session, gây tốn bộ nhớ khi có nhiều người dùng.
- Khó mở rộng (scaling) khi có nhiều máy chủ — cần chia sẻ session giữa các server.
- Không thuận tiện khi dùng với API RESTful hoặc ứng dụng mobile.

## 2.Token-based Authentication (ví dụ: JWT)

### Khái niệm
Là cơ chế xác thực hiện đại, thường dùng trong RESTful API .
Khi người dùng đăng nhập:
1. Server xác thực thông tin.
2. Server tạo ra một **token** (thường là JWT – JSON Web Token) chứa dữ liệu mã hóa (vd: userID, role, thời hạn,...).
3. Client lưu token này (trong localStorage hoặc cookie).
4. Với mỗi request tiếp theo, client gửi kèm token (thường trong **Authorization header**).
5. Server **xác minh chữ ký token**, nếu hợp lệ thì cho phép truy cập (không cần lưu session ở server).

### Ví dụ

Client-(username/password)-> Server
Server -> tạo JWT (token) -> Client
Client -(Authorization: Bearer token)-> Server -> xác minh token

### Ưu điểm
- Stateless: Server không cần lưu session → dễ mở rộng (scalable).
- Dễ dùng với API RESTful và ứng dụng di động.
- Có thể dùng cùng nhiều dịch vụ khác nhau (microservices).

### Nhược điểm
- Không thể vô hiệu hóa token dễ dàng (trừ khi lưu blacklist).
- Token chứa dữ liệu nên phải bảo mật cẩn thận (chữ ký và mã hóa) không lưu thông tin nhạy cảm của người dùng như mật khẩu.
- Nếu token bị đánh cắp → kẻ xấu có thể dùng đến khi token hết hạn.

# JSON Web Token (JWT)

## JWT là gì?

- JWT (JSON Web Token) là một chuẩn (standard)dùng để truyền thông tin một cách an toàn giữa các bên dưới dạng chuỗi JSON.
-Thông tin trong JWT được ký để đảm bảo tính xác thực và toàn vẹn của dữ liệu.
## 2.Cấu trúc của JWT
Một JWT gồm **3 phần**, được ngăn cách bởi dấu chấm (`.`):
aaaaaa.bbbbbbbb.ccccccccccc
**Header** : Chứa thông tin về thuật toán ký (algorithm) và kiểu token.
**Payload** : Chứa dữ liệu (claims) — ví dụ: user ID, role, thời hạn hết hạn (`exp`).
**Signature** : Chữ ký số được tạo bằng cách mã hóa `Header + Payload` với khóa bí mật (secret key)
### Ví dụ JWT
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJ1c2VySWQiOiIxMjM0NTYiLCJyb2xlIjoiYWRtaW4iLCJleHAiOjE3MjgwNzg0MDB9.
TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ

- Khi **giải mã** JWT, ta nhận được:
{
  "userId": "1",
  "email":"abc@gmail.com",
  "role": "user",
  "exp": 1728078400
}

### Lợi ích của JWT
- Stateless (Không trạng thái):	Server không cần lưu session — chỉ cần xác minh chữ ký token.
- Hiệu quả và mở rộng tốt:	Dễ dàng triển khai trên nhiều server (microservices, load balancing).
- Bảo mật cao:	Dữ liệu được ký số → không thể chỉnh sửa mà không bị phát hiện.
- Tự chứa (Self-contained) :	JWT chứa đủ thông tin người dùng → không cần truy vấn DB mỗi lần.
- Đa nền tảng: Có thể dùng cho Web, Mobile, và API.