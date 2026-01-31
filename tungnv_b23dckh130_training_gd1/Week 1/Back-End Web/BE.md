# Backend Web Development

## Mục Lục
- [Phần 1: Database (CSDL Quan Hệ)](#phần-1-database-csdl-quan-hệ)
  - [Cài đặt DBMS](#cài-đặt-dbms)
  - [Thực hành với SQL](#thực-hành-tạo-csdl)
- [Phần 2: OOP](#phần-2-oop)
  - [OOP trong Java](#oop-trong-java)
  - [Dependency Injection và Inversion of Control](#dependency-injection-và-inversion-of-control)

---

## Phần 1: Database (CSDL Quan Hệ)

### Cài đặt DBMS

Có thể cài đặt một trong các hệ quản trị CSDL sau:
- **MySQL**
- **SQL Server**

#### Các phương thức cài đặt:
- **Online**: Supabase, Cloud Database Services
- **Local**:
  - MySQL Workbench
  - XAMPP
  - AMPPS
  - Docker Container

### Thực hành tạo CSDL

Tạo một cơ sở dữ liệu để quản lý thư viện với đầy đủ các tính năng: quản lý sách, tác giả, thành viên, phiếu mượn/trả.

#### Tổng quan Cơ sở dữ liệu

**Cơ sở dữ liệu gồm 6 bảng:**
- `author`: Quản lý tác giả
- `category`: Quản lý thể loại sách
- `book`: Quản lý sách (liên kết với author và category)
- `member`: Quản lý thành viên thư viện
- `loan`: Quản lý phiếu mượn sách
- `loan_detail`: Chi tiết từng cuốn sách trong phiếu mượn

---

#### Phần 1: Khởi tạo Database

```sql
CREATE DATABASE library_db
USE library_db;
```

---

#### Phần 2: Tạo Bảng (DDL - Data Definition Language)

##### 2.1. Bảng Author (Tác giả)

```sql
CREATE TABLE author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    country    VARCHAR(100)
);
```

---

##### 2.2. Bảng Category (Thể loại)


```sql
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE
);
```

---

##### 2.3. Bảng Book (Sách)

```sql
CREATE TABLE book (
    book_id         INT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    author_id       INT NOT NULL,
    category_id     INT NOT NULL,
    publish_year    YEAR,
    stock_total     INT NOT NULL DEFAULT 0,
    stock_available INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_book_author   FOREIGN KEY (author_id)   REFERENCES author(author_id),
    CONSTRAINT fk_book_category FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT chk_stock CHECK (stock_available >= 0 AND stock_total >= 0 AND stock_available <= stock_total)
);
```

---

##### 2.4. Bảng Member (Thành viên)

```sql
CREATE TABLE member (
    member_id  INT AUTO_INCREMENT PRIMARY KEY,
    full_name  VARCHAR(120) NOT NULL,
    email      VARCHAR(120) NOT NULL UNIQUE,
    joined_at  DATE NOT NULL DEFAULT (CURRENT_DATE)
);
```

---

##### 2.5. Bảng Loan (Phiếu mượn)

```sql
CREATE TABLE loan (
    loan_id     INT AUTO_INCREMENT PRIMARY KEY,
    member_id   INT NOT NULL,
    loan_date   DATE NOT NULL DEFAULT (CURRENT_DATE),
    due_date    DATE NOT NULL,
    return_date DATE NULL,
    status      ENUM('OPEN','CLOSED') NOT NULL DEFAULT 'OPEN',
    CONSTRAINT fk_loan_member FOREIGN KEY (member_id) REFERENCES member(member_id)
);
```

---

##### 2.6. Bảng Loan_Detail (Chi tiết phiếu mượn)

```sql
CREATE TABLE loan_detail (
    loan_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id        INT NOT NULL,
    book_id        INT NOT NULL,
    qty            INT NOT NULL CHECK (qty > 0),
    returned_qty   INT NOT NULL DEFAULT 0 CHECK (returned_qty >= 0),
    CONSTRAINT fk_ld_loan FOREIGN KEY (loan_id) REFERENCES loan(loan_id) ON DELETE CASCADE,
    CONSTRAINT fk_ld_book FOREIGN KEY (book_id) REFERENCES book(book_id),
    CONSTRAINT chk_returned_qty CHECK (returned_qty <= qty)
);
```

---

#### Phần 3: Thêm Dữ Liệu Mẫu (DML - Data Manipulation)

```sql
INSERT INTO author(name, country) VALUES
('Tố Hữu','Việt Nam'),
('J. K. Rowling','United Kingdom'),
('George Orwell','United Kingdom');

INSERT INTO category(name) VALUES
('Novel'),('Fantasy'),('Dystopia');

INSERT INTO book(title, author_id, category_id, publish_year, stock_total, stock_available) VALUES
('Norwegian Wood', 1, 1, 1987, 10, 10),
('Harry Potter and the Philosopher''s Stone', 2, 2, 1997, 8, 8),
('1984', 3, 3, 1949, 6, 6);

INSERT INTO member(full_name, email) VALUES
('Nguyen Van A','a@example.com'),
('Tran Thi B','b@example.com');
```

---

#### Phần 4: Trigger, Transaction và Procedure

#### Giao dịch (Transaction)

Một giao dịch là một nhóm các lệnh SQL phải thành công *toàn bộ* (COMMIT) hoặc thất bại *toàn bộ* (ROLLBACK).

```sql
/* Bắt đầu một giao dịch */
START TRANSACTION;

/* Thành viên mượn sách - cập nhật tồn kho và tạo phiếu mượn */
INSERT INTO loan(member_id, due_date, status) VALUES (1, '2025-11-10', 'OPEN');

INSERT INTO loan_detail(loan_id, book_id, qty) VALUES (1, 1, 2);

UPDATE book SET stock_available = stock_available - 2 WHERE book_id = 1;

/* Nếu không có lỗi, xác nhận giao dịch */
COMMIT;

/* Nếu có lỗi, hủy bỏ toàn bộ giao dịch */
-- ROLLBACK;
```

---

#### Stored Procedure (Thủ tục lưu trữ)

Một khối lệnh SQL được đặt tên, có thể được gọi lại nhiều lần.

```sql
/* Tạo procedure để mượn sách */
DELIMITER //
CREATE PROCEDURE sp_BorrowBook (
    IN p_member_id INT,
    IN p_book_id INT,
    IN p_qty INT,
    IN p_due_date DATE
)
BEGIN
    /* Tạo phiếu mượn */
    INSERT INTO loan(member_id, due_date, status) 
    VALUES (p_member_id, p_due_date, 'OPEN');
    
    /* Thêm chi tiết mượn */
    INSERT INTO loan_detail(loan_id, book_id, qty) 
    VALUES (LAST_INSERT_ID(), p_book_id, p_qty);
    
    /* Giảm số lượng sách có sẵn */
    UPDATE book SET stock_available = stock_available - p_qty 
    WHERE book_id = p_book_id;
END //
DELIMITER ;

/* Gọi procedure */
CALL sp_BorrowBook(1, 1, 2, '2025-11-10');
```

---

#### CREATE FUNCTION

Tương tự procedure nhưng bắt buộc phải trả về một giá trị.

```sql
/* Tạo function tính phí trễ hạn (3000đ/ngày) */
DELIMITER //
CREATE FUNCTION fn_LateFee (p_days_late INT)
RETURNS INT
DETERMINISTIC
BEGIN
    RETURN p_days_late * 3000;
END //
DELIMITER ;

/* Sử dụng function trong SELECT */
SELECT fn_LateFee(5) AS PhiTreHan; -- Kết quả: 15000
```

---

#### Trigger (Trình kích hoạt)

Tự động chạy khi một sự kiện DML (INSERT, UPDATE, DELETE) xảy ra.

```sql
/* Tạo trigger ngăn xóa tác giả nếu họ vẫn còn sách */
DELIMITER //
CREATE TRIGGER trg_BeforeDeleteAuthor
BEFORE DELETE ON author
FOR EACH ROW
BEGIN
    DECLARE book_count INT;
    
    /* Đếm số sách của tác giả sắp bị xóa */
    SELECT COUNT(*) INTO book_count 
    FROM book 
    WHERE author_id = OLD.author_id;
    
    /* Nếu họ vẫn còn sách, ném lỗi và hủy hành động DELETE */
    IF book_count > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Không thể xóa tác giả. Tác giả này vẫn còn sách.';
    END IF;
END //
DELIMITER ;
```

---

#### Phần 5: CREATE VIEW / DROP VIEW

View (Khung nhìn) là một bảng ảo, được định nghĩa bởi một câu lệnh SELECT.

```sql
/* Tạo View để xem các phiếu mượn đang mở */
CREATE VIEW V_CurrentLoans AS
SELECT 
    l.loan_id,
    m.full_name,
    m.email,
    l.loan_date,
    l.due_date
FROM loan l
JOIN member m ON l.member_id = m.member_id
WHERE l.status = 'OPEN';

/* Truy vấn View như một bảng bình thường */
SELECT * FROM V_CurrentLoans;

/* Xóa View */
DROP VIEW V_CurrentLoans;
```

---

#### Phần 6: Các toán tử và mệnh đề truy vấn (SELECT)

Các thành phần chính để xây dựng câu lệnh SELECT, theo thứ tự thực thi logic:


```sql
/* Tìm sách theo tên hoặc tác giả */
SELECT b.title, a.name AS author_name, b.publish_year
FROM book b
JOIN author a ON b.author_id = a.author_id
WHERE b.title LIKE '%Harry%' OR a.name LIKE '%Orwell%'
ORDER BY b.publish_year DESC;

/* Thống kê số sách được mượn */
SELECT b.title, COUNT(ld.loan_detail_id) AS times_borrowed
FROM book b
LEFT JOIN loan_detail ld ON b.book_id = ld.book_id
GROUP BY b.book_id, b.title
HAVING COUNT(ld.loan_detail_id) > 0
ORDER BY times_borrowed DESC;
```

---

#### Phần 7: UPDATE và DELETE

**UPDATE** - Cập nhật dữ liệu đã tồn tại trong bảng.

```sql
/* Cập nhật ngày trả sách cho phiếu mượn */
UPDATE loan
SET return_date = '2025-10-30', status = 'CLOSED'
WHERE loan_id = 1;

/* Cảnh báo: Luôn dùng WHERE khi UPDATE. 
   Nếu không, bạn sẽ cập nhật TOÀN BỘ các hàng trong bảng! */
```

**DELETE** - Xóa dữ liệu (hàng) khỏi bảng.

```sql
/* Xóa thành viên có member_id là 2 */
DELETE FROM member
WHERE member_id = 2;

/* Cảnh báo: Luôn dùng WHERE khi DELETE. 
   Nếu không, bạn sẽ xóa TOÀN BỘ dữ liệu trong bảng! */
```

---

#### Phần 8: ALTER TABLE và DROP TABLE

**ALTER TABLE** - Sửa đổi cấu trúc bảng đã tồn tại.

```sql
/* Thêm cột mới */
ALTER TABLE book
ADD COLUMN note VARCHAR(200);

/* Sửa kiểu dữ liệu của cột (MySQL) */
ALTER TABLE book
MODIFY COLUMN title VARCHAR(300);

/* Xóa một cột */
ALTER TABLE book
DROP COLUMN note;
```

**DROP TABLE** - Xóa hoàn toàn bảng (cả dữ liệu và cấu trúc).

```sql
/* Xóa bảng (ví dụ, nếu tạo sai) */
DROP TABLE loan_detail;
```

---

#### Phần 9: Phân Quyền (GRANT/REVOKE)

DCL dùng để quản lý quyền (ai được phép làm gì) trên CSDL.

```sql
/* (Giả sử đã tạo người dùng 'nhan_vien') */
-- CREATE USER 'nhan_vien'@'localhost' IDENTIFIED BY 'matkhau123';

/* GRANT - Cấp quyền */
/* Cho phép 'nhan_vien' chỉ được SELECT và INSERT trên bảng loan */
GRANT SELECT, INSERT ON library_db.loan TO 'nhan_vien'@'localhost';

/* Cấp tất cả quyền trên toàn bộ CSDL */
GRANT ALL PRIVILEGES ON library_db.* TO 'nhan_vien'@'localhost';

/* REVOKE - Thu hồi quyền */
/* Thu hồi lại quyền INSERT */
REVOKE INSERT ON library_db.loan FROM 'nhan_vien'@'localhost';

/* Thu hồi tất cả quyền */
REVOKE ALL PRIVILEGES ON library_db.* FROM 'nhan_vien'@'localhost';
```

## Phần 2: OOP

### OOP trong Java

#### Java Cơ Bản

##### 1. Object và Class

**Khái niệm:**
- **Class (Lớp)**: Là bản thiết kế (blueprint) hoặc khuôn mẫu để tạo ra các object. Class định nghĩa các thuộc tính (attributes) và hành vi (methods) mà các object sẽ có.
- **Object (Đối tượng)**: Là một thực thể cụ thể được tạo ra từ class. Mỗi object có trạng thái (giá trị của các thuộc tính) và hành vi (các phương thức) riêng.

```java
// Định nghĩa Class
public class Student {
    // Thuộc tính (attributes)
    private String name;
    private int age;
    private String studentId;
    
    // Constructor
    public Student(String name, int age, String studentId) {
        this.name = name;
        this.age = age;
        this.studentId = studentId;
    }
    
    // Phương thức (methods)
    public void study() {
        System.out.println(name + " is studying");
    }
    
    // Getter và Setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}

// Tạo Object
Student student1 = new Student("Nguyen Van A", 20, "SV001");
student1.study();
```

##### 2. Abstract Class

**Khái niệm:**
- **Abstract Class (Lớp trừu tượng)**: Là một class không thể tạo instance trực tiếp, chỉ dùng làm class cha để các class con kế thừa.
- **Đặc điểm**: Có thể chứa cả abstract methods (không có implementation) và concrete methods (có implementation đầy đủ).
- **Tại sao cần**: Dùng để tạo một "khung" chung cho các class liên quan, định nghĩa interface chung nhưng vẫn cho phép chia sẻ code implementation giữa các class con.
- **Khi nào dùng**: Khi có một nhóm class có chung một số hành vi nhưng cũng có những hành vi riêng biệt cần override.

```java
// Abstract Class
public abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    // Abstract method
    public abstract void makeSound();
    
    // Concrete method
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Kế thừa Abstract Class
public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println("Woof! Woof!");
    }
}
```

##### 3. Interface

**Khái niệm:**
- **Interface (Giao diện)**: Là một "hợp đồng" (contract) định nghĩa tập hợp các phương thức mà một class phải implement, nhưng không cung cấp implementation cụ thể.
- **Đặc điểm**: Tất cả phương thức trong interface đều là abstract (từ Java 8 có thể có default methods). Một class có thể implement nhiều interface.
- **Tại sao cần**: Giúp tách biệt "cái gì cần làm" khỏi "làm thế nào", tạo tính linh hoạt và giảm sự phụ thuộc giữa các class.
- **Interface vs Abstract Class**: Interface không có state, chỉ định nghĩa behavior. Abstract class có thể có cả state và behavior.
- **Khi nào dùng**: Khi muốn các class không liên quan có thể có chung behavior, hoặc khi cần multiple inheritance.

```java
// Interface
public interface Drawable {
    void draw();
    void resize(int width, int height);
}

public interface Movable {
    void move(int x, int y);
}

// Implement Interface
public class Circle implements Drawable, Movable {
    private int x, y, radius;
    
    @Override
    public void draw() {
        System.out.println("Drawing circle at (" + x + ", " + y + ")");
    }
    
    @Override
    public void resize(int width, int height) {
        this.radius = width / 2;
    }
    
    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

##### 4. Biến và Hàm

**Khái niệm:**
- **Biến Instance**: Thuộc về mỗi object riêng biệt, mỗi object có bản sao riêng. Phải tạo object để truy cập.
- **Biến Static**: Thuộc về class, được chia sẻ bởi tất cả instance. Chỉ có một bản sao duy nhất trong bộ nhớ.
- **Biến Final**: Hằng số, giá trị không thể thay đổi sau khi khởi tạo.
- **Phương thức Instance**: Gọi qua object, có thể truy cập cả biến instance và static.
- **Phương thức Static**: Gọi qua tên class, chỉ truy cập được biến/method static.
- **Tại sao cần**: Phân biệt rõ scope và lifecycle của biến/method, giúp quản lý bộ nhớ hiệu quả và tổ chức code logic hơn.

```java
public class Example {
    // Biến instance
    private int instanceVar;
    
    // Biến static
    private static int staticVar;
    
    // Biến final (hằng số)
    private static final int CONSTANT = 100;
    
    // Hàm instance
    public void instanceMethod() {
        System.out.println("Instance method");
    }
    
    // Hàm static
    public static void staticMethod() {
        System.out.println("Static method");
    }
}
```

##### 5. Input/Output (I/O)

**Khái niệm:**
- **I/O (Input/Output)**: Là quá trình đọc dữ liệu từ nguồn (console, file, network...) và ghi dữ liệu ra đích.
- **Stream**: Là dòng dữ liệu liên tục. Java sử dụng stream để xử lý I/O một cách thống nhất.
- **Scanner**: Class tiện lợi để đọc input từ nhiều nguồn khác nhau (console, file, string...).
- **BufferedReader/Writer**: Sử dụng buffer (bộ đệm) để tăng hiệu suất khi đọc/ghi file.
- **Exception Handling**: Thao tác I/O có thể gây lỗi nên cần xử lý exception (throws hoặc try-catch).
- **Tại sao cần**: Mọi ứng dụng đều cần tương tác với người dùng hoặc lưu trữ/đọc dữ liệu từ file.

```java
import java.util.Scanner;
import java.io.*;

public class IOExample {
    // Đọc từ bàn phím
    public static void readInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name);
        scanner.close();
    }
    
    // Đọc từ file
    public static void readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
    
    // In ra file
    public static void writeFile(String filename, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(content);
        writer.close();
    }
}
```

##### 6. Vòng lặp

**Khái niệm:**
- **Vòng lặp (Loop)**: Là cấu trúc điều khiển cho phép thực thi một đoạn code nhiều lần.
- **For loop**: Dùng khi biết trước số lần lặp. Cú pháp gọn, gộp khởi tạo, điều kiện và bước nhảy trong một dòng.
- **While loop**: Kiểm tra điều kiện trước khi thực hiện. Dùng khi chỉ biết điều kiện dừng, không biết số lần lặp.
- **Do-while loop**: Thực hiện ít nhất 1 lần trước khi kiểm tra điều kiện. Dùng khi muốn đảm bảo code chạy ít nhất 1 lần.
- **For-each loop**: Duyệt qua tất cả phần tử trong collection/array một cách đơn giản, không cần index.
- **Tại sao cần**: Giúp tránh việc viết code lặp đi lặp lại, xử lý dữ liệu trong collections, và thực hiện các tác vụ lặp đi lặp lại.

```java
public class Examples {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println("Count: " + i);
        }
        
        int j = 0;
        while (j < 5) {
            System.out.println("Count: " + j);
            j++;
        }
        
        int k = 0;
        do {
            System.out.println("Count: " + k);
            k++;
        } while (k < 5);
        
        int[] numbers = {1, 2, 3, 4, 5};
        for (int num : numbers) {
            System.out.println("Number: " + num);
        }
    }
}
```

### Các Tính Chất OOP

#### 1. Encapsulation (Đóng gói)

**Khái niệm:**
Encapsulation là việc ẩn giấu thông tin chi tiết bên trong của object và chỉ cho phép truy cập thông qua các phương thức công khai. Đây là một trong bốn tính chất cốt lõi của OOP.

**Mục đích:**
- Bảo vệ dữ liệu khỏi truy cập và chỉnh sửa không mong muốn
- Kiểm soát cách dữ liệu được đọc và ghi
- Dễ dàng thay đổi implementation mà không ảnh hưởng code bên ngoài
- Tăng tính bảo mật và toàn vẹn dữ liệu

```java
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        if (initialBalance > 0) {
            this.balance = initialBalance;
        }
    }
    
    // Public methods để truy cập và thay đổi balance
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}
```

#### 2. Inheritance (Kế thừa)

**Khái niệm:**
Inheritance là cơ chế cho phép một class kế thừa các thuộc tính và phương thức từ một class khác. Đây là một trong bốn tính chất cốt lõi của OOP.

**Mục đích:**
- Tái sử dụng code: Không cần viết lại code đã có ở class cha
- Tạo quan hệ phân cấp: Thể hiện mối quan hệ "is-a"
- Dễ bảo trì: Thay đổi ở class cha tự động áp dụng cho class con
- Mở rộng chức năng: Class con có thể thêm thuộc tính/phương thức riêng

```java
// Class cha
public class Vehicle {
    protected String brand;
    protected int year;
    
