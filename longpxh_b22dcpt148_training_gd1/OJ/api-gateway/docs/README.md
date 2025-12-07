# Online Judge (OJ) - Tài liệu API Gateway

## Tổng quan

Đây là Backend API Gateway cho hệ thống Online Judge (Hệ thống chấm bài tự động), được xây dựng bằng **Spring Boot 3.5.7** và **Java 21**. Ứng dụng được thiết kế dưới dạng dự án Maven đa module (multi-module), tuân thủ nguyên lý **Clean Architecture** và **Domain-Driven Design (DDD)** để quản lý bài tập lập trình, bài nộp, lớp học và quy trình chấm bài tự động.

**Phiên bản hiện tại:** 1.0.0-SNAPSHOT

## Cấu trúc dự án

```
api-gateway/
├── application/                      # Module ứng dụng chính (Presentation Layer)
│   ├── controller/                   # Các REST controller
│   ├── dto/                          # Data Transfer Objects (Request/Response)
│   └── security/                     # Cấu hình bảo mật & filters
├── platform/
│   ├── core/                         # Các thành phần cốt lõi & abstractions
│   │   ├── domain/                   # Domain objects chung (Pagination, PageResult)
│   │   └── http/                     # Tiện ích HTTP (ApiResponse, HttpStatus)
│   ├── components/                   # Triển khai nghiệp vụ (Domain Implementation)
│   │   ├── iam/                      # Quản lý định danh & quyền truy cập
│   │   ├── problem-management/       # Quản lý bài tập & Test case
│   │   ├── code-runner/              # Xử lý bài nộp & chấm điểm
│   │   ├── class-management/         # Quản lý lớp học & ghi danh
│   │   ├── test-case/                # Logic xử lý test case
│   │   ├── result-processor/         # Xử lý kết quả chấm bài
│   │   └── storage/                  # Quản lý lưu trữ file
│   └── libs/                         # Thư viện tiện ích chia sẻ
│       ├── file/                     # Xử lý file
│       └── env/                      # Quản lý biến môi trường (.env)
├── migration/                        # Module quản lý DB migration (Flyway)
└── start/                            # Điểm khởi chạy ứng dụng (Entry point)
```

## Các Module Chính

### **platform/core** - Shared Abstractions
Định nghĩa các interface và tiện ích chung được sử dụng toàn hệ thống. Module này **KHÔNG** phụ thuộc vào bất kỳ component nghiệp vụ nào.

**Nội dung:**
- `ApiResponse<T>`: Wrapper chuẩn hóa phản hồi API.
- `PageResult<T>`: Abstraction cho phân trang.
- `HttpStatus`: Các mã trạng thái tùy chỉnh.

### **platform/components** - Domain Implementation Layer
Chứa toàn bộ logic nghiệp vụ (business logic), được tổ chức theo mô hình **Bounded Context**.

