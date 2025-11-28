

# 2. Dependency Injection(DI), Inversion of Control(Ioc)


## Inversion of Control(Ioc)
- Ý tưởng: Thay vì lập trình viên tự kiểm soát vòng đời của 1 class, chúng ta để việc đó cho các framwork (ví dụ Spring Boot của Java), khi đó lập trình viên được giải phóng và chỉ tập trung vào các logic nghiệp vụ
- IOC là 1 cách tiếp cận, 1 concept (thiết kế phần mềm, nguyên lý)
- Nguyên lý đảo ngược quyền kiểm soát việc tạo và quản lý các phụ thuộc trong chương trình.
- Thay vì class tự quản lý các phụ thuộc, một framework hoặc container sẽ đảm nhiệm việc này (ví dụ: Spring).
- Trong Spring FrameWork, để Spring biết đđến sự tồn tại của 1 class, ta đơn giản chỉ cần đánh dấu class đó với các anotation như @Component, @Repository, @Service, @Controller,....
Sau đó khi start application, spring sẽ quét (auto scan) các class này và cho vào 1 container spring (chung cho cả hệ thống), lúc này nếu cần dùng class đó chỗ này chúng ta chỉ việc lấy từ container này ra mà không cần khởi tạo (new) một đối tượng mới!

**Ví dụ**
```java
@Component
public class UserRepository {}

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```


## Dependency Injection(DI)

- Ở IOC, ta đã biết khi cần dùng 1 class nào đó ta không cần khởi tạo lại 1 đối tượng mới mà sẽ lấy nó ra từ trong container đã được spring quản lý, hành động này chính là DI.
- Hiểu đơn giản, DI chính là công cụ để chúng ta áp dụng IOC vào dự án.
- Kỹ thuật truyền các phụ thuộc vào class thay vì để class tự tạo ra chúng.
- Giúp giảm sự phụ thuộc giữa các module, dễ dàng kiểm thử và bảo trì.
- Có 3 cách DI:

### 1. Constructor Injection (Khuyến khích sử dụng)
- Inject dependency thông qua constructor
- Đảm bảo dependency được khởi tạo khi tạo object
- Immutable sau khi khởi tạo

**Ví dụ**
```java
@Service
public class UserService {
    private final UserRepository userRepository;

    // Inject qua constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### 2. Setter Injection
- Inject dependency thông qua setter method
- Cho phép thay đổi dependency sau khi object được tạo
- Có thể optional dependency

**Ví dụ**
```java
@Service
public class UserService {
    private UserRepository userRepository;

    // Inject qua setter
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### 3. Field Injection
- Inject trực tiếp vào field
- Đơn giản nhưng không khuyến khích vì khó test và debug
- Sử dụng annotation @Autowired

**Ví dụ**
```java
@Service
public class UserService {
    // Inject trực tiếp vào field
    @Autowired
    private UserRepository userRepository;
}
```

**Lưu ý:** Spring khuyến khích sử dụng Constructor Injection vì:
- Đảm bảo dependency bắt buộc
- Tạo immutable object
- Dễ dàng testing
- Phát hiện circular dependency sớm



# Quan hệ giữa IOC và DI là gì?
IOC là concept, DI là công cụ để thực hiện hóa IOC.