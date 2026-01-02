# I) Tìm hiểu các khái niệm

## Authentication (Xác thực)

- Là việc kiểm tra “bạn là ai”. Hệ thống yêu cầu bằng chứng (mật khẩu, OTP, khóa công khai, v.v.) và chỉ khi bằng chứng hợp lệ thì mới coi bạn là người dùng X.

## Authorization (Phân quyền)

- Là việc kiểm tra “bạn được phép làm gì” sau khi **đã biết bạn là ai**.

  Ví dụ: user thường không được xóa dữ liệu, admin thì được.

# II) Phân biệt cơ chế xác thực

## 1) Session-based

- Luồng hoạt động:

  1. Client gửi username/password → server kiểm tra.

  2. Server tạo session (bản ghi trong bộ nhớ/Redis/DB) và gửi Session ID (thường là cookie).

  3. Mỗi request sau gửi cookie, server sẽ tra session để biết user là ai.

- Ưu điểm:

  1. Đơn giản, hỗ trợ invalidate dễ (xóa session là xong).

  2. Hạn chế rò rỉ thông tin nhạy cảm sang client (vì dữ liệu nằm ở server).

- Nhược điểm:

  1. Stateful: server phải lưu session → khó scale (cần sticky session/Redis).

  2. Dễ dính CSRF nếu dùng cookie không cẩn thận (cần SameSite/CSRF token).

## 2) Token-based

- Luồng hoạt động:

  1. Client đăng nhập → server phát token (ví dụ JWT).

  2. Client tự lưu token (thường trong memory hoặc cookie an toàn).

  3. Mỗi request gắn Authorization: Bearer <token> → server verify token.

- Ưu điểm:

  1. Stateless, dễ scale (không cần lưu session).

  2. Dễ chia sẻ giữa nhiều dịch vụ (microservices, mobile/web).

- Nhược điểm:

  1. Thu hồi (revoke) khó hơn (vì token tự xác thực): cần TTL ngắn + refresh token + danh sách đen/rotation.

  2. Nếu lưu token không an toàn (localStorage) dễ bị XSS; nếu dùng cookie phải cấu hình SameSite/HttpOnly/Secure.

# III) Tìm hiểu về JWT (JSON WEB TOKEN)

## 1) JWT là gì ?

- JWT là chuỗi token mã hóa dạng base64 gồm 3 phần **header.payload.signature** dùng để tự xác thực: server kiểm chữ ký để tin phần payload (không cần tra DB mỗi lần).

## 2) Lợi ích của JWT ?

=> **Lợi ích chính**:

- **Stateless**: xác thực nhanh, phù hợp microservices/CDN/API.

- **Mang claims**: kèm thông tin user/role/expiry trong payload, tiện authorization.

- **Đa nền tảng**: chuẩn mở (JWS/JWA), dễ dùng trên web, mobile, gateway.

**Lưu ý bảo mật:**

- JWT không mã hóa payload mặc định, không nhét dữ liệu nhạy cảm (mật khẩu, số thẻ…).

- Phải có expiration (exp) ngắn cho access token.

- Dùng thuật toán mạnh:

  - HS256 với secret đủ mạnh
  - RS256/ES256 với khóa bất đối xứng.

- Cần cơ chế refresh token + token rotation để thu hồi hiệu quả.

- Chỉ truyền qua HTTPS.

# IV) Chạy test dự án

## 1) Đăng ký (Register) :

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/register`

### 1.1. Khi đăng ký với role là USER :

<p align="center">
<img src="image/register_user.jpg" width="70%">
</p>

- Trong ảnh trả về status : 201 Create => Tạo thành công

- Trong phần response trả về các trường thông tin : accessToken, refreshToken,tokenType,userId,email,role

- Lưu lại token để test các API khác

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJJZCI6MSwic3ViIjoidXNlcjFAZ21haWwuY29tIiwiaWF0IjoxNzYyNjExNDcwLCJleHAiOjE3NjI2MTUwNzB9.S2KXwPA4VzhpQbNBnEc8CbConb-yI2LdvZbjUzdlukvhREdtaiqFohvg4xwJom9OFiOMLD7NCLgB5CGXGuiATA",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInR5cGUiOiJyZWZyZXNoIiwidXNlcklkIjoxLCJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTE0NzAsImV4cCI6MTc2MjY5Nzg3MH0.F4_sc97KzUSXzXCSg7seZhYMNTqXTEVNNATaW260wqaiTfALLXS2_65lATWR1O-FX63AXFN3i1AGt4BHB08TDQ",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user1@gmail.com",
  "role": "USER"
}
```

