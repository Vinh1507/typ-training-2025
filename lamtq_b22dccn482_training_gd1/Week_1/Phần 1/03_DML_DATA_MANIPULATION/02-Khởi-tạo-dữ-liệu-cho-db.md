use typ;
DELETE FROM DIEM_SO;
DELETE FROM DANG_KY;
DELETE FROM MON_HOC;
DELETE FROM SINH_VIEN;
DELETE FROM GIANG_VIEN;
DELETE FROM KHOA;

# 1. Dữ liệu bảng KHOA (3 bản ghi)

```bash
INSERT INTO KHOA (MaKhoa, TenKhoa, DienThoai, DiaChiVanPhong) VALUES ('CNTT', 'Cong Nghe Thong Tin', '02438634263', 'Toa nha A1');
INSERT INTO KHOA (MaKhoa, TenKhoa, DienThoai, DiaChiVanPhong) VALUES ('KT', 'Kinh Te', '02438634200', 'Toa nha C2');
INSERT INTO KHOA (MaKhoa, TenKhoa, DienThoai, DiaChiVanPhong) VALUES ('DT', 'Dien Tu Vien Thong', '02438634500', 'Toa nha B3');
```

# 2. Dữ liệu bảng GIANG_VIEN (10 bản ghi)

```bash
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV001', 'Nguyen Thi Thu', 0, '1980-11-20', 'Tien si', 'thunt@fedu.vn', 'CNTT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV002', 'Tran Van Long', 1, '1975-03-15', 'Thac si', 'longtv@fedu.vn', 'CNTT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV003', 'Le Minh Khai', 1, '1985-07-01', 'Giao su', 'khailm@fedu.vn', 'KT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV004', 'Pham Thi Huong', 0, '1988-04-12', 'Thac si', 'huongpt@fedu.vn', 'KT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV005', 'Do Quoc Anh', 1, '1979-09-05', 'Pho Giao su', 'anhdq@fedu.vn', 'DT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV006', 'Vu Thi Thanh', 0, '1990-01-22', 'Thac si', 'thanhtv@fedu.vn', 'CNTT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV007', 'Hoang Trong Nghia', 1, '1983-06-30', 'Tien si', 'nghiatrh@fedu.vn', 'DT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV008', 'Mai Hong Nhung', 0, '1992-02-18', 'Thac si', 'nhungmh@fedu.vn', 'KT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV009', 'Trinh Dinh Nam', 1, '1970-12-03', 'Giao su', 'namtd@fedu.vn', 'CNTT');
INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, NgaySinh, HocVi, Email, MaKhoa) VALUES ('GV010', 'Dang Phuong Linh', 0, '1995-10-10', 'Thac si', 'linhdp@fedu.vn', 'DT');
```

# 3. Dữ liệu bảng SINH_VIEN (20 bản ghi)

```bash
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B20CN001', 'Pham Van Anh', '2002-01-15', 1, 'Ha Noi', '0912345671', 'anhpv1@fedu.vn', '2020', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B20CN002', 'Do Thi Mai', '2002-05-25', 0, 'Hai Phong', '0912345672', 'maidt2@fedu.vn', '2020', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B20CN003', 'Le Trong Hung', '2002-08-10', 1, 'Nam Dinh', '0912345673', 'hunlt3@fedu.vn', '2020', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B20CN004', 'Tran Phuong Ly', '2002-04-01', 0, 'Quang Ninh', '0912345674', 'lytp4@fedu.vn', '2020', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B20CN005', 'Ngo Minh Duc', '2002-09-19', 1, 'Thai Binh', '0912345675', 'ducnm5@fedu.vn', '2020', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B21KT006', 'Vu Dinh Nam', '2003-09-10', 1, 'Thanh Hoa', '0912345676', 'namvd6@fedu.vn', '2021', 'KT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B21KT007', 'Hoang Thu Thao', '2003-03-22', 0, 'Nghe An', '0912345677', 'thaoth7@fedu.vn', '2021', 'KT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B21KT008', 'Dang Van Son', '2003-06-06', 1, 'Ha Tinh', '0912345678', 'sondv8@fedu.vn', '2021', 'KT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B21KT009', 'Chu Minh Anh', '2003-11-29', 0, 'Quang Binh', '0912345679', 'anhcm9@fedu.vn', '2021', 'KT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B21KT010', 'Nguyen Thi Kim', '2003-02-14', 0, 'Da Nang', '0912345680', 'kimnt10@fedu.vn', '2021', 'KT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B22DT011', 'Le Minh Tuan', '2004-01-05', 1, 'Ho Chi Minh', '0912345681', 'tuanlm11@fedu.vn', '2022', 'DT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B22DT012', 'Bui Thanh Truc', '2004-10-18', 0, 'Dong Nai', '0912345682', 'trucbt12@fedu.vn', '2022', 'DT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B22DT013', 'Cao Xuan Cuong', '2004-07-27', 1, 'Binh Duong', '0912345683', 'cuongcx13@fedu.vn', '2022', 'DT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B22DT014', 'Phan Dinh Phung', '2004-03-03', 1, 'Long An', '0912345684', 'phungpd14@fedu.vn', '2022', 'DT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B22DT015', 'Nguyen Thu Hang', '2004-12-31', 0, 'Can Tho', '0912345685', 'hangnt15@fedu.vn', '2022', 'DT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B23CN016', 'Tran Manh Kien', '2005-06-16', 1, 'Ha Noi', '0912345686', 'kientm16@fedu.vn', '2023', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B23CN017', 'Dinh Tuan Tu', '2005-04-04', 1, 'Ha Noi', '0912345687', 'tudt17@fedu.vn', '2023', 'CNTT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B23KT018', 'Nguyen Bao Ngoc', '2005-01-01', 0, 'Hai Duong', '0912345688', 'ngocnb18@fedu.vn', '2023', 'KT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B23DT019', 'Pham Hai Dang', '2005-11-11', 1, 'Hung Yen', '0912345689', 'dangph19@fedu.vn', '2023', 'DT');
INSERT INTO SINH_VIEN (MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, Email, NamNhapHoc, MaKhoa) VALUES ('B23CN020', 'Nguyen An An', '2005-07-07', 0, 'Ha Noi', '0912345690', 'anan20@fedu.vn', '2023', 'CNTT');
```

