-- Bảng User
CREATE TABLE IF NOT EXISTS `User` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) DEFAULT 'ACTIVE',
    PRIMARY KEY (`id`)
);

-- Bảng Role
CREATE TABLE IF NOT EXISTS `Role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `role_name` VARCHAR(255) NOT NULL,
    `role_code` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
);

-- Liên kết n-n giữa User và Role
CREATE TABLE IF NOT EXISTS `User_Role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `Role`(`id`) ON DELETE CASCADE
);

-- Bảng Clinic
CREATE TABLE IF NOT EXISTS `Clinic` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `city` VARCHAR(100),
    `phoneNumber` VARCHAR(50),
    `email` VARCHAR(255),
    `description` TEXT,
    PRIMARY KEY (`id`)
);

-- Bảng Doctor
CREATE TABLE IF NOT EXISTS `Doctor` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `clinic_id` BIGINT NOT NULL,
    `specialization` VARCHAR(255),
    `fullname` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(50),
    `email` VARCHAR(255),
    `status` VARCHAR(50),
    `image_url` TEXT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`clinic_id`) REFERENCES `Clinic`(`id`) ON DELETE CASCADE
);

-- Bảng Patient
CREATE TABLE IF NOT EXISTS `Patient` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `fullname` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255),
    `gender` VARCHAR(10) NOT NULL,
    `phone` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`id`) ON DELETE CASCADE
);

-- Bảng Time_Slot
CREATE TABLE IF NOT EXISTS `Time_Slot` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `start_time` DATETIME,
    `end_time` DATETIME,
    `description` TEXT,
    PRIMARY KEY (`id`)
);

-- Bảng Appointment
CREATE TABLE IF NOT EXISTS `Appointment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `doctor_id` BIGINT NOT NULL,
    `patient_id` BIGINT NOT NULL,
    `clinic_id` BIGINT NOT NULL,
    `time_slot_id` BIGINT NOT NULL,
    `note` TEXT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`doctor_id`) REFERENCES `Doctor`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`patient_id`) REFERENCES `Patient`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`clinic_id`) REFERENCES `Clinic`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`time_slot_id`) REFERENCES `Time_Slot`(`id`) ON DELETE CASCADE
);

-- Bảng Service
CREATE TABLE IF NOT EXISTS `Service` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `price` DOUBLE,
    PRIMARY KEY (`id`)
);

-- Liên kết n-n giữa Appointment và Service
CREATE TABLE IF NOT EXISTS `Appointment_Service` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `appointment_id` BIGINT NOT NULL,
    `service_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`appointment_id`) REFERENCES `Appointment`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`service_id`) REFERENCES `Service`(`id`) ON DELETE CASCADE
);

-- Bảng Payment
CREATE TABLE IF NOT EXISTS `Payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `appointment_id` BIGINT NOT NULL,
    `amount` DOUBLE NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`appointment_id`) REFERENCES `Appointment`(`id`) ON DELETE CASCADE
);

-- DDL --

-- Tao bang moi : Phong ban gom id va name
CREATE TABLE Department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Xoa 1 bang : Xoa bang phong ban
DROP TABLE Department;

-- Thay doi cau truc bang
-- Thêm cột : Them cot dia chi cho bang Doctor
ALTER TABLE Doctor ADD COLUMN address VARCHAR(255);
-- Sửa tên cột : Doi ten cot phonr trong bang Patient thanh phone_number
ALTER TABLE Patient RENAME COLUMN phone TO phone_number;
-- Xoá cột : Xoa cot description trong bang Clinic
ALTER TABLE Clinic DROP COLUMN description;

-- Tao bang ao Active_Doctors de xem danh sach bac si dang hoat dong
CREATE VIEW Active_Doctors AS
SELECT fullname, specialization
FROM Doctor
WHERE status = 'Active';


-- Xoa bang ao Active_Doctors
DROP VIEW Active_Doctors;


-- DML --

-- Lay ra name,email benh nhan Nam trong bang Patient
SELECT fullname, email
FROM Patient
WHERE gender = 'Male';

-- Them vai tro bac si vao bang role
INSERT INTO Role (role_name, role_code)
VALUES ('Bác sĩ', 'DOCTOR');

-- Set trang thai bac si co id = 3 la khong hoat dong
UPDATE Doctor
SET status = 'Inactive'
WHERE id = 3;

-- Xoa lich hen co id = 12
DELETE FROM Appointment
WHERE id = 12;

-- Chi dinh bang lay du lieu
SELECT * FROM Doctor;

-- Loc theo dieu kien 
SELECT * FROM Patient WHERE gender = 'Female';

-- Sap xep kqua tang dan ASC hoac giam dan DESC
SELECT * FROM Doctor ORDER BY fullname ASC;

-- Lay cac phong kham co > 3 bac si
SELECT clinic_id, COUNT(*) AS num_doctors
FROM Doctor
GROUP BY clinic_id
HAVING num_doctors > 3;

-- Dung de ket hop nhieu dieu kien 
SELECT * FROM Appointment
WHERE status = 'Done' AND doctor_id = 2;



-- Trigger, Transaction -- 


-- Tao thu tuc lay list bac si cua 1 phong kham
DELIMITER $$
CREATE PROCEDURE GetDoctorsByClinic(IN clinicId BIGINT)
BEGIN
    SELECT fullname, specialization
    FROM Doctor
    WHERE clinic_id = clinicId;
END $$
DELIMITER ;

-- Ghep ho ten vao 1 chuoi hoan chinh
DELIMITER $$
CREATE FUNCTION GetFullName(first VARCHAR(50), last VARCHAR(50))
RETURNS VARCHAR(100)
RETURN CONCAT(first, ' ', last);
$$
DELIMITER ;

-- Tu ghi log moi khi them benh nhan moi
CREATE TRIGGER after_new_patient
AFTER INSERT ON Patient
FOR EACH ROW
INSERT INTO Log (message, created_at)
VALUES (CONCAT('New patient added: ', NEW.fullname), NOW());

-- Ghi nhan ca 2 lenh neu khong loi gi 
START TRANSACTION;
UPDATE Payment SET status = 'Paid' WHERE id = 5;
INSERT INTO Log VALUES ('Payment updated', NOW());
COMMIT;

-- Huy tat ca thay doi khi gap loi
START TRANSACTION;
UPDATE Payment SET status = 'Paid' WHERE id = 5;
	-- loi xay ra o day
ROLLBACK;

-- Chi hoan tac phan truoc savepoint, phan sau khi rollback no van tinh
START TRANSACTION;
INSERT INTO Patient VALUES ();
SAVEPOINT sp1;
INSERT INTO Appointment VALUES ();
ROLLBACK TO sp1;
COMMIT;

-- Cho doctor_user quyen xem bang Clinic
GRANT SELECT, INSERT ON Clinic TO 'doctor_user'@'localhost';

-- Thu hoi lai quyen
REVOKE INSERT ON Clinic FROM 'doctor_user'@'localhost';














