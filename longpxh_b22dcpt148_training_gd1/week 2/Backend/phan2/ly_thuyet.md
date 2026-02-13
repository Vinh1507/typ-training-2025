# Phần 2: Tích hợp Database (ORM)

## ORM là gì ?
- nó là 1 cách tiếp cận (không phải framework hay thư viện), giúp ánh xạ đối tượng trong code với các bảng trong database
- thay vì phải thao tác trực tiếp với dữ liệu, ta sẽ thao tác thông qua các đối tượng ánh xạ này.


## Sử dụng ORM trong Spring:
- JPA: là 1 chuẩn trong Java để làm việc với ORM , quy định các anotation và interface.
- Hibernate: Là 1 framework ORM - thực thi ORM, triển khai theo chuẩn JPA.  
- Trong Spring Boot, ta thường dùng Spring Data JPA, là một module của Spring hỗ trợ làm việc với JPA dễ hơn.
- Để sử dụng, ta tạo các entity object, đánh dấu bằng anotation @Entity, bắt buộc phải có id với anotation @Id, có thể sử dụng thêm Lombok để có các anotation như @Data, @Getter, @Setter,.... giúp code gọn hơn.
- Ở tầng repository, ta tạo các interface kế thừa JpaRepository - đây là 1 interface trong Spring Data Jpa, nó giúp tự động sinh ra các hàm CRUD cơ bản mà ta không cần viết truy vấn SQL.
