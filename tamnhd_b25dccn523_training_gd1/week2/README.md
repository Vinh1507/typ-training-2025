# [Nguyen Hoang Duc Tam - B25DCCN523] Training Basic - Week 2

## Phần 1: Java
### 1. Cài đặt JDK và Netbeans
### 2. Làm quen với kiểu dữ liệu và cú pháp
#### a. Input, output, khai báo cơ bản
```Java
// Cách comment tương tự với C++ 
Scanner sc = new Scanner(System.in);
int a = sc.nextInt();
System.out.print("Ham print co kha nang noi string " + a + "\n");
System.out.println("TYP");
System.out.printf("%s", "printf dung dac ta nhu C");
```
#### b. Kiểu dữ liệu

- Cách khai báo biến tương tự như C++
- Ở đây các kiểu dữ liệu gần tương tự với C++ nên em sẽ không đi quá sâu vào nữa (int, long long - long, int, float, double, bool - boolean, String).
- Ép kiểu long ta dùng dùng 1L, dùng (long), (int), (float), ...
- Java sẽ không tự động ép từ kiểu dữ liệu có độ chính xác lớn hơn -> kiểu dữ liệu có độ chính xác bé hơn.
```Java
int t = 1.0; // Error
int t1 = (int) 1.0 // Working
```
- Với const ta define như sau:
```Java
final int MOD = (int)1e9 + 7;
```
#### c. Câu lệnh điều kiện - toán tử logic
- Java không cho phép sử dụng 0 - 1 thay cho boolean, bắt buộc dùng ```false``` và ```true```
- ```if```, ```else```, ```else if```, toán tử 3 ngôi, ```switch case``` hoàn toàn tương tự với C++.
- Một vài hàm toán thường dùng như ```max```, ```min```, ```sqrt```, ```pow```, ```abs```, ```round```, ```ceil```, ```floor```.
```Java
System.out.print(Math.sqrt(2));
```
#### d. Mảng 1 chiều, 2 chiều
- Java sử dụng 0-index array
- Cách khai báo mảng 1 chiều trong Java
```Java
int[] prime = {2, 3, 5, 7};

Scanner sc = new Scanner(System.in);

int[] arr = new int[4]; 
for(int i = 0; i < 3; ++i)
    arr[i] = sc.nextInt();
```

#### e. Vòng lặp
- Hoàn toàn tương tự với C++, ```for```, ```while```, ```do-while```, ```break```, ```continue```, ...

#### f. Hàm

- Nếu chưa đi sâu vào OOP thì ta có thể dùng hàm 1 cách đơn giản như sau, trong cùng 1 class
```Java
public class testJava 
{
    public static boolean isPrime(int a)
    {
        for(int i = 2; i * i <= a; ++i)
            if(a % i == 0)
                return false;
        return a > 1;
    }
    
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        System.out.println(isPrime(a) ? "YES" : "NO");
    }
}
```
D25 chưa học OOP nên trên codePTIT chưa cấp bài ạ.

## Phần 2: Database (Thiết kế lược đồ E-R)
### 1. Các khái niệm
#### a. Entity
- Entity (thực thể) là các đối tượng được miêu tả qua data, ví dụ *Nhân viên*, *Khách hàng*, *Công ty*, được miêu tả trong ERD bằng hình chữ nhật.
- Strong entity (thực thể mạnh) là loại thực thể có 1 thuộc tính key, thực thể mạnh tồn tại riêng biệt.
- Weak entity (thực thể yếu) là loại thực thể không có thuộc tính key, sự tồn tại phụ thuộc vào 1 strong entity, được ký hiệu bằng hình chữ nhật đôi.
- Entity set (tập thực thể) là tập các thực thể có chung kiểu, ví dụ tập *D25* chứa các thực thể có chung kiểu *sinh viên*.
  
<p align="center">
<img align="center" src="assets/1.svg">
</p>

#### b. Attribute
- Attribute (thuộc tính) là các đặc tính, đặc điểm của một entity, ví dụ với *Sinh viên*, ta có các attribute như *Mã SV*, *Số CCCD*, *Họ và tên*, *Ngày sinh*,... Attribute được miêu tả trong ERD bằng hình oval
- Key attribute có thể dùng để phân biệt đôi một các entity trong một entity set, thuộc tính key phải đảm bảo giá trị unique giữa các thực thể. Ví dụ *Số CCCD* là thuộc tính key cho entity set chứa toàn bộ người VN. Được ký hiệu bằng hình oval, với tên được gạch dưới.
- Composite attribute (thuộc tính phức) được cấu thành từ nhiều thuộc tính con nhỏ hơn. 
- Multivalued attribute là loại thuộc tính có thể đồng thời chứa nhiều giá trị trên một thực thể như *SDT*, được ký hiệu bằng hình oval đôi.
- Derived attribute (thuộc tính phái sinh) là thuộc tính mà giá trị của nó có thể được suy ra từ thuộc tính khác, ví dụ *tuổi* được suy ra từ *ngày sinh*, được ký hiệu bằng hình oval nét đứt.

