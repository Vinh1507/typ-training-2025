## DML (Data Manipulation Language)

> **DML** là tập hợp các lệnh SQL dùng để **thao tác trực tiếp với dữ liệu** trong cơ sở dữ liệu, bao gồm:  
> - **Thêm dữ liệu** (INSERT)
> - **Cập nhật dữ liệu** (UPDATE)
> - **Xóa dữ liệu** (DELETE)
> - **Truy vấn dữ liệu** (SELECT)

# 1. INSERT
**Mục đích:**  
Thêm dữ liệu mới vào bảng trong cơ sở dữ liệu.

**Cú pháp SQL:**
```sql
INSERT INTO ten_bang (cot_1, cot_2, ...)
VALUES (gia_tri_1, gia_tri_2, ...);
```

# 2. UPDATE
**Mục đích:**  
Cập nhật dữ liệu hiện có trong bảng theo điều kiện xác định.

**Cú pháp SQL:**
```sql
UPDATE ten_bang
SET cot_1 = gia_tri_moi_1,
    cot_2 = gia_tri_moi_2,
    ...
WHERE dieu_kien;
```

# 3. DELETE
**Mục đích:**  
Xóa dữ liệu khỏi bảng theo điều kiện.

⚠️ Sau khi thực hiện, dữ liệu bị xóa **không thể khôi phục** nếu không có bản sao lưu.

**Cú pháp SQL:**
```sql
DELETE FROM ten_bang
WHERE dieu_kien;
```

# 4. SELECT
**Mục đích:**  
Có thể sử dụng để **lọc**, **sắp xếp**, **nhóm dữ liệu** hoặc **kết hợp nhiều bảng**.

**Cú pháp SQL:**
```sql
SELECT cot_1, cot_2, ...
FROM ten_bang
WHERE [dieu_kien]
GROUP BY [cot_nhom]
HAVING [dieu_kien_nhom]
ORDER BY [cot_sap_xep] [ASC|DESC];
```
