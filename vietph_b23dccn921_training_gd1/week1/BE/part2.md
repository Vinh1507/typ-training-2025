# Phần 2: Lập trình Hướng đối tượng (OOP)

Phần này tập trung vào các khái niệm cốt lõi của Lập trình Hướng đối tượng (OOP) trong Java, cũng như các nguyên lý thiết kế nâng cao như Inversion of Control (IoC) và Dependency Injection (DI).

## 1. OOP trong Java

OOP là một mô hình lập trình tập trung vào việc tổ chức mã nguồn xung quanh các "đối tượng" (objects) thay vì các "hàm" (functions) và "logic".

### 1.1. Java Cơ bản

Đây là các khối xây dựng cơ bản của Java:

* **Lớp (Class):** Là một *bản thiết kế* hoặc *khuôn mẫu* (blueprint) để tạo ra các đối tượng. Lớp định nghĩa các thuộc tính (biến) và hành vi (phương thức) mà các đối tượng của nó sẽ có.
    * *Ví dụ:* `class Dog { ... }`
* **Đối tượng (Object):** Là một *thể hiện* (instance) cụ thể của một lớp. Nếu `Dog` là bản thiết kế, thì một con chó cụ thể tên "Buddy" là một đối tượng.
    * *Ví dụ:* `Dog buddy = new Dog();`
* **Biến (Variable):** Dùng để lưu trữ dữ liệu.
    * **Biến instance (Instance Variables):** Thuộc tính của đối tượng (ví dụ: `name`, `age` của `Dog`).
    * **Biến local (Local Variables):** Biến tạm thời bên trong một phương thức.
* **Hàm (Method):** Định nghĩa các *hành vi* hoặc *hành động* mà một đối tượng có thể thực hiện.
    * *Ví dụ:* `buddy.bark();`
* **Interface (Giao diện):** Một bản *hợp đồng* thuần túy. Nó định nghĩa một tập hợp các phương thức mà một lớp *phải* triển khai (implement) nếu nó "ký" vào hợp đồng đó. Nó chỉ nói "làm gì", không nói "làm như thế nào".
    * *Ví dụ:* `interface Animal { void makeSound(); }`
* **Abstract Class (Lớp trừu tượng):** Một lớp cha "chưa hoàn thiện". Nó có thể chứa cả các phương thức đã được triển khai (có thân hàm) và các phương thức trừu tượng (abstract methods - chưa có thân hàm). Lớp con kế thừa nó *bắt buộc* phải triển khai các phương thức trừu tượng đó. Nó là sự kết hợp giữa class và interface.
* **Vòng lặp (Loops):** Cấu trúc điều khiển để lặp lại một khối mã (ví dụ: `for`, `while`, `do-while`).
* **Thao tác File (File I/O):** Java cung cấp các thư viện (`java.io` và `java.nio`) để đọc và ghi dữ liệu từ/đến các tệp tin.

### 1.2. Bốn tính chất của OOP

Dựa trên các nguyên lý OOP chuẩn (như được đề cập trong tài liệu tham khảo của bạn), đây là 4 tính chất cốt lõi:

#### 1. Tính đóng gói (Encapsulation)
* **Khái niệm:** Là cơ chế *che giấu dữ liệu* (data hiding) và *bó buộc* (binding) dữ liệu (thuộc tính) cùng với các phương thức xử lý dữ liệu đó (hàm) vào bên trong một đối tượng duy nhất (lớp).
* **Mục đích:** Ngăn chặn truy cập trực tiếp từ bên ngoài vào các thuộc tính của đối tượng. Thay vào đó, việc truy cập phải thông qua các phương thức công khai (public methods) gọi là `getters` và `setters`.
* **Ví dụ:**
    ```java
    public class Person {
        // 'name' bị che giấu, không thể truy cập trực tiếp
        private String name; 

        // Cung cấp một phương thức công khai để lấy dữ liệu
        public String getName() {
            return this.name;
        }

        // Cung cấp một phương thức công khai để thay đổi dữ liệu (có thể thêm logic kiểm tra)
        public void setName(String name) {
            if (name != null && !name.isEmpty()) {
                this.name = name;
            }
        }
    }
    ```

#### 2. Tính kế thừa (Inheritance)
* **Khái niệm:** Cho phép một lớp (gọi là *lớp con* - Subclass) thừa hưởng lại các thuộc tính và phương thức của một lớp khác (gọi là *lớp cha* - Superclass).
* **Mục đích:** Tái sử dụng mã nguồn và xây dựng một hệ thống phân cấp logic (quan hệ "IS-A" - "là một").
* **Ví dụ:**
    ```java
    // Lớp cha
    public class Animal {
        public void eat() {
            System.out.println("This animal eats food.");
        }
    }

    // Lớp con 'Dog' KẾ THỪA 'Animal'
    // Dog "IS-A" (là một) Animal
    public class Dog extends Animal {
        // Dog tự động có phương thức eat()
        public void bark() {
            System.out.println("Woof!");
        }
    }
    
    // Sử dụng:
    Dog myDog = new Dog();
    myDog.eat();  // Gọi phương thức từ lớp cha
    myDog.bark(); // Gọi phương thức của chính nó
    ```

