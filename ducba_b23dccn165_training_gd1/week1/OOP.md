# A) Biến (Variables)

## 1) Khái niệm & phân loại

- **Biến (variable):** tên trỏ tới một giá trị/đối tượng.
- **Theo kiểu dữ liệu**
  - **Nguyên thủy (primitive):** `byte, short, int, long, float, double, char, boolean`.  
    Kích thước số: 8/16/32/64-bit; `char` 16-bit (UTF-16); `boolean` `true/false`.  
    Hậu tố literal: `L` (long), `F` (float); dùng `_` phân cách: `1_000_000`; nhị phân `0b`, hex `0x`, bát phân `0`.
  - **Tham chiếu (reference):** `String`, mảng `int[]`, class, interface, enum… (field mặc định `null`).

## 2) Phạm vi (scope) & vòng đời

- **Local variable:** trong method/khối; _không có_ giá trị mặc định → phải gán trước khi dùng.
- **Instance field:** thuộc đối tượng; mặc định: số `0`, `boolean=false`, tham chiếu `null`.
- **Static field:** thuộc lớp, dùng chung cho mọi instance.
- **Parameter:** biến hình thức của phương thức.
- **Shadowing:** tên biến cục bộ trùng tên field → dùng `this.field`.

## 3) Từ khóa hay dùng

- `final`: không thể gán lại tham chiếu/giá trị (trạng thái đối tượng vẫn có thể đổi).
- `static`: thuộc lớp → `ClassName.field`.
- `var` (Java 10+): suy luận kiểu cho **biến cục bộ**; bắt buộc khởi tạo; **không** dùng cho field/parameter.
- `volatile`: đảm bảo tính hiển thị giữa các thread (không thay thế đồng bộ).
- `transient`: field không được serialize.

## 4) Truyền tham trị (pass-by-value)

- Java _luôn_ truyền tham trị. Với biến tham chiếu: truyền **bản sao tham chiếu** → có thể đổi **nội dung** đối tượng, không đổi được **liên kết** ở phía gọi.

## 5) Wrapper & Boxing

- Kiểu bao: `Integer, Long, Double, ...`
- **Auto-boxing/unboxing:** `int ↔ Integer`, v.v.

```java
class DemoVar {
    static int s;       // 0
    int x;              // 0
    public static void main(String[] args) {
        var y = 10;     // suy luận int
        final int z = 5; // không gán lại được
        Integer w = y;  // auto-boxing
        System.out.println(s); // 0
    }
}
```

---

# B) Vòng lặp (Loops)

## Khái niệm

Cấu trúc điều khiển lặp một khối mã cho đến khi điều kiện không còn thỏa (trả về `false`). Ba loại chính: `for`, `while`, `do-while`.

## Ví dụ

```java
for (int i = 1; i <= 5; i++) {
    System.out.print(i + " ");
}

int j = 1;
while (j <= 5) {
    System.out.print(j + " ");
    j++;
}

int k = 1;
do {
    System.out.print(k + " ");
    k++;
} while (k <= 5);
```

---

# C) Hàm (Method)

## Khái niệm

Khối mã có tên thực hiện nhiệm vụ cụ thể; có **tham số** (parameters) và có thể **trả về** giá trị.

## Ví dụ

```java
public static int tinhTong(int a, int b) {
    return a + b;
}

public static void main(String[] args) {
    System.out.println(tinhTong(3, 4)); // 7
}
```

---

# D) Thao tác File (File Operations)

## Khái niệm

Thao tác file là quá trình chương trình tương tác với các tệp tin được lưu trữ trên ổ đĩa của máy tính, bao gồm các hành động chính như tạo file, ghi dữ liệu vào file (Write) và đọc dữ liệu từ file (Read). Java cung cấp các lớp trong gói `java.io` (như `FileWriter, BufferedReader`) hoặc `java.nio` để thực hiện việc này.

## Ví dụ: ghi rồi đọc lại

```java
import java.io.*;

public class FileDemo {
    public static void main(String[] args) throws IOException {
        // Ghi
        try (FileWriter fw = new FileWriter("baocao.txt")) {
            fw.write("Xin chao PTIT!");
        }
        // Đọc
        try (BufferedReader br = new BufferedReader(new FileReader("baocao.txt"))) {
            String line;
            while ((line = br.readLine()) != null) System.out.println(line);
        }
    }
}
```

---

# E) Class (Lớp)

## Khái niệm

Class (Lớp) là bản thiết kế hoặc khuôn mẫu (blueprint) để tạo ra các Đối tượng (Object). Nó định nghĩa các thuộc tính (properties, là dữ liệu) và các phương thức (methods, là hành vi) mà các đối tượng thuộc lớp đó sẽ có. Class là khái niệm cốt lõi của Lập trình Hướng đối tượng (OOP).

## Ví dụ

Định nghĩa một lớp XeHoi. Mỗi chiếc xe hơi sẽ có thuộc tính mauSon và tenHang, cùng với hành vi khoiDong().

```java
class XeHoi {
    String mauSon;
    String tenHang;

    XeHoi(String mauSon, String tenHang) {
        this.mauSon = mauSon;
        this.tenHang = tenHang;
    }

    void khoiDong() {
        System.out.println("Xe " + tenHang + " dang khoi dong...");
    }
}
```

---

# F) Object (Đối tượng)

## Khái niệm

Object (Đối tượng) là một thể hiện (instance) cụ thể của một Class. Nếu Class (ví dụ: XeHoi) là bản thiết kế, thì Object là chiếc xe hơi thực tế được tạo ra từ bản thiết kế đó (ví dụ: chiếc XeHoi màu đen, hãng Vinfast).

## Ví dụ

Sử dụng lớp XeHoi đã định nghĩa ở trên, chúng ta tạo ra hai đối tượng xeVinfast và xeMerc

```java
public class Main {
    public static void main(String[] args) {
        XeHoi xeVinfast = new XeHoi("Den", "VinFast");
        XeHoi xeMerc = new XeHoi("Trang", "Merc");
        xeVinfast.khoiDong();
        xeMerc.khoiDong();
    }
}
```

---

# G) Abstract Class (Lớp trừu tượng)

## Khái niệm

Lớp đánh dấu `abstract`, **không** tạo đối tượng trực tiếp; đóng vai trò lớp cha chung.  
Có thể chứa **phương thức thường** và **phương thức trừu tượng** (không có thân, buộc lớp con override).

## Ví dụ

```java
abstract class DongVat {
    void an() {
        System.out.println("Dang an");
    }
    abstract void keu(); // lớp con phải triển khai
}

class Cho extends DongVat {
    @Override void keu() { System.out.println("Gau gau"); }
}
```

---

# H) Interface

## Khái niệm

“Hợp đồng” về **hành vi**. Lớp **implements** interface phải cung cấp triển khai cho các phương thức.  
Một lớp có thể **implements nhiều** interface (đa kế thừa hành vi).

## So sánh nhanh

- **Abstract Class (IS-A):** định nghĩa bản chất, có state & code dùng chung.
- **Interface (CAN-DO):** định nghĩa khả năng/hành vi.

## Ví dụ

```java
interface CoTheBay {
    void catCanh();
    void haCanh();
}

class Chim implements CoTheBay {
    public void catCanh() {
        System.out.println("Chim cat canh");
    }
    public void haCanh() {
        System.out.println("Chim ha canh");
    }
}

class MayBay implements CoTheBay {
    public void catCanh() {
        System.out.println("May bay cat canh");
    }
    public void haCanh() {
        System.out.println("May bay ha canh");
    }
}
```
