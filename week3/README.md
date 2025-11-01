# Tuần 3: Authentication & Authorization

---

## 1. Tìm hiểu các khái niệm

### Authentication (Xác thực) ?

### Authorization (Phân quyền) ?

---

## 2. Phân biệt cơ chế xác thực

### Session-based

### Token-based

---

## 3. Tìm hiểu JWT (JSON Web Token)

### JWT là gì ?

### Lợi ích của JWT ?

---

## 4. Cấu trúc của JWT ?

## Thực hành

### Phát triển từ Project CRUD Tuần 2

1. Tạo API đăng nhập trả về JWT (Có thể xây luồng đăng kí trước xong dùng tài khoản đã đăng kí để đăng nhập)
2. Tạo API kiểm tra JWT
3. Hashing Mật khẩu
4. Tạo Middleware/Filter Xác thực (Kiểm tra JWT)
5. Phân quyền truy cập dựa trên role trong JWT (Có các API cho admin, user....)
6. Refresh Token

---

## Output

- Trình bày lí thuyết.
- Làm đầy đủ phần thực hành. Chụp/quay màn hình các kết quả.

---

## Tham khảo

- [jwt.io](https://jwt.io/): Trang chủ để debug (giải mã) JWT.
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html): Các tiêu chuẩn bảo mật về xác thực.
- [OWASP Top 10 - A01: Broken Access Control](https://owasp.org/Top10/A01_2021-Broken_Access_Control/): Lỗi về phân quyền sai.

### Tài liệu framework

- **NodeJS:** [Passport.js (passport-jwt)](http://www.passportjs.org/packages/passport-jwt/)
- **Java:** [Spring Security with JWT](https://www.baeldung.com/spring-security-oauth-jwt)
- **Python:** [Django REST Framework - JWT](https://www.django-rest-framework.org/api-guide/authentication/#json-web-token-authentication)
