# Online Judge (OJ) C++ Platform

## Tổng Quan

Nền tảng Online Judge C++ được xây dựng với Spring Boot 3.5.7 và Java 21, được thiết kế để quản lý bài tập lập trình, test cases, thực thi code và xử lý kết quả submission.


## Project Structure

```
api-gateway/
├── start/                          # Điểm khởi động ứng dụng Spring Boot
├── apps/                           # Các module ứng dụng
├── platform/
│   ├── core/                       # bussiness logic 
│   │   └── src/main/java/com/     
│   ├── libs/                       
│   │   ├── env/                    # Tiện ích cấu hình môi trường
│   │   └── file/                   # Tiện ích xử lý file (CSV, Excel)
│   ├── infrastructure/             # Các lớp JPA cơ sở và tầng lưu trữ
│   │   └── src/main/java/com/     
│   └── components/                 # Triển khai theo miền nghiệp vụ cụ thể
│       ├── iam/                    # Quản lý danh tính và truy cập
│       ├── storage/                # Triển khai lưu trữ file (S3, local)
│       ├── problem-management/     # Quản lý bài tập (CRUD)
│       ├── test-case/              # Quản lý test case
│       ├── code-runner/            # Dịch vụ thực thi code
│       └── result-processor/       # Xử lý kết quả bài nộp
└── docs/                           # Tài liệu dự án
```

## Core Modules

### **platform/core/**

### **platform/infrastructure/** - Tầng Hạ Tầng JPA

### **platform/components/** - Tầng Triển Khai Domain

#### **1. iam/** - Identity and Access Management

#### **2. storage/** 

#### **3. problem-management/** 

#### **4. test-case/** - Quản Lý Test Case

#### **5. code-runner/** 

#### **6. result-processor/**
### **platform/libs/**
#### **libs/env/** - Environment Configuration
#### **libs/file/** - File Handling Utilities


## Technology Stack

| Library | Version | Purpose |
|---------|---------|---------|
| Spring Boot | 3.5.7 | Web framework and auto-configuration |
| Spring Framework | 6.2.12 | Core application framework |
| Spring Data JPA | 3.5.7 | ORM and data access layer |
| Hibernate | Latest (via Spring) | JPA implementation |
| Flyway | 11.7.2 | Database migration |
| Lombok | 1.18.30 | Code generation via annotations |
| JUnit Jupiter | 5.12.2 | Unit testing framework |
| Mockito | 5.12.0 | Mocking framework for tests |
| Apache POI | 5.4.0 | Excel file parsing (XLSX) |
| Apache Commons Lang3 | 3.18.0 | Utility functions |
| Logback | 1.5.19 | Logging implementation |

## System Requirements

- **Java**: 21 or higher
- **Maven**: 3.8+ (Maven wrapper included)
- **Spring Boot**: 3.5.7
- **Database**: PostgreSQL / MySQL (configured via Flyway)
