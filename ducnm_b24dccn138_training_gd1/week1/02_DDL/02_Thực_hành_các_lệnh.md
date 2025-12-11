# 1. Tạo bảng BOOKS

**Mục đích:**  
Tạo bảng `BOOKS` trong cơ sở dữ liệu để lưu thông tin về sách, bao gồm **mã sách, tiêu đề, tác giả, thể loại, năm xuất bản**.

**Cú pháp SQL:**
```sql
CREATE TABLE BOOKS (
    book_id INT,
    tittle VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    category VARCHAR(50),
    year_published INT,
    CONSTRAINT pk_BOOKS PRIMARY KEY (book_id)
);
```
# 2. Tạo bảng Readers
**Mục đích:**
Tạo bảng `Readers` trong cơ sở dữ liệu để lưu thông tin người đọc.

**Cú pháp SQL:**
```sql
CREATE TABLE Readers(
	 reader_id INT,
	 name VARCHAR(20) NOT NULL,
	 living VARCHAR(200),
	 phone VARCHAR(15),
	 CONSTRAINT pk_Readers PRIMARY KEY(reader_id)
);
```

# 3. Tạo bảng BOOK_RECORDS
**Mục đích:**
Tạo bảng `BOOK RECORDS` trong cơ sở dữ liệu để lưu thông tin mượn sách.

**Cú pháp SQL:**
```sql
CREATE TABLE BOOK_RECORDS(
	borrow_id INT,
	reader_id INT,
	book_id INT,
	borrow_date DATE,
	return_date DATE,
	Statuss VARCHAR(20) DEFAULT 'Đang mượn',
	FOREIGN KEY(reader_id) REFERENCES Readers(reader_id),
	FOREIGN KEY(book_id) REFERENCES BOOKS(book_id)
)
```

# 4. Xóa bảng Readers

**Cú pháp SQL:**
```sql
DROP TABLE Readers;
```

# 5. Thêm cột name trong BOOK_RECORDS
**Cú pháp SQL:**
```sql
ALTER TABLE BOOK_RECORDS(
	ADD COLUMN name VARCHAR(50)
);	
```

# 6. CREATE VIEW: View sách theo thể loại Romantic
**Mục đích:**  
Tạo một **view ảo** để hiển thị tất cả sách thuộc thể loại `Romantic` trong bảng `BOOKS`.  

**Cú pháp SQL:**
```sql
CREATE VIEW View_Romantic AS
SELECT
    book_id,
    tittle,
    author,
    year_published
FROM
    BOOKS
WHERE
    category = 'Romantic';
```

# 7. XÓA VIEW_Romantic
**Mục đích:**  
Xóa vĩnh viễn view `View_Romantic`. Dữ liệu trong bảng `BOOKS` **không bị ảnh hưởng**. 

**Cú pháp SQL:**
```sql
DROP VIEW View_FictionBooks;
```
