# Phần 1: Database (Tập trung vào CSDL Quan hệ)

Phần này tổng hợp các khái niệm cơ bản về CSDL quan hệ, cách cài đặt DBMS, và các câu lệnh SQL cốt lõi, bao gồm DDL, DML, DCL, và TCL.

## 1. Cài đặt Hệ quản trị CSDL (DBMS)

Để làm việc với CSDL quan hệ (RDBMS), bạn cần cài đặt một Hệ quản trị CSDL. Dưới đây là một số lựa chọn phổ biến và cách cài đặt:

### Các DBMS phổ biến

1.  **MySQL:**
    * **Mô tả:** Hệ CSDL quan hệ mã nguồn mở phổ biến nhất thế giới.
    * **Công cụ quản lý:** MySQL Workbench (chính thức), DBeaver, DataGrip.
2.  **SQL Server:**
    * **Mô tả:** Sản phẩm của Microsoft, rất mạnh mẽ, thường dùng trong môi trường doanh nghiệp (đặc biệt là các ứng dụng .NET).
    * **Công cụ quản lý:** SQL Server Management Studio (SSMS).
3.  **PostgreSQL:**
    * **Mô tả:** Một CSDL quan hệ mã nguồn mở mạnh mẽ, tập trung vào tính mở rộng và tuân thủ chuẩn SQL.
    * **Công cụ quản lý:** pgAdmin, DBeaver.

### Phương thức Cài đặt

Bạn có thể chọn một trong các phương thức sau để có môi trường thực hành:

1.  **Cài đặt Local (Toàn bộ):**
    * **MySQL Workbench:** Tải và cài đặt MySQL Server và Workbench (công cụ GUI) trực tiếp từ trang chủ MySQL.
    * **XAMPP / AMPPS / WAMP / MAMP:** Đây là các gói cài đặt "tất cả trong một", bao gồm Apache (web server), MySQL (database), và PHP/Perl. Đây là lựa chọn đơn giản để bắt đầu.
    * **SQL Server Express Edition:** Bản miễn phí của SQL Server, cài đặt kèm với SSMS.

2.  **Sử dụng Container (Docker):**
    * **Mô tả:** Cách tiếp cận hiện đại, linh hoạt. Bạn có thể khởi chạy một container chứa (MySQL, SQL Server, Postgres) trong vài giây mà không làm ảnh hưởng đến máy chính.
    * **Yêu cầu:** Cài đặt Docker Desktop.

3.  **Sử dụng Dịch vụ Online (DBaaS - Database as a Service):**
    * **Mô tả:** Không cần cài đặt, bạn chỉ cần đăng ký tài khoản và sử dụng CSDL trên đám mây.
    * **Ví dụ:** **Supabase** (cung cấp CSDL Postgres), Amazon RDS, Google Cloud SQL, Azure SQL Database.

---

## 2. Bài toán thực hành: CSDL Quản lý Thư viện

Để thực hành các lệnh SQL bên dưới, chúng ta sẽ tạo một CSDL đơn giản tên là `LibraryDB` để quản lý việc mượn sách.

CSDL này sẽ bao gồm 4 bảng:

1.  **`Authors`**: Lưu thông tin tác giả.
    * `AuthorID` (Khóa chính)
    * `AuthorName` (Tên tác giả)
2.  **`Books`**: Lưu thông tin sách.
    * `BookID` (Khóa chính)
    * `Title` (Tựa đề)
    * `Genre` (Thể loại)
    * `AuthorID` (Khóa ngoại, liên kết đến bảng `Authors`)
3.  **`Members`**: Lưu thông tin độc giả.
    * `MemberID` (Khóa chính)
    * `MemberName` (Tên độc giả)
    * `Email`
4.  **`Borrows`**: Lưu thông tin mượn/trả sách.
    * `BorrowID` (Khóa chính)
    * `BookID` (Khóa ngoại)
    * `MemberID` (Khóa ngoại)
    * `BorrowDate` (Ngày mượn)
    * `ReturnDate` (Ngày trả, có thể `NULL` nếu chưa trả)

---

## 3. Các câu lệnh SQL

### 3.1. Định nghĩa Dữ liệu (DDL - Data Definition Language)

DDL dùng để định nghĩa, tạo mới, thay đổi, và xóa cấu trúc của các đối tượng CSDL (như Bảng, View, Index).

#### CREATE TABLE
Tạo các bảng cho bài toán `LibraryDB`.

