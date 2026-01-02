-- Tạo database và sử dụng
DROP DATABASE IF EXISTS library;
CREATE DATABASE library CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library;

-- Tạo Table danh mục cơ bản
CREATE TABLE authors (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    birth_year INT NULL
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE publishers (
    publisher_id INT AUTO_INCREMENT PRIMARY KEY,
    publisher_name VARCHAR(200) NOT NULL,
    country VARCHAR(100)
);

-- Bảng books có mqh many-to-many với tác giả thông qua book_authors
CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE,
    title VARCHAR(255) NOT NULL,
    publisher_id INT,
    published_year INT,
    category_id INT,
    CONSTRAINT fk_books_pub FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id),
    CONSTRAINT fk_books_cat FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE book_authors (
    book_id INT NOT NULL,
    author_id INT NOT NULL,
    role_desc VARCHAR(100) NULL,          
    PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_ba_book FOREIGN KEY (book_id) REFERENCES books(book_id),
    CONSTRAINT fk_ba_author FOREIGN KEY (author_id) REFERENCES authors(author_id)
);

-- Bảng bản sao vật lý của sách => dùng để quản lý từng quyển
CREATE TABLE book_copies (
    copy_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    shelf_code VARCHAR(50) NOT NULL,       -- vị trí kệ sách
    status ENUM('AVAILABLE','BORROWED','LOST','MAINTENANCE') DEFAULT 'AVAILABLE',
    CONSTRAINT fk_copies_book FOREIGN KEY (book_id) REFERENCES books(book_id),
    INDEX idx_copies_book_status (book_id, status)
);

-- Bảng thành viên và nhân viên
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    phone VARCHAR(30),
    joined_date DATE NOT NULL DEFAULT (CURRENT_DATE)
);

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL
);

-- Phiếu mượn và chi tiết
CREATE TABLE loans (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    staff_id INT NOT NULL,
    -- nhân viên lập phiếu mượn
    loan_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date DATETIME NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_loans_member FOREIGN KEY (member_id) REFERENCES members(member_id),
    CONSTRAINT fk_loans_staff FOREIGN KEY (staff_id) REFERENCES staff(staff_id),
    INDEX idx_loans_member_due (member_id, due_date)
);

CREATE TABLE loan_items (
    loan_item_id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id INT NOT NULL,
    copy_id INT NOT NULL,
    returned_date DATETIME NULL,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    CONSTRAINT fk_li_loan FOREIGN KEY (loan_id) REFERENCES loans(loan_id),
    CONSTRAINT fk_li_copy FOREIGN KEY (copy_id) REFERENCES book_copies(copy_id),
    UNIQUE KEY uq_loan_copy_once (loan_id, copy_id)
);

-- ALTER TABLE vào trong table books cột title 
ALTER TABLE books ADD INDEX idx_books_title (title);

-- Lệnh INSERT INTO : dùng để thêm các bản ghi
-- Cho bảng authors
INSERT INTO authors(full_name,birth_year) VALUES
('J.K. Rowling',1965),('Nguyễn Nhật Ánh',1955),('Haruki Murakami',1949);

-- Cho bảng categories
INSERT INTO categories(category_name) VALUES
('Thiếu nhi'),('Văn học'),('Khoa học'),('CNTT');

-- Cho bảng publishers
INSERT INTO publishers(publisher_name,country) VALUES
('NXB Trẻ','Việt Nam'),('Bloomsbury','UK'),('Kodansha','Japan');

-- Cho bảng books
INSERT INTO books(isbn,title,publisher_id,published_year,category_id) VALUES
('9780747532743','Harry Potter and the Philosopher''s Stone',2,1997,1),
('9786041146445','Cho tôi xin một vé đi tuổi thơ',1,2008,1),
('9784061860252','Norwegian Wood',3,1987,2);

-- Cho bảng book_authors
INSERT INTO book_authors(book_id,author_id,role_desc) VALUES
(1,1,'Author'),
(2,2,'Tác giả'),
(3,3,'Author');

-- Thêm 5 bản sao cho mỗi sách
INSERT INTO book_copies(book_id,shelf_code) VALUES
(1,'A1-HP-01'),(1,'A1-HP-02'),(1,'A1-HP-03'),(1,'A1-HP-04'),(1,'A1-HP-05'),
(2,'B2-NNA-01'),(2,'B2-NNA-02'),(2,'B2-NNA-03'),(2,'B2-NNA-04'),(2,'B2-NNA-05'),
(3,'C3-NW-01'),(3,'C3-NW-02'),(3,'C3-NW-03'),(3,'C3-NW-04'),(3,'C3-NW-05');

-- Cho bảng members
INSERT INTO members(full_name,email,phone) VALUES
('Bùi Anh Đức','duc@gmail.com','0900000001'),
('Phạm Văn Tư','tu@gmail.com','0900000002');

INSERT INTO staff(full_name,email) VALUES
('Thủ thư A','staffA@gmail.com'),
('Thủ thư B','staffB@example.com');

-- Tìm sách theo tiêu đề (sử dụng FROM, WHERE, ORDER BY, OR)
SELECT 
		b.book_id,
		b.title,
        c.category_name,
        b.published_year,
        p.publisher_name
FROM books b
JOIN categories c ON b.category_id = c.category_id
LEFT JOIN publishers p ON b.publisher_id = p.publisher_id
WHERE (b.title LIKE '%Potter%' OR c.category_name = 'Văn học')
ORDER BY b.published_year ASC;

-- Thống kê số bản sao sẵn có theo sách => sử dụng GROUP BY, HAVING
SELECT 
		b.book_id,
        b.title,
        SUM(bc.status = 'AVAILABLE') AS available_copies