    public Vehicle(String brand, int year) {
        this.brand = brand;
        this.year = year;
    }
    
    public void start() {
        System.out.println("Vehicle is starting");
    }
}

// Class con kế thừa
public class Car extends Vehicle {
    private int numberOfDoors;
    
    public Car(String brand, int year, int numberOfDoors) {
        super(brand, year); // Gọi constructor của class cha
        this.numberOfDoors = numberOfDoors;
    }
    
    @Override
    public void start() {
        System.out.println("Car is starting with " + numberOfDoors + " doors");
    }
}
```

#### 3. Polymorphism (Đa hình)

**Khái niệm:**
Polymorphism (đa hình) là khả năng một object có thể có nhiều hình thái khác nhau. Cùng một method call có thể thực hiện các hành vi khác nhau tùy thuộc vào object thực tế. Đây là một trong bốn tính chất cốt lõi của OOP.

**Các loại Polymorphism:**
- **Compile-time (Static)**: Method overloading - cùng tên method nhưng khác tham số
- **Runtime (Dynamic)**: Method overriding - class con override method của class cha

**Mục đích:**
- Xử lý nhiều loại object khác nhau thông qua interface chung
- Code linh hoạt và dễ mở rộng

```java
public class Shape {
    public double calculateArea() {
        return 0;
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

// Sử dụng Polymorphism
public class Main {
    public static void main(String[] args) {
        Shape shape1 = new Rectangle(5, 10);
        Shape shape2 = new Circle(7);
        
        System.out.println("Rectangle area: " + shape1.calculateArea());
        System.out.println("Circle area: " + shape2.calculateArea());
    }
}
```

#### 4. Abstraction (Trừu tượng)

**Khái niệm:**
Abstraction là việc ẩn đi các chi tiết triển khai phức tạp và chỉ hiển thị các tính năng cần thiết cho người sử dụng. Đây là một trong bốn tính chất cốt lõi của OOP.

**Cách thực hiện:**
- Sử dụng Abstract Class: Có thể có cả abstract và concrete methods
- Sử dụng Interface: Chỉ định nghĩa method signatures

**Mục đích:**
- Giảm độ phức tạp: Người dùng chỉ cần biết "cái gì" chứ không cần biết "làm thế nào"
- Tách biệt interface và implementation
- Dễ maintain và mở rộng
- Focus vào essential features, bỏ qua details không cần thiết

```java
public abstract class PaymentMethod {
    protected double amount;
    
    public PaymentMethod(double amount) {
        this.amount = amount;
    }
    
    // Abstract method
    public abstract boolean processPayment();
    
    // Concrete method
    public void printReceipt() {
        System.out.println("Payment of " + amount + " processed successfully");
    }
}

public class CreditCardPayment extends PaymentMethod {
    private String cardNumber;
    
    public CreditCardPayment(double amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }
    
    @Override
    public boolean processPayment() {
        // Logic xử lý thanh toán qua thẻ tín dụng
        System.out.println("Processing credit card payment...");
        return true;
    }
}
```

---

## Dependency Injection và Inversion of Control

### 1. Dependency Injection (DI)

**Khái niệm:**
Dependency Injection (DI) là một design pattern trong đó các dependencies (phụ thuộc) của một object được "inject" từ bên ngoài vào, thay vì object tự tạo ra chúng.

**Dependency là gì?**
Dependency là một object mà class hiện tại cần để thực hiện chức năng của nó. Ví dụ: `UserService` cần `UserRepository` để truy cập database → `UserRepository` là dependency của `UserService`.

**Nguyên lý hoạt động:**
Thay vì class tự khởi tạo dependencies (`new UserRepository()`), dependencies được truyền vào từ bên ngoài thông qua constructor, setter, hoặc interface.

**Lợi ích:**
- Giảm sự phụ thuộc giữa các class
- Dễ dàng testing
- Code dễ bảo trì và mở rộng
- Tăng tính linh hoạt

#### Ví dụ không sử dụng DI:
```java
public class UserService {
    private UserRepository repository;
    
    public UserService() {
        this.repository = new UserRepository(); // Tight coupling
    }
    
    public User getUser(int id) {
        return repository.findById(id);
    }
}
```

**Không nên vì:**
- **Tight Coupling (Liên kết chặt)**: `UserService` phụ thuộc trực tiếp vào `UserRepository` cụ thể. Nếu muốn đổi sang database khác, phải sửa code trong `UserService`.
- **Khó test**: Không thể mock `UserRepository` để test `UserService` độc lập. Phải kết nối database thật khi test.
- **Không linh hoạt**: Không thể thay đổi implementation của repository tại runtime hoặc dựa vào cấu hình.
- **Vi phạm Dependency Inversion Principle**: Class nên phụ thuộc vào abstraction, không phải concrete implementation.

#### Ví dụ sử dụng DI:

##### a) Constructor Injection
```java
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public User getUser(int id) {
        return repository.findById(id);
    }
}

// Sử dụng
UserRepository repository = new UserRepository();
UserService service = new UserService(repository);
```

##### b) Setter Injection
```java
public class UserService {
    private UserRepository repository;
    
