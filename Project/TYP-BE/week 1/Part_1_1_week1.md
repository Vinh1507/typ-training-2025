# Phần 1: Database

## 1. DBMS: MySQL Workbench

## 2.1 Các câu lệnh cơ bản với SQL
### a. Tạo 1 CSDL: Quản lý giải đấu bóng đá

```sql
-- Bảng đội bóng
CREATE TABLE team (
    team_id INT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL,
    coach_name VARCHAR(100),
    founded_year INT
);

-- Bảng cầu thủ
CREATE TABLE player (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    birth_date DATE,
    position VARCHAR(50),
    team_id INT,
    FOREIGN KEY (team_id) REFERENCES team(team_id)
);

-- Bảng trận đấu
CREATE TABLE match_info (
    match_id INT AUTO_INCREMENT PRIMARY KEY,
    home_team_id INT,
    away_team_id INT,
    match_date DATE,
    home_score INT DEFAULT 0,
    away_score INT DEFAULT 0,
    FOREIGN KEY (home_team_id) REFERENCES team(team_id),
    FOREIGN KEY (away_team_id) REFERENCES team(team_id)
);

```

###  b. DDL: 
- CREATE TABLE
- DROP TABLE
- ALTER TABLE
- CREATE VIEW
- DROP VIEW
#### Ví dụ 
- Tạo bảng 
```sql
CREATE DATABASE football_tournament;
USE football_tournament;

-- Tạo bảng đội bóng
CREATE TABLE team (
    team_id INT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL,
    coach_name VARCHAR(100),
    founded_year INT
);
```

- Sửa cấu trúc: thêm cột quốc gia

```sql
ALTER TABLE team ADD COLUMN country VARCHAR(50);
```

- Xóa bảng nếu không cần

```sql
DROP TABLE team;
```

- Tạo View: hiển thị thông tin đội và huấn luyện viên

```sql
CREATE VIEW detail_match AS 
SELECT t1.team_name AS team_home_name, 
    CONCAT(m.home_score, ' - ', m.away_score) AS score, 
    t2.team_name AS team_away_name
FROM match_info m 
    JOIN team t1 ON m.home_team_id = t1.team_id 
    JOIN team t2 ON m.away_team_id = t2.team_id
```

- Xóa view

```sql
DROP VIEW v_team_info;
```

### c. DML:

- SELECT
```sql
SELECT team_name, founded_year
FROM team
ORDER BY founded_year ASC;
```

- INSERT

```sql
-- THÊM DỮ LIỆU CHO BẢNG TEAM
INSERT INTO team (team_name, coach_name, founded_year, country)
VALUES
('Manchester United', 'Erik Ten Hag', 1878, 'England'),
('Manchester City', 'Pep Guardiola', 1880, 'England'),
('Arsenal', 'Mikel Arteta', 1886, 'England');
-- THÊM DỮ LIỆU CHO BẢNG CẦU THỦ
INSERT INTO player (player_name, birth_date, position, team_id)
VALUES
('Marcus Rashford', '1997-10-31', 'Forward', 1),
('Erling Haaland', '2000-07-21', 'Striker', 2),
('Bukayo Saka', '2001-09-05', 'Winger', 3);
-- THÊM DỮ LIỆU CHO BẢNG TRẬN ĐẤU
INSERT INTO match_info (home_team_id, away_team_id, match_date, home_score, away_score)
VALUES (1, 2, '2025-10-25', 2, 1);
```

- UPDATE

```sql
-- THAY ĐỔI TỶ SỐ
UPDATE match_info
SET home_score = home_score + 1
WHERE match_id = 1;
``` 

- DELETE

```sql
-- XÓA NHỮNG TRẬN ĐẤU TRƯỚC NGÀY 01/01/2025
DELETE FROM match_info
WHERE match_date < '2025-01-01';
```
### d. Toán tử truy vấn: FROM, WHERE, ORDER BY, HAVING, AND, OR, ...

- FROM : lấy dữ liệu từ bảng nào 
- WHERE : lấy dữ liệu theo điều kiện
- AND, OR, NOT
- ORDER BY : sắp xếp theo tiêu chí
- GROUP BY : nhóm các hàng cùng giá trị trong nhiều cột, đi kèm (COUNT, SUM, AVG, MAX, MIN)
- HAVING : giống WHERE nhưng lọc cho GROUP BY
- DISTINCT : loại các giá trị trùng lặp khi truy vấn 
- LIMIT / OFFSET : dùng phân trang hoặc lấy 1 phần kết quả

