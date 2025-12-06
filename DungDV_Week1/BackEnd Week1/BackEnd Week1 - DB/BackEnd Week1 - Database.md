
# 3. Các câu lệnh SQL

## 3.1. Ngôn ngữ Định nghĩa Dữ liệu (DDL – Data Definition Language)

DDL được sử dụng để mô tả, khởi tạo, chỉnh sửa hoặc loại bỏ cấu trúc của các đối tượng trong cơ sở dữ liệu như bảng, view, hoặc index.  

### CREATE TABLE

Tạo các bảng cần thiết cho hệ thống `LibrarySystem`.

```sql
/* Bảng Nhà xuất bản */
CREATE TABLE Publishers (
    PublisherID INT PRIMARY KEY AUTO_INCREMENT,
    PublisherName VARCHAR(120) NOT NULL,
    Country VARCHAR(80)
);

/* Bảng Thể loại sách */
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY AUTO_INCREMENT,
    CategoryName VARCHAR(100) UNIQUE
);

/* Bảng Tác giả */
CREATE TABLE Authors (
    AuthorID INT PRIMARY KEY AUTO_INCREMENT,
    AuthorName VARCHAR(150) NOT NULL
);

/* Bảng Sách */
CREATE TABLE Books (
    BookID INT PRIMARY KEY AUTO_INCREMENT,
    BookTitle VARCHAR(255) NOT NULL,
    PublishedYear INT CHECK (PublishedYear >= 1900),
    CategoryID INT,
    PublisherID INT,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID),
    FOREIGN KEY (PublisherID) REFERENCES Publishers(PublisherID)
);

/* Bảng Liên kết Sách – Tác giả (quan hệ N-N) */
CREATE TABLE BookAuthors (
    BookID INT,
    AuthorID INT,
    PRIMARY KEY (BookID, AuthorID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID)
);

/* Bảng Độc giả */
CREATE TABLE Readers (
    ReaderID INT PRIMARY KEY AUTO_INCREMENT,
    FullName VARCHAR(120) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    JoinedAt DATE DEFAULT (CURDATE())
);

/* Bảng Giao dịch mượn – trả sách */
CREATE TABLE BorrowRecords (
    RecordID INT PRIMARY KEY AUTO_INCREMENT,
    ReaderID INT,
    BookID INT,
    BorrowedOn DATE NOT NULL,
    ExpectedReturn DATE NOT NULL,
    ReturnedOn DATE,
    FOREIGN KEY (ReaderID) REFERENCES Readers(ReaderID),
    FOREIGN KEY (BookID) REFERENCES Books(BookID)
);
```

### ALTER TABLE

Câu lệnh `ALTER TABLE` được dùng để thay đổi cấu trúc bảng hiện có: thêm, xóa hoặc chỉnh sửa cột.

```sql
/* Thêm cột Số điện thoại cho độc giả */
ALTER TABLE Readers
ADD COLUMN PhoneNumber VARCHAR(20);

/* Đổi tên cột PublishedYear thành ReleaseYear */
ALTER TABLE Books
CHANGE COLUMN PublishedYear ReleaseYear INT;

/* Xóa cột Số điện thoại */
ALTER TABLE Readers
DROP COLUMN PhoneNumber;
```

### DROP TABLE

Xóa vĩnh viễn một bảng khỏi cơ sở dữ liệu (bao gồm cả dữ liệu trong đó).

```sql
/* Xóa bảng BorrowRecords nếu muốn khởi tạo lại */
DROP TABLE BorrowRecords;
```

### CREATE VIEW / DROP VIEW

`VIEW` là bảng ảo, được tạo dựa trên một câu truy vấn SELECT, giúp xem dữ liệu tổng hợp một cách tiện lợi.

```sql
/* Tạo view thống kê sách đang được mượn */
CREATE VIEW V_Borrowing AS
SELECT 
    b.BookTitle,
    r.FullName AS Borrower,
    br.BorrowedOn,
    br.ExpectedReturn
FROM BorrowRecords br
JOIN Books b ON b.BookID = br.BookID
JOIN Readers r ON r.ReaderID = br.ReaderID
WHERE br.ReturnedOn IS NULL;

/* Truy vấn view */
SELECT * FROM V_Borrowing;

/* Xóa view */
DROP VIEW V_Borrowing;
```

## 3.2. Ngôn ngữ Thao tác Dữ liệu (DML – Data Manipulation Language)

DML được sử dụng để thao tác dữ liệu trong các bảng — thêm mới, cập nhật, xóa hoặc truy vấn.

### INSERT

Thêm dữ liệu mới vào các bảng.

