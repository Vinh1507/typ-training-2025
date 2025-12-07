**Authentication và Authorization**

1. **Khái niệm cơ bản**  
- Authentication(xác thực): Là quá trình kiểm tra danh tính ví dụ như username, password, token,... để tạo 1 đối tượng Authentication hoặc Principal đại diện cho người dùng lưu vào SecurityContext để cho các lớp khác sử dụng.

  \+, 1 số phương thức phổ biến: session-based, token-based, Oauth2/OpenID Connect,...

- Authorization(phân quyền): Cấp quyền quyết định 1 người dùng đã xác thực được phép làm gì bằng cách kiểm tra quyền/role/permission trước khi cho truy cập API

  \+, Sau khi authentication thành công, component quyết định là FilterSecurityInterceptor trong chuỗi filter web hoặc annotation/ method security cho các call nội bộ

2. **Phân biệt cơ chế xác thực**  
   	**2.1 Session-based Authentication(xác thực dựa trên phiên)**  
- Là cơ chế truyền thống được sử dụng phổ biến trong các ứng dụng Web server-side ví dụ như JSP, Spring MVC,...  
- Khi người dùng đăng nhập, server sẽ tạo 1 session(phiên làm việc) lưu thông tin người dùng tạm thời như username, role, trạng thái đăng nhập  
- Flow hoạt động:

  	\+, Đăng nhập: người dùng gửi username, password đến server qua form login

  	\+, Kiểm tra: Server xác thực thông tin trong DB

  	\+, Tạo session: Nếu hợp lệ Server tạo 1 session ID và lưu thông tin user trong RAM, Redis hoặc DB

  	\+, Gửi cookie: Server gửi lại cho client 1 cookie chứa sessionID 

  	\+, Các request tiếp theo của client sẽ tự động gửi cookie đi theo

  	\+, Xác minh session: server dùng session Id để tìm trong session store, xác định user và cho phép truy cập api

- Ưu điểm:

  \+, Dễ triển khai, Spring Security hỗ trợ sẵn, phù hợp với web MVC

  \+, Có thể vô hiệu hoá session trên server ⇒ user bị đăng xuất ngay

- Nhược điểm:

  \+, Phụ thuộc vào server memory, mỗi server phải lưu session

  \+, Không phù hợp với REST API,...

  \+, Cookie có thể bị đánh cắp

  \+, Cần dùng load balancer hoặc session replication nếu có nhiều server

	**2.2 Token-based Authentication(Xác thực dựa trên Token)**

- Khái niệm: Token-based Authentication thường là JWT là cơ chế không lưu trạng thái phiên làm việc trên server. Sau khi đăng nhập thành công, server tạo ra 1 token chứa thông tin người dùng được mã hoá, client sẽ dùng token này cho các request tiếp theo  
- Quy trình hoạt động:

  \+, Đăng nhập: Client gửi username, password

  \+, Tạo token: Server xác thực thông tin sau đó sinh ra 1 token(JWT) chứ thông tin như userid, role, thời gian hết hạn. Token sẽ được ký bằng secret key

  \+, Token sau đó được gửi về cho client trong JSON

  \+, Client lưu token lại trong localStorage hoặc sessionStorage của trình duyệt

  \+, Các request sau đó client sẽ gửi token trong header

  \+, Server kiểm tra chữ ký và thời gian hết hạn,... Nếu hợp lệ sẽ cho phép truy cập

3. **JWT**  
- JWT(Json Web Token) là 1 chuẩn mở dùng để truyền thông tin giữa các bên 1 cách an toàn, gọn nhẹ và không cần lưu trạng thái. Thông tin trong JWT được mã hoá dạng JSON và ký bằng secret key (HMAC) hoặc khoá công khai(RSA, ECDSA) để đảm bảo tính toàn vẹn  
- Cấu trúc JWT: gồm 3 phần ngang cách nhau bởi dấu .

  \+, header: kiểu token \+ thuận toán mã hoá

  \+, payload: thông tin user như id, role và hạn

  \+, signature: phần được ký bằng secret key ⇒ đảm bảo token không bị sửa

- Ưu điểm:

  \+, Stateless: server không cần lưu session → dễ mở rộng (scalable).

  \+, Phù hợp với REST API, ứng dụng mobile, SPA (React, Angular...).

  \+, Có thể dùng chung giữa nhiều dịch vụ (microservices).  
  \+,Token có thể chứa thông tin role/permission → giảm truy vấn DB.

- Nhược điểm:

  \+, Không thể xoá token trước khi hết hạn nên nếu bị mất cắp sẽ bị bên đánh cắp sử dụng đến khi hết hạn

4. **Thực hành**