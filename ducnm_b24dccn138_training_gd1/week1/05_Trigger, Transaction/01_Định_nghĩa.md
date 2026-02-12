# 1. CREATE PROCEDURE
**Mục đích:**
Tạo một **thủ tục lưu trữ (stored procedure)** trên cơ sở dữ liệu để thực hiện một hoặc nhiều thao tác SQL cùng lúc.  
Thường dùng để **tự động hóa các tác vụ lặp lại** như thêm, sửa, xóa dữ liệu.

**Cú pháp SQL:**
```sql
CREATE PROCEDURE ten_procedure
AS
BEGIN
    -- Câu lệnh SQL
END;
```

# 2. CREATE FUNCTION
**Mục đích:**
Tạo một **hàm (function)** trong cơ sở dữ liệu, thực hiện tính toán hoặc xử lý dữ liệu và trả về một giá trị.  
Thường dùng để **tính toán, kiểm tra điều kiện hoặc định dạng dữ liệu**.

**Cú pháp SQL:**
```sql
CREATE FUNCTION ten_function (@tham_so kieu_du_lieu)
RETURNS kieu_du_lieu
AS
BEGIN
    -- Câu lệnh SQL xử lý dữ liệu
    RETURN gia_tri_tra_ve;
END;
```

# 3. CREATE TRIGGER
**Mục đích:**
Tạo một **trigger (kích hoạt)** trên bảng để tự động thực hiện một hành động khi có **INSERT, UPDATE hoặc DELETE** xảy ra.  
Thường dùng để **tự động kiểm tra, cập nhật hoặc ghi nhật ký dữ liệu**.

**Cú pháp SQL:**
```sql
CREATE TRIGGER ten_trigger
ON ten_bang
AFTER INSERT|UPDATE|DELETE
AS
BEGIN
    -- Câu lệnh SQL thực hiện tự động khi trigger được kích hoạt
END;
```

# 4. COMMIT
**Mục đích:**
Thực hiện **xác nhận các thay đổi** đã thực hiện trong giao dịch hiện tại vào cơ sở dữ liệu.  
Thường dùng để **lưu các thao tác INSERT, UPDATE, DELETE** một cách vĩnh viễn.

**Cú pháp SQL:**
```sql
COMMIT;
```

# 5. ROLLBACK
**Mục đích:**
Hủy bỏ **tất cả các thay đổi** trong giao dịch hiện tại và đưa cơ sở dữ liệu về trạng thái trước khi bắt đầu giao dịch.  
Thường dùng để **hoàn tác các thao tác INSERT, UPDATE, DELETE** khi xảy ra lỗi hoặc điều kiện không hợp lệ.

**Cú pháp SQL:**
```sql
ROLLBACK;
```

# 6. SAVE TRANSACTION
**Mục đích:**
Tạo một **điểm lưu (savepoint)** trong giao dịch hiện tại, cho phép **rollback một phần** thay vì toàn bộ giao dịch.  
Thường dùng để **kiểm soát và phục hồi dữ liệu một cách linh hoạt** trong các giao dịch phức tạp.

**Cú pháp SQL:**
```sql
SAVE TRANSACTION ten_savepoint;
```
