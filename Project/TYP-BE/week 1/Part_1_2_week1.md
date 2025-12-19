## 2.2 Các câu lệnh cơ bản với SQL
---
### a. Trigger :
- là một đoạn mã SQL tự động chạy (tự động bị kích hoạt) khi có một sự kiện xảy ra trên bảng

#### STORED PROCEDURES :
- Là tập hợp các câu lệnh SQL được lưu sẵn trong CSDL.
- Có thể truyền tham số vào, và gọi lại nhiều lần mà không cần viết lại câu lệnh dài dòng.
- Cấu trúc 
```sql
DELIMITER $$

CREATE PROCEDURE tên_thủ_tục (tham_số_1 kiểu_dữ_liệu, tham_số_2 kiểu_dữ_liệu, ...)
BEGIN
    -- Các lệnh SQL
END $$

DELIMITER ;
```
- IN : input cho procedure
- OUT : ouput của procedure
- INOUT : truyền vào và thay đổi giá trị

- Ví dụ:
```sql
DELIMITER $$

CREATE PROCEDURE AddMatch(
    IN home_id INT,
    IN away_id INT,
    IN match_date DATE,
    IN home_score INT,
    IN away_score INT
)
BEGIN
    INSERT INTO match_info(home_team_id, away_team_id, match_date, home_score, away_score)
    VALUES (home_id, away_id, match_date, home_score, away_score);
END$$

DELIMITER ;
-- Gọi thủ tục:
CALL AddMatch(1, 2, '2025-10-25', 3, 1);
```

#### CREATE FUNCTION : 
- Giống như hàm trong lập trình.
- Trả về 1 giá trị duy nhất (ví dụ: tổng điểm, số đội, số trận thắng...).
- Có thể dùng trong SELECT như một cột tính toán.
-  Cấu trúc 
```sql
CREATE FUNCTION tên_hàm(tham_số_1 kiểu_dữ_liệu, tham_số_2 kiểu_dữ_liệu, ...)
RETURNS kiểu_dữ_liệu_trả_về
DETERMINISTIC
BEGIN
    -- Phần thân hàm
    RETURN giá_trị;
END;
```
- tên_hàm: tên tự đặt, không trùng với tên có sẵn.
- tham_số: có thể có hoặc không.
- RETURNS: kiểu dữ liệu kết quả (INT, VARCHAR, DECIMAL, v.v...)
- DETERMINISTIC: nghĩa là với cùng input, kết quả luôn giống nhau (MySQL yêu cầu để tối ưu).
- BEGIN ... END: phần thân hàm.
- RETURN: giá trị trả về.

- Ví dụ : 
```sql
DELIMITER $$

CREATE FUNCTION GetTeamCount()
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE cnt INT;
    SELECT COUNT(*) INTO cnt FROM team;
    RETURN cnt;
END$$

DELIMITER ;

-- Gọi trong truy vấn
SELECT GetTeamCount() AS TotalTeams;
```

#### CREATE TRIGGER : 
- Cấu trúc :
```sql
CREATE TRIGGER tên_trigger
THỜI_ĐIỂM SỰ_KIỆN
ON tên_bảng
FOR EACH ROW
BEGIN
    -- Các lệnh SQL muốn thực thi tự động
END;
```
- THỜI_ĐIỂM: BEFORE hoặc AFTER → Thực thi trước hoặc sau khi thao tác xảy ra.
- SỰ_KIỆN: INSERT, UPDATE, DELETE
- FOR EACH ROW: nghĩa là trigger này chạy cho từng dòng bị ảnh hưởng.

- Ví dụ
```sql
DELIMITER $$
CREATE TRIGGER check_score_before_insert
BEFORE INSERT ON match_info
FOR EACH ROW
BEGIN
    IF NEW.home_score < 0 OR NEW.away_score < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Điểm không được âm!';
    END IF;
END$$

DELIMITER ;
```
---
### b. TRANSACTION :
- Là nhóm các thao tác SQL được thực hiện cùng nhau.
- Nếu 1 thao tác lỗi, tất cả thao tác trước đó được hủy (ROLLBACK) để đảm bảo tính toàn vẹn dữ liệu.

| Lệnh | Ý nghĩa |
|-------|----------|
| START TRANSACTION hoặc BEGIN | Bắt đầu giao dịch |
| COMMIT | Xác nhận thay đổi (lưu vĩnh viễn) |
| ROLLBACK | Hủy toàn bộ thay đổi từ đầu giao dịch |
| SAVEPOINT | Đặt điểm tạm để có thể rollback đến đó |

- Ví dụ : 
``` sql
START TRANSACTION;

-- Thêm đội bóng
INSERT INTO team (team_name, coach_name, founded_year, country)
VALUES ('Liverpool', 'Jurgen Klopp', 1892, 'England');

-- Lưu điểm tạm
SAVEPOINT before_match;

-- Thêm trận đấu
INSERT INTO match_info (home_team_id, away_team_id, match_date, home_score, away_score)
VALUES (4, 1, '2025-10-26', -2, 1);  -- điểm âm => trigger lỗi

-- Nếu trigger báo lỗi, ta có thể quay lại điểm an toàn
ROLLBACK TO before_match;

-- Lưu thay đổi còn lại
COMMIT;
```
---
## Điều khiển Truy cập: GRANT, REVOKE
- Dùng để quản lý quyền hạn của người dùng, mỗi người có thể được phép xem, thêm, sửa, xóa dữ liệu, hoặc tạo/xóa bảng, view, trigger,... tùy theo phân quyền.
- GRANT : Cấp quyền cho người dùng.
- INVOKE : Thu hồi quyền đã cấp.

### GRANT :
- Cấu trúc 
```sql
GRANT quyền1, quyền2, ...
ON tên_cơ_sở_dữ_liệu.tên_bảng
TO 'tên_user'@'host'
[IDENTIFIED BY 'mật_khẩu'];
```

- Ví dụ : 
```sql
GRANT SELECT
ON football_tournament.team
TO 'giang'@'localhost';
-- User giang chỉ có thể xem dữ liệu từ bảng team, không thể update, insert, delete 
```

```sql
GRANT INSERT, UPDATE
ON football_tournament.team
TO 'giang'@'localhost';
-- User giang chỉ có thể sửa dữ liệu từ bảng team, không thể xem 
```
### REVOKE :
- Cấu trúc :
```sql
REVOKE quyền1, quyền2
ON tên_CSDL.tên_bảng
FROM 'tên_user'@'host';
```

- Ví dụ : 
```sql
REVOKE INSERT
ON football_tournament.team
FROM 'giang'@'localhost';

-- Thu hồi quyền insert của giang
```

### Một số loại quyền phổ biến 
| Loại quyền       | Mô tả                                      |
| ---------------- | ------------------------------------------ |
| `SELECT`         | Xem dữ liệu                                |
| `INSERT`         | Thêm dữ liệu                               |
| `UPDATE`         | Cập nhật dữ liệu                           |
| `DELETE`         | Xóa dữ liệu                                |
| `CREATE`, `DROP` | Tạo / Xóa bảng                             |
| `ALTER`          | Thay đổi cấu trúc bảng                     |
| `INDEX`          | Tạo chỉ mục                                |
| `CREATE ROUTINE` | Tạo Function/Procedure                     |
| `ALTER ROUTINE`  | Sửa Function/Procedure                     |
| `EXECUTE`        | Gọi (thực thi) Function/Procedure          |
| `GRANT OPTION`   | Cho phép user này cấp quyền cho người khác |


