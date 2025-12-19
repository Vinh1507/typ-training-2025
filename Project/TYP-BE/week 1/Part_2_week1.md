# Phần 2: OOP

## a. OOP trong Java

### 1. Object
- Trong thực tế: một thực thể tồn tại ở thế giới thực
- Trong lập trình: là các thực thể trong hệ thống
- Được xác định bởi các yếu tố: định danh, trạng thái, hoạt động

### 2. Class
- Khái niệm: khái niệm trừu tượng dùng để biểu diễn một tập hợp các đối tượng trong hệ thống
- Đối tượng là 1 instance của class
- Ví dụ: 1 chiếc ô tô thì là đối tượng, ô tô thì là class nói chung
- Các thành phần:
    - Định danh: Tên lớp
    - Các trạng thái: attributes
    - Các hoạt động: methods
- Class mà không có attributes và method thì gọi là abstract class

### 3. Interface
- Khái niệm: quy định những methods mà class phải có nhưng không triển khai
- Interface định nghĩa class làm gì, class quyết định làm như thế nào

### 4. Thao tác FILE

#### 4.1. File
- Cho phép thao tác với các file được lưu trên phần cứng máy tính (on disk)
- Nằm trong package `java.io`

```java
import java.io.File;
...
File file = new File("file_name.txt");
// file nằm trong folder hiện tại (directory của project)
```

#### 4.2. Đọc/Ghi theo luồng

##### 4.2.1. Luồng kí tự: FileReader, BufferedReader, FileWriter, BufferedWriter
- Ví dụ:

```java
FileReader fr = new FileReader("input.in");
int c;
while ((c = fr.read()) != -1)
    System.out.print((char) c);
```

```java
BufferedReader br = new BufferedReader(new FileReader("input.in"));
String line;
while ((line = br.readLine()) != null)
    System.out.println(line);
```

##### 4.2.2. Luồng nhị phân: FileInputStream, FileOutputStream
- Ví dụ:

```java
FileInputStream fis = new FileInputStream("input.jpg");
FileOutputStream fos = new FileOutputStream("output.jpg");
int b;
while ((b = fis.read()) != -1)
    fos.write(b);
fis.close();
fos.close();
```

### 4.3. Đọc/Ghi Object
- Sử dụng `ObjectOutputStream` và `ObjectInputStream`
- Object phải `implements Serializable`

```java
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ObjectFile.dat"));
oos.writeObject(person);
oos.close();

ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ObjectFile.dat"));
Person p = (Person) ois.readObject();
ois.close();
```

## b. Dependency Injection (DI)

### Khái niệm
- Dependency Injection (DI) là kỹ thuật lập trình mà đối tượng (object) không tự tạo ra các phụ thuộc (dependency) của nó, mà các phụ thuộc đó được “tiêm vào” (injected) từ bên ngoài.
- “Thay vì tự new trong class, thì có người khác new giúp và đưa sẵn cho mình dùng.”

#### Vấn đề trước khi có DI

```java
class Engine {
    void start() {
        System.out.println("Engine started!");
    }
}

class Car {
    private Engine engine = new Engine(); // <-- phụ thuộc cứng

    void start() {
        engine.start();
    }
}
```

#### Giải pháp: DI

```java
class Engine {
    void start() {
        System.out.println("Động cơ xăng khởi động!");
    }
}

class ElectricEngine extends Engine {
    @Override
    void start() {
        System.out.println("Động cơ điện khởi động!");
    }
}

class Car {
    private Engine engine;

    // Constructor Injection
    public Car(Engine engine) {
        this.engine = engine;
    }

    void start() {
        engine.start();
    }
}

public class Main {
    public static void main(String[] args) {
        Engine engine = new ElectricEngine(); // inject dependency
        Car car = new Car(engine);
        // Car không phụ thuộc cứng vào engine nữa
        car.start();
    }
}
```

## c. Inversion of Control (IoC)

### Khái niệm
- Bình thường code của bạn gọi thư viện, còn trong IoC, thư viện (hoặc framework) sẽ gọi code của bạn.
- Quyền điều khiển bị đảo ngược — nên gọi là Inversion of Control.

#### Khi chưa có IoC

```java
class Database {
    void connect() {
        System.out.println("Kết nối tới Database!");
    }
}

class App {
    public static void main(String[] args) {
        Database db = new Database();  // Tự new
        db.connect();                  // Tự gọi
        System.out.println("Ứng dụng chạy!");
    }
}
```

#### Khi có IoC (Spring)

```java
@Component
class Database {
    void connect() {
        System.out.println("Database đã được container kết nối!");
    }
}

@Component
class AppRunner implements CommandLineRunner {
    private final Database db;

    @Autowired
    public AppRunner(Database db) { // Spring bơm (inject)
        this.db = db;
    }

    @Override
    public void run(String... args) {
        db.connect(); // Spring gọi run() -> bạn chỉ viết logic
        System.out.println("Ứng dụng chạy!");
    }
}

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
    // Mình tự custom logic và để Spring quản lý và gọi
    // -> IoC
}
```