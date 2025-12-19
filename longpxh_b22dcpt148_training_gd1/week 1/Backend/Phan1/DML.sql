-- Thao tác dữ liệu (DML)

-- Thêm dữ liệu cho các bảng

INSERT INTO khoa (MaKhoa, TenKhoa) VALUES
('CNTT', 'Công nghệ thông tin'),
('KT', 'Kế toán'),
('QTKD', 'Quản trị kinh doanh'),
('NN', 'Ngôn ngữ Anh');

INSERT INTO sinhvien (MaSV, HoTen, NgaySinh, GioiTinh, Email, MaKhoa) VALUES
('SV001', 'Nguyễn Văn An', '2003-05-14', 'Nam', 'an.nguyen@example.com', 'CNTT'),
('SV002', 'Trần Thị Bình', '2004-03-21', 'Nữ', 'binh.tran@example.com', 'KT'),
('SV003', 'Lê Quốc Cường', '2003-11-09', 'Nam', 'cuong.le@example.com', 'QTKD'),
('SV004', 'Phạm Thu Dung', '2004-01-17', 'Nữ', 'dung.pham@example.com', 'NN');


INSERT INTO monhoc (MaMonHoc, TenMonHoc, SoTinChi) VALUES
('MH001', 'Lập trình C++', 3),
('MH002', 'Cơ sở dữ liệu', 3),
('MH003', 'Kế toán tài chính', 4),
('MH004', 'Giao tiếp tiếng Anh', 2);


INSERT INTO ketqua (MaSV, MaMonHoc, Diem) VALUES
('SV001', 'MH001', 8.5),
('SV001', 'MH002', 7.8),
('SV002', 'MH003', 9.0),
('SV003', 'MH002', 6.5),
('SV004', 'MH004', 8.0);

-- Lấy ra toàn bộ danh sách sinh viên có giới tinh nam
select * from sinhvien sv
where sv.GioiTinh = 'Nam';

-- Lấy họ tên sinh viên, tên khoa, tên môn học và điểm những môn trên 
-- 8 điểm của sinh viên Nguyễn Văn An
select sv.HoTen, k.TenKhoa, mh.TenMonHoc, kq.Diem
from monhoc mh
join ketqua kq on mh.MaMonHoc = kq.MaMonHoc
join sinhvien sv on kq.MaSV = sv.MaSV
join khoa k on sv.MaKhoa = k.MaKhoa
where sv.HoTen = 'Nguyễn Văn An'and kq.Diem > 8;


-- Cập nhật email cho sinh viên SV002 
-- binh.tran@example.com => binh.tran.update@example.com
UPDATE sinhvien sv
set sv.Email = 'binh.tran.update@example.com'
WHERE MaSV = 'SV002';

-- xóa kết quả của Sinh viên có mã sinh viên SV001 ở môn học MH001
delete from ketqua kq
where kq.MaSV = 'SV001' and kq.MaMonHoc = 'MH001'