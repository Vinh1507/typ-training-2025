# OOP
## OOP trong Java
1. Java cơ bản
(object, class, abstract class, interface, biến, hàm, I/O, vòng lặp, thao tác file)

    a. Class
    - Là 1 khuôn để tạo ra các đối tượng. Nó định nghĩa các thuộc tính và các hành vi chung của tất cả các đối tượng được tạo ra từ lớp đó.
    ```
    public class ClassName {
        String ten;
        int tuoi;   //thuộc tính

        //Hành vi
        public void chao() {
            System.out.println("Xin chào");
        }
    }
    ```

    b. Object
    - Object là 1 thể hiện cụ thể của lớp. Nó đại diện 1 thực thể trong thế giới thực và chứa đựng những giá trị cụ thể cho các thuộc tính được định nghĩa trong Class.

    c. Abstract class (Lớp trừu tượng)
    - Là 1 lớp được khai báo với từ khóa `abstract`
    - Nó không được khởi tạo trực tiếp (nghĩa là không thể tạo đối tượng bằng `new`) mà thường được dùng làm lớp cơ sở cho các lớp khác kế thừa.
    - Đặc điểm:
        - Có thể chứa hàm trừu tượng (chỉ khai báo, không có thân hàm) và các hàm đã triển khai (có thân hàm)
        - Nếu 1 lớp chứa ít nhất 1 hàm trừu tượng, nó phải được khai báo là `abstract class`.
        - Một lớp chỉ được kế thừa duy nhất 1 `abstract class`.

    d. Interface (Giao diện)
    - Interface là 1 tập hợp các phương thức và các hằng số.
    - Nó định nghĩa 1 "hợp đồng" về các hành vi mà 1 lớp **phải** thực hiện. (nếu nó triển khai `implement` Interface đó)
    - Đặc điểm:
        - Các hàm/method trong Interface mặc định là `public`
        - Chỉ có các hằng số, nghĩa là với các attributor, phải là `public static final`
        - Một lớp có thể triển khai nhiều Interface.

    e. Biến (Variable) 
    - Biến là tên được gán cho 1 vùng nhớ để lưu trữ dữ liệu.
    - Phạm vi:
        - Biến cục bộ (local variable): khai báo bên trong 1 hàm và chỉ tồn tại trong phạm vi hàm đó.
        - Biến thể hiện (Instance variable): khai báo bên trong 1 class, nhưng khai báo bên ngoài bất kỳ hàm nào trong class đó. Mỗi đối tượng có bản sao riêng.
        - Biến lớp/Tĩnh (Class/Static variable): được khai báo với từ khóa `static`. Chỉ có 1 bản sao duy nhất được chia sẻ bởi các đối tượng của Class.
        - Ví dụ:
        ```
        public class SinhVien {
            public String ten;
            public static int tongSoSV = 0;

            public SinhVien(String ten) {
                this.ten = ten;
            }

            public void show() {
                System.out.println("tongSoSV: " + tongSoSV);
            }
        }

        public static void main(String[] args) {
            SinhVien a = new SinhVien("Huy");
            SinhVien b = new SinhVien("Lam");

            a.show();       //tongSoSV: 0
            a.tongSoSV = 15;
            b.show();       //tongSoSV: 15
        }
        ```
    
    f. Hàm
    - Hàm là 1 khối mã thực hiện 1 tác vụ cụ thể. Nó là nơi định nghĩa hành vi của Class.
    - Cấu trúc:
    ```
    <modifier> <returnType> <methodName>(<parameterList>) {
        // Thân hàm
        // lệnh return (nếu returnType khác void)
    }

    //Ví dụ:
    public int Sum(int a, int b) {
        return a + b;
    }
    ```

    g. Vòng lặp.
    - Vòng lặp được sử dụng để thực thi 1 khối lệnh nhiều lần cho đến khi 1 điều kiện nhất định không còn đúng.   

    | Loại vòng lặp | Mục đích | Cú pháp |
    | :--- | :--- | :--- |
    | **for** | Lặp lại 1 số lần xác định | `for (khởi tạo; điều kiện; bước nhảy) {};` |
    | **while** | Lặp lại khi điều kiện còn đúng. | `while (điều kiện) { ... }` |
    | **do-while** | Lặp lại ít nhất một lần, sau đó kiểm tra điều kiện. | `do { ... } while (điều kiện);` |

    h. I/O cơ bản
    - Java cung cấp gói `java.io` để xử lý I/O.
    - Đầu vào (input): thường sử dụng lớp `Scanner` trong `java.util` để đọc dữ liệu từ bản phím (chuẩn `System.in`): `Scanner sc = new Scanner(System.in);`
    - Đầu ra (output): sử dụng `System.out.print()` hoặc `System.out.println()` để in ra console.

    i. Thao tác với File
    - Là việc đọc và ghi vào file. Java sử dụng các luồng (Stream) để xử lý I/O với file
    - Các lớp thường dùng: 
        - `File` (trong `java.io`): đại diện cho 1 file trong hệ thống. Dùng để kiểm tra tồn tại, tạo/xóa/đổi tên file/folder.
        - `FileWriter`/`FileReader`: dùng để ghi và đọc dữ liệu ký tự.  
        (Ví dụ: `FileWriter writer = new FileWriter("du_lieu.txt");`)
        - `FileOutputStream`/`FileInputStream`: dùng để ghi và đọc dữ liệu ký tự byte.
        - `BufferedReader`/`BufferredWriter`: cung cấp bộ đệm (buffering) để tăng hiệu suất đọc/ghi, thường bọc ngoài `FileReader`/`FileWriter`.
    - Lưu ý: những thao tác với file rất dễ tiềm ẩn lỗi: file không tồn tại, không có quyền ghi, do đó cần phải xử lý ngoại lệ.