```sql
/* Tạo bảng Tác giả */
CREATE TABLE Authors (
    AuthorID INT PRIMARY KEY AUTO_INCREMENT, -- AUTO_INCREMENT cho MySQL
    /* AuthorID INT PRIMARY KEY IDENTITY(1,1), */ -- Dùng IDENTITY cho SQL Server
    AuthorName VARCHAR(100) NOT NULL
);

/* Tạo bảng Độc giả */
CREATE TABLE Members (
    MemberID INT PRIMARY KEY AUTO_INCREMENT,
    MemberName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE
);

/* Tạo bảng Sách (với Khóa ngoại) */
CREATE TABLE Books (
    BookID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    Genre VARCHAR(50),
    AuthorID INT,
    FOREIGN KEY (AuthorID) REFERENCES Authors(AuthorID)
);

/* Tạo bảng Lượt mượn (với nhiều Khóa ngoại) */
CREATE TABLE Borrows (
    BorrowID INT PRIMARY KEY AUTO_INCREMENT,
    BookID INT,
    MemberID INT,
    BorrowDate DATE NOT NULL,
    ReturnDate DATE, /* Có thể NULL vì sách chưa được trả */
    FOREIGN KEY (BookID) REFERENCES Books(BookID),
    FOREIGN KEY (MemberID) REFERENCES Members(MemberID)
);
````

#### ALTER TABLE

Sửa đổi cấu trúc của một bảng đã tồn tại (thêm, xóa, sửa cột).

```sql
/* Thêm một cột 'JoinDate' vào bảng Members */
ALTER TABLE Members
ADD COLUMN JoinDate DATE;

/* Sửa kiểu dữ liệu của một cột */
ALTER TABLE Books
MODIFY COLUMN Genre VARCHAR(75); /* (Cú pháp MySQL) */
/* ALTER TABLE Books ALTER COLUMN Genre VARCHAR(75); */ /* (Cú pháp SQL Server) */

/* Xóa một cột */
ALTER TABLE Members
DROP COLUMN JoinDate;
```

#### DROP TABLE

Xóa hoàn toàn một bảng (bao gồm cả dữ liệu và cấu trúc).

```sql
/* Xóa bảng (ví dụ, nếu tạo sai) */
DROP TABLE Borrows;
```

#### CREATE VIEW / DROP VIEW (Tham khảo thêm)

View (Khung nhìn) là một bảng ảo, được định nghĩa bởi một câu lệnh `SELECT`.

```sql
/* Tạo View để xem sách nào đang được mượn và ai mượn */
CREATE VIEW V_BooksOnLoan AS
SELECT 
    b.Title, 
    m.MemberName, 
    br.BorrowDate
FROM Borrows br
JOIN Books b ON br.BookID = b.BookID
JOIN Members m ON br.MemberID = m.MemberID
WHERE br.ReturnDate IS NULL;

/* Truy vấn View như một bảng bình thường */
SELECT * FROM V_BooksOnLoan;

/* Xóa View */
DROP VIEW V_BooksOnLoan;
```

-----

### 3.2. Thao tác Dữ liệu (DML - Data Manipulation Language)

DML dùng để thêm, sửa, xóa và truy vấn dữ liệu *bên trong* các bảng.

#### INSERT

Thêm dữ liệu (hàng) mới vào bảng.

```sql
/* Thêm tác giả */
INSERT INTO Authors (AuthorName) 
VALUES ('Nguyễn Nhật Ánh'), ('J.K. Rowling');

/* Thêm độc giả */
INSERT INTO Members (MemberName, Email)
VALUES ('Trần Văn An', 'an.tv@email.com'), ('Lê Thị Bích', 'bich.lt@email.com');

/* Thêm sách */
INSERT INTO Books (Title, Genre, AuthorID)
VALUES 
    ('Mắt Biếc', 'Tiểu thuyết', 1), 
    ('Harry Potter and the Sorcerer''s Stone', 'Fantasy', 2);

/* Thêm lượt mượn */
INSERT INTO Borrows (BookID, MemberID, BorrowDate)
VALUES (1, 1, '2025-10-20'); -- An mượn Mắt Biếc
```

#### SELECT

Truy vấn (lấy) dữ liệu từ bảng. (Chi tiết các mệnh đề ở mục 4).

```sql
/* Lấy tất cả thông tin sách */
SELECT * FROM Books;

