# I. Tìm hiểu các khái niệm

1. Authentication (Xác thực)
- Khái niệm: Xác định danh tính người dùng hoặc hệ thống. Cách gọi khác là đăng nhập
2. Authorization (Phân quyền)
- Khái niệm: Kiểm tra quyền truy cập của người dùng
- Đặc điểm: Sau khi đăng nhập xong thì hệ thống sẽ kiểm tra quyền và vai trò để được phép làm gì

---

# II. Phân biệt cơ chế xác thực

1. Session-based (Xác thực dựa trên phiên làm việc)

- Người dùng gửi thông tin đăng nhập đến server (có thể bao gồm username và password)
- Server kiểm tra thông tin. Nếu hợp lệ thì tạo một session cho người dùng
- Server lưu thông tin session này trong database hoặc bộ nhớ server
- Server gửi lại cho client một Session ID (thường lưu trong cookie)
- Mỗi request tiếp theo từ client sẽ tự động gửi cookie chứa Session ID, server tra chúng trong bộ nhớ để xác định người dùng

2. Token-base (Xác thực dựa trên mã token)

- Người dùng gửi thông tin đăng nhập đến server
- Server kiểm tra thông tin. Nếu hợp lệ thì tạo token
- Token sẽ chứa thông tin người dùng và được ký số (digital signature) để chống giả mạo
- Server không lưu token
- Client lưu token ở bộ nhớ cục bộ hoặc header
- Mỗi request sau đó client gửi token qua HTTP header, server xác thực token để xử lí yêu cầu

3. So sánh 2 kiểu xác thực

| **Tiêu chí** | **Session-based** | **Token-based** |
|-|-|-|
| **Kiểu lưu trữ** | Server lưu trữ session | Client giữ token|
| **Trạng thái** | Có | Không |
| **Nơi lưu thông tin** | Cookie (chứa Session ID) | LocalStorage hoặc HTTP Header |
| **Phù hợp cho** | Web app truyền thống (Spring MVC, JSP) | REST API, SPA (React, Angular, Vue), mobile app |
| **Hiệu năng** | Phụ thuộc vào server (tốn RAM khi nhiều người dùng) | Nhẹ hơn vì không lưu trạng thái trên server |
| **Bảo mật** | Dễ quản lý, dễ logout | Cần bảo vệ token (nguy hiểm nếu lưu sai cách) |
| **Cơ chế đăng xuất** | Xóa session trên server là hết hiệu lực | Cần blacklist token hoặc chờ token hết hạn |
| **Khả năng mở rộng** | Kém, khó mở rộng vì server phải chia sẻ session | Tốt, dễ mở rộng do stateless |
| **Triển khai** | Đơn giản, có sẵn trong Spring Security | Phức tạp hơn, cần thêm bước sinh & xác thực token |

---

# III. JWT (JSON Web Token)
1. JWT là gì?
- JWT là một chuẩn mở dùng để truyền thông tin giữa các bên một cách an toàn, ngắn gọn và có thể xác minh được
2. Lợi ích của JWT
- Hiệu năng cao: Không cần truy vấn database để kiểm tra session mỗi lần request
- Không lưu trạng thái (Stateless): Server không lưu session. Token đã chứa tất cả thông tin giảm tải cho server
- Dễ mở rộng hệ thống: Phù hợp cho kiến trúc phân tán
- An toàn và xác minh được: JWT được ký bằng thuật toán mã hoá => Phát hiện được nếu token bị thay đổi
- Chứa được thông tin người dùng: Payload có thể chứa thông tin cơ bản mà không cần truy vấn lại database
- Tự động hết hạn: Token có thể tự động vô hiệu sau một thời gian

---

# IV. Cấu trúc của JWT
- Một JWT gồm 3 phần, được ngăn cách bằng dấu chấm
            Ví dụ: aaa.bbb.ccc
   * Phần Header: Chứa thông tin kiểu token và thuật toán ký (phần .aaa)
   * Phần Payload: Chứa dữ liệu (phần .bbb) - Người dùng gửi lên
   * Phần Signature: Chữ ký số tạo ra từ Header + Payload + secret key, dùng để xác minh tính toàn vẹn của token (.ccc)
