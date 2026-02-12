# Báo cáo tuần 2 (BE)
## Phần 1: Framework & Xây dựng RESTful API
1. Lựa chọn Framework: **Java: Spring Boot**
2. Tạo project & Cấu trúc
- Để khởi tạo 1 project Spring Boot thì có nhiều cách và cách đơn giản nhất là vào trang https://start.spring.io/ chọn config rồi generate về máy.
- Cấu trúc thư mục: để làm 1 dự án web theo cấu trúc 3 tầng thì
  - TypTrainingApplication(src/main/java/com/example/TYP_training/TypTrainingApplication.java): mỗi dự án Spring boot sẽ đều cần có file này(So sánh đơn giản như một lớp main trong project java bình thường). Khi chạy class này project của mình sẽ được cấu hình dựa theo các dependency trong file pom.xml, Spring sẽ tự tạo ra container thông qua các anotation như @Controller, @Service, @Repository, @Component, ... Và không được đặt file này ở vị trí khác nếu không Spring sẽ không tìm thấy do cấu hình của Spring sẽ tìm trong com.example.ProjectName
  - models: là các entity dùng để đại diện cho các bảng dữ liệu trong db.
  - controller: là tầng tiếp nhận các http request từ client sau đó sẽ gọi đến tầng service, cuối cùng trả về respone cho client. 
  - service: là tầng để xử lý các logic của ứng dụng và gọi đến repo.
  - repository: là tầng làm việc trực tiếp đến DB, thực hiện các câu lệnh SQL để truy cập xử lý dữ liệu.
  - application.properties: là file cấu hình hệ thống. Ví dự: các cấu hình môi trường, server, db, port, ... Và được đọc sau khi run file Application.
3. Xây dựng RESTful API cơ bản
- RESTful API: là một chuẩn phổ biế để thiết kế API cho các ứng dụng web để tiện cho việc quản lý resource dựa theo giao thức HTTP.
- Giao thức HTTP: là một nền tảng dùng cho World Wide Web, cho phép các client và server giao tiếp với nhau.
  - URL: giống như là địa chỉ giúp server xác định vị tài nguyên và được gửi từ phias client.
  - Các phương thức HTTP phổ biến: 
    - GET: dùng để yêu cầu lấy dữ liệu từ server.
    - POST: dùng để yêu cầu server lưu thực thể vào db.
    - PUT: dùng để yêu cầu server sửa đổi thực thể đã tồn tại trong db nếu không có thì sẽ tạo mới thực thể.
    - DELETE: dùng để yêu cầu server xoá thực thể được chỉ định.
    - PATCH: dùng để yêu cầu server sửa đổi 1 phần của thực thể đã tồn tại trong db.
  - Khi server trả về 1 respone sẽ kèm theo 1 mã trạng thái để xác định được rõ kết quả.
    - 1xx: Thông tin
    - 2xx: Thành công
    - 3xx: Sự điều hướng lại
    - 4xx: Lỗi phía client
    - 5xx: Lỗi phía server
4. Thực hành
- [Xem video demo](https://drive.google.com/drive/u/0/folders/16MwiK9D1FeL8CPQtMPUlA3AIvA26jLMV)
## Phần 2: Tích hợp Database (ORM)
1. Khái niệm ORM
2. Sử dụng ORM theo Spring Boot