/* Lấy tên sách và thể loại */
SELECT Title, Genre FROM Books;
```

#### UPDATE

Cập nhật dữ liệu đã tồn tại trong bảng.

```sql
/* Cập nhật ngày trả sách cho lượt mượn ID 1 */
UPDATE Borrows
SET ReturnDate = '2025-10-25'
WHERE BorrowID = 1;

/* Cảnh báo: Luôn luôn dùng mệnh đề WHERE khi UPDATE. 
Nếu không, bạn sẽ cập nhật TOÀN BỘ các hàng trong bảng!
*/
```

#### DELETE

Xóa dữ liệu (hàng) khỏi bảng.

```sql
/* Xóa độc giả có MemberID là 2 */
DELETE FROM Members
WHERE MemberID = 2;

/* Cảnh báo: Luôn luôn dùng mệnh đề WHERE khi DELETE. 
Nếu không, bạn sẽ xóa TOÀN BỘ dữ liệu trong bảng!
*/
```

-----

### 3.3. Trigger, Transaction và Procedure

Đây là các khái niệm lập trình nâng cao trong CSDL.

#### Giao dịch (Transaction) - TCL

Một giao dịch là một nhóm các lệnh SQL phải thành công *toàn bộ* (COMMIT) hoặc thất bại *toàn bộ* (ROLLBACK). Điều này đảm bảo tính toàn vẹn dữ liệu (Atomicity).

  * **`COMMIT`**: Lưu vĩnh viễn các thay đổi của giao dịch.
  * **`ROLLBACK`**: Hủy bỏ các thay đổi đã thực hiện trong giao dịch.
  * **`SAVE TRANSACTION` / `SAVEPOINT`**: Đặt một điểm lưu tạm thời trong giao dịch.

<!-- end list -->

```sql
/* Bắt đầu một giao dịch (MySQL/Postgres) */
START TRANSACTION;

/* Giả sử An (MemberID 1) mượn 2 cuốn sách (BookID 1 và 2)
Chúng ta muốn cả 2 lượt mượn phải thành công, hoặc không cái nào cả.
*/
INSERT INTO Borrows (BookID, MemberID, BorrowDate) VALUES (1, 1, CURDATE());
INSERT INTO Borrows (BookID, MemberID, BorrowDate) VALUES (2, 1, CURDATE());

/* Nếu không có lỗi nào xảy ra, chúng ta xác nhận giao dịch
*/
COMMIT;

/* Nếu có lỗi (ví dụ: BookID 2 không tồn tại), 
lệnh thứ 2 sẽ thất bại và chúng ta có thể hủy bỏ toàn bộ giao dịch
*/
-- ROLLBACK;
```

#### Stored Procedure (Thủ tục lưu trữ)

Một khối lệnh SQL được đặt tên, lưu trữ và có thể được gọi lại nhiều lần.

```sql
/* (Cú pháp MySQL/MariaDB) */
/* Tạo một procedure để ghi nhận việc mượn sách */
DELIMITER //
CREATE PROCEDURE sp_BorrowBook (
    IN p_BookID INT,
    IN p_MemberID INT
)
BEGIN
    /* Chúng ta có thể thêm logic kiểm tra (ví dụ: sách còn không?) ở đây */
    INSERT INTO Borrows (BookID, MemberID, BorrowDate)
    VALUES (p_BookID, p_MemberID, CURDATE());
    
    /* Có thể cập nhật trạng thái sách */
    -- UPDATE Books SET Status = 'On Loan' WHERE BookID = p_BookID;
    
END //
DELIMITTER ;

/* Gọi procedure để mượn sách */
CALL sp_BorrowBook(2, 1);
```

*(**`CREATE FUNCTION`** tương tự như `CREATE PROCEDURE` nhưng bắt buộc phải trả về một giá trị).*

#### Trigger (Trình kích hoạt)

Một procedure đặc biệt, tự động chạy khi một sự kiện DML (INSERT, UPDATE, DELETE) xảy ra trên một bảng.

```sql
/* Tạo một trigger ngăn cản việc xóa tác giả 
nếu họ vẫn còn sách trong bảng Books 
*/
DELIMITER //
CREATE TRIGGER trg_BeforeDeleteAuthor
BEFORE DELETE ON Authors
FOR EACH ROW
BEGIN
    DECLARE book_count INT;

    /* Đếm số sách của tác giả sắp bị xóa (OLD.AuthorID) */
    SELECT COUNT(*) INTO book_count 
    FROM Books 
    WHERE AuthorID = OLD.AuthorID; 

    /* Nếu họ vẫn còn sách (count > 0), thì ném lỗi và hủy hành động DELETE */
    IF book_count > 0 THEN
        /* Ném ra một lỗi (SQLSTATE '45000' là lỗi tùy chỉnh) */
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Không thể xóa tác giả. Tác giả này vẫn còn sách.';
    END IF;
