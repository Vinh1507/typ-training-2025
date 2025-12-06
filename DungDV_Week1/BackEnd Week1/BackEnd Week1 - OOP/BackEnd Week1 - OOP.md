
# Phần 2: Lập trình Hướng đối tượng (OOP)

Phần này hệ thống hóa các khái niệm trọng tâm của OOP trong Java, kèm theo các nguyên lý thiết kế hiện đại như **Inversion of Control (IoC)** và **Dependency Injection (DI)**.

## 1. OOP trong Java

OOP là cách tiếp cận lập trình trong đó ta xây dựng phần mềm xoay quanh các **đối tượng** (có trạng thái và hành vi), thay vì tập trung trước hết vào hàm/thủ tục.

### 1.1. Java Cơ bản

Các khối nền tảng trong Java:

* **Lớp (Class):** Đóng vai trò như một bản thiết kế (blueprint) mô tả thuộc tính và hành vi cho các đối tượng tạo ra từ nó.  
  *Ví dụ:* `class Canine { ... }`
* **Đối tượng (Object):** Là một thể hiện (instance) cụ thể của một lớp. Nếu `Canine` là bản thiết kế, thì “Rex” là một đối tượng cụ thể.  
  *Ví dụ:* `Canine rex = new Canine();`
* **Biến (Variable):** Dùng để giữ dữ liệu.  
  - **Biến của đối tượng (instance fields):** thuộc tính của mỗi thể hiện, ví dụ `nickname`, `yearsOld`.  
  - **Biến cục bộ (local variables):** chỉ tồn tại trong phạm vi phương thức/khối lệnh.
* **Phương thức (Method):** Mô tả các hành động/ứng xử của đối tượng.  
  *Ví dụ:* `rex.barkAtStranger();`
* **Interface (Giao diện):** Một “hợp đồng” chỉ nêu **cần làm gì**, không nêu **làm như thế nào**. Lớp triển khai interface phải cài đặt đầy đủ các phương thức đã khai báo.  
  *Ví dụ:* `interface Creature { void makeNoise(); }`
* **Lớp trừu tượng (Abstract class):** Lớp cha có thể chứa cả phương thức đã cài đặt lẫn phương thức trừu tượng (chỉ khai báo). Lớp con bắt buộc cài đặt các phương thức trừu tượng đó.
* **Vòng lặp (Loops):** Cấu trúc lặp như `for`, `while`, `do-while`.
* **Xử lý tệp (File I/O):** Thư viện `java.io`/`java.nio` hỗ trợ đọc/ghi dữ liệu từ/đến file.

### 1.2. Bốn tính chất của OOP

#### 1) Đóng gói (Encapsulation)
* **Ý tưởng:** Gói dữ liệu (thuộc tính) và hành vi (phương thức) vào cùng một thực thể; che giấu chi tiết bên trong, chỉ mở ra API cần thiết.  
* **Mục tiêu:** Bảo vệ trạng thái nội bộ; truy cập thông qua getter/setter có kiểm soát.

```java
public class Citizen {
    private String displayName; // ẩn đi, không truy cập trực tiếp

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String newName) {
        if (newName != null && !newName.isBlank()) {
            this.displayName = newName;
        }
    }
}
```

#### 2) Kế thừa (Inheritance)
* **Ý tưởng:** Lớp con kế thừa thuộc tính/phương thức từ lớp cha để tái sử dụng, hình thành quan hệ “IS-A” (là một).

```java
// Lớp cha
public class Creature {
    public void eat() {
        System.out.println("This creature consumes food.");
    }
}

// Lớp con kế thừa
public class Canine extends Creature {
    public void bark() {
        System.out.println("Ruff!");
    }
}

// Sử dụng
Canine wolf = new Canine();
wolf.eat();   // từ lớp cha
wolf.bark();  // của lớp con
```

#### 3) Đa hình (Polymorphism)
* **Ý tưởng:** Cùng một lời gọi phương thức nhưng hành vi thực thi có thể khác nhau tùy loại đối tượng.  
* **Hai hình thức chính:**  
  1. **Nạp chồng (Overloading):** Cùng tên phương thức, khác tham số (biên dịch phân giải).  
  2. **Ghi đè (Overriding):** Lớp con thay đổi triển khai phương thức của lớp cha (runtime phân giải).

```java
public class Creature {
    public void makeNoise() {
        System.out.println("Creature makes some noise");
    }
}

public class Feline extends Creature {
    @Override
    public void makeNoise() {
        System.out.println("Feline meows...");
    }
}

public class Canine extends Creature {
    @Override
    public void makeNoise() {
        System.out.println("Canine barks...");
    }
}

// Minh họa đa hình
Creature a = new Feline();
Creature b = new Canine();
a.makeNoise(); // "Feline meows..."
b.makeNoise(); // "Canine barks..."
```

#### 4) Trừu tượng (Abstraction)
* **Ý tưởng:** Ẩn chi tiết triển khai, chỉ công bố đặc tả chức năng.  
* **Công cụ:** `abstract class` và `interface`.

