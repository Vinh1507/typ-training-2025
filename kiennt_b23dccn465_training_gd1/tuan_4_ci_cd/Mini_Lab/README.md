# StemHub
**StemHub** là nền tảng chia sẻ tài liệu, bài học, và học liệu thuộc lĩnh vực STEM giữa cộng đồng – một phần của dự án **STEMIND**. Ứng dụng được xây dựng trên Spring Boot kết hợp Thymeleaf cho giao diện server-side, sử dụng MySQL làm cơ sở dữ liệu quan hệ và Cloudflare R2 để lưu trữ nội dung tĩnh / media. Hạ tầng đóng gói bằng Docker để dễ dàng triển khai.
## 1. Mục tiêu
- Tạo kho học liệu STEM mở và có tổ chức.
- Cho phép người dùng đăng tải, tìm kiếm, đánh dấu yêu thích tài liệu.
- Hỗ trợ mở rộng về sau (ví dụ: đánh giá, phân loại theo chủ đề, gợi ý học liệu).
## 2. Tính năng chính (hiện tại / dự kiến)
- Đăng ký / đăng nhập (JWT + session tầng ứng dụng).
- Quản lý hồ sơ người dùng.
- Upload tài liệu / media (đẩy lên Cloudflare R2).
- Tìm kiếm / lọc tài liệu.
- Giao diện trang chủ, chi tiết, yêu thích, hồ sơ, upload bằng Thymeleaf.
## 3. Tech Stack
| Thành phần | Công nghệ | Ưu điểm |
| --- | --- | --- |
| Backend | Spring Boot (Java 21), Spring Data JPA | **Phát triển nhanh** nhờ tự động cấu hình (Spring Boot) và đơn giản hóa việc truy cập database (JPA). |
| Template Engine | Thymeleaf | Tích hợp **mượt mà với Spring**, cú pháp tự nhiên (viết HTML chuẩn, có thể xem trực tiếp trên trình duyệt). |
| Database | MySQL 8.x | **Ổn định, phổ biến**, mã nguồn mở (miễn phí) và có cộng đồng hỗ trợ lớn. |
| Object Storage | Cloudflare R2 | Tương thích API của S3, nhưng **không tốn phí băng thông ra** (zero egress fees), giúp chi phí cực thấp. |
| Build / Packaging | Maven + Docker multi-stage | Quản lý dependency mạnh mẽ (Maven) và tạo ra **Docker image gọn nhẹ, an toàn** (multi-stage). |
| Triển khai | Docker Compose | **Đơn giản hóa** việc chạy và kết nối nhiều service (Backend, DB) trong môi trường dev và production. |

## 4. Cấu trúc thư mục chính
- `StemHub/` mã nguồn ứng dụng Spring Boot
- `docker-compose.yml` orchestration cho app + MySQL
- `mysql/` (volume Docker) chứa dữ liệu MySQL (không commit lên git)
- `.env` (chứa biến môi trường thật – đã được ignore) / `.env.example` (mẫu an toàn)
## 5. Yêu cầu hệ thống
- Docker & Docker Compose
- (Tùy chọn phát triển cục bộ) JDK 21+, Maven 3.9+
## 6. Biến môi trường
Tạo file `.env` ở thư mục gốc dự án dựa trên mẫu `.env.example`:

Các biến hiện dùng:
- `MYSQL_ROOT_PASSWORD` mật khẩu root MySQL
- `MYSQL_DATABASE` tên database ứng dụng (ví dụ: StemHub)
- `MYSQL_USER`, `MYSQL_PASSWORD` user riêng cho ứng dụng
- `JWT_SECRET_KEY` khóa bí mật để ký JWT (>=32 ký tự, ngẫu nhiên)
- `CLOUDFLARE_R2_*` thông tin truy cập Cloudflare R2 (nên tạo key có phạm vi tối thiểu)

Lưu ý bảo mật: KHÔNG commit file `.env`. Nếu lỡ để lộ (như đã thấy), hãy xoay vòng (rotate) toàn bộ Access Key / Secret Key trên Cloudflare R2 và đổi JWT secret ngay.
## 7. Docker Compose – Chạy nhanh
1. Sao chép mẫu:
```
cp .env.example .env
# Chỉnh sửa lại giá trị cho phù hợp môi trường
```
2. Khởi chạy:
```
docker-compose up --build
```
3. Truy cập ứng dụng tại: `http://localhost:8080`
4. Dừng:
```
docker-compose down
```
## 8. Build & Run Local (không Docker)
1. Đảm bảo MySQL đang chạy và tạo database `StemHub` (hoặc để Hibernate tạo bảng tự động).
2. Xuất các biến môi trường (hoặc chỉnh trực tiếp `application.properties`).
3. Chạy:
```
cd StemHub
./mvnw spring-boot:run
```
4. Hoặc build jar:
```
./mvnw clean package -DskipTests
java -jar target/StemHub-*.jar
```
## 9. Tính năng phát triếp tiếp theo (Dự kiến)
* Đổi mật khẩu, thay đổi thông tin người dùng.
* Hỗ trợ file `docx` và `pptx`, cho phép upload video.
* Tích hợp `elastic search`.
* Nâng cấp, cải thiện các tính năng hiện tại.
