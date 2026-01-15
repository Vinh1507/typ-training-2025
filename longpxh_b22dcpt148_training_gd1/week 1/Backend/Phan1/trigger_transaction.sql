-- Trigger & Transaction

-- 1. STORED PROCEDURE
-- procedure: là một đoạn mã SQL được đặt tên và lưu sẵn, có thể gọi lại nhiều lần.
DELIMITER $$
CREATE PROCEDURE get_sv_nam()
BEGIN
    SELECT * FROM sinhvien 
    WHERE GioiTinh = 'Nam';
END $$
DELIMITER ;

-- call get_sv_nam();

-- procedure với tham số đầu vào
DELIMITER $$
CREATE PROCEDURE get_sv_by_gender(IN gender VARCHAR(10))
BEGIN
    SELECT * FROM sinhvien 
    WHERE GioiTinh = gender;
END $$
DELIMITER ;

-- CALL get_sv_by_gender('Nữ');

-- procedure với tham số đầu vào và đầu ra
DELIMITER $$
CREATE PROCEDURE count_and_get_sv(
    IN gender VARCHAR(10),
    OUT total_count INT
)
BEGIN
    SELECT COUNT(*) INTO total_count
    FROM sinhvien 
    WHERE GioiTinh = gender;
    
    SELECT * FROM sinhvien 
    WHERE GioiTinh = gender;
END $$
DELIMITER ;

-- call count_and_get_sv('Nam', @count);
-- select @count AS TotalMaleStudents;

-- 2. FUNCTION
-- function: trả về một giá trị duy nhất, có thể sử dụng trong câu lệnh SELECT

DELIMITER $$
CREATE FUNCTION count_sv_by_gender(gender VARCHAR(10))
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE soLuong INT;

    SELECT COUNT(*) INTO soLuong
    FROM sinhvien
    WHERE GioiTinh = gender;

    RETURN soLuong;
END $$
DELIMITER ;

-- sử dụng function
SELECT count_sv_by_gender('Nữ') AS SoLuongNu;
SELECT count_sv_by_gender('Nam') AS SoLuongNam;

-- function tính điểm trung bình
DELIMITER $$
CREATE FUNCTION calc_average_score(student_id INT)
RETURNS DECIMAL(3,2)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE avg_score DECIMAL(3,2);
    
    SELECT AVG(Diem) INTO avg_score
    FROM diem d
    WHERE d.MaSV = student_id;
    
    RETURN IFNULL(avg_score, 0);
END $$
DELIMITER ;

-- 3. TRIGGER
-- trigger: tự động thực thi khi có sự kiện INSERT, UPDATE, DELETE xảy ra

-- tạo bảng log để ghi lại các thay đổi
CREATE TABLE IF NOT EXISTS audit_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50),
    operation VARCHAR(10),
    user VARCHAR(50),
    timestamp DATETIME,
    old_data TEXT,
    new_data TEXT
);

-- trigger AFTER INSERT - ghi log khi thêm sinh viên mới
DELIMITER $$
CREATE TRIGGER tr_sinhvien_after_insert
AFTER INSERT ON sinhvien
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_name, operation, user, timestamp, new_data)
    VALUES ('sinhvien', 'INSERT', USER(), NOW(), 
            CONCAT('MaSV: ', NEW.MaSV, ', HoTen: ', NEW.HoTen, ', GioiTinh: ', NEW.GioiTinh));
END $$
DELIMITER ;

-- trigger BEFORE UPDATE - kiểm tra dữ liệu trước khi cập nhật
DELIMITER $$
CREATE TRIGGER tr_sinhvien_before_update
BEFORE UPDATE ON sinhvien
FOR EACH ROW
BEGIN
    -- kiểm tra tuổi hợp lệ
    IF NEW.NgaySinh > CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ngày sinh không được lớn hơn ngày hiện tại';
    END IF;
    
    -- ghi log thay đổi
    INSERT INTO audit_log (table_name, operation, user, timestamp, old_data, new_data)
    VALUES ('sinhvien', 'UPDATE', USER(), NOW(),
            CONCAT('Old - MaSV: ', OLD.MaSV, ', HoTen: ', OLD.HoTen),
            CONCAT('New - MaSV: ', NEW.MaSV, ', HoTen: ', NEW.HoTen));
END $$
DELIMITER ;


-- 4. TRANSACTION
-- transaction: nhóm các câu lệnh SQL thành một đơn vị logic
-- đảm bảo tính ACID (Atomicity, Consistency, Isolation, Durability)

-- ví dụ Transaction với SAVEPOINT
START TRANSACTION;

INSERT INTO sinhvien (MaSV, HoTen, GioiTinh, NgaySinh) 
VALUES ('SV003', 'Le Van C', 'Nam', '1999-12-10');

-- savepoint
SAVEPOINT sp1;

INSERT INTO diem (MaSV, MaMH, Diem) 
VALUES ('SV003', 'MH001', 9.0);

-- savepoint 2
SAVEPOINT sp2;

INSERT INTO sinhvien (MaSV, HoTen, GioiTinh, NgaySinh) 
VALUES ('SV004', 'Pham Thi D', 'Nữ', '2000-07-25');

-- nếu có lỗi ở bước cuối, có thể rollback về savepoint
-- ROLLBACK TO sp2; -- chỉ hủy bỏ việc thêm sinh viên thứ hai
-- ROLLBACK TO sp1; -- hủy bỏ cả việc thêm điểm và sinh viên thứ hai

COMMIT; -- hoặc commit tất cả
