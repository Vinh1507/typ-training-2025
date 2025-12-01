BÁO CÁO PHẦN 1: LẬP TRÌNH JAVA
1.Mục tiêu: cài đặt JDK và 1 IDE bất kỳ
2.Làm quen với các kiểu dữ liệu và cú pháp
•Các kiểu dữ liệu : 
    + số nguyên: int, short, long, byte
    + số thực: double, float
    + ký tự: char
    + logic: boôlean
    + xâu: string
VD chương trình cơ bản:
   public class Demo {
          public static void main (String [ ] args)  {
          int a=5;
          double b=2.5;
          String s = “xin chao”;
          boolean ok = true;

         System.out.println(“ a+b = “  + (a+b));

•Câu lệnh điều kiện : if, if-else

•Toán tử logic và so sánh
   + so sánh: ==, !=, <, >, <=, >=
   + logic: && (and), || (or), ! (not)
VD so sánh chiều cao :
   public class CompareHeight {
    public static void main(String[] args) {
        double Hoa = 1.68;
        double Linh = 1.72; 
System.out.println("Chiều cao A: " + Hoa + " m");
 System.out.println("Chiều cao B: " + Linh + " m");
        if (Hoa == Linh) {
  System.out.println("Hai người cao bằng nhau.");
        } else if (Hoa > Linh) {
            System.out.println("Hoa cao hơn Linh.");
        } else {
            System.out.println("Linh cao hơn Hoa.");
        }
    }
}


3.Làm quen mảng và vòng lặp
• Mảng:
  + 1 chiều: int [ ]
  + 2 chiều: [ ] [ ]
• Vòng lặp: for, while, do while
VD in số từ 1 đến n:
    int i = 1;
while (i <= n) {
    System.out.println(i);
    i++;
}
 

4. Hàm
- Hàm là một khối mã thực hiện 1 nhiệm vụ, có thể nhận tham số và trả về giá trị. Giúp tách nhỏ chương trình, tái sử dụng và dễ kiểm thử
- Cấu trúc:
 [ modifier ] returnType methodName(parẩmeterList) {
          //thân hàm
}
 
*Em chưa có tài khoản code ptit ạ