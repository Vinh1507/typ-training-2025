1. GRANT

`GRANT SELECT ON SINH_VIEN TO 'lecturer'@'localhost';`

- Mục đích: Cấp quyền đọc (SELECT) trên bảng SINH_VIEN cho người dùng có tên là lecturer khi họ kết nối từ máy chủ localhost

`GRANT INSERT, UPDATE, DELETE ON DIEM_SO TO 'admin'@'%';`

- Mục đích: Cấp quyền chèn, cập nhật, xóa (INSERT, UPDATE, DELETE) trên bảng DIEM_SO cho người dùng admin kết nối từ bất kỳ đâu (%)

2. REVOKE

`REVOKE DELETE ON DIEM_SO FROM 'lecturer'@'localhost';`

- Mục đích: Thu hồi quyền xóa (DELETE) trên bảng DIEM_SO khỏi người dùng lecturer

`REVOKE ALL PRIVILEGES ON MON_HOC FROM 'admin'@'%';`

- Mục đích: Thu hồi tất cả quyền (ALL PRIVILEGES) trên bảng MON_HOC khỏi người dùng admin