### 1.2. Khi đăng ký với role là ADMIN :

<p align="center">
<img src="image/register_admin.jpg" width="70%">
</p>

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjIsInN1YiI6ImFkbWluMUBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTE1NDQsImV4cCI6MTc2MjYxNTE0NH0.jI4j4EhSC7gOY11cA5WZqqqXvatkeancJSboYcT4Upg3gQDvgZrEefdz66KC28Lapd4Z2YD8ZWsZZZRNKaRhag",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJ0eXBlIjoicmVmcmVzaCIsInVzZXJJZCI6Miwic3ViIjoiYWRtaW4xQGdtYWlsLmNvbSIsImlhdCI6MTc2MjYxMTU0NCwiZXhwIjoxNzYyNjk3OTQ0fQ.69Xo5eMuxCDY128PcbVi6Of-TH48mmDRhLNHUDmP1lf-pHu2D38BImc7KOhPFiF3f5rzSfTzi_4GKB77LNkKLQ",
  "tokenType": "Bearer",
  "userId": 2,
  "email": "admin1@gmail.com",
  "role": "ADMIN"
}
```

### 1.3. Khi đăng ký nhưng bỏ trống trường thông tin role, ta sẽ nhận được role mặc định như đã cài đặt default trong code là USER :

<p align="center">
<img src="image/register_miss_role.jpg" width="70%">
</p>

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJJZCI6Mywic3ViIjoidXNlcjJAZ21haWwuY29tIiwiaWF0IjoxNzYyNjExNjE3LCJleHAiOjE3NjI2MTUyMTd9.NTIoF9MOZncXUlgr6lTaepYT319ptwbFA67s3-1cIX7FtE8w4u33nAH5MgvbhjSspNY95KyyjtqItE1rnBFshg",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInR5cGUiOiJyZWZyZXNoIiwidXNlcklkIjozLCJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTE2MTcsImV4cCI6MTc2MjY5ODAxN30.0PGlq-vnM1Y-Ro60LPBbrf0xvPnHYE5PrESYVcOZjh4wmCNyYsKA-mLJgQC5rjsGiWN1ng1toK7vAoSLXpsewQ",
  "tokenType": "Bearer",
  "userId": 3,
  "email": "user2@gmail.com",
  "role": "USER"
}
```

### => Điểm chung : Ta đều nhận được accessToken và refreshToken, các token sẽ được lưu lại để sử dụng test cho những API sau.

### 1.4. Đăng ký với email trùng lặp => Hiển thị message lỗi

<p align="center">
<img src="image/register_error_exist.jpg" width="70%">
</p>

- Status : 400 Bad Request
- Message : "Email already exists"

### Kiểm tra database :

- Database ban đầu (Dư âm của week2, xóa đi để tạo mới)

<p align="center">
<img src="image/database_week2.jpg" width="70%">
</p>

- Sau khi được tạo lại :

<p align="center">
<img src="image/database_week3.jpg" width="70%">
</p>

Ở đây trường password đã được Hash bởi Bcrypt, ngoài ra còn có thêm trường refreshToken và role.

## 2) Đăng Nhập (Login) :