# 4. Dữ liệu bảng MON_HOC (10 bản ghi)

```bash
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('CSDL01', 'Co so du lieu', 3, 'GV001');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('LTDT01', 'Lap trinh di dong', 3, 'GV002');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('KT01', 'Nguyen ly Ke toan', 4, 'GV003');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('AV01', 'Tieng Anh co ban', 2, 'GV004');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('VL01', 'Vat Ly Dai Cuong', 3, 'GV005');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('MMT01', 'Mang May Tinh', 3, 'GV006');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('GT01', 'Giai Tich I', 4, 'GV009');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('DL01', 'Dien Tu Co Ban', 3, 'GV007');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('XSTK01', 'Xac suat Thong ke', 3, 'GV008');
INSERT INTO MON_HOC (MaMH, TenMH, SoTinChi, MaGV) VALUES ('PTTK01', 'Phan Tich Thiet Ke HT', 4, 'GV001');
```

# 5. Dữ liệu bảng DANG_KY (30 bản ghi)

```bash
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN001', 'CSDL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN001', 'LTDT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN001', 'GT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN002', 'CSDL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN002', 'AV01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN002', 'MMT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN003', 'CSDL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN003', 'GT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN003', 'PTTK01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN004', 'CSDL01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN004', 'LTDT01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN004', 'MMT01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN005', 'CSDL01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B20CN005', 'GT01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT006', 'KT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT006', 'XSTK01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT007', 'KT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT007', 'AV01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT008', 'KT01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT008', 'XSTK01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B21KT009', 'KT01', 2, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B22DT011', 'DL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B22DT011', 'VL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B22DT012', 'DL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B22DT012', 'VL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B23CN016', 'GT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B23CN017', 'AV01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B23KT018', 'KT01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B23DT019', 'VL01', 1, '2023');
INSERT INTO DANG_KY (MaSV, MaMH, HocKy, NamHoc) VALUES ('B23CN020', 'CSDL01', 1, '2023');
```

# 6. Dữ liệu bảng DIEM_SO (30 bản ghi)

_DiemTongKet = (CC*0.1 + GK*0.3 + CK\*0.6)_

```bash
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN001', 'CSDL01', 1, '2023', 9, 8.5, 7.0, 7.6, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN001', 'LTDT01', 1, '2023', 8, 5.0, 6.5, 6.1, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN001', 'GT01', 1, '2023', 10, 9.0, 8.5, 8.8, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN002', 'CSDL01', 1, '2023', 7, 3.0, 3.5, 3.7, 'Khong dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN002', 'AV01', 1, '2023', 9, 8.0, 8.0, 8.0, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN002', 'MMT01', 1, '2023', 10, 7.5, 7.0, 7.3, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN003', 'CSDL01', 1, '2023', 8, 6.0, 7.0, 6.6, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN003', 'GT01', 1, '2023', 9, 8.0, 7.5, 7.8, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN003', 'PTTK01', 1, '2023', 10, 9.5, 9.0, 9.1, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN004', 'CSDL01', 2, '2023', 10, 8.0, 7.5, 7.9, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN004', 'LTDT01', 2, '2023', 9, 7.0, 6.0, 6.6, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN004', 'MMT01', 2, '2023', 8, 5.5, 4.0, 4.7, 'Khong dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN005', 'CSDL01', 2, '2023', 9, 8.5, 8.0, 8.3, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B20CN005', 'GT01', 2, '2023', 7, 3.0, 3.5, 3.7, 'Khong dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT006', 'KT01', 1, '2023', 10, 9.0, 8.0, 8.5, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT006', 'XSTK01', 1, '2023', 9, 7.0, 7.5, 7.5, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT007', 'KT01', 1, '2023', 8, 6.5, 5.0, 5.7, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT007', 'AV01', 1, '2023', 10, 9.0, 9.0, 9.0, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT008', 'KT01', 2, '2023', 9, 8.0, 7.0, 7.5, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT008', 'XSTK01', 2, '2023', 10, 9.5, 9.0, 9.1, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B21KT009', 'KT01', 2, '2023', 7, 5.0, 4.5, 4.6, 'Khong dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B22DT011', 'DL01', 1, '2023', 9, 8.0, 8.5, 8.5, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B22DT011', 'VL01', 1, '2023', 10, 9.0, 9.0, 9.0, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B22DT012', 'DL01', 1, '2023', 8, 7.0, 6.5, 6.8, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B22DT012', 'VL01', 1, '2023', 9, 8.5, 7.0, 7.6, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B23CN016', 'GT01', 1, '2023', 8, 7.0, 7.5, 7.4, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B23CN017', 'AV01', 1, '2023', 10, 9.0, 8.5, 8.8, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B23KT018', 'KT01', 1, '2023', 9, 8.0, 7.5, 7.8, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B23DT019', 'VL01', 1, '2023', 8, 6.0, 5.5, 5.9, 'Dat');
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B23CN020', 'CSDL01', 1, '2023', 10, 9.0, 9.0, 9.0, 'Dat');

```
