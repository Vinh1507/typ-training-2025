#(Database + OOP)

```text
                                                        		Phần 1: Database

Data Types : Số nguyên:  (TINY/SMALL/MEDIUM/BIG)INT
	     Số thực: FLOAT(chính xác thấp), DOUBLE(chính xác hơn FLOAT), DECIMAL(chính xác cao)
	     Kiểu chuỗi: CHAR(độ dài cố định,tối đa 255 kí tự), 
			 VARCHAR(độ dài thay đổi, tối đa 65535 kí tự), 
			 TEXT(lưu trữ văn bản dài, tối đa 4GB)
	     Kiểu ngày và thời gian:
			 DATE: YYYY-MM-DD
			 DATETIME: YYYY-MM-DD HH:MM:SS
			 TIMESTAMP: giống TIMEDATE nhưng được cập nhật tự động khi có thay đổi dữ liệu

So sánh trong MySQL: =, !=, <>, >, <, >=, <= (so sánh được chuỗi, số, ngày tháng)

Ràng buộc toàn vẹn trong bảng:
	Các ràng buộc nhất quán:
		primary key(A1,..., An)
		foreign key(Am,..., An)
		not null

Cấu trúc của 1 SQL Query: 
  	select A1,...,An (danh sách thuộc tính) (select * để chọn full thuộc tính) (distinct để trả về giá trị khác nhau)
  	from r1,...,rn (các bảng)
  	where P (điều kiện)

Khởi tạo giá trị mặc định cho cột khi tạo bảng = x: column_name <datatype> default x
 
Trả về thời gian thực: now()
Giá trị cột kiểu số nguyên tự động tăng dần: auto_increment

DDL: 
  	Tạo bảng: create table <nametable> ();
  	Xóa bảng: drop table <nametable>;
  	Đổi tên bảng: rename table <oldname> to <newname>;
    alter table <table_name> + 
        Đổi tên thuộc tính: rename column <old_name> to <new_name>
    		Thêm cột:  add column <tên cột> <datatype> default x; (mặc định là null);
    		Xóa cột: drop <tên cột>;
    		Thêm khóa chính: add primary key(ID);
    		Xóa khóa chính: drop primary key;
    		Thêm Foreign key: add foreign key (PersonID) references Persons(PersonID);
    		Đặt tên cho foreign key: add constraint (FK_PersonOrder) foreign key (PersonID) references Persons(PersonID);
    		Xóa Foreign key: drop foreign key (FK_PersonOrder);
    		Tạo cột có các giá trị không trùng nhau: thêm UNIQUE vào sau.
    		Tạo ràng buộc: add constraint <constraint_name> check (mệnh đề điều kiện);
    		Xóa ràng buộc: drop <constraint_name>;
	
DML: 
    Thêm dữ liệu: insert into <table_name> (<danh sách thuộc tính cần thêm>)
  	      value (___),(___),...,(___);
  	Sửa đổi bản ghi hiện có:
  		update <table_name>
  		set column1 = value1, column2 = value2,...
  		where <condition>;	
  	Xóa bản ghi hiện có: 
  		delete from <table_name> where <condition>;
  		limit <num> - giới hạn số bản ghi trả về

Aggregation:
  	avg: trả về giá trị trung bình của cột được chọn
  	min: trả về giá trị nhỏ nhất của cột được chọn
  	max: trả về giá trị lớn nhất của cột được chọn
  	sum: tính tổng các giá trị của cột
  	count: đếm
  		select count(*)   |
  		from <table_name> | đến số dòng của bảng
  	  select count(column_name)  |
  		from <table_name> 	       | đếm số giá trị !=null của cột
  		select count(distinct column_name)  |
  	  from <table_name>		                | đếm số giá trị khác nhau !=null của cột

Toán tử (NOT)BETWEEN: lấy trong khoảng
Toán tử LIKE: 	được sử dụng trong where để tìm 1 mẫu cụ thể
		select * from <table_name> where <column_name> like '';

REGEXP: mạnh hơn LIKE
  	cú pháp : select * from <table_name> where <column_name> regexp '';
  	các pattern hay sử dụng:
  		'A' : có chứa từ 'A'
  		'A|B': có chứ từ 'A' hoặc 'B'
  		'^A': bắt đầu bằng 'A'
  		'A$': kết thúc bằng 'A'

Toán từ IN: cho phép chỉ định nhiều giá trị trong mệnh đề WHERE (viết tắt của điều kiện OR)

Điều kiện rẽ nhánh:
  	CASE
  	    WHEN condition1 THEN result1
  	    WHEN condition2 THEN result2
  	    WHEN condition3 THEN result3
  	    ELSE result
  	END;
    IF(condition, result1, result2);
    IFNULL(column_name,value): null -> value
    NULLIF(column_name,value): value -> null
  
Date Functions:
  	date_format(date,format): định dạng ngày
  	timestampdiff(unit,start,end): tính khoảng cách giữa 2 mốc tgian
  	DATE_ADD(date, INTERVAL n unit): cộng thêm tgian
  	DATE_SUB(date, INTERVAL n unit): trừ đi tgian
String Functions:
  	length(string): độ dài chuỗi
  	lower(string): chuyển thành chữ thường
  	upper(string): chuyển thành chữ hoa
  	left(string,n): lấy n kí tự từ trái
  	right(string,n): lấy n kí tự từ phải
  	substring()/substr(string,start,len): cắt chuỗi bắt đầu từ start độ dài len
  	trim(string): xóa khoảng trắng ở đầu và cuối
  	replace(string,old,new): thay thế chuỗi con
  	concat(string1, string 2,...): nối chuỗi
  	instr(string, substring): tìm vị trí chuỗi con
  	reverse(string): đảo ngược chuỗi
  	substring_index(str, delimiter, count): cắt chuỗi đến trước delimiter thứ count, có thể dùng count<0 để cắt từ cuối
Math Functions:
  	abs(number): giá trị tuyệt đối
  	ceil(number): làm tròn lên
  	floor(number): làm tròn xuống
  	round(number,n): làm tròn n chữ số thập phân
  	rand(): số ngẫu nhiên từ 0 đến 1
  	power(x,y): x^y
  	sqrt(x): căn bậc 2
  	mod(a,b): a%b
  	PI(): số pi(3.14159...)
  	sin(angle)/cos(angle)/tan(angle): hàm lượng giác		
	
Tập Hợp:
  	UNION: hợp 2 tập kết quả và loại bỏ giá trị trùng lặp
  	UNION ALL: hợp 2 tập kết quả và KHÔNG loại bỏ giá trị trùng lặp
  	INTERSECT: giao 2 tập dữ liệu (có thể dùng INNER JOIN để thay thế)
  	EXCEPT: lấy phần hiệu
  	EXISTS: kiểm tra sự tồn tại của bất kì bản ghi nào trong truy vấn phụ

Sắp xếp dữ liệu(ORDER BY):
  	ASC: sắp xếp tăng dần
  	DESC: sắp xếp giảm dần
Nhóm dữ liệu(GROUP by):
  	HAVING: lọc dữ liệu sau khi phân nhóm

Thứ tự chạy các câu lệnh:
  	SELECT...    -- (4) Thực hiện SELECT
  	FROM...      -- (1) Đọc và join bảng
  	WHERE...     -- (2) Lọc dữ liệu
  	GROUP BY...  -- (3) Nhóm dữ liệu
  	<Aggregation>-- (3.1) Chạy các Aggregation
  	HAVING...    -- (3.2) Lọc sau khi nhóm
  	ORDER BY...  -- (5) Sắp xếp kết quả
  	LIMIT...     -- (6) Giới hạn số lượng


JOIN:
  	INNER JOIN: chỉ lấy các bản ghi có điểm chung giữa các bản
  	LEFT JOIN: lấy tất cả bản ghi bảng bên trái, nếu không có khớp từ bảng bên phải thì trả về NULL
  	RIGHT JOIN: lấy tất cả bản ghi bảng bên phải, nếu không có khớp từ bảng bên trái thì trả về NULL
  	FULL OUTER JOIN: lấy tất cả bản ghi từ cả 2 bảng, nếu không khớp thì trả về NULL
  	CROSS JOIN: Nhân chéo 2 bảng (tạo tổ hợp tất cả các bản ghi)

VIEW:
  	Tạo VIEW:
  		CREATE VIEW view_name 
  		AS
  		    SELECT column1, column2, ...
  		    FROM table_name
  		    WHERE condition;
  	Xóa VIEW: DROP VIEW view_name;

PROCEDURE:
  	Tạo PROCEDURE:
  		DELIMITER //
  		CREATE PROCEDURE procedure_name (<tham_số>)
  		BEGIN
      		-- các câu lệnh SQL
  		END //
  		DELIMITER ;
  	Gọi PROCEDURE:
  		CALL procedure_name(<tham số>);

FUNCTION:
  	DELIMITER //
  	CREATE FUNCTION function_name([tham_số])
  	RETURNS kiểu_dữ_liệu
  	DETERMINISTIC
  	BEGIN
      	    -- khối lệnh xử lý
      	    RETURN giá_trị;
  	END //
  	DELIMITER ;

TRANSACTION:
  	Tạo TRANSACTION:
  		BEGIN TRANSACTION;
  		-- xác nhận thay đổi khi không có lỗi
  		COMMIT;
  	
  Hủy bỏ giao dịch nếu có lỗi: ROLLBACK
  	Tạo savepoint để có thể rollback 1 phần: SAVEPOINT savepoint_name;
  	Xóa savepoint: RELEASE SAVEPOINT savepoint_name;
  	ROLLBACK 1 phần: ROLLBACK TO savepoint_name;


TRIGGER: là đoạn code SQL tự động chạy khi có hành động INSERT, UPDATE, DELETE trên một bảng.
  	Tạo TRIGGER
    		CREATE TRIGGER trigger_name
    		BEFORE|AFTER INSERT|UPDATE|DELETE
    		ON table_name
    		FOR EACH ROW
    		BEGIN
      		-- code tự động
    		END;
  	Từ khóa NEW: Biểu diễn giá trị mới của dòng dữ liệu (sắp được chèn hoặc cập nhật)
  	Từ khóa OLD: Biểu diễn giá trị cũ của dòng dữ liệu (trước khi cập nhật hoặc xóa)

Access Control:
  	Tạo người dùng mới: 
  		CREATE USER 'tên_user'@'host' IDENTIFIED BY 'mật_khẩu';
  		ví dụ: CREATE USER 'student'@'localhost' IDENTIFIED BY '123456';
  	Xóa người dùng:
  		DROP USER 'tên_user'@'host';
  	Cấp quyền với GRANT: 
  		GRANT quyền_danh_sách ON cơ_sở_dữ_liệu.tên_bảng TO 'tên_user'@'host';
  		ví dụ: GRANT SELECT, INSERT ON library.books TO 'student'@'localhost';
  	Thu hồi quyền với REVOKE:
  		REVOKE quyền_danh_sách ON cơ_sở_dữ_liệu.tên_bảng FROM 'tên_user'@'host';
  		ví dụ: REVOKE INSERT ON library.books FROM 'student'@'localhost';
  	Kiểm tra quyền của user:
  		SHOW GRANTS FOR 'tên_user'@'host';
  	Xem quyền của người đang đăng nhập:
  		SHOW GRANTS;

    



                                      							Phần 2: OOP

Biến:
	Là vùng nhớ dùng để lưu dữ liệu.
	Mỗi biến có tên, kiểu dữ liệu và giá trị.

Hàm:
	Là khối lệnh thực hiện một nhiệm vụ cụ thể.
	Có thể truyền tham số và trả về giá trị.

I/O
	Nhập dữ liệu:
		import java.util.Scanner;
		Scanner sc = new Scanner(System.in);
	Xuất dữ liệu:
		System.out.println();

Vòng Lặp:
	Dùng để lặp lại một đoạn mã nhiều lần.
	Các loại vòng lặp phổ biến: for, while, do-while.

Thao tác file:
	Package cần dùng : 
		import java.io.*;
		import java.util.Scanner;
	Tạo file mới:
		import java.io.File;
		import java.io.IOException;

	  public class CreateFile {
		    public static void main(String[] args) {
	        	try {
            			File f = new File("data.txt"); // tên file
            			if (f.createNewFile()) {
                			System.out.println("Đã tạo file: " + f.getName());
            			} else {
                			System.out.println("File đã tồn tại.");
            			}
        		} catch (IOException e) {
            			System.out.println("Lỗi khi tạo file.");
            			e.printStackTrace();
        		}
    		    }
		}
	
	Xóa file: 
		import java.io.File;

		public class DeleteFile {
    			public static void main(String[] args) {
        			File f = new File("data.txt");
        			if (f.delete()) {
            				System.out.println("Đã xóa file: " + f.getName());
        			} else {
            				System.out.println("Xóa thất bại.");
        			}
    			}
		}
  Ghi dữ liệu vào file: Dùng FileWriter
	Đọc dữ liệu từ file: Dùng Scaner

class: Là khuôn mẫu (mẫu thiết kế) để tạo ra đối tượng (object).
object: Là thực thể được tạo ra từ một class.
abstract class: Là class không thể tạo object trực tiếp, chỉ dùng để kế thừa, có thể chứa cả abstract method và cả method thường.
interface:
	Interface là 1 bản thiết kế chỉ chứa các phương thức trừu tượng và hằng số.
	Giúp đa hình và tách biệt logic.
	khởi tạo : interface + <tên interface>{...}


Các tính chất OOP:
	Tính đóng gói: che giấu dữ liệu bên trong đối tượng (private), chỉ cho phép truy cập thông qua các phương thức công khai (getter / setter).
	Tính kế thừa: Cho phép class con sử dụng thuộc tính và phương thức của class cha
	Tính đa hình: 
		Class có nhiều phương thức hoặc constructor cùng tên nhưng khác nhau về số lượng tham số hoặc kiểu dữ liệu
		Class con định nghĩa lại phương thức của class cha.
	Tính trừu tượng: 
		Ẩn đi chi tiết, chỉ hiện thị các thông tin cần thiết. Biết được nó làm gì, không cần biết nó làm thế nào.
		Trừu tượng trong java thể hiện bằng abstract class hoặc interface.

Inversion of Control(Ioc):là nguyên tắc đảo ngược quyền tạo và quản lý đối tượng từ chương trình của bạn sang một thành phần bên ngoài
Dependency Injection(DI): là cách hiện thực cụ thể của IoC.Nó “tiêm” (inject) các đối tượng phụ thuộc vào bên trong class thay vì để class tự tạo.
	Constructor Injection: truyền dependency qua hàm khởi tạo (constructor).
	Setter Injection: truyền dependency qua phương thức setter
	


```