2. 4 tính chất của OOP  
4 tính chất của OOP lần lượt là: *Trừu tượng(Abstraction) - Đóng gói (Encapsulation) - Kế thừa (Inheritance) - Đa hình (Polymorphism)*  

    a. Đóng gói (Encapsulation)
    - Đóng gói là cơ chế "gói lại", "đóng gói" các thuộc tính (dữ liệu) và các phương thức (hành vi) vào 1 Lớp duy nhất, đồng thời kiểm soát quyền truy cập từ bên ngoài.
    - Mục đích: bảo vệ dữ liệu khỏi sự truy cập và thay đổi từ bên ngoài, tạo tính nhất quán và bảo mật cho đối tượng?
    - Mức độ truy cập:  
        - `private`: chỉ có thể được truy cập từ bên trong lớp khai báo. (Các hàm bên trong lớp có thể gọi được)
        - `protected`: có thể truy cập từ bên trong lớp khai báo + các lớp kế thừa
        - `public`: có thể được truy cập từ bất cứ đâu trong chương trình. (chỉ nên dùng cho các thành phần được sử dụng rộng rãi như: API hoặc các phương thức chính)
    - Để truy cập được các thuộc tính được bảo vệ, ta sử dụng các phương thức Getter(để lấy giá trị) và Setter (để thiết lập giá trị).

    ? Câu hỏi đặt ra?  
    - 2 đoạn code dưới đây khác gì nhau?
    ```
    //Đoạn code 1:
    private int age;
    ...
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    //Đoạn code 2:
    public int age;
    ```
    Tuy đều có thể truy cập và thay đổi được biến `age`, nhưng cách truy cập và mức độ kiểm soát khác nhau!  
        - Cách 1: Truy cập gián tiếp thông qua Getter và Setter. -> Ở đây, bạn có thể kiểm soát logic tốt hơn (thêm logic không cho giá trị âm trong setAge()). -> rất dễ bảo trì và mở rộng khi cần thay đổi cách lưu trữ hoặc xử lý age.  
        - Cách 2: truy cập trực tiếp từ ngoài lớp. -> Bất cứ ai cũng có thể thay đổi age. -> Nếu sau này cần thay đổi logic, bạn cần phải sửa cách truy cập age ở nhiều nơi

    b. Trừu tượng (Abstraction)  
    - Trừu tượng là quá trình ẩn đi các chi tiết phức tạp bên trong và chỉ hiển thị những thông tin cần thiết ra bên ngoài cho người dùng.  
    -> Bạn biết "nó làm cái gì"? Bạn không cần biết "nó làm như thế nào"?
    - Trong Java, bạn có thể sử dụng Interface và Abstract Class để định nghĩa các chức năng (method/phương thức) mà không cần cung cấp mã cụ thể:
    - Ví dụ:
    ```
    abstract class Animal {
        //Phương thức abstract không có thân. Không cần biết nó thực hiện như nào, các lớp con kế thừa sẽ định nghĩa nó được thực hiện như nào!
        public abstract void makeSound();
    }

    class Dog extends Animal {
        //Lớp con ghi đè lên phương thức makeSound().
        @Override
        public void makeSound() {
            System.out.println("Woof!");
        }
    }
    ```
    
    c. Kế thừa (Inheritance)
    - Kế thừa là cơ chế cho phép 1 lớp con tái sử dụng thuộc tính và phương thức của lớp cha.
    - Mục đích: tái sử dụng mã nguồn, giảm sự trùng lặp code, thiết lập một mối quan hệ "is-a" giữa các lớp, và dễ bảo trì
    - Trong Java, các lớp con tự động có tất cả các thuộc tính và phương thức mà không phải `private` của lớp cha.
    - Lớp con có thể thêm thuộc tính mới, hoặc ghi đè lên các phương thức của lớp cha để thay đổi hành vi.

    d. Đa hình (polymorphism)
    - Đa hình: (là nhiều hình thái), nghĩa là khả năng 1 hành động (1 method/phương thức) được thực hiện theo nhiều cách khác nhau.
    - Đa hình thể hiện qua 2 hình thức:
        - Đa hình lúc biên dịch (Nạp chồng overloading):  
        Tạo ra nhiều phương thức cùng tên trong 1 lớp, nhưng có danh sách tham số khác nhau(số lượng, kiểu dữ liệu)
        - Đa hình lúc chạy (ghi đè overriding):  
        Lớp con định nghĩa lại (Ghi đè) 1 phương thức đã có sẵn trong lớp cha.  
     
    ? Câu hỏi đặt ra: Vậy trừu tượng và đa hình khá giống nhau? Chúng đều có thể ghi đè lại phương thức? Vậy chúng khác gì nhau?
    - Về mặt kỹ thuật, ban đầu có thể thấy chúng giống nhau. Đều "thực hiện 1 hành động theo nhiều cách khác nhau".
    - Nhưng đa hình và trừu tượng khác nhau từ bản chất, mục đích thiết kế.
        - Trừu tượng: là để ẩn đi những gì phức tạp!  
        1 abstract class sẽ như 1 bộ khung có sẵn, các lớp con kế thừa sẽ có những method/hành vi này.  
        1 lớp con chỉ có thể kế thừa 1 abstract class. Vậy nên, nó sẽ tập trung vào "WHAT". Lớp này có thể làm được gì?
        - Đa hình: thực thi các hành vi khác nhau, tập trung vào cách thức "HOW".  
        1 lớp có thể kế thừa được nhiều interface - là giao diện hành vi. Lớp con được quyền quyết định cách thực hiện như thế nào -> Đa hình.


