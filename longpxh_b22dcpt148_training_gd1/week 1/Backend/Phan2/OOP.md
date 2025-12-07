# 1. Java cơ bản
## 1.1. Object 
- Đối tượng là thực thể trong chương trình, đại diện cho một vật thể hoặc khái niệm cụ thể.
- Đối tượng có thuộc tính (biến) và hành vi (phương thức).

**Ví dụ:**
```java
public class Student {
    String name;
    int age;

    void study() {
        System.out.println(name + " is studying.");
    }
}

Student s = new Student();
s.name = "Long";
s.age = 22;
s.study();
```

## 1.2. Class
- Lớp là khuôn mẫu để tạo ra các đối tượng.
- Lớp định nghĩa các thuộc tính và phương thức mà đối tượng sẽ có.

**Ví dụ:**
```java
public class Car {
    String brand;
    int year;

    void run() {
        System.out.println(brand + " is running.");
    }
}
```

## 1.3. Abstract Class 
- Lớp trừu tượng không thể tạo đối tượng trực tiếp.
- Có thể chứa cả phương thức đã triển khai và phương thức trừu tượng (chỉ khai báo).

**Ví dụ:**
```java
public abstract class Animal {
    abstract void makeSound();

    void eat() {
        System.out.println("Animal is eating.");
    }
}

public class Dog extends Animal {
    void makeSound() {
        System.out.println("Woof!");
    }
}
```

## 1.4. Interface 
- Interface chỉ chứa khai báo các phương thức (không có nội dung).
- Lớp triển khai interface phải định nghĩa lại toàn bộ các phương thức.

**Ví dụ:**
```java
interface Drawable {
    void draw();
}

class Circle implements Drawable {
    public void draw() {
        System.out.println("Drawing Circle");
    }
}
```
## 1.5. Biến
- Biến dùng để lưu trữ dữ liệu.
- Có các loại: biến cục bộ, biến instance, biến static.


**Ví dụ:**
```java
public class Example {
    int instanceVar; // biến instance
    static int staticVar; // biến static

    void method() {
        int localVar = 10; // biến cục bộ
    }
}
```

## 1.6. Hàm
- Hàm định nghĩa hành động mà đối tượng có thể thực hiện.

**Ví dụ:**
```java
public int sum(int a, int b) {
    return a + b;
}
```

## 1.7. I/O 

- Quản lý việc nhập/xuất dữ liệu.

**Ví dụ: Nhập từ bàn phím**
```java
import java.util.Scanner;

Scanner sc = new Scanner(System.in);
System.out.print("Nhập tên: ");
String name = sc.nextLine();
System.out.println("Tên bạn là: " + name);
```


**Ví dụ:  Đọc/Ghi file**
```java
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

FileWriter writer = new FileWriter("output.txt");
writer.write("Hello Java");
writer.close();

FileReader reader = new FileReader("output.txt");
int data = reader.read();
while(data != -1){
    System.out.print((char)data);
    data = reader.read();
}
reader.close();
```

# 1.8  Vòng lặp 

- Dùng để lặp lại một khối lệnh nhiều lần.

**Ví dụ**
```java
for(int i = 0; i < 5; i++) {
    System.out.println("i = " + i);
}

int j = 0;
while(j < 5) {
    System.out.println("j = " + j);
    j++;
}
```

# 2. 4 tính chất OOP
## 2.1. Đóng gói

- Che giấu thông tin bên trong đối tượng, chỉ cho phép truy cập thông qua các phương thức công khai.
- Phạm vi truy cập:
  - **public**: Truy cập từ mọi nơi
  - **private**: Chỉ truy cập trong cùng class
  - **protected**: Truy cập trong cùng package và class con
  - **default** (không có từ khóa): Truy cập trong cùng package



**Ví dụ**
```java
public class Account {
    private int balance;

    public void deposit(int amount) {
        if(amount > 0) balance += amount;
    }

    public int getBalance() {
        return balance;
    }
}
```
## 2.2. Kế thừa
- Lớp con kế thừa thuộc tính và phương thức từ lớp cha.

**Ví dụ**
```java
public class Animal {
    void eat() {
        System.out.println("Eating...");
    }
}

public class Cat extends Animal {
    void meow() {
        System.out.println("Meow!");
    }
}
```

## 2.3. Đa hình
- Một phương thức hoặc đối tượng có thể có nhiều hình thức khác nhau.


**Ví dụ**
```java
public class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

public class Dog extends Animal {
    void sound() {
        System.out.println("Woof");
    }
}

public class Cat extends Animal {
    void sound() {
        System.out.println("Meow");
    }
}

Animal a1 = new Dog();
Animal a2 = new Cat();
a1.sound(); // Woof
a2.sound(); // Meow
```

## 2.4. Trừu tượng

- Ẩn chi tiết triển khai, chỉ hiển thị những gì cần thiết.
- Được thực hiện thông qua Abstract Class và Interface.
- Giúp đơn giản hóa code và tăng tính bảo mật.

**Ví dụ**
```java
abstract class Shape {
    abstract void draw();
    
    // Phương thức cụ thể
    void displayInfo() {
        System.out.println("This is a shape");
    }
}

class Rectangle extends Shape {
    void draw() {
        System.out.println("Drawing Rectangle");
    }
}

class Circle extends Shape {
    void draw() {
        System.out.println("Drawing Circle");
    }
}
```
