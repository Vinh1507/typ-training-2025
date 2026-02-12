# BÁO CÁO TUẦN 1 (BE)
## Phần 1: Database
1. Cài đặt DBMS: **MySQL**
2. Các câu lệnh cơ bản với SQL
- Đề tài QLSV gồm 3 quan hệ: Sinh viên, Giáo viên và Khoa.
  ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.45.14 AM.png)
- DDL:
    - Sử dụng: **CREATE DATABASE** để tạo db QLSV và **CREATE TABLE** để tạo các quan hệ.
      ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.41.56 AM.png)
    - **DROP TABLE** để xoá bảng mình muốn.
        - Cú pháp: DROP TABLE (table_name);
    - **ALTER TABLE** để thêm sửa xoá các cột trong các bảng đã tồn tại.
        - Cú pháp: ALTER TABLE table_name;
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.45.58 AM.png)
    - **CREATE VIEW** để tạo bảng ảo kết quả chứa các dòng và cột giống như bảng bình thường.
        - Cú pháp: CREATE VIEW view_name AS (viết lệnh SQL bình thường)
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.47.10 AM.png)
        - Từ bảng ảo được tạo chúng ta có thể thao tác với nó như các bảng bình thường.
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.49.09 AM.png)
    - **DROP VIEW** để xoá view (bởi vì khi tạo view thì view sẽ được lưu ở trong db vì vậy khi tạo nhiều cũng sẽ gây tốn dung lượng)
        - Cú pháp: DROP VIEW view_name;
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.49.40 AM.png)
- DML:
    - **SELECT** để lấy dữ liệu từ db(có thể áp dụng cùng các lệnh khác để lấy data theo mong muốn như WHERE, JOIN,...  )
        - Cú pháp: SELECT column_name, ... FROM table_name
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.50.18 AM.png)
        - Nếu muốn lấy hết các cột trong bảng thì sử dụng cú pháp (SELECT *)
    - **INSERT** để truyển dữ liệu vào bảng (**INSERT INTO**) hoặc có thể copy dữ liệu từ bảng này qua bảng khác(**INSERT INTO SELECT**)
        - **INSERT INTO**:
            - Cú pháp: INSERT INTO table_name(column1, column2, ..) VALUES ();
              ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.50.59 AM.png)
        - **INSERT INTO SELECT**:
            - Cú pháp: INSERT INTO table_name1(column1, ...)
              SELECT column1, ...
              FROM  table_name2
              WHERE condition;
    - **UPDATE** để sửa lại bản ghi đã tồn tại trong bảng
        - Cú pháp: UPDATE table_name
          SET column1 = value, column2 = value, ...
          WHERE condition;
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.54.38 AM.png)
          -> ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.55.19 AM.png)
    - **DELETE** để xoá bản ghi đã tồn tại trong bảng
        - Cú pháp: DELETE FROM table_name WHERE condition;
- Function, trigger:
    - **CREATE PROCEDURE** : để tạo ra 1 hàm chứa sẵn sql mình chuẩn bị trước và dùng để tái sử dụng nhiều lần. Khi dùng thì mình chỉ cần CALL procedure đã tạo thôi mà không cần phải viết lại SQL.
        - Cú pháp: DELIMITER //
          CREATE PROCEDURE procedure_name()
          BEGIN
          sql statement
          END //
          DELIMITER ;
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.55.58 AM.png)
    - **CREATE FUNCTIONS** : để tự tạo ra các functions kiểu như các functions có sẵn(CONCAT(), ISNULL(), LENGTH(),...),
        - Cú pháp: DELIMITER //
          CREATE FUNCTION function_name()
          RETURN data_type
          DETERMINISTIC
          BEGIN
          sql statement
          END //
          DELIMITER ;
          ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/Screenshot 2025-11-02 at 1.56.33 AM.png)
        - Sau khi tạo xong muốn sử dụng thì dùng câu lệnh: SELECT function_name();
    - **CREATE TRIGGER** : để tạo các lệnh tự động chạy khi có 1 sự kiện INSERT, UPDATE hay DELETE trên bảng.
        - Cú pháp: ![](/Users/apple/DevTYPTIT/typ-training-2025/nhathh_b23dvcn121_training_gd1/week_1/cu-phap-trigger-sql.webp)
- TCL: là một tập lệnh SQL được thực thi tuần tự và độc lập. Một Transaction được thực hiện thành công khi tất cả câu lệnh đều thành công. Nếu chỉ một trong số đó thất bại thì dữ liệu sẽ được rollback về trạng thái ban đầu.
    - **COMMIT** : để lưu các thay đổi
    - **ROLLBACK**: để quay trở lại trạng thái trước khi có thay đổi
    - **SAVEPOINT**: để tạo các điểm trạng thái bên trong transaction
    - **SET TRANSACTION**: để đặt tên cho một transaction.
- DCL: dùng để phân quyền hoặc thu hồi quyền truy cập dữ liệu.
    - **GRANT**: để cấp quyền cho user.
        - Cú pháp: GRANT SELECT, INSERT ON table_name TO user;
    - **REVOKE**: để thu hồi quyền đã cấp
        - Cú pháp: REVOKE INSERT ON table_name FROM user;
## Phần 2: OOP
3. OOP trong Java:
- Java cơ bản:
    - Biến(Variable): là vùng nhớ để lưu giá trị
        - Cú pháp: DataType variable_name.
        - Ví dụ: int number; double gpa = 4.0; String name;
        - Gồm có 2 loại cơ bản:
            - Biến cục bộ: là biến được khởi tạo trong hàm và chỉ có giá trị khi ở trong hàm đấy.
            - Biến toàn cục: là biến được khởi tạo ở trong class và tất cả các phương thức trong hàm có thể gọi được biến đấy.
        - Ngoài ra còn có biến static và final:
            - Biến static: là biến thuộc class và được khởi tạo cùng với class. Các instant của class đấy sẽ đều có thuộc tính đấy.
                - Cú pháp: static DataType variable_name;
                - Ví dụ: static int count = 0;
            - Biến final: là biến có giá trị không thể thay đổi(giống như hằng số)
                - Cú pháp: final DataType variable_name = value;
                - Ví dụ: final int scoreFinal = 9;
    - Hàm(Method): là khối lệnh thực hiện một chức năng cụ thể và chỉ chạy khi được gọi.
        - Cú pháp: AccessModifiers ReturnDataType methodName(){}
            - Ví dụ: public int countStudent(){
              return 10;
              }
        - Kiểu dữ liệu trả về là void thì sẽ không cần phải return;
    - Lớp(Class): là kiến trúc của một đối tượng gồm có các thuộc tính(variables) và có các chức năng(methods)
        - Cú pháp: AccessModifiers class class_name {}
        - Ví dụ: public class Main {}
    - Object: là một
    - Abstract class
    - Interface
    - I/O
    - Vòng lặp
    - Thao tác file
- Các tính chất OOP:
    - Tính đóng gói
    - Tính kế thừa
    - Tính trừu tượng
    - Tính đa hình
4. Dependency Injecttion(DI), Inversion of Control(IoC)
