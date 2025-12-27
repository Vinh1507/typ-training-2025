1. SELECT

```bash
SELECT
    SV.HoTen,
    MH.TenMH,
    DS.DiemTongKet
FROM SINH_VIEN SV
JOIN DIEM_SO DS ON SV.MaSV = DS.MaSV
JOIN MON_HOC MH ON DS.MaMH = MH.MaMH
WHERE
    SV.MaKhoa = 'CNTT'
    AND DS.HocKy = 1
    AND DS.NamHoc = 2023
    AND DS.TrangThai = 'Dat';

```

- Ý nghĩa: Lấy trong CSDL các sinh viên có mã khoa là CNTT, có điểm số học kỳ 1 năm 2023 là Đạt

2. INSERT

```bash
INSERT INTO DIEM_SO (MaSV, MaMH, HocKy, NamHoc, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet, TrangThai) VALUES ('B23CN020', 'CSDL01', 1, '2023', 10, 9.0, 9.0, 9.0, 'Dat');
```

- Ý nghĩa: Chèn vào bảng DIEM_SO 1 bản ghi có các giá trị tương ứng

3. UPDATE

```bash
UPDATE GIANG_VIEN SET HocVi = 'Pho Giao su' WHERE MaGV = 'GV001';
```

- Ý nghĩa: Sửa đổi giá trị cột `HocVi` từ Tiến sĩ thành phó giáo sư cho giảng vien cho mã là GV001

4. DELETE

```bash
DELETE FROM SINH_VIEN WHERE MaSV = 'B20CN005';
```

- Ý nghĩa: Lệnh này xóa sinh viên có mã B20CN005 trong bảng SINH_VIEN
