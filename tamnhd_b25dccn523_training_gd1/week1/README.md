# [Nguyen Hoang Duc Tam - B25DCCN523] Training Basic - Week 1

## Background knowledge

- Sử dụng cơ bản XAMPP/AMPPS để host các backend project php
- Sử dụng phpmyadmin để quản trị một số database rất cơ bản

## Phần 1: Database (Tập trung vào CSDL quan hệ)

### 1. Cài đặt DBMS

Em cài và sử dụng MySQL Server và Workbench version 8.0. MySQL sẽ sử dụng keyword và syntax khác với Microsoft SQL (T-SQL)

### 2. Các câu lệnh cơ bản với SQL

MySQL là một ngôn ngữ case-insensitive, keyword có thể là UPPERCASE, lowercase, Capital. Nhưng để dễ phân biệt ta sẽ thường dùng UPPERCASE cho keyword.

#### Tạo CSDL

```sql
CREATE DATABASE DataBaseName;
```

#### Xoá CSDL

```sql
DROP DATABASE DataBaseName;
```

#### Đặt chế độ read-only cho CSDL

```sql
ALTER DATABASE DataBaseName READ ONLY = 0/1;
```

#### Sử dụng CSDL

Trong khi sử dụng các câu lệnh cần nêu rõ việc sử dụng CSDL nào

```sql
USE DataBaseName;
```

#### Tạo bảng với các cột

```sql
CREATE TABLE table_name (
    column_name1 datatype1,
    column_name2 datatype2,
    ...
);
```

**Ví dụ:**

```sql
CREATE TABLE Library (
    book_id VARCHAR(30),
    book_name VARCHAR(150),
    in_stock INT
);
```

#### Một vài kiểu dữ liệu thường dùng

| Kiểu dữ liệu | Nội dung |
|---|---|
| `INT` | Số nguyên |
| `BIT` | BOOLEAN |
| `DECIMAL` | Số thập phân với số lượng chữ số sau dấu phẩy xác định (Thường dùng cho tiền tệ) |
| `FLOAT` | Số thập phân |
| `VARCHAR(MAXCHAR)` | Chuỗi có xác định độ dài max |
| `TEXT` | Chuỗi không xác định độ dài max |
| `DATE` | YYYY-MM-DD |
| `TIME` | HH-MM-SS |
| `DATETIME` | YYYY-MM-DD HH-MM-SS |

#### Chọn/xem nội dung trong 1 bảng

```sql
SELECT * FROM table_name;
```

#### Đổi tên 1 bảng

```sql
RENAME TABLE OriginalName TO NewName;
```

#### Xoá 1 bảng

```sql
DROP TABLE table_name;
```

#### Sửa các phần config của bảng

Để sửa các phần config của bảng ta dùng:

```sql
ALTER TABLE table_name
COMMAND;
```

**Một vài lệnh:**

Đổi tên cột:
```sql
RENAME COLUMN OriginalName TO NewName;
```

Thêm cột:
```sql
ADD column_name datatype;
```

Xoá cột:
```sql
DROP COLUMN column_name;
```

Sửa kiểu dữ liệu cho cột:
```sql
MODIFY COLUMN column_name datatype;
```

#### Di chuyển vị trí các cột

```sql
ALTER TABLE table_name
MODIFY column_name datatype
location_command;
```

**Location command:**

```sql
AFTER column_name
```

```sql
FIRST
```

#### Lệnh `INSERT`

```sql
INSERT INTO table_name
VALUES (val1, val2, val3, ...),
       (val7, val8, val9, ...);
```

Với mỗi cặp dấu `()` ta đang thêm vào bảng 1 dòng, các giá trị `val1, val2, val3,...` phải được đồng thời lineup với thứ tự các cột hiện tại trong bảng.

Với cách làm trên ta phải nhớ thứ tự cột và đồng thời thêm giá trị vào tất cả các cột. Nếu ta chỉ muốn thêm giá trị vào 1 số cột nhất định ta làm như sau:

```sql
INSERT INTO table_name(col1, col2, col3)
VALUES (val1, val2, val3),
       (val4, val5, val6);
```

#### Lệnh `SELECT`

```sql
SELECT location FROM table_name;
```

```sql
SELECT location FROM table_name WHERE condition;
```

**Ví dụ:**

```sql
SELECT * FROM Library;
SELECT book_id, book_name FROM Library;
SELECT * FROM Library WHERE copy_in_stock >= 5;
```

#### Lệnh `UPDATE`

```sql
UPDATE table_name
SET col1 = val1, col2 = val2, ...
WHERE condition;
```

#### Lệnh `DELETE`

