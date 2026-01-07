# Phần 1: Chạy MySQL ở trên MySQL Workbench CE 8.0

---

# Phần 2: Các câu lệnh cơ bản với SQL

1. Tạo bài toán Quản Lý Sinh Viên với các bảng Khoa, Sinh Viên, Điểm (Create database, Create table):
```
Create database qlsinhvien;

Create table qlsinhvien.Khoa(
    MaKhoa char(5) primary key,
    TenKhoa varchar(255)
);
Create table qlsinhvien.SinhVien(
	  MSV char(5) primary key,
    Ten varchar(255),
    NgaySinh date,
    GioiTinh Varchar(255),
    MaKhoa char(5) references Khoa(MaKhoa)
);
Create table qlsinhvien.Diem(
  	MSV char(5),
    MonHoc varchar(255),
    Diem float,
    primary key (MSV, MonHoc)
)
```
2. Thao tác dữ liệu (Insert into, Select, Update, Delete)
```
-- Thêm dữ liệu
Use qlsinhvien;
Insert into Khoa Values
  ('KHMT', 'Khoa học máy tính'),
  ('CNTT', 'Công nghệ thông tin');
Insert into SinhVien values
  ('SV01', 'Nguyễn Văn A', '2005-11-08', 'Nam', 'CNTT'),
  ('SV02', 'Nguyên Thị B', '2005-10-26', 'Nữ', 'KHMT');
Insert into Diem values
  ('SV01', 'OOP', '8.5'),
  ('SV01', 'DSA', '7.0'),
  ('SV02', 'C++', '9.0');

-- Xem dữ liệu
  Select * from khoa;
  Select * from SinhVien;

-- Cập nhật
  Update Diem set diem = 10.0 where MSV = 'SV01' and MonHoc = 'OOP';

-- Xoá 
  Delete from SinhVien where MSV = 'SV02';
```
3. Định nghĩa dữ liệu (Ví dụ cho CREATE VIEW)
```
Create view DanhSachDiem as
Select sv.MSV, sv.Ten, sv.GioiTinh, k.TenKhoa, d.MonHoc, d.Diem
From SinhVien sv
Join Khoa k on sv.MaKhoa = k.MaKhoa
Join Diem d on sv.MSV = d.MSV;
```
4.1. Trigger (Kiểm tra điểm nhập vào - Dùng create trigger)
```
DELIMITER $$
CREATE TRIGGER trg_KiemTraDiem
BEFORE INSERT ON Diem
FOR EACH ROW
BEGIN
    IF NEW.Diem < 0 OR NEW.Diem > 10 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Điểm không hợp lệ! (phải từ 0 đến 10)';
    END IF;
END$$
DELIMITER ;
```
4.2. Procedure + Transaction (Thêm sinh viên mới)
```
DELIMITER $$
CREATE PROCEDURE sp_ThemSinhVien (
    IN p_MSV CHAR(5),
    IN p_Ten NVARCHAR(50),
    IN p_NgaySinh DATE,
    IN p_GioiTinh NVARCHAR(10),
    IN p_MaKhoa CHAR(5)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Dữ liệu chưa được lưu.' AS ThongBao;
    END;

    START TRANSACTION;

    INSERT INTO SinhVien (MSV, Ten, NgaySinh, GioiTinh, MaKhoa)
    VALUES (p_MSV, p_Ten, p_NgaySinh, p_GioiTinh, p_MaKhoa);

    COMMIT;
    SELECT 'Đã thêm sinh viên!' AS ThongBao;
END$$
DELIMITER ;
```
5. Phân quyền (Grant / Revoke)
```
GRANT SELECT ON SinhVien TO PUBLIC;
REVOKE SELECT ON SinhVien FROM PUBLIC;
```