    // Dependency được inject qua setter
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
    
    public User getUser(int id) {
        return repository.findById(id);
    }
}
```

##### c) Interface Injection
```java
public interface RepositoryInjector {
    void injectRepository(UserRepository repository);
}

public class UserService implements RepositoryInjector {
    private UserRepository repository;
    
    @Override
    public void injectRepository(UserRepository repository) {
        this.repository = repository;
    }
}
```

### 2. Inversion of Control (IoC)

**Khái niệm:**
Inversion of Control (IoC) là một nguyên lý thiết kế trong đó việc điều khiển luồng chương trình được "đảo ngược". Thay vì code chủ động tạo và quản lý dependencies, một framework hoặc container sẽ làm việc này.

**"Đảo ngược" nghĩa là gì?**
- **Cách truyền thống**: Code gọi library/framework → Kiểm soát flow
- **IoC**: Framework gọi code → Framework kiểm soát flow

**Quan hệ với DI:**
- IoC là nguyên lý chung
- DI là một cách cụ thể để implement IoC (implementation)

**Mục đích:**
- Giảm coupling giữa các components
- Tăng tính module hóa
- Framework quản lý lifecycle của objects
- Tự động resolve dependencies

#### IoC Container
```java
// IoC Container đơn giản
public class SimpleIoCContainer {
    private Map<Class<?>, Object> instances = new HashMap<>();
    