```sql
/* Thêm nhà xuất bản */
INSERT INTO Publishers (PublisherName, Country)
VALUES ('NXB Kim Đồng', 'Việt Nam'), ('Penguin Books', 'Anh');

/* Thêm thể loại */
INSERT INTO Categories (CategoryName)
VALUES ('Thiếu nhi'), ('Khoa học'), ('Văn học');

/* Thêm tác giả */
INSERT INTO Authors (AuthorName)
VALUES ('Nguyễn Nhật Ánh'), ('Isaac Asimov');

/* Thêm sách */
INSERT INTO Books (BookTitle, ReleaseYear, CategoryID, PublisherID)
VALUES 
('Cho tôi xin một vé đi tuổi thơ', 2010, 1, 1),
('Foundation', 1951, 2, 2);

/* Liên kết sách và tác giả */
INSERT INTO BookAuthors (BookID, AuthorID)
VALUES (1, 1), (2, 2);

/* Thêm độc giả */
INSERT INTO Readers (FullName, Email)
VALUES ('Trần Văn Bình', 'binh@example.com'), ('Lê Thị Mai', 'mai@example.com');

/* Thêm bản ghi mượn sách */
INSERT INTO BorrowRecords (ReaderID, BookID, BorrowedOn, ExpectedReturn)
VALUES (1, 1, '2025-10-01', '2025-10-15');
```

### SELECT

Truy vấn dữ liệu từ các bảng.

```sql
/* Lấy danh sách toàn bộ sách */
SELECT * FROM Books;

/* Lấy tên sách và năm phát hành */
SELECT BookTitle, ReleaseYear FROM Books;

/* Xem các sách thuộc thể loại “Khoa học” */
SELECT b.BookTitle, c.CategoryName
FROM Books b
JOIN Categories c ON b.CategoryID = c.CategoryID
WHERE c.CategoryName = 'Khoa học';
```

### UPDATE

Cập nhật thông tin trong bảng (luôn nhớ dùng `WHERE` để tránh thay đổi toàn bộ dữ liệu).

```sql
/* Cập nhật ngày trả cho bản ghi mượn số 1 */
UPDATE BorrowRecords
SET ReturnedOn = '2025-10-18'
WHERE RecordID = 1;
```

### DELETE

Xóa dữ liệu khỏi bảng.

```sql
/* Xóa độc giả có mã ReaderID = 2 */
DELETE FROM Readers
WHERE ReaderID = 2;
```

## 3.3. Giao dịch, Thủ tục và Trigger

### Giao dịch (Transaction – TCL)

Một giao dịch là tập hợp nhiều câu lệnh SQL được thực hiện cùng nhau: nếu một bước lỗi, toàn bộ giao dịch sẽ bị hủy (ROLLBACK).

```sql
START TRANSACTION;

/* Mượn 2 cuốn sách cùng lúc */
INSERT INTO BorrowRecords (ReaderID, BookID, BorrowedOn, ExpectedReturn)
VALUES (1, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY));

INSERT INTO BorrowRecords (ReaderID, BookID, BorrowedOn, ExpectedReturn)
VALUES (1, 2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY));

/* Nếu thành công */
COMMIT;

/* Nếu có lỗi, dùng ROLLBACK */
-- ROLLBACK;
```

### Stored Procedure (Thủ tục lưu trữ)

```sql
DELIMITER //
CREATE PROCEDURE sp_ReturnBook (
    IN p_RecordID INT
)
BEGIN
    UPDATE BorrowRecords
    SET ReturnedOn = CURDATE()
    WHERE RecordID = p_RecordID;
END //
DELIMITER ;

CALL sp_ReturnBook(1);
```

### Trigger (Bộ kích hoạt)

```sql
DELIMITER //
CREATE TRIGGER trg_PreventDeleteBook
BEFORE DELETE ON Books
FOR EACH ROW
BEGIN
    DECLARE borrow_count INT;
    SELECT COUNT(*) INTO borrow_count
    FROM BorrowRecords
    WHERE BookID = OLD.BookID AND ReturnedOn IS NULL;

    IF borrow_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Không thể xóa sách vì vẫn còn bản ghi mượn.';
    END IF;
END //
DELIMITER ;
```

## 3.4. Ngôn ngữ Điều khiển Truy cập (DCL – Data Control Language)

```sql
-- CREATE USER 'staff'@'localhost' IDENTIFIED BY 'abc123';
GRANT SELECT, INSERT ON LibrarySystem.BorrowRecords TO 'staff'@'localhost';
GRANT SELECT ON LibrarySystem.Books TO 'staff'@'localhost';
REVOKE INSERT ON LibrarySystem.BorrowRecords FROM 'staff'@'localhost';
```

# 4. Các mệnh đề và toán tử trong SELECT

```sql
SELECT 
    c.CategoryName,                   
    COUNT(b.BookID) AS TotalBooks     
FROM 
    Books b                           
JOIN 
    Categories c ON b.CategoryID = c.CategoryID  
WHERE 
    b.ReleaseYear >= 2000             
GROUP BY 
    c.CategoryName                    
HAVING 
    COUNT(b.BookID) > 0               
ORDER BY 
    TotalBooks DESC;                  
```

**Giải thích:**
1. **FROM** – chỉ định bảng lấy dữ liệu  
2. **JOIN** – kết nối các bảng liên quan  
3. **WHERE** – lọc dữ liệu thô trước khi nhóm  
4. **GROUP BY** – gộp nhóm dữ liệu có cùng đặc trưng  
5. **HAVING** – lọc sau khi nhóm  
6. **SELECT** – chỉ định cột hiển thị  
7. **ORDER BY** – sắp xếp kết quả theo thứ tự