```sql
DELETE FROM table_name
WHERE condition;
```

**Lưu ý:** Tuyệt đối không viết thiếu `WHERE` trong câu lệnh `UPDATE` và `DELETE` vì sẽ gây ảnh hưởng đến toàn bảng.

---

### `PROCEDURE` và `FUNCTION`

`PROCEDURE` được thiết kế để thực hiện các hành động như `INSERT`, `UPDATE`, `DELETE` cho dữ liệu trong bảng.

`FUNCTION` được thiết kế để trả về kết quả cho các tính toán.

#### Tạo PROCEDURE

```sql
CREATE PROCEDURE procedure_name(arg)
BEGIN
    COMMANDS;
END;
```

Nhưng nếu viết như thế này ta sẽ gặp lỗi vì MySQL sẽ dừng lại tại dấu `;` mà nó gặp. Ta cần khai báo lại delimiter bằng câu lệnh:

```sql 
DELIMITER + delimiter_char
```

**Ví dụ:**

```sql
DELIMITER // -- Set delimiter thành //
CREATE PROCEDURE procedure_name()
BEGIN
    COMMANDS;
END // 
DELIMITER ; -- Trả về gốc
```

Để gọi PROCEDURE ta dùng:

```sql
CALL procedure_name();
```

**Ví dụ cụ thể:**

```sql
DELIMITER //
CREATE PROCEDURE tim_sach(IN id VARCHAR(30))
BEGIN
    SELECT * FROM Library WHERE book_id = id;
END //
DELIMITER ;

CALL tim_sach('PTIT001');
```

```sql
DELIMITER //
CREATE PROCEDURE loai_sach(
    IN id VARCHAR(30),
    OUT loai VARCHAR(30)
)
BEGIN
    DECLARE n_pages INT;
    SELECT pages INTO n_pages FROM Library WHERE book_id = id;
    IF n_pages <= 300 THEN
        SET loai = 'Sach mong';
    ELSEIF n_pages <= 500 THEN
        SET loai = 'Sach trung binh';
    ELSE 
        SET loai = 'Sach day';
    END IF;
END //
DELIMITER ;

CALL loai_sach('PTIT001', @loai); -- Với các biến output, ta cần thêm @ ở trước nó
SELECT @loai AS book_type;
```

#### Tạo FUNCTION

Để khai báo FUNCTION, FUNCTION thì bắt buộc phải có trả về giá trị:

```sql
DELIMITER //
CREATE FUNCTION function_name(arg)
RETURNS datatype
[NOT] DETERMINISTIC 
NO SQL / READS SQL DATA / MODIFIES SQL DATA
BEGIN
    COMMANDS;
    RETURN ....;
END // 
DELIMITER ; 
```

**Giải thích các thuộc tính:**

**`DETERMINISTIC`**: Tính xác định, tức là khi cùng 1 input sẽ trả luôn luôn cùng 1 kết quả. Ví dụ hàm tính thuế. Kết quả của hàm có thể được cache. Các function có các hàm như `RAND()`, `NOW()` không được xem là hàm có tính xác định. Nếu không được define thì giá trị mặc định là `NOT DETERMINISTIC`.

**`NO SQL`**: Hàm không sử dụng các lệnh liên quan CSDL, chỉ đơn thuần tính toán. Thường được kết hợp với `DETERMINISTIC`.

**`READS SQL DATA`**: Hàm chỉ sử dụng data từ CSDL, nếu được kết hợp cùng `DETERMINISTIC` kết quả hàm có thể được cache và sẽ được update khi giá trị trong CSDL thay đổi.

**`MODIFIES SQL DATA`**: Hàm có bao gồm các câu lệnh có thể write data vào CSDL. Thường kết hợp với `READS SQL DATA`.

Trong 1 function, nhất định phải define 1 trong 3 command sau nếu không sẽ gây lỗi: `DETERMINISTIC`, `NO SQL`, `READS SQL DATA`.

FUNCTION không thể được gọi bằng `CALL`, ta thường kết hợp FUNCTION với `SELECT`.

**Ví dụ:**

```sql
DELIMITER // 
CREATE FUNCTION so_trang(id VARCHAR(30))
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE res INT;
    SELECT pages INTO res FROM library WHERE book_id = id;
    RETURN res;
END // 
DELIMITER ; 

SELECT so_trang('PTIT001');
```

---

### `TRIGGER`

`TRIGGER` là một tập lệnh sẽ được tự động gọi khi có một điều kiện được đáp ứng (dữ liệu thay đổi, thêm dòng, xoá dòng, v.v). `TRIGGER` được define như sau:

```sql
DELIMITER //

CREATE TRIGGER trigger_name
[BEFORE/AFTER] [INSERT/UPDATE/DELETE] ON table_name      
FOR EACH ROW                   
BEGIN
    COMMANDS;
END //

DELIMITER ;
```

#### `BEFORE` / `AFTER`

**`BEFORE`**: Với lệnh `BEFORE` ta có thể thay đổi được dữ liệu trước khi ghi vào CSDL.

**`AFTER`**: Với lệnh `AFTER` ta không thể thay đổi được cách dữ liệu, nên dùng lệnh này cho việc logging sang các bảng khác.

Trong các câu lệnh, ta có thể dùng `OLD.col_name` hoặc `NEW.col_name`, để sử dụng các giá trị trước và sau khi thay đổi của cột đó. Tất yếu `OLD` chỉ dùng trong câu lệnh `UPDATE` và `DELETE`, `NEW` chỉ dùng trong `INSERT` và `UPDATE`.

**Ví dụ:**

```sql
DELIMITER //

CREATE TRIGGER book_insert
BEFORE INSERT ON library
FOR EACH ROW
BEGIN
    SET NEW.copy_in_stock = NEW.total_copy;
END //

DELIMITER ;
```

---

### `AUTOCOMMIT`, `COMMIT`, `ROLLBACK`

Sẽ khá nguy hiểm cho CSDL nếu chúng ta làm việc trực tiếp, nên ta có thể tự cứu sống mình bằng câu lệnh sau:

Đầu tiên tạo 1 SAVE POINT tại điểm bắt đầu (tạm hiểu như 1 bản backup của server ngay tại lúc chạy câu lệnh):

```sql
START TRANSACTION;
```

Nếu ta cần tạo 1 SAVE POINT khác trong quá trình xử lí ngoài điểm bắt đầu ta dùng:

```sql
SAVEPOINT savepoint_name;
```

Sau đó ta thực hiện các lệnh như bình thường. Nếu các lệnh ta thực hiện đều đúng, ta dùng:

```sql
COMMIT;
```

để lưu vĩnh viễn tất cả thay đổi kể từ khi `START TRANSACTION`.

Nếu sai:

```sql
ROLLBACK;
```

để đưa CSDL về điểm bắt đầu, hoặc:

```sql
ROLLBACK TO savepoint_name;
```

để đưa về 1 SAVE POINT khác.

**Lưu ý:** Có một số câu lệnh sẽ nằm ngoài `TRANSACTION`: `CREATE`, `ALTER`, `DROP`, `GRANT`, `REVOKE`. Thực thi những câu lệnh này thì sẽ không có điểm quay đầu.

---

### `GRANT`, `REVOKE`

Khi thêm 1 user mới vào để quản trị CSDL, mặc định user sẽ không được cấp bất cứ quyền gì. `GRANT` để cấp quyền, `REVOKE` để thu hồi.

```sql
GRANT privilege_name ON database_name.table_name TO 'username'@'host';
```

```sql
REVOKE privilege_name ON database_name.table_name FROM 'username'@'host';
```

**privilege_name**: `SELECT`, `INSERT`, `UPDATE`, `DELETE`, `EXECUTE` (`PROCEDURE` | `FUNCTION`), `CREATE`, `DROP`, `ALTER`, `GRANT`, `ALL` (trừ `GRANT`)

**Check quyền:**

```sql
SHOW GRANTS FOR 'username'@'host'; 
```

---

### Toán tử truy vấn

- **`FROM`**: Xác định nguồn dữ liệu
- **`WHERE`**: Lọc dữ liệu theo cột
- **`GROUP BY`**: Lọc các dòng có chung giá trị cột và ghép thành nhóm
- **`HAVING`**: Tương tự `WHERE`, dùng khi đã `GROUP BY`
- **`ORDER BY`**: Sắp xếp `ASC` hoặc `DESC`
- **`AND`, `OR`, `NOT`**: Các phép boolean
- **`BETWEEN ... AND ...`**: Lấy các giá trị trong đoạn
- **`IN(val1, val2, ...)`**: Lấy các giá trị trong danh sách
- **`IS NULL`, `IS NOT NULL`**: Kiểm tra giá trị trống (vì không dùng toán tử so sánh `=` được với `NULL`)

---

Em viết file này dựa trên quá trình tự tìm hiểu các tài liệu nước ngoài kết hợp với hỗ trợ từ AI. Vì kiến thức tự học đôi khi còn hạn chế, em rất mong nhận được những nhận xét từ anh/chị để hoàn thiện hơn. Cảm ơn anh/chị đã review bài cho em. Hy vọng sớm có dịp được cộng tác và học hỏi thêm từ anh/chị!