### 2.1. Đăng nhập với role User thường

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/login`

Body raw JSON :

```json
{
  "email": "user1@gmail.com",
  "password": "1234567"
}
```

<p align="center">
<img src="image/login_user.jpg" width="70%">
</p>

- Status : 200 OK => Login thành công
- Response trả về :

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJJZCI6MSwic3ViIjoidXNlcjFAZ21haWwuY29tIiwiaWF0IjoxNzYyNjEyMTkxLCJleHAiOjE3NjI2MTU3OTF9.fNXCW6ZAzTDXnMUrk4foFwt_lIBpgHrTF6xlUS3I-Mhdg0nVvXYHfXcnmfiD1igm3j-BSSQrSy_55DD-tmop1g",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInR5cGUiOiJyZWZyZXNoIiwidXNlcklkIjoxLCJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTIxOTEsImV4cCI6MTc2MjY5ODU5MX0.H7iCCGQY42RRzYBd4xHfxxAOtdwvzQem7406zCOO0wJ-Pt_CcqRUozuQi1T6HKHAX1DcRaKUSM1xd5XjRYf1cg",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "user1@gmail.com",
  "role": "USER"
}
```

### 2.2. Đăng nhập với role Admin

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/login`

```json
{
  "email": "admin1@gmail.com",
  "password": "1234567"
}
```

<p align="center">
<img src="image/login_admin.jpg" width="70%">
</p>

- Status : 200 OK => Login thành công
- Response trả về :

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjIsInN1YiI6ImFkbWluMUBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTIzODEsImV4cCI6MTc2MjYxNTk4MX0.5DoeLYJm75Z4OL-CdOUH8yHtCgTexKXp04lwUkOB9D8HE7pNJXWneQgorqZ_Eu_aUZH5HrrxZ82J8Gj2EMqI9g",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJ0eXBlIjoicmVmcmVzaCIsInVzZXJJZCI6Miwic3ViIjoiYWRtaW4xQGdtYWlsLmNvbSIsImlhdCI6MTc2MjYxMjM4MSwiZXhwIjoxNzYyNjk4NzgxfQ.mhxzEQyOaWvygzWVkUv5v_fnK66BVi-2vUOBqNs1FflSz3qDmq8DVB6BC2tIwVQvHlmOLrNCzLPvBPHXZEfDcw",
  "tokenType": "Bearer",
  "userId": 2,
  "email": "admin1@gmail.com",
  "role": "ADMIN"
}
```

### 2.3. Đăng nhập lỗi (lấy ví dụ ở đây là sai password) :

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/login`

```json
{
  "email": "admin1@gmail.com",
  "password": "123456"
}
```

<p align="center">
<img src="image/login_error.jpg" width="70%">
</p>

- Status : 401 Unauthorized
- Response trả về :

```json
{
  "message": "Invalid email or password"
}
```

## 3) Test API kiểm tra JWT :

### 3.1. Validate token hợp lệ

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/validate`

<p align="center">
<img src="image/validate_token_hople.jpg" width="70%">
</p>

<p align="center">
<img src="image/done_validate_token.jpg" width="70%">
</p>

Status trả về : 200 OK => Thành công
Response trả về :

```json
{
  "valid": true,
  "message": "Token hợp lệ",
  "userId": 1,
  "email": "user1@gmail.com",
  "role": "USER"
}
```

### 3.2. Validate token không hợp lệ

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/validate`

- Giả sử thêm kí tự 'a' vào để làm sai token đi => lỗi

<p align="center">
<img src="image/validate_token_khonghople.jpg" width="70%">
</p>

- Status : 200 OK

- Response trả về :

```json
{
  "valid": false,
  "message": "Token không hợp lệ hoặc đã hết hạn",
  "userId": null,
  "email": null,
  "role": null
}
```

## 4) Test API Refresh Token

### 4.1. Refresh token hợp lệ

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/refresh`

**Body (raw JSON):**

```json
{
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInR5cGUiOiJyZWZyZXNoIiwidXNlcklkIjozLCJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTE2MTcsImV4cCI6MTc2MjY5ODAxN30.0PGlq-vnM1Y-Ro60LPBbrf0xvPnHYE5PrESYVcOZjh4wmCNyYsKA-mLJgQC5rjsGiWN1ng1toK7vAoSLXpsewQ"
}
```

<p align="center">
<img src="image/refresh_token_ok.jpg" width="70%">
</p>

