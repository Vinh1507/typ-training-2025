-- Tạo người dùng mới
CREATE USER gv_khoa WITH PASSWORD '123';
CREATE USER nhanvien WITH PASSWORD 'abc';

-- Xóa người dùng
DROP OWNED BY gv_khoa;
DROP USER gv_khoa;

-- Cấp quyền SELECT (chỉ xem, không được thêm/sửa/xóa) cho giảng viên khoa (gv_khoa)
GRANT SELECT 
ON SinhVien, KetQua
TO gv_khoa;

-- Cấp quyền INSERT và UPDATE cho nhân viên
GRANT INSERT, UPDATE
ON SinhVien
TO nhanvien;

-- Giới hạn quyền chi tiết ở cột(column-level permission)
GRANT UPDATE (ho_ten)
ON SinhVien
TO nhanvien;

-- Thu hồi quyền (REVOKE)
REVOKE SELECT
ON KetQua
FROM gv_khoa;


CREATE USER gv_khoa WITH PASSWORD '123';
CREATE USER gv_pdt WITH PASSWORD '124';
CREATE USER nhanvien WITH PASSWORD '125';

-- 1. Người dùng gv_khoa chỉ được xem thông tin sinh viên (SELECT bảng SinhVien).
GRANT SELECT
ON SinhVien
TO gv_khoa;

-- 2. Người dùng gv_pdt được phép thêm và sửa điểm trong bảng KetQua.
GRANT INSERT, UPDATE
ON KetQua
TO gv_pdt;

-- 3. Người dùng nhanvien chỉ được xem điểm, không được thêm/sửa/xóa.
GRANT SELECT 
ON KetQua
TO nhanvien;

-- 4. Sau khi cấp quyền, thu hồi quyền xem điểm của nhanvien.
REVOKE SELECT
ON KetQua
FROM nhanvien;