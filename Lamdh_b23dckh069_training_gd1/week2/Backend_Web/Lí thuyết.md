# Phần 1: Framework & Xây dựng RESTful API

1. Chọn java - Spring Boot
2. Tạo Project & cấu trúc:

   a. Khởi tạo project bằng trang web: [Spring Initializr](https://start.spring.io)
   
   b. Tìm hiểu cấu trúc thư mục:
      - Controller: Xử lí request từ client, gọi service và trả response.
      - Models: Chứa các class entity và ánh xạ tới bảng database.
      - Repository: Tương tác với database, thực hiện CRUD
      - Services: Chứa logic nghiệp vụ, tách biệt xử lí dữ liệu khỏi Controller
      - Migration: Tự động tạo hoặc cập nhật bảng, theo dõi phiên bản của database
      - Config: Chứa các class cấu hình ứng dụng
      - Resources: Lưu cấu hình, tài nguyên của ứng dụng
   
   c. File cấu hình - Application.properties
      - Giúp quản lý cấu hình của ứng dụng:
         * Kết nối database
         * Điều chỉnh cổng server, security,...
         * Điều chỉnh hành vi của Spring Boot không cần thay đổi code

4. Xây dựng RESTful API cơ bản

   a. Rest API
      - Rest API là một cách thiết kế giao tiếp giữa Client và Server thông qua giao thức HTTP
   
   b. HTTP Method
      - Phương thức HTTP là hành động mà Client yêu cầu Server thực hiện trên một tài nguyên nào đó
      - Bao gồm:
         * GET: Lấy dữ liệu
         * POST: Thêm dữ liệu
         * PUT: Cập nhật tất cả
         * PATCH: Cập nhật một phần
         * DELETE: Xoá dữ liệu

---
# Phần 2: Tích hợp Database (ORM)

1. Khái niệm ORM
   - ORM là cầu nối giữa cơ sở dữ liệu quan hệ và ngôn ngữ lập trình hướng đối tượng
   - Lợi ích:
      * Tiết kiệm thời gian: Không cần viết SQL thủ công nhiều lần
      * Tăng tính trực quan: Truy xuất dữ liệu bằng object trong code
      * Giảm lỗi
      * Bảo mật
      * Hỗ trợ quan hệ giữa các bảng thông qua object
2. Sử dụng ORM theo Framework
