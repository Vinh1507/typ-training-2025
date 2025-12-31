# Báo cáo: Week 1

### Các câu lệnh cơ bản với SQL

**Bài toán thực hành:** Xây dựng một CSDL đơn giản cho việc **"Quản lý trận đấu"**, bao gồm các thực thể **Đội bóng** , **Cầu thủ** , **Trận đấu** , **Kết quả**

## 1. Định nghĩa Dữ liệu (DDL - Data Definition Language)

Dùng để định nghĩa và quản lý cấu trúc của các đối tượng CSDL.

- `CREATE TABLE`: Tạo một bảng mới.
- `DROP TABLE`: Xóa một bảng hiện có.
- `ALTER TABLE`: Sửa đổi cấu trúc của một bảng (thêm, xóa, sửa đổi cột).
- `CREATE VIEW`: Tạo một bảng ảo dựa trên kết quả của một câu lệnh `SELECT`.
- `DROP VIEW`: Xóa một view.

```sql
-- Tạo database
CREATE DATABASE quanlytrandau;

-- Table Đội Bóng
CREATE TABLE quanlytrandau.DOI_BONG (
    MaDoi VARCHAR(10) PRIMARY KEY,
    TenDoi NVARCHAR(50) NOT NULL,
    TenHLV NVARCHAR(50)
);

-- Table Cầu thủ
CREATE TABLE quanlytrandau.CAU_THU (
    MaCauThu VARCHAR(10) PRIMARY KEY,
    TenCauThu NVARCHAR(50) NOT NULL,
    ViTri NVARCHAR(20),
    NgaySinh DATE,
    MaDoi VARCHAR(10),
    FOREIGN KEY (MaDoi) REFERENCES DOI_BONG(MaDoi)
);

-- Table Trận đấu
CREATE TABLE quanlytrandau.TRAN_DAU (
    MaTranDau INT PRIMARY KEY AUTO_INCREMENT,
    NgayThiDau DATETIME,
    SanDau NVARCHAR(100),
    MaDoiNha VARCHAR(10),
    MaDoiKhach VARCHAR(10),
    FOREIGN KEY (MaDoiNha) REFERENCES DOI_BONG(MaDoi),
    FOREIGN KEY (MaDoiKhach) REFERENCES DOI_BONG(MaDoi)
);

-- Table kết quả
CREATE TABLE quanlytrandau.KET_QUA (
    MaBanThang INT PRIMARY KEY AUTO_INCREMENT,
    MaTranDau INT,
    MaCauThu VARCHAR(10),
    ThoiDiemGhiBan INT,
    FOREIGN KEY (MaTranDau) REFERENCES TRAN_DAU(MaTranDau),
    FOREIGN KEY (MaCauThu) REFERENCES CAU_THU(MaCauThu)
);
```

#### Sửa đổi cấu trúc bảng

```sql
-- Thêm cột số áo cho table
ALTER TABLE CAU_THU
ADD SoAo INT;
```

#### Tạo một bảng ảo dựa trên `SELECT`

```sql
CREATE VIEW V_ThongTinCauThu AS
SELECT
    CT.TenCauThu,
    CT.ViTri,
    CT.SoAo,
    DB.TenDoi
FROM
    CAU_THU AS CT
JOIN
    DOI_BONG AS DB ON CT.MaDoi = DB.MaDoi;
```

#### Xóa một bảng hoặc 1 view ở cs dữ liệu

```sql
-- Xóa view
DROP VIEW V_ThongTinCauThu;

-- Xóa bảng (lưu ý thứ tự xóa để không vi phạm khóa ngoại)
DROP TABLE KET_QUA;
DROP TABLE TRAN_DAU;
DROP TABLE CAU_THU;
DROP TABLE DOI_BONG;
```

## 2. Thao tác Dữ liệu (DML - Data Manipulation Language)

Dùng để truy vấn và sửa đổi dữ liệu trong các bảng.