- Status: `200 OK`
- Response chứa `accessToken` và `refreshToken` mới
- Lưu lại tokens mới để test tiếp

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJJZCI6Mywic3ViIjoidXNlcjJAZ21haWwuY29tIiwiaWF0IjoxNzYyNjEzNzA3LCJleHAiOjE3NjI2MTczMDd9.7nbn_CseBfRlV8KHWvFmWGHDf0OtM6nl6KNhfI-613nGggQM4gFxWr-0Ox_zfs6QBRGxTVfMAuxYgfNr84HjaQ",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInR5cGUiOiJyZWZyZXNoIiwidXNlcklkIjozLCJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTM3MDcsImV4cCI6MTc2MjcwMDEwN30.V4F5MuNcRgzx4G_O8GdfebfjfL01A_t7GT1ZpCqfw1q9aoQ9xTYnr-JHc86mq74QLLu6Q_yK-XkLhmJzGFq9QA",
  "tokenType": "Bearer",
  "userId": 3,
  "email": "user2@gmail.com",
  "role": "USER"
}
```

---

### 4.2. Refresh token không hợp lệ (Error case)

**Method:** `POST`  
**URL:** `http://localhost:8080/api/auth/refresh`

**Body:**

```json
{
  "refreshToken": "yJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInR5cGUiOiJyZWZyZXNoIiwidXNlcklkIjozLCJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJpYXQiOjE3NjI2MTE2MTcsImV4cCI6MTc2MjY5ODAxN30.0PGlq-vnM1Y-Ro60LPBbrf0xvPnHYE5PrESYVcOZjh4wmCNyYsKA-mLJgQC5rjsGiWN1ng1toK7vAoSLXpsewQ"
}
```

<p align="center">
<img src="image/refresh_token_error.jpg" width="70%">
</p>

```json
{
  "refreshToken": "invalid_refresh_token"
}
```

- Status: `401 Unauthorized`
- Message: "Invalid refresh token"

## 5) Test API được bảo vệ - User APIs

### 5.1. GET /api/user/profile (với token hợp lệ)

**Method:** `GET`  
**URL:** `http://localhost:8080/api/user/profile`

(Phải truyền accessToken vào Bearer token như lúc nãy để làm)

<p align="center">
<img src="image/test_user_api1.jpg" width="70%">
</p>

- Status: `200 OK`
- Response chứa thông tin profile của user

```json
{
  "access": "User và Admin có thể truy cập",
  "message": "User profile",
  "authorities": [
    {
      "authority": "ROLE_USER"
    }
  ],
  "username": "user1@gmail.com"
}
```

### 5.2. GET /api/user/dashboard (với token hợp lệ)

**Method:** `GET`  
**URL:** `http://localhost:8080/api/user/dashboard`

<p align="center">
<img src="image/test_user_api2.jpg" width="70%">
</p>

- Status: `200 OK`
- Response:

```json
{
  "access": "User and Admin có thể truy cập",
  "message": "Welcome to User Dashboard"
}
```

---

### 5.3. GET /api/user/data (với token hợp lệ)

**Method:** `GET`  
**URL:** `http://localhost:8080/api/user/data`

<p align="center">
<img src="image/test_user_api3.jpg" width="70%">
</p>

- Status: `200 OK`
- Response:

```json
{
  "note": "User and Admin đều có thể truy cập",
  "message": "User data"
}
```

### 5.4. Test User API không có token (Error case)

**Method:** `GET`  
**URL:** `http://localhost:8080/api/user/profile`

**Không thêm Header Authorization**

- Status: `403 Forbidden`

<p align="center">
<img src="image/test_user_api4.jpg" width="70%">
</p>

### 5.5 Test Admin API với User token (Test phân quyền)

**Method:** `GET`  
**URL:** `http://localhost:8080/api/admin/dashboard`

Trong Bearer Token nhập token của user vào

<p align="center">
<img src="image/test_admin_api_phanquyen.jpg" width="70%">
</p>

- Status: `403 Forbidden` (Access Denied)
