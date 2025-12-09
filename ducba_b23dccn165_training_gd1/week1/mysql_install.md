# I) CÀI ĐẶT DBMS MySQL

## 1. Tổng quan

MySQL là hệ quản trị CSDL quan hệ (RDBMS) phổ biến, dùng mô hình **client–server**, cổng mặc định **`3306`**, hỗ trợ **SQL** chuẩn, **replication**, **backup**, và công cụ quản trị (**MySQL Workbench**).

**Có nhiều cách cài đặt:**

- **Local:** MySQL Installer (Windows), Homebrew (macOS), APT/YUM (Linux), Docker.
- **Bundle:** XAMPP/AMPPS (thường là MariaDB – tương thích MySQL).
- **Cloud (MySQL-compatible):** Amazon RDS, Google Cloud SQL, Azure Database for MySQL, PlanetScale.

## 2. Yêu cầu hệ thống

- CPU **x86_64/ARM64**, RAM **≥ 2 GB** (khuyến nghị **4 GB+**).
- Quyền **admin/sudo** (cài service).
- Mở cổng **`3306`** nếu cần truy cập từ xa (cân nhắc bảo mật).

## 3. Cài đặt trên Windows (MySQL chính hãng)

### MySQL Installer

1. Tải **MySQL Installer for Windows** (bản “with Community”).
2. Mở Installer → chọn **Developer Default** (gồm MySQL Server, Workbench, Shell).
3. Thiết lập:
   - **Config Type:** _Development Computer_.
   - **Connectivity:** _TCP/IP_, **Port `3306`**.
   - **Authentication:** _Strong Password Encryption_ (`caching_sha2_password`).
   - Tạo **root password**.
4. Hoàn tất và khởi động dịch vụ.

### Kiểm tra

- Mở **MySQL Workbench** → **Local instance MySQL80** → nhập _root password_, kết nối thành công.
- Hoặc dùng CLI:
  ```bash
  mysql -u root -p
  ```