    public <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        return (T) instances.get(type);
    }
}

// Sử dụng IoC Container
public class Main {
    public static void main(String[] args) {
        SimpleIoCContainer container = new SimpleIoCContainer();
        
        // Đăng ký dependencies
        UserRepository repository = new UserRepository();
        container.register(UserRepository.class, repository);
        
        // Resolve dependency
        UserRepository resolvedRepo = container.resolve(UserRepository.class);
        UserService service = new UserService(resolvedRepo);
    }
}
```

### 3. DI/IoC trong Spring Framework

**Khái niệm:**
Spring Framework là một framework Java phổ biến nhất, cung cấp IoC Container mạnh mẽ để quản lý dependencies tự động.

**Spring IoC Container:**
- Tự động tạo và quản lý các Spring Beans (objects được quản lý bởi Spring)
- Tự động inject dependencies vào các beans
- Quản lý lifecycle của beans (creation, initialization, destruction)
- Hỗ trợ nhiều scopes: singleton, prototype, request, session...

**Annotations chính:**
- `@Component`, `@Service`, `@Repository`, `@Controller`: Đánh dấu class là Spring Bean
- `@Autowired`: Đánh dấu nơi cần inject dependency
- `@Configuration`, `@Bean`: Dùng để cấu hình beans thủ công

#### Ví dụ với Spring Boot:

```java
// Repository
@Repository
public class UserRepository {
    public User findById(Long id) {
        // Logic truy vấn database
        return new User();
    }
}

// Service với Constructor Injection
@Service
public class UserService {
    private final UserRepository userRepository;
    
    // Spring tự động inject UserRepository
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User getUser(Long id) {
        return userRepository.findById(id);
    }
}

// Controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
```

#### Configuration với Spring:

```java
@Configuration
public class AppConfig {
    
    @Bean
    public UserRepository userRepository() {
        return new UserRepository();
    }
    
    @Bean
    public UserService userService(UserRepository repository) {
        return new UserService(repository);
    }
}
```