FROM books b
JOIN book_copies bc ON bc.book_id = b.book_id
GROUP BY b.book_id, b.title
HAVING available_copies > 0;

-- Cập nhật thông tin liên hệ thành viên
UPDATE members
SET phone = '0945268805'
WHERE email = 'duc@gmail.com';

-- SELECT * FROM members => Dùng để test câu lệnh vừa update data => Done

-- Xoá 1 bản sao bị mất
DELETE FROM book_copies
WHERE copy_id = 1 AND status = 'LOST';


-- VIEW 
CREATE OR REPLACE VIEW v_book_overview AS
SELECT b.book_id, b.isbn, b.title,
       c.category_name,
       p.publisher_name,
       GROUP_CONCAT(a.full_name ORDER BY a.full_name SEPARATOR ', ') AS authors
FROM books b
LEFT JOIN categories c ON c.category_id = b.category_id
LEFT JOIN publishers p ON p.publisher_id = b.publisher_id
LEFT JOIN book_authors ba ON ba.book_id = b.book_id
LEFT JOIN authors a ON a.author_id = ba.author_id
GROUP BY b.book_id, b.isbn, b.title, c.category_name, p.publisher_name;

-- FUNCTION : tính tiền phạt theo số ngày chậm trễ (5k/ngày)
DROP FUNCTION IF EXISTS fn_calc_fine;
DELIMITER $$
CREATE FUNCTION fn_calc_fine(late_days INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
NO SQL
SQL SECURITY INVOKER
BEGIN
    DECLARE rate DECIMAL(10,2) DEFAULT 5000.00;
    IF late_days IS NULL OR late_days <= 0 THEN
        RETURN 0.00;
    END IF;
    RETURN late_days * rate;
END$$
DELIMITER ;

-- PROCEDURE mượn và trả sách (có transaction)
-- MƯỢN SÁCH: member mượn 1 copy, due_date được truyền vào
DELIMITER $$
CREATE PROCEDURE sp_borrow_book(
    IN p_member_id INT,
    IN p_staff_id  INT,
    IN p_copy_id   INT,
    IN p_due_date  DATETIME
)
BEGIN
    DECLARE v_book_id INT;

    START TRANSACTION;

    -- Kiểm tra copy còn AVAILABLE
    IF (SELECT status FROM book_copies WHERE copy_id = p_copy_id FOR UPDATE) <> 'AVAILABLE' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Copy is not AVAILABLE';
    END IF;

    -- Lấy book_id (nếu cần cho nghiệp vụ khác)
    SELECT book_id INTO v_book_id FROM book_copies WHERE copy_id = p_copy_id;

    -- Tạo loan nếu cần (1 phiếu/1 lần mượn)
    INSERT INTO loans(member_id, staff_id, due_date, note)
    VALUES(p_member_id, p_staff_id, p_due_date, 'Borrow');

    -- Thêm item
    INSERT INTO loan_items(loan_id, copy_id)
    VALUES(LAST_INSERT_ID(), p_copy_id);

    -- Đánh dấu copy đã mượn
    UPDATE book_copies SET status = 'BORROWED' WHERE copy_id = p_copy_id;

    COMMIT;
END$$
DELIMITER ;

-- TRẢ SÁCH: set returned_date, tính tiền phạt bằng hàm fn_calc_fine(ngày trễ) vừa viết ở trên
DELIMITER $$
CREATE PROCEDURE sp_return_book(
    IN p_loan_item_id INT
)
BEGIN
    DECLARE v_loan_id INT;
    DECLARE v_copy_id INT;
    DECLARE v_due_date DATETIME;
    DECLARE v_late_days INT;

    START TRANSACTION;

    -- Khoá dòng loan_item
    SELECT li.loan_id, li.copy_id
    INTO v_loan_id, v_copy_id
    FROM loan_items li
    WHERE li.loan_item_id = p_loan_item_id
    FOR UPDATE;

    -- due_date từ loans
    SELECT due_date INTO v_due_date FROM loans WHERE loan_id = v_loan_id;

    -- Cập nhật returned_date
    UPDATE loan_items
    SET returned_date = NOW()
    WHERE loan_item_id = p_loan_item_id;

    -- Tính phạt
    SET v_late_days = DATEDIFF(NOW(), v_due_date);
    UPDATE loan_items
    SET fine_amount = fn_calc_fine(v_late_days)
    WHERE loan_item_id = p_loan_item_id;

    -- Mở khoá copy
    UPDATE book_copies
    SET status = 'AVAILABLE'
    WHERE copy_id = v_copy_id;

    COMMIT;
END$$
DELIMITER ;


-- Điều khiển Truy cập: GRANT, REVOKE
-- Tạo user thủ thư
CREATE USER IF NOT EXISTS 'new_librarian'@'%' IDENTIFIED BY 'StrongPass!';

-- Cấp quyền cơ bản
GRANT SELECT, INSERT, UPDATE ON library.* TO 'new_librarian'@'%';

-- Ví dụ thu hồi quyền DELETE trên loan_items
REVOKE DELETE ON library.loan_items FROM 'new_librarian'@'%';
-- Áp dụng thay đổi
FLUSH PRIVILEGES;


-- Kiểm tra user vừa create, thấy hiện ra new_librarian 
SELECT user, host, plugin, account_locked, password_expired
FROM mysql.user
ORDER BY user, host;

-- Mượn/trả sử dụng function vừa xây dựng : 
-- Mượn 1 bản sao: member 1, staff 1, copy_id = 2, hạn trả 7 ngày
CALL sp_borrow_book(1, 1, 2, DATE_ADD(NOW(), INTERVAL 7 DAY));

-- Trả sách: truyền loan_item_id tương ứng (ở đây truyền vào là 1)
CALL sp_return_book(1);

