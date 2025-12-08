# Week 5 -- Java & Database Training

## Phần 1: Java -- Exception Handling & File I/O

### 1. Exception Handling (Xử lý ngoại lệ)

Nghiên cứu và trình bày: - Ngoại lệ là gì? Phân loại: - Checked
Exception - Unchecked Exception - Các khối lệnh xử lý ngoại lệ: - try --
catch -- finally - throw và throws - Custom Exception (tự định nghĩa
ngoại lệ) - Vai trò của xử lý ngoại lệ trong hệ thống thực tế

### 2. File I/O trong Java

Các nội dung cần nắm: - Đọc/ghi file dạng text: - File, FileReader,
BufferedReader - FileWriter, BufferedWriter - Đọc/ghi file dạng nhị
phân: - FileInputStream, FileOutputStream - Sử dụng try-with-resources
để tránh rò rỉ tài nguyên

### Output -- Phần Java

-   01 file tài liệu mô tả (PDF hoặc DOCX) gồm:
    -   Giải thích từng khái niệm
    -   Ví dụ code do bạn tự viết
    -   Bài tập:
        1.  Đọc 1 file text chứa danh sách nhân viên → in ra thông tin
        2.  Bắt lỗi file không tồn tại → trả thông báo phù hợp
        3.  Ghi danh sách nhân viên vào file theo định dạng CSV

------------------------------------------------------------------------

## Phần 2: Database -- SQL Nâng Cao & Truy vấn quan hệ

### 1. JOIN trong SQL

Trình bày + minh hoạ bằng ví dụ: - INNER JOIN - LEFT JOIN - RIGHT JOIN -
FULL OUTER JOIN - SELF JOIN (quan trọng vì bài toán có nhân viên quản lý
nhân viên)

### 2. Các truy vấn nâng cao

-   GROUP BY -- HAVING
-   Subquery (truy vấn con):
    -   Truy vấn con trong WHERE
    -   Truy vấn con trong FROM
-   EXISTS / NOT EXISTS
-   UNION / UNION ALL

### 3. Truy vấn trên mô hình tuần 4

Yêu cầu viết truy vấn: 1. Liệt kê tất cả nhân viên và tên phòng ban của
họ 2. Tìm tất cả nhân viên đang quản lý người khác (self join) 3. Tìm
nhân viên tham gia trên 2 dự án 4. Liệt kê phòng ban có nhiều hơn 5 nhân
viên 5. Danh sách nhân viên và số lượng người phụ thuộc tương ứng

### Output -- Phần Database

-   01 file .sql chứa toàn bộ truy vấn
-   01 tài liệu PDF/DOCX mô tả:
    -   Hình minh hoạ JOIN
    -   Giải thích từng truy vấn
    -   Kết quả chạy truy vấn

------------------------------------------------------------------------

