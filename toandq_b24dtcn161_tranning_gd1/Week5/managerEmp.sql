CREATE DATABASE managerEmp;

USE managerEmp;

CREATE TABLE Department (
    DepartmentID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Address VARCHAR(200),
    ManagerID INT NULL,
    StartDate DATE
);

CREATE TABLE Employee (
    EmployeeID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Gender VARCHAR(10),
    Birthday DATE,
    Salary DECIMAL(15, 2),
    PhoneNumber VARCHAR(20),
    DepartmentID INT,
    SupervisorID INT,
    CONSTRAINT FK_Employee_Department FOREIGN KEY (DepartmentID)
        REFERENCES Department(DepartmentID) ON DELETE SET NULL,
    CONSTRAINT FK_Employee_Supervisor FOREIGN KEY (SupervisorID)
        REFERENCES Employee(EmployeeID) ON DELETE SET NULL
);

ALTER TABLE Department
ADD CONSTRAINT FK_Department_Manager FOREIGN KEY (ManagerID)
    REFERENCES Employee(EmployeeID) ON DELETE SET NULL;

CREATE TABLE Project (
    ProjectID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Address VARCHAR(200),
    DepartmentID INT NOT NULL,
    CONSTRAINT FK_Project_Department FOREIGN KEY (DepartmentID)
        REFERENCES Department(DepartmentID) ON DELETE CASCADE
);

CREATE TABLE DependentPerson (
    EmployeeID INT,
    Name VARCHAR(100),
    Gender VARCHAR(10),
    Birthday DATE,
    PRIMARY KEY (EmployeeID, Name),
    CONSTRAINT FK_Dependent_Employee FOREIGN KEY (EmployeeID)
        REFERENCES Employee(EmployeeID) ON DELETE CASCADE
);

CREATE TABLE JoinProject (
    EmployeeID INT,
    ProjectID INT,
    TimeWork DECIMAL(5, 2) DEFAULT 0,
    PRIMARY KEY (EmployeeID, ProjectID),
    CONSTRAINT FK_JoinProject_Employee FOREIGN KEY (EmployeeID)
        REFERENCES Employee(EmployeeID) ON DELETE CASCADE,

    CONSTRAINT FK_JoinProject_Project FOREIGN KEY (ProjectID)
        REFERENCES Project(ProjectID) ON DELETE CASCADE
);

INSERT INTO Department (DepartmentID, Name, Address, ManagerID, StartDate) VALUES
(1, 'IT', 'Tầng 5, Tòa nhà A', NULL, '2020-01-10'),
(2, 'Nhân sự (HR)', 'Tầng 3, Tòa nhà A', NULL, '2019-05-20'),
(3, 'Kinh doanh (Sales)', 'Tầng 3, Tòa nhà B', NULL, '2021-03-15'),
(4, 'Marketing', 'Tầng 4, Tòa nhà B', NULL, '2022-01-01');

INSERT INTO Employee (EmployeeID, Name, Gender, Birthday, Salary, PhoneNumber, DepartmentID, SupervisorID) VALUES
(101, 'Trần Trí Dương', 'Nam', '2006-01-15', 5000.00, '0901234567', 1, NULL),
(102, 'Trần Thị B', 'Nữ', '1985-05-20', 3000.00, '0902234567', 2, 101),
(103, 'Quách Văn Sơn Bách', 'Nam', '2006-08-10', 3200.00, '0903234567', 3, 101),
(104, 'Phạm Thị D', 'Nữ', '1990-12-05', 2800.00, '0904234567', 1, 101),
(105, 'Đinh Trọng An', 'Nam', '1995-02-14', 1500.00, '0905234567', 1, 104),
(106, 'Phạm Phương Anh', 'Nữ', '1997-06-01', 1200.00, '0906234567', 2, 102),
(107, 'Đinh Đình Thành', 'Nam', '1993-09-09', 2000.00, '0907234567', 3, 103),
(108, 'Bùi Thị H', 'Nữ', '1999-11-11', 1000.00, '0908234567', NULL, 102),
(109, 'Mai Văn Huy', 'Nam', '2006-05-12', 2800.00, '0992734535', 1, 101),
(110, 'Đặng Quốc Toàn', 'Nam', '2006-08-15', 2800.00, '0907423462', 1, 101),
(111, 'Nguyễn Nhật Minh', 'Nam', '2006-09-23', 2800.00, '0934672484', 1, 101)
;

UPDATE Department SET ManagerID = 101 WHERE DepartmentID = 1;
UPDATE Department SET ManagerID = 102 WHERE DepartmentID = 2;
UPDATE Department SET ManagerID = 103 WHERE DepartmentID = 3;

UPDATE Employee SET DepartmentID = 1 WHERE EmployeeID = 108;

INSERT INTO Project (ProjectID, Name, Address, DepartmentID) VALUES
(1, 'Hệ thống ERP', 'Hà Nội', 1),
(2, 'Tuyển dụng Fresher', 'HCM', 2),
(3, 'Mở rộng thị trường', 'Đà Nẵng', 3),
(4, 'Website nội bộ', 'Hà Nội', 1);

INSERT INTO DependentPerson (EmployeeID, Name, Gender, Birthday) VALUES
(101, 'Nguyễn Văn A', 'Nam', '2010-01-01'),
(102, 'Trần Văn F', 'Nam', '1982-02-02'),
(103, 'Lê Thị C', 'Nữ', '1990-03-03'),
(105, 'Hoàng Thị E', 'Nữ', '2020-05-05');

INSERT INTO JoinProject (EmployeeID, ProjectID, TimeWork) VALUES
(104, 1, 20.5),
(105, 1, 40.0),
(105, 4, 10.0),
(106, 2, 15.0),
(107, 3, 30.0),
(108, 1, 20.0),
(108, 3, 25.0),
(108, 2, 20.0)
;

# Liệt kê nhân viên và phòng ban
SELECT
    e.EmployeeID,
    e.Name AS TenNhanVien,
    d.Name AS TenPhongBan
FROM Employee e
LEFT JOIN Department d ON e.DepartmentID = d.DepartmentID;

# Nhân viên đang quản lý người khác
SELECT DISTINCT
    Manager.EmployeeID,
    Manager.Name AS TenNguoiQuanLy
FROM Employee AS Manager
JOIN Employee AS Staff ON Manager.EmployeeID = Staff.SupervisorID;

# Nhân viên tham gia trên 2 dự án
SELECT
    e.Name AS TenNhanVien,
    COUNT(jp.ProjectID) AS SoLuongDuAn
FROM Employee e
JOIN JoinProject jp ON e.EmployeeID = jp.EmployeeID
GROUP BY e.EmployeeID, e.Name
HAVING COUNT(jp.ProjectID) > 2;

# Phòng ban có nhiều hơn 5 thành viên
SELECT
    d.Name AS TenPhongBan,
    COUNT(e.EmployeeID) AS SoLuongNhanVien
FROM Department d
JOIN Employee e ON d.DepartmentID = e.DepartmentID
GROUP BY d.DepartmentID, d.Name
HAVING COUNT(e.EmployeeID) > 5;

# Danh Sách nv và số người phụ thuộc
SELECT
    e.Name AS TenNhanVien,
    COUNT(dp.Name) AS SoNguoiPhuThuoc
FROM Employee e
LEFT JOIN DependentPerson dp ON e.EmployeeID = dp.EmployeeID
GROUP BY e.EmployeeID, e.Name
ORDER BY SoNguoiPhuThuoc DESC;