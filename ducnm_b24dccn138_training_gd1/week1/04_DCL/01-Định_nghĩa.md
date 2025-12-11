## DCL (Data Control Language)

**Định nghĩa:**  
> **DCL** là tập hợp các câu lệnh SQL dùng để **quản lý quyền truy cập và bảo mật dữ liệu** trong cơ sở dữ liệu.  
> Nó cho phép **cấp quyền (GRANT)** và **thu hồi quyền (REVOKE)** cho người dùng hoặc nhóm người dùng, đảm bảo chỉ những người được phép mới thực hiện các thao tác trên dữ liệu.

# 1. GRANT
**Mục đích:**  
Câu lệnh `GRANT` dùng để **cấp quyền** cho người dùng (user) hoặc nhóm người dùng (role) trên cơ sở dữ liệu, bảng, cột hoặc thủ tục.  
Nhờ đó, người được cấp quyền có thể **thực hiện các thao tác như SELECT, INSERT, UPDATE, DELETE…** tùy quyền được cấp.

**Cú pháp SQL:**
```sql
GRANT quyen ON ten_bang TO nguoi_dung;
```

# 2. REVOKE
**Mục đích:**  
Câu lệnh `REVOKE` dùng để **thu hồi quyền** đã cấp trước đó từ người dùng (user) hoặc nhóm người dùng (role) trên cơ sở dữ liệu, bảng, cột hoặc thủ tục.  
Nhờ đó, người bị thu hồi quyền sẽ **không còn thực hiện các thao tác như SELECT, INSERT, UPDATE, DELETE…** nữa.

**Cú pháp SQL:**
```sql
REVOKE quyen ON ten_bang FROM nguoi_dung;
```
