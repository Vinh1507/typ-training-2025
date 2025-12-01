-- 1. Danh sách sinh viên khoa CNTT Khóa 2002-2006
SELECT SINHVIEN.* FROM SinhVien
JOIN Lop ON SinhVien.ma_lop = Lop.ma_lop
JOIN KhoaHoc ON KhoaHoc.ma_khoa_hoc = Lop.ma_khoa_hoc
JOIN Khoa ON Khoa.ma_khoa = Lop.ma_khoa
WHERE Khoa.ma_khoa = 'CNTT' 
AND KhoaHoc.ma_khoa_hoc = 'K2002';

-- 2. Cho biết các sinh viên (Mã sinh viên, Họ tên, Năm sinh)
-- của các sinh viên học sớm hơn tuổi quy định
-- (Theo tuổi quy định thì sinh viên đủ 18 tuổi khi bắt đầu khóa học)
SELECT sv.ma_sv, sv.ho_ten, sv.nam_sinh FROM SinhVien sv
JOIN Lop l ON l.ma_lop = sv.ma_lop
JOIN KhoaHoc kh ON kh.ma_khoa_hoc = l.ma_khoa_hoc
WHERE kh.nam_bat_dau - sv.nam_sinh < 18;

-- 3. Cho biết sinh viên khoa CNTT, khóa 2002-2006 chưa học môn 'Cấu trúc dữ liệu 1'
SELECT * FROM SinhVien sv
JOIN Lop l ON l.ma_lop = sv.ma_lop
JOIN Khoa k ON k.ma_khoa = l.ma_khoa
JOIN KhoaHoc kh ON kh.ma_khoa_hoc = l.ma_khoa_hoc
WHERE k.ma_khoa = 'CNTT' 
AND kh.ma_khoa_hoc = 'K2002'
AND sv.ma_sv NOT IN (
    SELECT kq.ma_sv FROM KetQua kq
    JOIN MonHoc mh ON mh.ma_mon_hoc = kq.ma_mon_hoc
    WHERE mh.ten_mon_hoc = 'Cấu trúc dữ liệu 1'
);

-- 4. Cho biết sinh viên thi không đậu (điểm < 5) môn Cấu trúc dữ liệu 1 nhưng chưa thi lại
-- C1:
SELECT * FROM SinhVien sv
JOIN KetQua kq ON kq.ma_sv = sv.ma_sv
JOIN MonHoc mh ON mh.ma_mon_hoc = kq.ma_mon_hoc
WHERE mh.ten_mon_hoc = 'Cấu trúc dữ liệu 1'
AND kq.diem < 5 
AND kq.lan_thi = 1
AND NOT EXISTS (
	SELECT 1 FROM KetQua kq2
	WHERE kq2.ma_sv = sv.ma_sv
	AND kq2.ma_mon_hoc = mh.ma_mon_hoc
	AND kq2.lan_thi > 1
);
-- C2:
SELECT * FROM SinhVien sv
JOIN KetQua kq ON kq.ma_sv = sv.ma_sv
JOIN MonHoc mh ON mh.ma_mon_hoc = kq.ma_mon_hoc
WHERE mh.ten_mon_hoc = 'Cấu trúc dữ liệu 1'
AND kq.diem < 5 
AND kq.lan_thi = 1
AND sv.ma_sv NOT IN (
	SELECT sv.ma_sv FROM SinhVien sv
	JOIN KetQua kq ON kq.ma_sv = sv.ma_sv
	JOIN MonHoc mh ON mh.ma_mon_hoc = kq.ma_mon_hoc
	WHERE mh.ten_mon_hoc = 'Cấu trúc dữ liệu 1'
	AND kq.lan_thi > 1
);
-- C3:
SELECT sv.ma_sv, sv.ho_ten, sv.nam_sinh, count(*) FROM SinhVien sv
JOIN KetQua kq ON kq.ma_sv = sv.ma_sv
JOIN MonHoc mh ON mh.ma_mon_hoc = kq.ma_mon_hoc
WHERE mh.ten_mon_hoc = 'Cấu trúc dữ liệu 1'
AND kq.diem < 5
GROUP BY sv.ma_sv, sv.ho_ten, sv.nam_sinh
HAVING count(*) = 1;

-- 5. Với mỗi lớp thuộc khoa CNTT, cho biết mã lớp, mã khóa học tên chương trình và số sinh viên thuộc lớp đó
SELECT l.ma_lop, kh.ma_khoa_hoc, cth.ten_chuong_trinh, COUNT(sv.ma_sv) AS so_sinh_vien 
FROM Khoa k
JOIN Lop l ON k.ma_khoa = l.ma_khoa
JOIN ChuongTrinhHoc cth ON cth.ma_chuong_trinh = l.ma_chuong_trinh
JOIN KhoaHoc kh ON kh.ma_khoa_hoc = l.ma_khoa_hoc
LEFT JOIN SinhVien sv ON sv.ma_lop = l.ma_lop
WHERE k.ma_khoa = 'CNTT' 
GROUP BY l.ma_lop, kh.ma_khoa_hoc, cth.ten_chuong_trinh;