- `SELECT`: Truy vấn và lấy dữ liệu từ một hoặc nhiều bảng.
- `INSERT`: Thêm một hoặc nhiều bản ghi mới vào một bảng.
- `UPDATE`: Cập nhật các bản ghi hiện có trong một bảng.
- `DELETE`: Xóa các bản ghi khỏi một bảng.

#### Lấy tất cả dữ liệu

```sql
-- lấy tất cả dữ liệu đội bóng
SELECT * FROM quanlytrandau.doi_bong;
```

#### Thêm dữ liệu vào bảng

```sql
-- Thêm đội bóng
INSERT INTO DOI_BONG (MaDoi, TenDoi, TenHLV)
VALUES ('HCM', N'TP. Hồ Chí Minh', N'Alexandre Polking'),
       ('HAGL', N'Hoàng Anh Gia Lai', N'Kiatisuk Senamuang');

-- Thêm cầu thủ
INSERT INTO CAU_THU (MaCauThu, TenCauThu, ViTri, NgaySinh, MaDoi, SoAo)
VALUES ('CT01', N'Lê Công Vinh', N'Tiền đạo', '1985-12-10', 'HAGL', 9),
       ('CT02', N'Nguyễn Quang Hải', N'Tiền vệ', '1997-04-12', 'HCM', 19);
```

Cập nhật dữ liệu

```sql
-- Cập nhật số áo cho Nguyễn Quang Hải
UPDATE CAU_THU
SET SoAo = 10
WHERE MaCauThu = 'CT02';
```

Xóa bản ghi

```sql
-- Xóa một cầu thủ không còn thi đấu
DELETE FROM CAU_THU
WHERE MaCauThu = 'CT01';
```

## 3. Trigger và Transaction

- **Stored Procedure, Function:**
  - `CREATE PROCEDURE`: Tạo một thủ tục lưu trữ .
  - `CREATE FUNCTION`: Tạo một hàm do người dùng định nghĩa, trả về một giá trị.

#### Tạo một procedure để thêm một cầu thủ mới.

```sql
CREATE PROCEDURE sp_ThemCauThu
    @MaCauThu VARCHAR(10),
    @TenCauThu NVARCHAR(50),
    @ViTri NVARCHAR(20),
    @MaDoi VARCHAR(10),
    @SoAo INT
AS
BEGIN
    INSERT INTO CAU_THU (MaCauThu, TenCauThu, ViTri, MaDoi, SoAo)
    VALUES (@MaCauThu, @TenCauThu, @ViTri, @MaDoi, @SoAo);
END;
```

#### Tạo hàm lấy tên đội bóng từ mã đội

```sql
CREATE FUNCTION fn_LayTenDoi(@MaDoi VARCHAR(10))
RETURNS NVARCHAR(50)
AS
BEGIN
    DECLARE @TenDoi NVARCHAR(50);
    SELECT @TenDoi = TenDoi FROM DOI_BONG WHERE MaDoi = @MaDoi;
    RETURN @TenDoi;
END;
```

- **Trigger:**

  - `CREATE TRIGGER`: Tạo một trigger, là một thủ tục tự động thực thi khi có một sự kiện (INSERT, UPDATE, DELETE) xảy ra trên bảng.

  #### Tạo một trigger để kiểm tra số lượng cầu thủ trong một đội không được vượt quá 25 khi thêm mới

```sql
CREATE TRIGGER trg_KiemTraSoLuongCauThu
ON CAU_THU
AFTER INSERT
AS
BEGIN
    DECLARE @MaDoi VARCHAR(10);
    DECLARE @SoLuong INT;

    SELECT @MaDoi = MaDoi FROM inserted;
    SELECT @SoLuong = COUNT(*) FROM CAU_THU WHERE MaDoi = @MaDoi;

    IF @SoLuong > 25
    BEGIN
        PRINT N'Lỗi: Mỗi đội không được có quá 25 cầu thủ.';
        ROLLBACK TRANSACTION; -- Hủy bỏ thao tác INSERT
    END
END;
```

