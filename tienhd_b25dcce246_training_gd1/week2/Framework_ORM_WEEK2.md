# Tuần 2: Framework & ORM

---

##  Phần 1: Framework & Xây dựng RESTful API

###  1. Java: Spring Boot

###  2. Tạo Project & Cấu trúc

####  Khởi tạo project
[Spring Initializr](https://start.spring.io/)

####  Cấu trúc thư mục

| Thư mục | Mô tả |
|----------|-------|
| **controllers** | Bộ điều khiển — xử lý request từ client, gọi service và trả response |
| **models** | Mô hình dữ liệu — ánh xạ các bảng trong database |
| **services** | Chứa business logic |
| **repositories** | Giao tiếp với DB, kế thừa `JpaRepository` hoặc `CrudRepository` |
| **migrations** | Quản lý thay đổi cấu trúc DB (schema) |
| **config** | Cấu hình hệ thống (DB, env, middleware, security, v.v.) |

####  File `application.properties`
Dùng để thiết lập cấu hình như:

- Kết nối CSDL  
- Port chạy server  
- Logging  
- Bảo mật  

 **Vị trí:** `src/main/resources/application.properties`

**Ưu điểm:**  
- Tách biệt cấu hình & logic  
- Hỗ trợ nhiều môi trường (`dev`, `test`, `prod`)  
- Tự động load khi app khởi động  

---

### 3. RESTful API Cơ bản

**Khái niệm:**  
REST API là giao diện cho phép hệ thống giao tiếp qua HTTP.
HTTP Methods là các hành động mà client yêu cầu server thực hiện trên một tài nguyên
**HTTP Methods:**

| Method | Mô tả | Ví dụ |
|--------|-------|-------|
| GET | Lấy danh sách | `/items` |
| GET | Lấy chi tiết | `/items/{id}` |
| POST | Thêm mới | `/items` |
| PUT | Cập nhật | `/items/{id}` |
| DELETE | Xóa | `/items/{id}` |

---

##  Phần 2: Tích hợp Database (ORM)

### 1. Khái niệm ORM

> ORM (Object Relational Mapping) là kỹ thuật ánh xạ dữ liệu giữa CSDL quan hệ và đối tượng trong ngôn ngữ lập trình hướng đối tượng.

### 2. Lợi ích của ORM
**Cách hoạt động:**
- Đặc trưng cơ bản của ORM là gói gọn CSDL trong 1 object. 1 phần của object sẽ chứa data, 
    và phần còn lại lo việc data xử lý như nào và biến nó thành CSDL quan hệ.

**Ưu điểm:**
- Tuân thủ nguyên tắc **DRY**
- Giảm code lặp, dễ bảo trì
- Không cần viết SQL thủ công
- Tận dụng ưu điểm OOP (kế thừa, đóng gói)

**Nhược điểm:**
- Mỗi framework có ORM riêng → tốn thời gian học
- Không mạnh bằng **Raw SQL**

### 3. ORM trong Spring Boot

#### JPA & Hibernate
| Thành phần | Vai trò |
|-------------|----------|
| **JPA (Java Persistence API)** | Chuẩn để ánh xạ object ↔ database |
| **Hibernate** | Thư viện thực thi JPA, tự động tạo SQL và quản lý DB |

#### Cấu hình Spring Data JPA

**`pom.xml`**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

**`application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_name
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### Định nghĩa Entity
- @Entity: biến class thành bảng trong database			
- @Table(name = "user"): Chỉ rõ tên bảng
- @Id: Khóa chính
- @GeneratedValue: Tự động sinh ID

```java
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

#### Tạo Repository kế thừa JpaRepository

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> { }
```

---

## Kiến thức Bổ sung

### Thymeleaf

- Java Template Engine để render HTML.  
- Dùng cú pháp `${variable}` để lấy giá trị từ **Model**.

```html
<h1 th:text="${name}"></h1>
```

### Spring Beans

Lấy bean thủ công:
```java
InterfaceName var = context.getBean(InterfaceName.class);
```

### Annotation

#### Các Annotation có sẵn

| Annotation | Mô tả |
|-------------|-------|
| `@Override` | Ghi đè phương thức từ lớp cha |
| `@Deprecated` | Đánh dấu phương thức không nên dùng |
| `@SuppressWarnings` | Bỏ qua cảnh báo trình biên dịch |
| `@FunctionalInterface` | Đánh dấu interface chỉ có 1 phương thức trừu tượng |

---

#### Annotation trong Spring

| Annotation | Mô tả |
|-------------|-------|
| `@Component` | Đánh dấu class là Bean |
| `@PostConstruct` | Chạy sau khi bean được khởi tạo xong |
| `@PreDestroy` | Chạy trước khi bean bị hủy |
| `@Primary` | Đặt mặc định nếu có nhiều bean cùng kiểu |
| `@Qualifier` | Chỉ định cụ thể bean nào cần inject |
| `@Service` | xử("bean_name") lý logic nghiệp vụ của ứng dụng |
| `@Repository` | Làm việc với database |
| `@Controller` | Xử lý request từ người dùng, trả về view hoặc JSON |
| `@RestController` | Xử lý request từ người dùng, trả về view hoặc JSON |
| `@Autowired` | Tự động tiêm Bean vào nơi cần dùng |
| `@Configuration` | Đánh dấu một class là “class cấu hình” (configuration class) - chứa các bean được Spring quản lý |
| `@Bean` | Dùng trong class `@Configuration` để tạo Bean thủ công |
| `@RequestMapping` | Đặt trên class hoặc method, định nghĩa URL chung (prefix) hoặc cụ thể |

---

#### Ví dụ Mapping trong Spring

```java
@GetMapping("/path")    = @RequestMapping(value="/path", method=RequestMethod.GET)
@PostMapping("/path")   = @RequestMapping(value="/path", method=RequestMethod.POST)
@PutMapping("/path")    = @RequestMapping(value="/path", method=RequestMethod.PUT)
@DeleteMapping("/path") = @RequestMapping(value="/path", method=RequestMethod.DELETE)
@PatchMapping("/path")  = @RequestMapping(value="/path", method=RequestMethod.PATCH)
```

---

#### Khác

| Annotation | Mô tả |
|-------------|-------|
| `@PostMapping` | Có nhiệm vụ đánh dấu hàm xử lý POST request trong Controller. |
| `@RequestBody` | Ánh xạ dữ liệu JSON từ phần body của HTTP request thành một đối tượng Java. |


---

## Spring MVC

**Mô hình:**
- **Model:** đại diện dữ liệu (table trong DB)
- **View:** giao diện hiển thị
- **Controller:** xử lý logic và request

---

## Spring Security

**Authentication:** xác minh danh tính người dùng  
**Authorization:** xác định quyền của người dùng  

**Nguyên tắc:**  
1. Trust nothing — luôn xác thực mọi request  
2. Xác định rõ quyền hạn  
3. Nhiều lớp bảo mật  
4. Giữ kiến trúc đơn giản  

**Cài đặt:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**Sau khi cài đặt:**
- Có sẵn `/login`, `/logout`  
- Password hiển thị trong console  
**Các tính năng khác:**
- Bảo vệ CORS (Cross-Origin Resource Sharing): CorsFilter
- Bảo vệ CSRF (Cross Site Request Forgery): CsrfFilter
- Login Page, Logout Page được cung cấp sẵn
- Thứ tự của Filter là "quan trọng". ChainFilter có nghĩa là được thực hiện tuần tự, ưu tiên như sau:
		1. Check bảo mật với các tấn công hay gặp
		Basic Check Filter: CORS, CSRF...
		2. Xác thực người dùng là ai ?
		Authentication Filter
		3. Xác định người dùng có thể làm gì ?
		Authorization Filter
		....

---

## HTML

> HTML (HyperText Markup Language) là ngôn ngữ để tạo và cấu trúc nội dung web.

---
## Tài liệu Tham khảo (Dạng Markdown)

- [Khái niệm Tight Coupling & Loosely Coupled](https://viblo.asia/p/khai-niem-tight-coupling-lien-ket-rang-buoc-va-cach-loosely-coupled-63vKjVERK2R)  
- [Dependency Injection & IoC (Ngọc Trinh)](https://viblo.asia/p/springgiai-thich-dependency-injection-di-va-ioc-bang-ngoc-trinh-naQZRW3Alvx)  
- [Spring Boot 1 — @Component và @Autowired](https://viblo.asia/p/spring-boot-1-huong-dan-atcomponent-va-atautowired-E375zXvJZGW)  
- [Spring Boot 2 — @Autowired, @Primary, @Qualifier](https://viblo.asia/p/spring-boot-2-atautowired-atprimary-atqualifier-GrLZD8d3Zk0)  
- [Spring Boot 3 — Spring Bean Life Cycle (@PostConstruct & @PreDestroy)](https://viblo.asia/p/spring-boot-3-spring-bean-life-cycle-atpostconstruct-va-atpredestroy-Qbq5Q4nmlD8)  
- [Spring Boot 4 — @Component vs @Service vs @Repository](https://viblo.asia/p/spring-boot-4-atcomponent-vs-atservice-vs-atrepository-maGK7k2AKj2)  
- [Spring Boot 5 — Component Scan là gì](https://viblo.asia/p/spring-boot-5-component-scan-la-gi-3P0lP4RGlox)  
- [Spring Boot 6 — @Configuration và @Bean](https://viblo.asia/p/spring-boot-6-atconfiguration-va-atbean-bJzKmyprK9N)  
- [Spring Boot 7 — Application Config & @Value](https://viblo.asia/p/spring-boot-7-spring-boot-application-config-va-atvalue-RQqKLwr657z)  
- [Spring Boot 8 — Hello World với @Controller & Thymeleaf](https://viblo.asia/p/spring-boot-8-tao-web-helloworld-voi-atcontroller-thymeleaf-Az45bG1qKxY)  
- [Spring Boot 9 — Cách Thymeleaf vận hành & Expression Demo](https://viblo.asia/p/spring-boot-9-giai-thich-cach-thymeleaf-van-hanh-expression-demo-full-gDVK227rKLj)  
- [Spring Boot 10 — @RequestMapping, @PostMapping, @ModelAttribute, @RequestParam](https://viblo.asia/p/spring-boot-10-atrequestmapping-atpostmapping-atmodelattribute-atrequestparam-web-to-do-voi-thymeleaf-aWj53NPQl6m)  
- [Spring Boot 11 — Hướng dẫn Spring Boot JPA MySQL](https://viblo.asia/p/spring-boot-11-huong-dan-spring-boot-jpa-mysql-GrLZD8dgZk0)  
- [Spring Boot 12 — Spring JPA Method & @Query](https://viblo.asia/p/spring-boot-12-spring-jpa-method-atquery-Qbq5Q4nGlD8)  
- [Spring Boot 13 — Thymeleaf + MySQL + i18n Web Demo](https://viblo.asia/p/spring-boot-13-special-chi-tiet-spring-boot-thymeleaf-mysql-i18n-web-demo-1VgZvXnm5Aw)  
- [Spring Boot 14 — RESTful API, @RestController, @PathVariable, @RequestBody](https://viblo.asia/p/spring-boot-14-restful-api-atrestcontroller-atpathvariable-atrequestbody-924lJd66KPM)  
- [Spring Boot 15 — Exception Handling (@ExceptionHandler, @RestControllerAdvice...)](https://viblo.asia/p/spring-boot-15-exception-handling-atexceptionhandler-atrestcontrolleradvice-atcontrolleradvice-atresponsestatus-maGK7k2eKj2)  
- [Spring Boot 16 — @ConfigurationProperties](https://viblo.asia/p/spring-boot-16-huong-dan-su-dung-atconfigurationproperties-3P0lP4Rolox)  
- [Spring Boot 17 — Spring Profiles](https://viblo.asia/p/spring-boot-17-chay-nhieu-moi-truong-voi-spring-profile-bJzKmypYK9N)  
- [Spring Boot 18 — Test Spring Boot](https://viblo.asia/p/spring-boot-18-huong-dan-chi-tiet-test-spring-boot-RQqKLwr457z)  
- [HTTP Request Methods (Wikipedia)](https://vi.wikipedia.org/wiki/Hypertext_Transfer_Protocol#HTTP_Request_methods)  
- [Application Properties (StackJava)](https://stackjava.com/spring/spring-boot-application-properties-cau-hinh-file-application-properties.html)  
- [Bean & ApplicationContext trong Spring Boot](https://viblo.asia/p/bean-va-applicationcontext-la-gi-trong-spring-boot-Ljy5Vjwj5ra)  
- [Annotation trong Java](https://viblo.asia/p/annotation-in-java-5OXLAYw8LGr)  
- [Spring Core Beans (Docs chính thức)](https://docs.spring.io/spring-framework/reference/core/beans.html)  
- [Object Relational Mapping (ORM)](https://viblo.asia/p/object-relational-mapping-djeZ1PQ3KWz)  
- [Spring MVC Annotations (Baeldung)](https://www.baeldung.com/spring-mvc-annotations)  
- [Spring MVC Config — Static Resources](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/static-resources.html)  
- [Spring MVC View (Docs chính thức)](https://docs.spring.io/spring-framework/reference/web/webmvc-view.html)  
- [Thymeleaf vs JSP](https://www.thymeleaf.org/doc/articles/thvsjsp.html)  

---