**Các Component chính:**
1.  **iam/**: Users, Roles, Permissions, Authentication.
2.  **problem-management/**: Bài tập, độ khó, giới hạn thời gian/bộ nhớ.
3.  **code-runner/**: Bài nộp, kết quả chấm (AC, WA, TLE), logic chấm bài.
4.  **class-management/**: Lớp học, phân công giáo viên/sinh viên.
5.  **result-processor/**: Xử lý và tổng hợp kết quả từ Code Runner.
6.  **storage/**: Abstraction và implementation cho việc lưu trữ file (đề bài, test case).

**Cấu trúc một Component:**
```
component/
├── domain/
│   ├── model/          # Pure Domain Entities (POJOs - Không phụ thuộc framework)
│   └── service/        # Domain Services (Logic nghiệp vụ)
└── infrastructure/
    └── persistence/
        └── jpa/
            ├── entity/       # JPA Entities (@Entity - Mapping Database)
            ├── repository/   # Spring Data JPA Repositories
            └── hydrator/     # Bộ chuyển đổi giữa Domain <-> Entity
```

### **platform/libs** - Shared Libraries
Các thư viện tiện ích dùng chung cho toàn bộ platform.
- **env/**: Hỗ trợ load biến môi trường từ file `.env`, giúp cấu hình ứng dụng linh hoạt.
- **file/**: Các tiện ích xử lý file (đọc/ghi, parse CSV/Excel...).

### **application** - REST API Layer
Tầng giao diện (Presentation layer) cung cấp các HTTP endpoints. Nhiệm vụ chính là điều phối request đến các domain services và xử lý mapping DTO.

**Trách nhiệm:**
- Xử lý HTTP Request/Response.
- Validate dữ liệu đầu vào (`@Valid`).
- Tích hợp ngữ cảnh bảo mật (`RequestAuthContext`).
- Xử lý lỗi toàn cục (Global Exception Handling).

## Các Mẫu Kiến Trúc (Architecture Patterns)

### Clean Layered Architecture (Kiến trúc phân lớp sạch)

Ứng dụng tuân thủ luồng phụ thuộc một chiều nghiêm ngặt:

```
┌──────────────────────────────────────────┐
│      application (Presentation)          │
│  Controllers → DTOs → ApiResponse        │
└──────────────────┬───────────────────────┘
                   │
                   ↓ (phụ thuộc vào)
┌──────────────────────────────────────────┐
│      platform/components (Domain)        │
│  Services → Models → Repositories        │
└──────────────────┬───────────────────────┘
                   │
                   ↓ (sử dụng)
┌──────────────────────────────────────────┐
│      Infrastructure (Persistence)        │
│  JPA Entities → Database                 │
└──────────────────────────────────────────┘
```

### Repository Pattern với Hydrator

Tách biệt hoàn toàn **Domain Models** (Logic nghiệp vụ) khỏi **JPA Entities** (Logic lưu trữ) bằng cách sử dụng Hydrator.

```java
// Domain Model (Java thuần)
public class Problem { ... }

// JPA Entity (Mapping Database)
@Entity
@Table(name = "problems")
public class ProblemJpa { ... }

// Hydrator (Bộ chuyển đổi)
@Component
public class ProblemJpaHydrator {
    public Problem toDomain(ProblemJpa entity) { ... }
    public ProblemJpa toEntity(Problem domain) { ... }
}
```

### Class-Based Filtering (Tính năng cốt lõi)

Một tính năng kiến trúc quan trọng là cô lập dữ liệu dựa trên lớp học.
- Người dùng chỉ có thể truy cập tài nguyên (bài tập, bài nộp) liên quan đến các lớp mà họ tham gia.
- **Triển khai**:
    1.  **JWT**: Chứa claim `class_ids`.
    2.  **Security Context**: `ClassAwareUser` lưu giữ các ID này.
    3.  **Repository**: Các truy vấn tự động lọc theo điều kiện `WHERE class_id IN (...)`.

## Công Nghệ Sử Dụng (Tech Stack)

### Core Dependencies
| Thư viện | Phiên bản | Mục đích |
|------------|---------|---------|
| Spring Boot | 3.5.7 | Web framework nền tảng |
| Spring Security | 6.x | Xác thực & Phân quyền |
| Spring Data JPA | 3.x | ORM & Truy cập dữ liệu |
| Flyway | 11.x | Quản lý version Database |
| PostgreSQL | 14+ | Cơ sở dữ liệu chính |

### Security & Tiện ích
| Thư viện | Phiên bản | Mục đích |
|------------|---------|---------|
| JJWT | 0.11.5 | Tạo & xác thực JWT Token |
| Lombok | 1.18.x | Giảm thiểu boilerplate code |
| MapStruct | 1.5.x | Mapping object |
| Java Dotenv | 5.2.2 | Quản lý biến môi trường |

## Cấu Hình

### Application Profiles
- **local**: Môi trường phát triển cục bộ (`application-local.yml`)
- **dev**: Môi trường development server
- **prod**: Môi trường production

### Các Properties quan trọng
```yaml
app:
  security:
    jwt:
      secret: ${JWT_SECRET}
      access-expiration: 900000 # 15 phút
      refresh-expiration: 604800000 # 7 ngày
```

## Cơ Sở Dữ Liệu (Database Schema)

Được quản lý bởi Flyway tại `migration/src/main/resources/db/migration/`.

**Các bảng chính:**
- `users`, `roles`, `permissions`: Quản lý người dùng & quyền.
- `classes`, `student_classes`: Quản lý lớp học.
- `problems`: Liên kết với `classes`.
- `submissions`: Liên kết với `users` và `problems`.

## Cài Đặt & Chạy

### Yêu cầu
- Java 21
- Maven 3.8+
- PostgreSQL

### Tạo file .env từ thư mục gốc (cấu trúc như .env.example)

### Build
```bash
mvn clean package -DskipTests
```

### Run
```bash
mvn spring-boot:run -pl start
```

### Script tạo bảng (linux)
```bash
./scripts/flyway.sh migrate
```

**Online Judge API Gateway**

Code by Hoàng Long (hoanglong2534)
