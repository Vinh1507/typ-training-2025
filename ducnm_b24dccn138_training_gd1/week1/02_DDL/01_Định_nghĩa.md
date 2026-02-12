## DDL (Data Definition Language)
> **DDL** là tập hợp các lệnh SQL dùng để **định nghĩa, tạo, sửa đổi và xóa cấu trúc** của cơ sở dữ liệu.  
> Nó tập trung vào **khung (schema)** của cơ sở dữ liệu chứ **không tác động trực tiếp đến dữ liệu** bên trong.

---

### 1. CREATE TABLE
**Mục đích:** Tạo một bảng mới trong cơ sở dữ liệu.  

**Cú pháp:**
```sql
CREATE TABLE ten_bang (
    ten_cot_1 kieu_du_lieu [rang_buoc_cot],
    ten_cot_2 kieu_du_lieu [rang_buoc_cot],
    ...
    [CONSTRAINT ten_rang_buoc_bang] [loai_rang_buoc] [ten_cot_lien_quan],
    ...
);

```

### 2. ALTER TABLE
**Mục đích:** Sửa đổi cấu trúc của một bảng đã tồn tại — có thể **thêm, sửa, hoặc xóa** cột, hoặc **thay đổi ràng buộc**.  

**Cú pháp:**
```sql
ALTER TABLE ten_bang
    [hanh_dong];

```

### 3. DROP TABLE
**Mục đích:** Lệnh này được sử dụng để **xóa vĩnh viễn** một bảng và **toàn bộ dữ liệu bên trong** bảng đó.  
> ⚠️ Sau khi thực hiện, **bảng sẽ bị xóa hoàn toàn** — không thể khôi phục nếu không có bản sao lưu.  

**Cú pháp:**  
```sql
DROP TABLE ten_bang;

```
### 4. CREATE VIEW
**Mục đích:**  
Lệnh này được sử dụng để **tạo một View (bảng ảo)** trong cơ sở dữ liệu.  
View **không lưu dữ liệu thực tế**, mà chỉ lưu **câu truy vấn SELECT**.  
Khi truy cập view, hệ thống sẽ **thực thi lại câu SELECT** để lấy dữ liệu mới nhất.  

**Cú pháp:**  
```sql
CREATE VIEW ten_view AS
SELECT
    cot_1,
    cot_2,
    ...
FROM
    ten_bang
WHERE
    [dieu_kien];

```
### 5. DROP VIEW
**Mục đích:**  
Lệnh này dùng để **xóa vĩnh viễn** một view đã tồn tại.  
> ⚠️ Lưu ý: Chỉ xóa **định nghĩa view**, **không xóa dữ liệu** trong bảng gốc.  

**Cú pháp:**  
```sql
DROP VIEW ten_view;
```
