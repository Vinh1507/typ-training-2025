# Phần 1: Framework & Xây dựng RESTful API

## 1. Lựa chọn Framework:
Java Spring Boot

## 2. Các thư mục chính thường gặp trong dự án
- models: chứa các domain object của dự án (là các class phục vụ theo nghiệp vụ)
- entities: chứa các entity object của dự án (là các class ánh xạ đến các bảng trong database)
- repository: chứa các class/interface làm việc với database
- service: tầng nghiệp vụ, thực hiện các logic nghiệp vụ
- controller: chứa các class định nghĩa các endpoint API, nhận và điều hướng các request đến server đến các class trong service để xử lý.
- migrations: tùy dự án có thể có hoặc không, ví dụ dự án kết hợp với Flyway để migration database, ở thư mục này chứa các file .sql tạo bảng migration, có thể chứa thêm folder seed/ để insert các bảng ghi, đánh index... sau khi tạo bảng migration.
- dto: chứa các DTO (Data Transfer Object - đối tượng luân chuyển dữ liệu giữa các tầng), có thể tách ra các folder con request/ và response/
- mapper hay hydrator: chứa các class thực hiện việc chuyển đổi qua lại giữa các đối tượng, ví dụ entity sang domain, domain sang dto,... và ngược lại

## 3. File cấu hình application
- là file cấu hình trung tâm của ứng dụng Spring Boot, dùng để khai báo và điều chỉnh toàn bộ các thiết lập cho ứng dụng mà không cần sửa trực tiếp trong code Java.
- chứa các thông số của dự án như port, kết nối đến database, .....

## 4. Rest API 
khái niệm: là 1 kiểu thiết kế cho phép client và server giao tiếp với nhau qua HTTP, đơn giản đây là các API được viết theo chuẩn REST (client-server, stateless, cacheable, uniform interface, layered system, code on demand)

## 5. HTTP Method 
- Hiểu đơn giản, client và server chỉ cần thống nhất chung tên method (GET/POST/PUT/DELETE...) và endpoint là đã có thể gửi nhận request, giao tiếp với nhau.
- Nhưng để áp dụng đúng chuẩn REST, ta sẽ định nghĩa các method trong các trường hợp như sau:

   - GET: lấy dữ liệu (ví dụ: GET /users/all - lấy tất cả user)
   - POST: tạo mới dữ liệu (ví dụ: POST /users/create - tạo user)
   - PUT: cập nhật dữ liệu (toàn bộ) (ví dụ: PUT /users/1 - cập nhật user có id = 1)
   - PATCH: cập nhật dữ liệu (chỉ những dữ liệu thay đổi) (ví dụ: tương tự PUT)
   - DELETE: xóa dữ liệu (ví dụ: DELETE /users/1 - xóa user có id = 1)