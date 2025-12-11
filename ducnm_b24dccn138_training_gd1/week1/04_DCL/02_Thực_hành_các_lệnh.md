# 1. GRANT

**Mục tiêu:**
Cấp quyền SELECT và INSERT trên bảng BOOKS cho user 'reader_user'

**Cú pháp SQL:**
```sql
GRANT SELECT, INSERT
ON BOOKS
TO reader_user;
```

**Mục tiêu:**
Cấp quyền UPDATE trên bảng BOOK_RECORDS cho user 'librarian'

**Cú pháp SQL:**
```sql
GRANT UPDATE
ON BOOK_RECORDS
TO librarian;
```

# 2. REVOKE

**Mục tiêu:**
Thu hồi quyền INSERT trên bảng BOOKS từ user 'reader_user'

**Cú pháp SQL:**
```sql
REVOKE INSERT
ON BOOKS
FROM reader_user;
```

**Mục tiêu:**
Thu hồi toàn bộ quyền trên bảng READERS từ 'Duc Ngo'

**Cú pháp SQL:**
```sql
REVOKE ALL
ON READERS
FROM 'Duc Ngo';
```

