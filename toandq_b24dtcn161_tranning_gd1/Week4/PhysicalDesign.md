# Thiết kế Vật lý (Physical Database Design)

## 1. Bảng Employee (Nhân viên)

| Tên cột (Attribute) | Kiểu dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả |
| :--- | :--- | :--- | :--- |
| **EmployeeID** | `INT` hoặc `CHAR(10)` | **PK**, NOT NULL | Mã định danh duy nhất của nhân viên. |
| Name | `NVARCHAR(100)` | NOT NULL | Họ và tên đầy đủ. |
| Gender | `NVARCHAR(10)` | | Giới tính (Nam/Nữ/Khác). |
| Birthday | `DATE` | | Ngày tháng năm sinh. |
| Salary | `DECIMAL(15, 2)` | CHECK (Salary > 0) | Mức lương hiện tại. |
| PhoneNumber | `VARCHAR(15)` | | Số điện thoại liên lạc. |
| **DepartmentID** | `INT` | **FK** | Tham chiếu đến bảng `Department`. |
| **SupervisorID** | `INT` | **FK** | Tham chiếu chính bảng `Employee` (Quản lý trực tiếp). |

---

## 2. Bảng Department (Phòng ban)

| Tên cột (Attribute) | Kiểu dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả |
| :--- | :--- | :--- | :--- |
| **DepartmentID** | `INT` hoặc `CHAR(10)` | **PK**, NOT NULL | Mã định danh phòng ban. |
| Name | `NVARCHAR(100)` | UNIQUE, NOT NULL | Tên phòng ban (không trùng lặp). |
| Address | `NVARCHAR(200)` | | Địa chỉ văn phòng của phòng ban. |
| **ManagerID** | `INT` | **FK**, UNIQUE | Tham chiếu đến `Employee` (Trưởng phòng). |
| StartDate | `DATE` | | Ngày bắt đầu nhiệm kỳ của trưởng phòng. |

---

## 3. Bảng Project (Dự án)

| Tên cột (Attribute) | Kiểu dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả |
| :--- | :--- | :--- | :--- |
| **ProjectID** | `INT` hoặc `CHAR(10)` | **PK**, NOT NULL | Mã định danh dự án. |
| Name | `NVARCHAR(100)` | NOT NULL | Tên dự án. |
| Address | `NVARCHAR(200)` | | Địa điểm triển khai dự án. |
| **DepartmentID** | `INT` | **FK**, NOT NULL | Phòng ban chịu trách nhiệm (Tham chiếu `Department`). |

---

## 4. Bảng DependentPerson (Thân nhân)

| Tên cột (Attribute) | Kiểu dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả |
| :--- | :--- | :--- | :--- |
| **EmployeeID** | `INT` | **PK, FK** | Mã nhân viên bảo lãnh (Tham chiếu `Employee`). |
| **Name** | `NVARCHAR(100)` | **PK** | Tên người thân. |
| Gender | `NVARCHAR(10)` | | Giới tính người thân. |
| Birthday | `DATE` | | Ngày sinh người thân. |


---

## 5. Bảng JoinProject (Phân công dự án)

| Tên cột (Attribute) | Kiểu dữ liệu (Data Type) | Ràng buộc (Constraints) | Mô tả |
| :--- | :--- | :--- | :--- |
| **EmployeeID** | `INT` | **PK, FK** | Tham chiếu đến bảng `Employee`. |
| **ProjectID** | `INT` | **PK, FK** | Tham chiếu đến bảng `Project`. |
| TimeWork | `FLOAT` hoặc `DECIMAL` | DEFAULT 0 | Số giờ/thời gian tham gia dự án. |


## 3. Script SQL CREATE TABLE (Chuẩn hóa 3NF)

```sql
CREATE TABLE Department (
    DepartmentID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE,
    Address VARCHAR(200),
    ManagerID INT NULL, 
    StartDate DATE
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

CREATE TABLE DependentPerson (
    EmployeeID INT,
    Name VARCHAR(100),
    Gender VARCHAR(10),
    Birthday DATE,
    
    PRIMARY KEY (EmployeeID, Name),
    
    CONSTRAINT FK_Dependent_Employee FOREIGN KEY (EmployeeID)
        REFERENCES Employee(EmployeeID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE JoinProject (
    EmployeeID INT,
    ProjectID INT,
    TimeWork DECIMAL(5, 2) DEFAULT 0,
    
    PRIMARY KEY (EmployeeID, ProjectID),
    
    CONSTRAINT FK_JoinProject_Employee FOREIGN KEY (EmployeeID)
        REFERENCES Employee(EmployeeID) ON DELETE CASCADE,
        
    CONSTRAINT FK_JoinProject_Project FOREIGN KEY (ProjectID)
        REFERENCES Project(ProjectID) ON DELETE CASCADE
) ENGINE=InnoDB;
```