# Tuần 2: Tìm hiểu Framework & ORM

> Tuần 2 tập trung vào **xây dựng RESTful API** và **kết nối với cơ sở dữ liệu (CSDL)** bằng **framework** và **ORM**.

---

## Phần 1: Framework & Xây dựng RESTful API

1. **Lựa chọn Framework (Chọn 1)**

   - **Java:** Spring Boot
   - **NodeJS:** Express.js hoặc NestJS
   - **Python:** Django 

2. **Tạo Project & Cấu trúc**

   - **Khởi tạo project:**
     - Spring Boot → [Spring Initializr](https://start.spring.io/)
     - NodeJS → `npm init`
     - Django → `django-admin startproject myproject`
   - **Tìm hiểu cấu trúc thư mục:**
     - Các thư mục `controllers`, `models`, `routes`, `services`, `migrations`, ...
     - Vai trò của từng file chính trong project.
   - **Tìm hiểu file cấu hình:**
     - `application.properties` (Spring Boot)
     - `.env` (NodeJS)
     - `settings.py` (Django)

3. **Xây dựng RESTful API cơ bản**

   - **Khái niệm REST API & HTTP Methods:**
     - `GET` → Lấy dữ liệu
     - `POST` → Thêm dữ liệu
     - `PUT/PATCH` → Cập nhật dữ liệu
     - `DELETE` → Xóa dữ liệu
   - **Cách định nghĩa Routes / Controllers:**
     - Mapping URL đến hàm xử lý tương ứng.
     - Ví dụ: `/api/students`, `/api/books`, `/api/products`.
   - **Xử lý Request:**
     - Đọc dữ liệu từ:
       - **Path Variable** (VD: `/users/{id}`)
       - **Query Param** (VD: `/users?age=20`)
       - **Request Body** (JSON payload)
   - **Xử lý Response:**
     - Trả về dữ liệu dạng **JSON**.
     - Gửi kèm **HTTP Status Code** thích hợp (`200`, `201`, `400`, `404`, ...).

4. **Thực hành**
   - **Bài tập:**  
     Xây dựng API CRUD quản lý một đối tượng (ví dụ: `Student`, `Book`, hoặc `Product`).
     - `GET /items` → Lấy danh sách
     - `GET /items/{id}` → Lấy chi tiết
     - `POST /items` → Thêm mới
     - `PUT /items/{id}` → Cập nhật
     - `DELETE /items/{id}` → Xóa
   - **Dữ liệu:** Lưu tạm trong **List/Map (Java/Python)** hoặc **Array/Object (NodeJS)**.
   - **Chưa cần kết nối Database.**
   - **Output sau phần 1:**
     - Trình bày phần Lí thuyết.
     - Một project backend chạy được.
     - Có ít nhất **5 API CRUD** hoạt động và trả về JSON đúng format. Quay video demo.
     - Kiểm thử API bằng **Postman** hoặc **cURL**.

---

## Phần 2: Tích hợp Database (ORM)

1. **Khái niệm ORM**

   - **ORM là gì?**
   - **Lợi ích?**

2. **Sử dụng ORM theo Framework**
   - **Java (Spring Boot)**
     - Tìm hiểu **JPA (Java Persistence API)** & **Hibernate**.
     - Cấu hình **Spring Data JPA**.
     - Định nghĩa **Entity** (`@Entity`, `@Id`, `@GeneratedValue`).
     - Tạo **Repository** kế thừa `JpaRepository`.
   - **NodeJS (Express/NestJS)**
     - Tìm hiểu **Prisma**, **TypeORM** hoặc **Sequelize**.
     - Định nghĩa **Schema / Entity**.
     - Cấu hình kết nối DB trong `.env`.
     - Thao tác CRUD qua repository hoặc client API.
   - **Python (Django)**
     - Tìm hiểu **Django ORM**.
     - Định nghĩa **Model** trong `models.py`.
     - Tạo bảng bằng:
       ```bash
       python manage.py makemigrations
       python manage.py migrate
       ```
   - **Output sau phần 2:**
     - Trình bày phần Lí thuyết
     - Thay thế phần CRUD thủ công phần 1 bằng ORM để thao tác dữ liệu thật. Quay video demo.