#### 3. Tính đa hình (Polymorphism)
* **Khái niệm:** "Poly" nghĩa là "nhiều", "morph" nghĩa là "hình thái". Đa hình là khả năng một đối tượng có thể mang *nhiều hình thái* khác nhau, hoặc một hành động có thể được thực hiện theo *nhiều cách* khác nhau.
* **Có 2 loại chính trong Java:**
    1.  **Nạp chồng (Overloading - Đa hình lúc biên dịch):** Các phương thức có *cùng tên* nhưng *khác nhau về tham số* (số lượng hoặc kiểu dữ liệu) trong cùng một lớp.
    2.  **Ghi đè (Overriding - Đa hình lúc chạy):** Lớp con cung cấp một *triển khai cụ thể* cho một phương thức đã được định nghĩa ở lớp cha.
* **Ví dụ (Ghi đè - Overriding):**
    ```java
    public class Animal {
        public void makeSound() {
            System.out.println("Animal makes a sound");
        }
    }

    public class Dog extends Animal {
        // GHI ĐÈ (Override) phương thức của lớp cha
        @Override
        public void makeSound() {
            System.out.println("Dog barks: Woof!");
        }
    }

    public class Cat extends Animal {
        // GHI ĐÈ (Override) phương thức của lớp cha
        @Override
        public void makeSound() {
            System.out.println("Cat meows: Meow!");
        }
    }

    // Tính đa hình thể hiện ở đây:
    Animal myAnimal1 = new Dog(); // Một Animal "hình thái" là Dog
    Animal myAnimal2 = new Cat(); // Một Animal "hình thái" là Cat

    myAnimal1.makeSound(); // Sẽ gọi hàm của Dog -> "Woof!"
    myAnimal2.makeSound(); // Sẽ gọi hàm của Cat -> "Meow!"
    ```

#### 4. Tính trừu tượng (Abstraction)
* **Khái niệm:** Là quá trình *che giấu sự phức tạp* của việc triển khai (implementation) và chỉ hiển thị các chức năng thiết yếu cho người dùng.
* **Mục đích:** Giúp đơn giản hóa hệ thống bằng cách tập trung vào "cái gì" (what) thay vì "như thế nào" (how).
* **Cách thực hiện:** Sử dụng **Abstract Class** và **Interface**.
* **Ví dụ (Interface):**
    ```java
    // Interface chỉ định "cái gì": một phương tiện PHẢI có khả năng khởi động
    public interface Vehicle {
        void start(); // Không có thân hàm, chỉ có chữ ký
        void stop();
    }

    // Lớp Car triển khai "như thế nào"
    public class Car implements Vehicle {
        @Override
        public void start() {
            // Che giấu sự phức tạp: vặn chìa khóa, đề nổ, kiểm tra xăng...
            System.out.println("Car engine started.");
        }
        
        @Override
        public void stop() {
            System.out.println("Car engine stopped.");
        }
    }

    // Người dùng chỉ cần biết:
    Vehicle myCar = new Car();
    myCar.start(); // Người dùng không cần biết chi tiết bên trong
    ```

---

## 2. Dependency Injection (DI) & Inversion of Control (IoC)

Đây là các nguyên lý thiết kế (Design Principles) quan trọng giúp xây dựng các hệ thống *linh hoạt*, *dễ bảo trì*, và *dễ kiểm thử* (testable).

### 2.1. Khái niệm

#### Inversion of Control (IoC - Đảo ngược Điều khiển)
* **Khái niệm:** Là một nguyên lý thiết kế. Theo truyền thống, một lớp tự chịu trách nhiệm *tạo ra* các đối tượng mà nó cần (các *phụ thuộc* - dependencies). Với IoC, quyền điều khiển này bị *đảo ngược*: lớp sẽ *không* tự tạo ra phụ thuộc của nó, mà thay vào đó, các phụ thuộc sẽ được *cung cấp* cho nó từ bên ngoài.
* **Nói đơn giản:** "Đừng gọi chúng tôi, chúng tôi sẽ gọi bạn."
* **Ví dụ truyền thống (KHÔNG IoC):**
    ```java
    public class Car {
        private Engine engine;
        public Car() {
            // Lớp Car TỰ TẠO ra phụ thuộc của nó (Engine)
            this.engine = new GasolineEngine(); 
        }
    }
    ```
    *Vấn đề:* Lớp `Car` bị *ràng buộc cứng* (tightly coupled) với `GasolineEngine`. Nếu muốn đổi sang `ElectricEngine`, ta phải *sửa lại* mã nguồn của lớp `Car`.

#### Dependency Injection (DI - Tiêm Phụ thuộc)
* **Khái niệm:** DI là một *kỹ thuật* (Pattern) để *thực hiện* nguyên lý IoC. Thay vì đối tượng tự tạo phụ thuộc, các phụ thuộc (Dependencies) sẽ được "tiêm" (Inject) vào đối tượng từ một nguồn bên ngoài (ví dụ: một IoC Container như Spring).
* **DI chính là CÁCH THỨC để đạt được IoC.**