<p align="center">
<img align="center" src="assets/2.svg">
</p>
<br><br><br>
<p align="center">
<img align="center" src="assets/3.svg">
</p>

#### c. Relationship
- Relationship (quan hệ) thể hiện sự liên kết giữa các thực thể, được ký hiệu bằng hình thoi.

<p align="center">
<img align="center" src="assets/4.svg">
</p>

- Weak relationship (quan hệ yếu), là quan hệ giữa thực thể yếu, và thực thể mà nó phụ thuộc, ký hiệu bằng hình thoi đôi.
  
<p align="center">
<img align="center" src="assets/5.svg">
</p>

- Tập quan hệ là tập hợp các quan hệ cùng loại giữa các tập thực thể.
##### i. Degree of a Relationship Set

1. Quan hệ đệ quy
   - Là loại quan hệ chỉ có 1 loại thực thể tham gia.

<p align="center">
<img align="center" src="assets/6.svg">
</p>

2. Quan hệ nhị phân
   - Là loại quan hệ có 2 loại thực thể đồng thời tham gia.
   
<p align="center">
<img align="center" src="assets/7.svg">
</p>

3. Quan hệ tam phân (Ternary Relationship)
   - Tương tự với 2 loại trước đó khi có 3 loại thực thể đồng thời tham gia vào quan hệ.

4. Quan hệ n-phân (N-ary Relationship)
   - Tương tự khi có 3 loại thực thể đồng thời tham gia vào quan hệ ta gọi là tam phân, n loại gọi là n-phân.
#### d. Cardinality
- Cardinality (bản số) là số lần mà loại thực thể đó được tham gia tối đa trong quan hệ.
1. Quan hệ 1 - 1
   - Ví dụ mỗi sinh viên chỉ được cấp một thẻ sinh viên, và thẻ sinh viên đó cũng chỉ sẽ được cấp cho một người duy nhất.

<p align="center">
<img align="center" src="assets/8.svg">
</p>

2. Quan hệ 1 - n
- Ví dụ một khoa có thể có nhiều giáo viên, nhưng mỗi giáo viên chỉ được thuộc duy nhất một khoa.

<p align="center">
<img align="center" src="assets/9.svg">
</p>

3. Quan hệ n - 1.
- Ví dụ nhiều một lớp học có thể được dạy bởi 1 giáo viên.

<p align="center">
<img align="center" src="assets/10.svg">
</p>


4. Quan hệ n - n.
- Một lớp có thể được tham gia bởi nhiều sinh viên, một sinh viên cũng có thể tham gia nhiều lớp.

<p align="center">
<img align="center" src="assets/11.svg">
</p>


##### e. Ràng buộc tham gia
- Tham gia toàn phần là khi toàn bộ thực thể trong tập thực thể đều tham gia vào quan hệ đó. Được biểu diễn bằng đường nối đôi.

<p align="center">
<img align="center" src="assets/12.svg">
</p>

- Tham gia một phần là khi thực thể bất kì trong tập thực thể có thể tham gia, hoặc không tham gia vào quan hệ.

### 2. ERD của tiệm hoa Offline

<p align="center">
<img align="center" src="assets/final.svg">
</p>

#### Giải thích:
1. Quan hệ 1-n giữa Khách hàng và Đơn hàng vì mỗi đơn hàng chỉ thuộc về 1 khách hàng, 1 khách hàng có thể sở hữu nhiều đơn hàng. Quan hệ n-n dành cho quan hệ Đơn hàng và Hoa vì 1 đơn hàng có thể chứa nhiều loại hoa, và 1 loại hoa có thể đồng thời có trong nhiều đơn hàng (với điều kiện là còn đủ hàng).
2. Tổng tiền là thuộc tính phái sinh do có thể tính ra từ đơn giá và số lượng. SĐT là thuộc tính multivalued

#### Hạn chế của ERD này
1. Hơi oversimplify.
2. Chưa có cơ chế rõ ràng cho việc ghi lại chi tiết đơn hàng.
---

Em viết file này dựa trên quá trình tự tìm hiểu các tài liệu nước ngoài kết hợp với hỗ trợ từ AI. Vì kiến thức tự học đôi khi còn hạn chế, em rất mong nhận được những nhận xét từ anh/chị để hoàn thiện hơn. Cảm ơn anh/chị đã review bài cho em. Hy vọng sớm có dịp được cộng tác và học hỏi thêm từ anh/chị!