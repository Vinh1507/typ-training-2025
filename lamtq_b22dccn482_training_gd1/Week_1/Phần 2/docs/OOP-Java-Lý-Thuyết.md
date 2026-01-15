# Lập trình Hướng đối tượng (OOP) là một mô hình lập trình cơ bản dựa trên khái niệm "đối tượng", nhằm mục đích mô hình hóa thế giới thực vào trong mã nguồn. Mỗi đối tượng kết hợp cả dữ liệu (thuộc tính) và hành vi (phương thức) vào một đơn vị duy nhất, giúp tăng cường khả năng tổ chức, tái sử dụng và bảo trì của chương trình.

1. Java Cơ bản và cấu trúc

- Class và Object
  Class (Lớp) đóng vai trò là bản thiết kế hay khuôn mẫu để tạo ra các đối tượng. Nó định nghĩa cấu trúc chung, bao gồm các biến thành viên (thuộc tính) và các phương thức (hàm) mà tất cả các đối tượng được tạo ra từ lớp đó sẽ sở hữu. Object (Đối tượng) là thể hiện cụ thể (instance) của Class, tồn tại trong bộ nhớ và được tạo ra bằng từ khóa `new`. Ví dụ, `Student` là một Class, và `Student studentA = new Student();` tạo ra một Object `studentA` từ Class đó.
- Variable và Method
  Variable (Biến) trong Java được sử dụng để lưu trữ dữ liệu. Tùy thuộc vào vị trí khai báo, biến có thể là biến instance (thuộc tính của đối tượng), biến cục bộ (chỉ tồn tại trong một khối lệnh/hàm) hoặc biến static (thuộc tính chung cho toàn bộ Class). Method (Phương thức) là các khối mã thực hiện một hành vi cụ thể của đối tượng. Khai báo phương thức bao gồm phạm vi truy cập, kiểu dữ liệu trả về và danh sách các tham số đầu vào.
- Abstract Class và Interface
  Interface (Giao diện) là một cơ chế trừu tượng thuần túy, định nghĩa một tập hợp các phương thức trừu tượng (không có phần thân) mà bất kỳ lớp nào triển khai (implement) giao diện đó đều phải định nghĩa lại. Ngược lại, Abstract Class (Lớp trừu tượng) là một lớp không thể tạo đối tượng trực tiếp (`new`) nhưng có thể chứa cả phương thức trừu tượng và phương thức có phần thân đã được định nghĩa. Đây là hai công cụ chính để đạt được tính trừu tượng trong Java.
- Vòng lặp và I/O
  Vòng lặp (`for`, `while`, `do-while`) cho phép thực thi lặp đi lặp lại một khối mã cho đến khi điều kiện lặp bị phá vỡ. I/O (Input/Output) và thao tác File liên quan đến việc quản lý luồng dữ liệu giữa chương trình và thế giới bên ngoài (bàn phím, màn hình, ổ đĩa, mạng). Các lớp như `Scanner`, `FileReader`, và các lớp `Stream` (`InputStream`, `OutputStream`) được sử dụng rộng rãi cho các tác vụ này.

2. Bốn tính chất cốt lỗi của OOP

- Tính đóng gói
  Tính Đóng gói là cơ chế bó buộc dữ liệu (thuộc tính) và các phương thức thao tác dữ liệu đó lại với nhau trong một Class duy nhất. Vai trò cốt lõi là che giấu thông tin (Data Hiding) và bảo vệ tính toàn vẹn của dữ liệu nội bộ của đối tượng. Trong Java, tính đóng gói được đạt được bằng cách khai báo các thuộc tính là `private` và cung cấp các phương thức truy cập công khai `public` (thường là Getters và Setters) để kiểm soát việc đọc hoặc ghi dữ liệu, cho phép áp dụng các logic kiểm tra hợp lệ trước khi thay đổi giá trị.
- Tính kế thừa
  Tính Kế thừa cho phép một Lớp con (Subclass) thừa hưởng các thuộc tính và phương thức từ một Lớp cha (Superclass). Mục đích chính là đạt được khả năng tái sử dụng mã (Code Reusability), giúp giảm thiểu sự trùng lặp code. Trong Java, tính kế thừa lớp được thực hiện bằng từ khóa `extends`, và nó chỉ hỗ trợ đơn kế thừa lớp (một lớp con chỉ có thể kế thừa trực tiếp từ một lớp cha). Lớp con có thể mở rộng hành vi của lớp cha hoặc ghi đè (override) các phương thức của lớp cha để thay đổi logic thực thi.
- Tính đa hình
  Tính Đa hình mô tả khả năng một đối tượng hoặc một tham chiếu có thể mang nhiều hình thái khác nhau hoặc một hành động có thể được thực hiện theo nhiều cách. Điều này làm cho mã trở nên linh hoạt và dễ mở rộng hơn. Trong Java, Đa hình được thể hiện ở hai cấp độ:
  - **Đa hình lúc biên dịch (Compile-time):** Thường được gọi là Overloading (Nạp chồng), xảy ra khi có nhiều phương thức cùng tên nhưng khác nhau về số lượng hoặc kiểu dữ liệu của tham số.
  - **Đa hình lúc chạy (Runtime):** Thường được gọi là Overriding (Ghi đè), xảy ra khi một phương thức trong lớp cha được định nghĩa lại trong lớp con thông qua kế thừa, cho phép tham chiếu kiểu lớp cha gọi đến phiên bản phương thức của lớp con.
- Tính trừu tượng
  Tính Trừu tượng là quá trình tập trung vào các chi tiết cần thiết và ẩn đi các chi tiết triển khai phức tạp, không liên quan. Mục đích là giảm thiểu độ phức tạp cho người dùng hoặc các lớp khác. Trong Java, tính trừu tượng được triển khai thông qua việc sử dụng Interface (trừu tượng hoàn toàn) và Abstract Class (trừu tượng một phần). Chúng định nghĩa một khuôn khổ chung mà các lớp cụ thể phải tuân theo, cho phép lập trình viên làm việc với các đối tượng ở mức độ khái niệm mà không cần biết đến logic thực thi chi tiết bên trong.
