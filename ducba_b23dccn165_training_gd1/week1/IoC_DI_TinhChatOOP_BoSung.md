# 1) IoC (Inversion of Control)

- Định nghĩa: Đảo chiều luồng điều khiển; thay vì code của ta chủ động gọi framework, giờ framework gọi ta qua các hook/callback/lifecycle.

- Ưu điểm của kiến trúc này gồm:

  1. Tách rời việc thực thi một tác vụ khỏi phần implementation của nó.
  2. Giúp dễ dàng chuyển đổi giữa các implementations khác nhau.
  3. Tăng tính mô-đun (modularity) của chương trình.
  4. Dễ kiểm thử chương trình hơn bằng cách cô lập một thành phần hoặc giả lập (mock) các phụ thuộc của nó, và cho phép các thành phần giao tiếp thông qua các interface

**Inversion of Control có thể được hiện thực bằng nhiều cơ chế**. Phổ biến và được ưu tiên là **Dependency Injection**. Một số mẫu khác cũng thể hiện IoC ở các khía cạnh riêng: Factory/Abstract Factory (đảo chiều việc tạo đối tượng), Strategy/Template Method/Observer (đảo chiều hành vi thông qua callback). Service Locator cũng cung cấp IoC nhưng thường bị hạn chế do phụ thuộc bị ẩn và khó kiểm thử, nên kém ưu tiên hơn DI.

# 2) DI (Dependency Injection)

**Dependency injection (DI)** là một kỹ thuật lập trình giúp tách một class độc lập với các biến phụ thuộc. Với lập trình hướng đối tượng, chúng ta hầu như luôn phải làm việc với rất nhiều class trong một chương trình. Các class được liên kết với nhau theo một mối quan hệ nào đó. Dependency là một loại quan hệ giữa 2 class mà trong đó một class hoạt động độc lập và class còn lại phụ thuộc bởi class kia.

### Nhiệm vụ của Dependency Injection là:

- Tạo ra các object.
- Biết được class nào cần những object đấy.
- Cung cấp cho những class đó những object chúng cần.

### Dependency Injection có thể được thực hiện dựa trên các quy tắc sau:

- Các class sẽ không phụ thuộc trực tiếp lẫn nhau mà thay vào đó chúng sẽ liên kết với nhau thông qua một Interface hoặc base class (đối với một số ngôn ngữ không hỗ trợ Interface)
- Việc khởi tạo các class sẽ do các Interface quản lí thay vì class phụ thuộc nó
- Có 2 cách để implement dependency injection
  1. Dựa vào constructor
  2. Dựa vào Setter method

```java
class Car{
    private Wheels wheel;
    private Battery battery;
    // Cách 1 : Dựa vào constructor
    Car(Wheel wh, Battery bt) {
        this.wh = wh;
        this.bt = bt;
    }

    // Cách 2 : Dựa vào Setter method
    void setWheel(Batter bt){
        this.bt = bt;
    }
}
```

### Ưu điểm và hạn chế của DI :

Ưu điểm

    1. Giảm sự kết dính giữa các module
    2. Dễ test và dễ viết Unit Test
    3. Code dễ bảo trì, dễ thay thế module
    4. Dễ dàng thấy quan hệ giữa các module

Hạn chế

    1. Sử dụng interface nên đôi khi khó debug, do không biết chính xác module nào được gọi
    2. Các object được khởi tạo toàn bộ ngay từ đầu, có thể làm giảm performance
    3. Làm tăng độ phức tạp của code

### Ví dụ :

Cấu trúc thư mục :

```java
src/main/java/com/example/iocdi/DemoApplication.java
src/main/java/com/example/iocdi/GreetingService.java
src/main/java/com/example/iocdi/EnglishGreetingService.java
src/main/java/com/example/iocdi/GreetingController.java
```

// Trong DemoApplication.java :

```java
package com.example.iocdi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
```

// Trong GreetingService.java

```java
package com.example.iocdi;
public interface GreetingService {
  String greet(String name);
}
```

// Trong EnglishGreetingService.java

```java
package com.example.iocdi;
import org.springframework.stereotype.Service;
@Service
public class EnglishGreetingService implements GreetingService {
  @Override
  public String greet(String name) {
    return "Hello, " + name + "!";
  }
}
```

// Trong GreetingController.java :

```java
package com.example.iocdi;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class GreetingController {
  private final GreetingService greetingService;
  // DI qua constructor :
  public GreetingController(GreetingService greetingService) {
    this.greetingService = greetingService;
  }
  @GetMapping("/greet")
  public String greet(@RequestParam(defaultValue = "PTIT") String name) {
    return greetingService.greet(name);
  }
}
```

# 3) Tính chất của OOP (Bổ sung thêm ở phần OOP đang bị thiếu)

## a) Đóng gói

- Mục đích : Giấu dữ liệu bên trong đối tượng, chỉ cho truy cập qua phương thức công khai.
- Lợi ích: Bảo vệ trạng thái, giảm phụ thuộc, dễ bảo trì.
- Cách làm: private field + public getter/setter.

```java
class BankAccount {
    private double balance;
    public double getBalance() {
        return balance;
    }
    public void deposit(double amount){
        if(amount>0)
            balance += amount;
    }
}
```

## b) Trừu tượng

- Mục đích : Che giấu chi tiết thừa, chỉ lộ ra cái cần dùng.
- Lợi ích: Đơn giản hoá, tập trung vào hành vi cốt lõi.
- Cách làm: interface, abstract class.

```java
// Định nghĩa hành vi trừu tượng
abstract class Shape {
    abstract double area();
    abstract double perimeter();
}

// Lớp con kế thừa và cài đặt chi tiết lại area và perimeter
class Circle extends Shape {
    private double r;

    Circle(double r) {
        this.r = r;
    }

    @Override
    double area() {
        return Math.PI * r * r;
    }

    @Override
    double perimeter() {
        return 2 * Math.PI * r;
    }
}

class Rectangle extends Shape {
    private double w, h;

    Rectangle(double w, double h) {
        this.w = w;
        this.h = h;
    }

    @Override
    double area() {
        return w * h;
    }

    @Override
    double perimeter() {
        return 2 * (w + h);
    }
}

public class Main {
    public static void main(String[] args) {
        Shape c = new Circle(5);
        Shape r = new Rectangle(4, 6);
        System.out.println(c.area());
        System.out.println(r.perimeter());
    }
}
```

## c) Kế thừa

- Mục đích : Lớp con tái sử dụng và mở rộng lớp cha.
- Lợi ích: Tái sử dụng code, tạo phân cấp rõ ràng.
- Lưu ý: Ưu tiên composition hơn kế thừa nếu quan hệ không phải “là một”.

```java
class Animal {
    void speak(){
        System.out.println("abcdxyz");
    }
}
class Dog extends Animal {
    @Override
    void speak(){
        System.out.println("Gâu gâu");
    }
}
```

## d) Đa hình

- Mục đích : Cùng 1 lời gọi, hành vi khác nhau tuỳ đối tượng thực.
- Hình thức:
  1. Override (đa hình lúc chạy – runtime): lớp con thay đổi hành vi phương thức lớp cha.
  2. Overload (đa hình lúc biên dịch – compile-time): cùng tên phương thức, khác tham số.

```java
// Override
Animal a = new Dog();
a.speak();
// Overload
int sum(int a,int b){
    return a+b;
}
```