END //
DELIMITTER ;
```

-----

### 3.4. Điều khiển Truy cập (DCL - Data Control Language)

DCL dùng để quản lý quyền (ai được phép làm gì) trên CSDL.

```sql
/* (Giả sử bạn đã tạo một người dùng 'nhan_vien' trên hệ thống) */
/* CREATE USER 'nhan_vien'@'localhost' IDENTIFIED BY 'matkhau123'; */


/* Cấp quyền (GRANT) */
/* Cho phép 'nhan_vien' chỉ được SELECT và INSERT trên bảng Borrows */
GRANT SELECT, INSERT ON LibraryDB.Borrows TO 'nhan_vien'@'localhost';

/* Cấp tất cả quyền trên toàn bộ CSDL LibraryDB */
GRANT ALL PRIVILEGES ON LibraryDB.* TO 'nhan_vien'@'localhost';

/* Thu hồi quyền (REVOKE) */
/* Thu hồi lại quyền INSERT */
REVOKE INSERT ON LibraryDB.Borrows FROM 'nhan_vien'@'localhost';

/* Thu hồi tất cả quyền */
REVOKE ALL PRIVILEGES ON LibraryDB.* FROM 'nhan_vien'@'localhost';
```

-----

## 4\. Các toán tử và mệnh đề truy vấn (SELECT)

Đây là các thành phần chính để xây dựng một câu lệnh `SELECT` phức tạp, theo thứ tự thực thi logic:

```sql
SELECT 
    b.Genre, -- (5) Chọn cột Thể loại
    COUNT(b.BookID) AS TotalBooks -- (5) Đếm số sách và đặt tên là TotalBooks
FROM 
    Books b -- (1) FROM: Chỉ định bảng chính (Books, bí danh 'b')
JOIN 
    Authors a ON b.AuthorID = a.AuthorID -- (2) JOIN: Kết nối với bảng Authors
WHERE 
    a.AuthorName <> 'Nguyễn Nhật Ánh' -- (3) WHERE: Lọc dữ liệu *trước khi* gộp nhóm
    AND b.Genre IS NOT NULL -- (AND, OR, <>, IS NOT NULL là các toán tử)
GROUP BY 
    b.Genre -- (4) GROUP BY: Gộp nhóm các sách theo Thể loại
HAVING 
    COUNT(b.BookID) >= 1 -- (6) HAVING: Lọc dữ liệu *sau khi* đã gộp nhóm
ORDER BY 
    TotalBooks DESC; -- (7) ORDER BY: Sắp xếp kết quả (DESC = Giảm dần)
```

1.  **`FROM`**: Chỉ định bảng nguồn để lấy dữ liệu.
2.  **`JOIN`**: (INNER JOIN, LEFT JOIN, ...) Dùng để kết hợp dữ liệu từ hai hay nhiều bảng dựa trên một điều kiện (mệnh đề `ON`).
3.  **`WHERE`**: Lọc các *hàng* dựa trên một điều kiện. Các toán tử phổ biến: `=`, `!=` (hoặc `<>`), `>`, `<`, `IN`, `BETWEEN`, `LIKE`, `IS NULL`.
4.  **`AND` / `OR`**: Các toán tử logic để kết hợp nhiều điều kiện trong `WHERE` hoặc `HAVING`.
5.  **`GROUP BY`**: Gộp các hàng có cùng giá trị (thường dùng với các hàm tổng hợp như `COUNT()`, `SUM()`, `AVG()`, `MAX()`, `MIN()`).
6.  **`HAVING`**: Lọc các *nhóm* đã được tạo bởi `GROUP BY` (hoạt động giống `WHERE` nhưng dành cho nhóm).
7.  **`SELECT`**: Chỉ định các cột sẽ được trả về.
8.  **`ORDER BY`**: Sắp xếp tập kết quả trả về (thứ tự `ASC` - Tăng dần (mặc định) hoặc `DESC` - Giảm dần).