----
## Dependency Injection(DI), Inversion of Control(Ioc)
###  Khái niệm, ví dụ? (implement DI/IoC có thể bằng Java, C#,...)
1. Dependency Injection (DI)
- Inversion of Control (IoC), hay Đảo Ngược Điều Khiển, là một nguyên lý thiết kế trong lập trình.
    - Điều khiển truyền thống: Trong lập trình thủ tục hoặc hướng đối tượng truyền thống, luồng điều khiển của chương trình được quyết định bởi người lập trình. Khi đối tượng A cần đối tượng B, A sẽ tự mình tạo ra B (ví dụ: B b = new B();).
    - Điều khiển bị đảo ngược (IoC): Thay vì để đối tượng A tự tạo và quản lý đối tượng B, một thành phần bên ngoài (thường gọi là IoC Container) sẽ đảm nhiệm việc tạo, quản lý và cung cấp đối tượng B cho A.
- Ví dụ:
```
class Car {
    // Car chỉ khai báo nhu cầu (dependency), nhưng không tự tạo
    private Engine engine; 
    
    // Constructor chờ Engine được đưa vào từ bên ngoài
    public Car(Engine engine) {
        this.engine = engine; // Engine được "đưa vào" (Injected)
    }
}
// Thành phần bên ngoài (Container) chịu trách nhiệm tạo và liên kết:
// Engine engine = new GasolineEngine();
// Car car = new Car(engine);
```

