1. DROP TABLE

`DROP TABLE diem_so;`

- Mục đích: Xóa bảng diem_so và tất cả dữ liệu bên trong

2. ALTER TABLE

```bash

ALTER TABLE giang_vien
ADD COLUMN NgayBatDauLam DATE;
```

- Mục đích: Thêm cột NgayBatDauLam vào bảng giang_vien

3. CREATE VIEW và DROP VIEW

```bash
CREATE VIEW V_BangDiemTongKet AS
SELECT
    sv.MaSV,
    sv.HoTen,
    mh.TenMH,
    ds.HocKy,
    ds.NamHoc,
    ds.DiemTongKet,
    ds.TrangThai
FROM
    sinh_vien sv
JOIN diem_so ds ON sv.MaSV = ds.MaSV
JOIN mon_hoc mh ON ds.MaMH = mh.MaMH
WHERE ds.DiemTongKet IS NOT NULL
AND ds.TrangThai IS NOT NULL;

```

- Mục đích: Tạo một View hiển thị Bảng điểm tổng kết của các sinh viên, chỉ lấy những thông tin quan trọng.
