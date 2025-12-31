# Database -- SQL Nâng Cao & Truy vấn quan hệ

## 1.JOIN trong SQL

### 1.1 INNER JOIN

- **Đ/n**: Trả về dữ liệu phù hợp với cả 2 bảng.

- **VD**:

```sql
SELECT NhanVien.TenNV, PhongBan.TenPB
FROM NhanVien
INNER JOIN PhongBan ON NhanVien.PhongBanID = PhongBan.ID;
```

### 1.2 LEFT JOIN

- **Đ/n**: Trả về tất cả các bản ghi từ bảng bên trái (bảng đầu tiên nêu trong truy vấn), và các bản ghi khớp từ bảng bên phải. Nếu không khớp, kết quả từ bảng bên phải sẽ là NULL.

- **VD**:

```sql
SELECT NhanVien.TenNV, PhongBan.TenPB
FROM NhanVien
LEFT JOIN PhongBan ON NhanVien.PhongBanID = PhongBan.ID;
```

### 1.3 RIGHT JOIN

- **Đ/n**: Ngược lại với `RIGHT`.

- **VD**:

```sql
SELECT NhanVien.TenNV, PhongBan.TenPB
FROM NhanVien
RIGHT JOIN PhongBan ON NhanVien.PhongBanID = PhongBan.ID;
```

### 1.4 FULL JOIN

- **Đ/n**: Trả về dữ liệu phù hợp với cả 2 bảng.

- **VD**:

```sql
SELECT NhanVien.TenNV, PhongBan.TenPB
FROM NhanVien
FULL OUTER JOIN PhongBan ON NhanVien.PhongBanID = PhongBan.ID;
```

### 1.5 SELF JOIN

- **Đ/n**: Là phép nối một bảng với chính nó. Thường dùng để xử lý dữ liệu có quan hệ phân cấp (cha-con) trong cùng một bảng, ví dụ: Nhân viên - Quản lý, Danh mục cha - Danh mục con.

- **Kỹ thuật**: Bạn phải đặt bí danh (alias) khác nhau cho bảng  để SQL phân biệt được đâu là vai trò nhân viên, đâu là vai trò quản lý.

- **VD**: 

```sql
SELECT 
    NV.TenNV AS NhanVien, 
    QL.TenNV AS NguoiQuanLy
FROM NhanVien NV
LEFT JOIN NhanVien QL
ON NV.QuanLyID = QL.ID;
```

## 2.Truy vấn nâng cao

### 2.1 GROUP BY -- HAVING

- **Đ/n**:

    - `GROUP BY`: Gom nhóm các hàng có cùng giá trị (thường dùng với hàm tổng hợp như `COUNT`, `SUM`, `AVG`).

    - `HAVING`: Dùng để lọc dữ liệu sau khi đã gom nhóm. (Lưu ý: `WHERE` lọc **trước** khi gom nhóm, `HAVING` lọc **sau** khi gom nhóm).

- **VD**:

```sql
SELECT PhongBanID, SUM(Luong) as TongLuong
FROM NhanVien
WHERE PhongBanID IS NOT NULL
GROUP BY PhongBanID
HAVING SUM(Luong) > 2500;
```

### 2.2 Subquery (Truy vấn con)

#### 2.2.1 Truy vấn con trong WHERE

- **Đ/n**: Dùng kết quả của câu lệnh SELECT con để làm điều kiện lọc cho câu lệnh cha

- **Vd**: 

```sql
SELECT TenNV, Luong
FROM NhanVien
WHERE Luong > (SELECT AVG(Luong) FROM NhanVien); 
```

#### 2.2.2 Truy vấn con trong FROM

- **Đn**: Biến kết quả của một câu select thành một "bảng tạm" để tiếp tục truy vấn trên đó.

- **vd**:

```sql
SELECT TenPB, LuongTB
FROM PhongBan PB
JOIN (
    SELECT PhongBanID, AVG(Luong) as LuongTB
    FROM NhanVien
    GROUP BY PhongBanID
) AS T ON PB.ID = T.PhongBanID
WHERE T.LuongTB > 1500;
```

### 2.3 EXISTS / NOT EXISTS

- **Đ/n**: Kiểm tra xem truy vấn con có trả về dòng nào không. Nó trả về **TRUE** hoặc **FALSE**. Thường nhanh hơn `IN` khi làm việc với dữ liệu lớn.

- **VD**: 

```sql
SELECT TenPB
FROM PhongBan PB
WHERE EXISTS (
    SELECT 1 
    FROM NhanVien NV 
    WHERE NV.PhongBanID = PB.ID
);
```

### 2.4 UNION / UNION ALL

- **Đ/n**: Dùng để gộp kết quả của 2 hay nhiều câu `SELECT` lại với nhau.

- **VD**: 

```sql
SELECT TenNV AS Ten, 'NhanVien' AS Loai FROM NhanVien
UNION ALL
SELECT TenDT AS Ten, 'DoiTac' AS Loai FROM DoiTac;
```