2. Dependency Injection (DI) - Tiêm phụ thuộc
- Dependency Injection (DI), hay Tiêm Phụ Thuộc, là một hình thức cụ thể để thực hiện nguyên lý IoC. Nó là cơ chế mà IoC Container sử dụng để cung cấp (tiêm) các dependency (đối tượng phụ thuộc) cho một đối tượng.
- Dependency (phụ thuộc): là đối tượng mà 1 đối tượng khác cần để thực hiện công việc của mình.
- Injection (tiêm): là hành động truyền vào 1 đối tượng dependency mà nó cần.
- Có 3 hình thức tiêm phụ thuộc:
    - Constructor Injection
    ```
    class ServiceA {
        private final ServiceB b; // Khuyến nghị là 'final'

        // Tiêm qua Constructor
        public ServiceA(ServiceB b) {
            this.b = b;
        }
    }
    ```
    - Setter Injection
    ```
    class ServiceA {
        private ServiceB b;

        // Tiêm qua Setter
        public void setServiceB(ServiceB b) {
            this.b = b;
        }
    }
    ```
    - Field/Property Injection (tiêm qua thuộc tính)
        - Dependency được gán trực tiếp vào thuộc tính của lớp qua Annotation
        - Ưu điểm: mã code sạch, gọn gàng
        - Khuyết điểm: khó kiểm thử độc lập và tạo ra sự phụ thuộc ngầm (không rõ ràng)
    ```
    class ServiceA {
        @Autowired // Spring tự động tìm và gán đối tượng ServiceB
        private ServiceB b; 
    }
    ```
### Tìm hiểu thêm: Ứng dụng DI, IoC trong Spring
- Trong Spring, IoC và DI là hai khái niệm nền tảng, được quản lý bởi IoC Container.
1. IoC Container (Thùng chứa Đảo ngược Điều khiển)
- Vai trò: Là trái tim của Spring Framework, chịu trách nhiệm tạo, cấu hình, quản lý và kết nối các Beans (đối tượng được quản lý bởi Spring).
- Hai loại Container chính: BeanFactory (đơn giản) và ApplicationContext (nâng cao hơn).
2. Beans:
- Beans: Là các đối tượng được khởi tạo, lắp ráp và quản lý bởi Spring IoC Container.
- Quản lý vòng đời: Container quản lý toàn bộ vòng đời của Bean: khởi tạo $\rightarrow$ thiết lập phụ thuộc $\rightarrow$ sử dụng $\rightarrow$ hủy bỏ.
3. Implement DI trong Spring
- Spring sử dụng Annotation để thực hiện DI một cách tự động (Autoconfiguration).
- Các annotation:
    - @Component: Chỉ định một lớp là Bean chung của Spring.
    - @Service: Dùng cho các lớp logic nghiệp vụ (Business Logic).
    - @Repository: Dùng cho các lớp truy cập dữ liệu (Data Access).
    - @Controller: Dùng cho các lớp xử lý Web Request.
- Tiêm phụ thuộc: Sử dụng `@Autowired` để yêu cầu Spring Container tiêm một Bean vào.
```
@Service
public class EmailService {
    public void send(String msg) {
        System.out.println("Email sent: " + msg);
    }
}

@Service
public class UserService {
    // 2. Khai báo nhu cầu (dependency)
    private final EmailService emailService;

    // 3. Tiêm qua Constructor (Khuyến nghị trong Spring Boot)
    // Spring tự động tìm Bean EmailService và truyền vào
    @Autowired
    public UserService(EmailService emailService) { 
        this.emailService = emailService;
    }
    
    public void registerUser(String username) {
        // Sử dụng dependency
        emailService.send("Welcome " + username);
    }
}
```