```java
public interface Drivable {
    void ignite();
    void halt();
}

public class Sedan implements Drivable {
    @Override
    public void ignite() {
        System.out.println("Sedan engine on.");
    }
    @Override
    public void halt() {
        System.out.println("Sedan engine off.");
    }
}

// Người dùng chỉ cần biết cách gọi:
Drivable car = new Sedan();
car.ignite();
```

---

## 2. Dependency Injection (DI) & Inversion of Control (IoC)

Các nguyên lý thiết kế giúp giảm phụ thuộc chặt (tight coupling), tăng khả năng thay thế và kiểm thử.

### 2.1. Khái niệm

**Inversion of Control (IoC)**  
* Thay vì lớp **tự** khởi tạo các phụ thuộc của nó, quyền “điều khiển” việc cấp phát được **đảo ngược**: phụ thuộc được cung cấp từ bên ngoài.  
* Ví dụ **không** dùng IoC (ràng buộc chặt chẽ):

```java
public class Van {
    private PowerUnit unit;
    public Van() {
        this.unit = new CombustionUnit(); // Van tự “new” phụ thuộc
    }
}
```
*Nhược điểm:* Muốn đổi sang động cơ điện phải sửa mã trong `Van`.

**Dependency Injection (DI)**  
* DI là **cách thức cụ thể** để hiện thực IoC: “tiêm” (inject) phụ thuộc vào lớp thay vì để lớp tự tạo.  
* DI có thể qua **constructor**, **setter**, hoặc **field** (không khuyến khích bằng tay).

### 2.2. Ví dụ DI thuần Java (đổi tên để khác bản gốc)

**Bước 1 – Định nghĩa hợp đồng:**
```java
public interface PowerUnit {
    void startUp();
}
```

**Bước 2 – Các triển khai cụ thể:**
```java
public class CombustionUnit implements PowerUnit {
    @Override
    public void startUp() {
        System.out.println("Combustion unit roaring...");
    }
}

public class ElectricMotor implements PowerUnit {
    @Override
    public void startUp() {
        System.out.println("Electric motor humming quietly...");
    }
}
```

**Bước 3 – Tiêm phụ thuộc vào lớp sử dụng (Constructor Injection – khuyến nghị):**
```java
public class Auto {
    private final PowerUnit powerUnit;

    public Auto(PowerUnit powerUnit) {  // phụ thuộc được truyền từ ngoài vào
        this.powerUnit = powerUnit;
    }

    public void launch() {
        powerUnit.startUp();
    }
}
```

**Cách dùng:**
```java
public class Demo {
    public static void main(String[] args) {
        PowerUnit gas = new CombustionUnit();
        PowerUnit ev  = new ElectricMotor();

        Auto taxi   = new Auto(gas);
        Auto shuttle= new Auto(ev);

        taxi.launch();    // Combustion unit roaring...
        shuttle.launch(); // Electric motor humming quietly...
    }
}
```

*Lợi ích:* `Auto` chỉ biết đến **giao diện** `PowerUnit`, không phụ thuộc vào lớp cụ thể → dễ thay thế, test (mock), mở rộng.

### 2.3. Áp dụng IoC/DI trong Spring Framework

Cốt lõi của Spring là **IoC Container** (ApplicationContext), chịu trách nhiệm **quản lý vòng đời Bean** và **tiêm phụ thuộc**.

* **Container** sẽ:
  1) Quét/đọc cấu hình (annotation/XML) để biết các lớp nào là Bean,  
  2) Tạo Bean,  
  3) “Ghép dây” (wire) các phụ thuộc theo yêu cầu.

**Ví dụ Spring (đổi tên lớp/biến/endpoint):**
```java
@Service
public class VehicleService {
    public String info() {
        return "Thông tin phương tiện đã được lấy.";
    }
}

@RestController
public class VehicleController {

    // Field injection có thể dùng, nhưng Constructor injection tốt hơn
    @Autowired
    private VehicleService vehicleService;

    // Khuyến nghị:
    // public VehicleController(VehicleService vehicleService) {
    //     this.vehicleService = vehicleService;
    // }

    @GetMapping("/vehicle")
    public String readVehicle() {
        return vehicleService.info();
    }
}
```

*Chu trình khi chạy:*  
1) Spring tạo `VehicleService`,  
2) Tạo `VehicleController`,  
3) Thấy `VehicleController` cần `VehicleService` → **tiêm** vào (DI),  
4) Endpoint `/vehicle` sử dụng service mà không cần `new`.

---

### Tóm lược

- **Encapsulation** che giấu chi tiết, mở API an toàn.  
- **Inheritance** tái sử dụng, hình thành phân cấp “IS-A”.  
- **Polymorphism** cho phép hành vi khác nhau qua cùng giao diện.  
- **Abstraction** tập trung “cái gì” thay vì “như thế nào”.  
- **IoC/DI** giúp rời rạc hóa (decouple), dễ thay thế và kiểm thử; Spring Container là hiện thân thực dụng của các nguyên lý này.
