**Phần 1: Framework và xây dựng RESTful API**  
**1\. Cấu trúc các thư mục và chức năng**

- controller: Xử lý http request từ client và chuyển xuống tầng service  
- service : nằm phía dưới controller xử lý các logic nghiệp vụ trước khi trả lại kết quả cho controller  
- repository: nằm dưới service tương tác trực tiếp với Database  
- entity: các entity mapping với database  
- dto (data object transfer): Là các java bean, có tác dụng như đối tượng trung chuyển dữ liệu giữa các controller, service  
- properties: file cấu hình ứng dụng: database, port, JPA, logging  
- pom.xml : quản lý các dependency Maven, phiên bản spring boot và các thư viện cần thiết

**2\. Xây dựng RESTful API cơ bản**

- Khái niệm REST API và HTTP Methods:

		\+, RESTful API là 1 kiến trúc giao tiếp giữa client và server dựa trên HTTP theo nguyên tắc REST(Representational State Transfer), là các để ứng dụng gửi và nhận dữ liệu qua mạng Internet 1 cách chuẩn mực, dễ mở rộng và dễ hiểu ⇒ Là API tuân theo kiến trúc REST  
\+, HTTP Methods:

- GET : Lấy dữ liệu từ server  
-  POST : Thêm mới dữ liệu  
- PUT/PATCH: Cập nhật dữ liệu  
- DELETE: Xoá dữ liệu

     \-     Định nghĩa class Controller:  
		\+, @RestController ⇒Annotation  định nghĩa class controller trả về JSON  
		\+, @RequestMapping(“/api/students”) ⇒Annotation  URL Base  
		\+, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping ⇒Annotation  ánh xạ HTTP method

- Xử lý Request:

  \+, Path Variable: lấy dữ liệu từ URL, thường áp dụng cho method delete

  	@GetMapping("/{id}")

  public Student getStudent(@PathVariable Long id) { ... }


  \+,Query Param: Lấy dữ liệu tử query string

  	@GetMapping

  public List\<Student\> getByAge(@RequestParam int age) { ... }

  \+, Request Body: Dữ liệu dạng Json gửi từ client

  	@PostMapping

  public Student createStudent(@RequestBody Student student) { ... }

- Xử lý response:   
  Client nhận kết quả dạng Json ( các DTO ) và Server sẽ response message \+ http status code để biết lỗi nếu sai  
  Các HTTP Status Code  
  	\+, 2xx : Thành công không lỗi gì  
  	\+, 3xx : Chuyển hướng  
  	\+, 4xx : Lỗi đa số bên phía client  
  		400 Bad Request: Request không hợp lệ  
  		401 Unauthorized: Chưa xác thực  
  		403 Forbidden : Đăng nhập thành công nhưng không có quyền truy cập vào API đó  
  		404 Not Found : Không tìm thấy tài nguyên  
  		405 Method Not Allowed : HTTP method không được cấp phép  
   	\+, 5xx : Lỗi chắc chắn thuộc về server  
  		500 Internal Server Error : Lỗi server chung  
  		502 Bad Gateway : Server nhận response không hợp lệ từ server khác  
  		503 Service Unavailable : Server tạm thời không phục vụ  
  		504 Gateway Timeout : Server trả quá chậm

**Phần 2 Tích hợp Database(ORM)**

1. **Khái niệm**  
- ORM ( Object-Relational Mapping) là kỹ thuật cho phép ánh xạ giữa các đối tượng trong code và bảng trong cơ sở dữ liệu RDBMS. Thay vì viết câu lệnh SQL trực tiếp khi truy vấn, ORM cho phép thao tác dữ liệu bằng đối tượng Java  
- Lợi ích:

  \+, Giảm viết SQL thủ công

  \+, Hỗ trợ maintainable, quản lý transaction tự động \-\> rollback khi lỗi

  \+, Dễ dàng chuyển đổi giữa các RDBMS khác nhau

  \+, Mapping quan hệ phức tạp ManyToMany, OneToMany,...

2. **Sử dụng ORM theo Framework**  
- JPA \- Java Persistence API: Chuẩn API để làm ORM trong Java  
- Hibernate: Một implementation phổ biến của JPA, thực thi các annotation và quản lý các entity  
- Cấu hình Spring Data JPA:

  spring.datasource.url \= jdbc:mysql://localhost:3306/dbtraining

  spring.datasource.username \= root

  spring.datasource.password \= 213205

  server.port\=8080


  

  spring.jpa.hibernate.ddl-auto \= none

  \#spring.jpa.hibernate.ddl-auto \= create

  \#spring.jpa.hibernate.ddl-auto \= update

  \#spring.jpa.hibernate.ddl-auto \= create-drop


  spring.jpa.properties.hibernate.dialect \= org.hibernate.dialect.MySQLDialect

  spring.jpa.properties.hibernate.enable\_lazy\_load\_no\_trans \= truesp

  	\+,ddl-auto=update ⇒ Tự động tạo và cập nhật bảng DB theo Entity

  	\+, show-sql \= true ⇒ Hiển thị câu lệnh SQL ra console

- Các annotation quan trọng:

  \+, @Entity : đánh dấu class là 1 entity ánh xạ bảng trong DB

  \+, @Table(name \= “ ”): ánh xạ bảng nào trong DB

  \+, @Id : Khoá chính

  \+, @GeneratedValue(stategy \= …) ⇒ Giá trị tự động tăng

  \+, @Column() : map với cột nào trong table 

- Tạo repository:


  @Repository

  public interface ProductRepository extends JpaRepository\<ProductEntity, Long\> {

     List\<ProductEntity\> findAllByIsDeletedFalse();

     Optional\<ProductEntity\> findByIdAndIsDeletedFalse(long id);

     Optional\<ProductEntity\> findByProductNameAndIsDeletedFalse(String name);

     void deleteById(long id);

  }


		⇒ Lợi ích của Spring Data JPA Repository:  
			\+, Có sẵn các phương thức CRUD  
			\+, Có thể viết Query method theo tên mà không cần dùng SQL  
	  
