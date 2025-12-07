### Phần 1: Framework & Xây dựng RESTful API
## Java(SpringBoot)
# Cấu trúc thư mục
src/
└── main/
    ├── java/
    │   └── com/dailycode/dreamshop/
    │       ├── controllers/
    │       │   └── _ProductController.java
    │       ├── service/
    │       │   ├── ProductService.java
    │       ├── repository/
    │       │   └── ProductRepository.java
    │       ├── model/ (hoặc entity/)
    │       │   └── Product.java
    │       ├── dto/
    │       │   ├── AddOrUpdateRequest.java
    │       └── DreamshopApplication.java
    │
    └── resources/
        ├── application.properties (hoặc application.yml)
        ├── static/         # file tĩnh (ảnh, css, js)
        ├── templates/      # view (nếu dùng Thymeleaf)
        └── db/             # script SQL, data mẫu
# Controllers
📌 Vai trò:
Xử lý HTTP request (từ client hoặc frontend), sau đó gọi xuống service.
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping
    public ResponseEntity<DataResponse> getAll(){
        DataResponse res= productService.getAll();
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
# Service 
📌 Vai trò:
Chứa logic nghiệp vụ chính của ứng dụng (business logic).
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ArrayList<Product> productsList= new ArrayList<>();
    public DataResponse getAll()
    {
       return new DataResponse(HttpStatus.OK, true, null,productsList);
    }
}
# Repository
📌 Vai trò:
Làm việc trực tiếp với database (CRUD, query, join, pagination...).
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
# Model
📌 Vai trò:
Khai báo các bảng trong database dưới dạng class (entity).
@Entity
@Table(name = "products")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String brand;
    private String name;
    private BigDecimal price;
    private int quantity;
}
# File application.properties
application.properties → cấu hình DB, port, JWT secret,...
spring.application.name=dreamshop
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/dreamshop?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=06012005Az09@
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
