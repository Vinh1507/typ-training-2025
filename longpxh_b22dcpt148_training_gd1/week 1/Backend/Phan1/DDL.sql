-- Định nghĩa Dữ liệu (DDL)

-- Taọ bảng tài liệu 
create table tailieu(
	id int auto_increment primary key,
	TenTaiLieu varchar(255),
	MaTaiLieu varchar(255),
	NgayXuatBan date ,
	SoLuong int
)
 
-- xóa bảng tài liệu
drop table tailieu;

-- thêm cột địa chỉ vào bảng sinhvien
alter table sinhvien 
add column DiaChi varchar(255);

-- tạo view 
CREATE VIEW view_sinhvien_khoa AS
SELECT sv.MaSV, sv.HoTen, k.TenKhoa
FROM sinhvien sv
JOIN khoa k ON sv.MaKhoa = k.MaKhoa;

-- select từ view 
SELECT * FROM view_sinhvien_khoa;

-- xóa view
DROP VIEW view_sinhvien_khoa;