### 2.2. Ví dụ (Implement DI bằng Java)

Hãy xem cách DI giải quyết vấn đề "ràng buộc cứng" ở trên.

**Bước 1: Định nghĩa Interface (Hợp đồng)**
Chúng ta định nghĩa một hợp đồng chung cho `Engine`.

```java
// Hợp đồng: Bất cứ thứ gì là Engine đều phải có thể start()
public interface Engine {
    void start();
}
````

**Bước 2: Tạo các lớp triển khai cụ thể**

```java
public class GasolineEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Started Gasoline Engine...");
    }
}

public class ElectricEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Started Electric Engine (silent)...");
    }
}
```

**Bước 3: Sử dụng DI trong lớp `Car` (Áp dụng IoC)**
Lớp `Car` bây giờ không tạo `Engine` nữa. Nó chỉ *yêu cầu* một `Engine` được "tiêm" vào.

**Cách 1: Tiêm qua Constructor (Constructor Injection - *Khuyến khích dùng*)**

```java
public class Car {
    private final Engine engine; // Phụ thuộc

    // Phụ thuộc được "tiêm" vào qua hàm khởi tạo
    public Car(Engine engine) {
        this.engine = engine; 
    }

    public void startCar() {
        this.engine.start();
    }
}
```

**Cách 2: Tiêm qua Setter (Setter Injection)**

```java
public class Car {
    private Engine engine; // Phụ thuộc

    // Cung cấp một setter để "tiêm"
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
    
    public void startCar() {
        if (engine != null) {
            this.engine.start();
        }
    }
}
```

-----

**Kết quả (Cách sử dụng):**

```java
public class Main {
    public static void main(String[] args) {
        // 1. Tạo ra phụ thuộc
        Engine gasEngine = new GasolineEngine();
        Engine electricEngine = new ElectricEngine();

        // 2. "Tiêm" phụ thuộc vào lớp Car
        Car car1 = new Car(gasEngine); // Tiêm động cơ xăng
        Car car2 = new Car(electricEngine); // Tiêm động cơ điện

        // Lớp Car hoạt động mà không cần biết nó đang dùng loại động cơ nào
        car1.startCar(); // Output: Started Gasoline Engine...
        car2.startCar(); // Output: Started Electric Engine (silent)...
    }
}
```

*Lợi ích:* Lớp `Car` giờ đây *độc lập* (decoupled) với các loại `Engine` cụ thể. Chúng ta có thể dễ dàng thay đổi, nâng cấp, hoặc thậm chí giả lập (mock) `Engine` để kiểm thử (test) lớp `Car` mà không cần sửa mã nguồn của `Car`.

### 2.3. Ứng dụng DI, IoC trong Spring Framework

Spring là một framework khổng lồ, nhưng cốt lõi của nó chính là **Spring IoC Container**.

  * **IoC Container (hoặc ApplicationContext):** Đây là một "cái hộp" ma thuật của Spring.
  * **Vai trò của Container:**
    1.  **Đọc cấu hình:** Nó đọc cấu hình của bạn (qua file XML, Annotation như `@Component`, `@Service`, `@Repository`...) để biết bạn có những lớp (Beans) nào.
    2.  **Khởi tạo Beans:** Nó tự động `new` các đối tượng (gọi là "Beans") này lên.
    3.  **Quản lý phụ thuộc (DI):** Khi nó thấy một `Bean` (ví dụ: `CarController`) cần một `Bean` khác (ví dụ: `CarService`), nó sẽ tự động "tiêm" (inject) `CarService` vào `CarController`.
  * **Ví dụ trong Spring:**

Bạn không cần tự tay `new` bất cứ thứ gì. Bạn chỉ cần "khai báo" các lớp và phụ thuộc của chúng bằng Annotation.

```java
// Đánh dấu đây là một Bean "Dịch vụ"
@Service
public class CarService {
    public String getCarDetails() {
        return "Đây là chi tiết xe";
    }
}

// Đánh dấu đây là một Bean "Controller"
@RestController
public class CarController {

    // Yêu cầu Spring "tiêm" một Bean CarService vào đây
    @Autowired
    private CarService carService; 

    /*
    // Spring sẽ tự động làm điều này (tương đương Constructor Injection)
    public CarController(CarService carService) {
        this.carService = carService;
    }
    */

    @GetMapping("/car")
    public String getCar() {
        // Bạn chỉ việc dùng, không cần khởi tạo carService
        return carService.getCarDetails();
    }
}
```

Khi ứng dụng Spring chạy, IoC Container sẽ:

1.  Tạo ra một đối tượng (Bean) `CarService`.
2.  Tạo ra một đối tượng (Bean) `CarController`.
3.  Thấy `CarController` cần `CarService` (do có `@Autowired`), nó sẽ lấy Bean `CarService` (đã tạo ở bước 1) và "tiêm" vào trường `carService` của `CarController`.