- **Transaction:**
  - `COMMIT`: Lưu vĩnh viễn các thay đổi của một giao dịch.
  - `ROLLBACK`: Hủy bỏ các thay đổi trong một giao dịch.
  - `SAVE TRANSACTION`: Đặt một điểm lưu trong một giao dịch để có thể rollback về điểm đó.

```sql
BEGIN TRANSACTION; -- Bắt đầu transaction

BEGIN TRY
    -- Chuyển cầu thủ 'CT02' từ đội 'HCM' sang 'HAGL'
    UPDATE CAU_THU
    SET MaDoi = 'HAGL'
    WHERE MaCauThu = 'CT02';

    -- Giả sử có một thao tác khác bị lỗi
    -- Ví dụ: Cập nhật một đội bóng không tồn tại
    UPDATE DOI_BONG
    SET TenHLV = 'Test'
    WHERE MaDoi = 'KHONGTONTAI';

    -- Nếu không có lỗi, lưu thay đổi
    COMMIT TRANSACTION;
    PRINT 'Chuyển nhượng thành công!';
END TRY
BEGIN CATCH
    -- Nếu có lỗi xảy ra, hoàn tác tất cả
    ROLLBACK TRANSACTION;
    PRINT 'Chuyển nhượng thất bại, đã hoàn tác!';
END CATCH;
```

## 4. Ngôn ngữ Điều khiển Truy cập (DCL - Data Control Language)

Dùng để quản lý quyền truy cập của người dùng vào dữ liệu.

- `GRANT`: Cấp quyền cho người dùng (ví dụ: quyền SELECT, INSERT trên một bảng).

#### Cấp quyền `SELECT` trên bảng `CAU_THU` cho một người dùng tên `guest`

```sql
GRANT SELECT ON CAU_THU TO guest;
```

- `REVOKE`: Thu hồi quyền đã được cấp.

```sql
REVOKE SELECT ON CAU_THU FROM guest;
```

## 5. Các toán tử và mệnh đề truy vấn

Các thành phần cơ bản để xây dựng một câu lệnh truy vấn phức tạp.

- `FROM`: Chỉ định bảng nguồn để lấy dữ liệu.
- `WHERE`: Lọc các bản ghi dựa trên một điều kiện.
- `ORDER BY`: Sắp xếp kết quả trả về.
- `HAVING`: Lọc kết quả sau khi đã nhóm (sử dụng với `GROUP BY`).
- `AND`, `OR`: Các toán tử logic để kết hợp nhiều điều kiện.
- Và nhiều toán tử khác như `GROUP BY`, `JOIN`, `LIKE`, `IN`, `BETWEEN`...

```sql
-- Lay tat ca cau thu cua doi HAGL
SELECT TenCauThu, ViTri, SoAo
FROM quanlytrandau.CAU_THU
WHERE MaDoi = 'HAGL';
```

#### Xắp xếp

```sql
-- Lay danh sach cau thu la 'Tien dao' or 'Tien ve' va sap xep theo ten
SELECT TenCauThu, ViTri, MaDoi
FROM quanlytrandau.CAU_THU
WHERE ViTri = N'Tiền đạo' OR ViTri = N'Tiền vệ'
ORDER BY TenCauThu ASC;
```

#### Lọc kết quả sau khi đã được nhóm bởi `GROUP BY` bằng `HAVING`

```sql
-- Dem so cau thu cua moi doi va chi hien thị doi co nhieu hon 5 cau thu
SELECT MaDoi, COUNT(MaCauThu) AS SoLuongCauThu
FROM quanlytrandau.CAU_THU
GROUP BY MaDoi
HAVING COUNT(MaCauThu) > 5;
```

#### Cập nhật dữ liệu

```sql
-- Cap nhat so ao cho Nguyen Quang Hai
UPDATE quanlytrandau.CAU_THU
SET SoAo = 10
WHERE MaCauThu = 'CT02';
```

#### Xóa

```sql
-- Xoa mot cau thu khong con thi dau
DELETE FROM quanlytrandau.CAU_THU
WHERE MaCauThu = 'CT01';
```
