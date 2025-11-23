# Báo cáo Tuần 2

## 1. Kiểu dữ liệu và cú pháp

- Cú pháp: `<type> <variable_name> = <initial_value>`

    vd: 
    ```java
     int a = 10;
     String str = "Hello world";
    ```

- **Các kiểu dữ liệu**:
    
    - `int`: Số nguyên
    - `double`: Số thực
    - `boolean`: Chỉ có 2 giá trị `true` & `false`
    - `char`: Lưu các kí tự
    - `String`: Dùng cho một chuỗi, dãy ký tự

- **Toán tử**:

    - *Toán tử số học*: `+`, `-`, `*`, `/`, `++`, `--`, `%`
    - *Toán tử gán*: `=`
    - *Toán tử so sánh*: `==`, `!=`, `>`, `<`, `<=`, `>=`
    - *Toán tử logic*:
        
        - `&&`: And
        - `||`: Or
        - `!`: not

- **Các hàm xuất ra màn hình**:

    1.1. In ra màn hình console
    
    ```java
    //in ra kết quả nhưng "không xuống dòng": print
    System.out.print( );

    //in ra kết quả rồi xuống dòng: print line
    System.out.println( );
    
    //in ra kết quả có định dạng: print format
    System.out.printf( );
    
    %d: số nguyên
    %f: số thực
    - mặc định là 6 số lẻ
    %.3f định dạng 3 số lẻ
    %s: chuỗi
    ```

    1.2. Nhập từ bàn phím

    - java.util.Scanner là công cụ được java hỗ trợ sẵn, dùng để nhập dữ liệu từ bàn phím

        **Bước 1**: Tạo đối tượng Scanner <br>
        - Scanner scanner = new Scanner(System.in);

        **Bước 2**: sử dụng các phương thức được cung cấp sẵn
        <br>- scanner.nextLine() => nhận 1 dòng nhập từ bàn phím => trả ra dữ liệu string
        <br>- scanner.nextInt() => nhận 1 số nguyên từ bàn phím => trả ra dữ liệu số nguyên
        <br>- scanner.nextDouble() => nhận 1 số thực từ bàn phím => trả ra dữ liệu số thực

- **Các câu lệnh điều kiện**:
    - Câu lệnh rẽ nhánh `if-else`

        vd:
        ```java
        int score = 85;

        if (score >= 50) {
            System.out.println("Bạn đã qua môn!");
        } else {
            System.out.println("Bạn cần cố gắng hơn.");
        }
        ```

    - Câu lệnh `switch-case`

        vd:
        ```java
        switch(n) {
        case 1:
            System.out.println("một");
        break;
        case 2:
            System.out.println("hai");
        break;
        default:
            System.out.println("Không tồn tại");
        }
        ```

## 2. Mảng và vòng lặp

- **Vòng lặp**:

  - Vòng lặp `for`:

    ```java
    for (int i = 0; i < 5; i++) {
      System.out.println(i);
    }
    ```

  - Vòng lặp `While/Do...While`:

    ```java
    int i = 0;
    while (i < 5) {
      System.out.println(i);
      i++;
    }
    ```

    - **Note**:
      + **Vòng lặp** `while`: Kiểm tra điều kiện **TRƯỚC**, thực thi **SAU**.
      + **Vòng lặp** `do-while`: Thực thi **TRƯỚC**, kiểm tra điều kiện **SAU**.
  
  - `Break/Continue`:

    - `Break`: Dừng khi thỏa mãn điều kiên.
    - `Continue`: Bỏ qua và chuyển sang phần tử tiếp theo.

- **Mảng**: Là các lưu trữ nhiều giá trị trong một biến duy nhất, và phải có cùng kiểu dữ liệu.

    - **Mảng một chiều**: 

        + Dữ liệu tuần tự, liền kê nhau trong bộ nhớ.
        + Truy cập bằng chỉ số.

    - **Mảng 2 chiều**:

        + Thực chất là một "mảng của các mảng". Mỗi phần tử của mảng cha lại là một mảng 1 chiều khác.
        + Truy cập phần tử bằng hai chỉ số: `array[chỉ_số_hàng][chỉ_số_cột]`.

    - Các thao tác với mảng.

        + Khai báo và khởi tạo: 

            ```java
                String[] fruits = {"Apple", "Orange", "Banana"};
            ```

        + Truy cập phần tử

            ```java
                int[] scores = {95, 88, 76, 100};

                int firstScore = scores[0];
            ```

            *Note*: Không thể truy cập phần tử ngoài mảng.

        + Lấy độ dài mảng

            ```java
            String[] cars = {"Vinfast", "Toyota", "Ford"};
            int numberOfCars = cars.length;
            ```

        + Duyệt mảng

            ```java
            // Dùng for
            char[] vowels = {'A', 'E', 'I', 'O', 'U'};
            for (int i = 0; i < vowels.length; i++) {
                System.out.println("Nguyên âm ở vị trí " + i + " là: " + vowels[i]);
            }

            // Dùng for-each
            int[] numbers = {10, 20, 30, 40, 50};
            int sum = 0;
            for (int num : numbers) {
                sum += num;
            }
            ```

        + Sắp xếp mảng

            ```java
                import java.util.Arrays;

                int[] messyNumbers = {5, 2, 8, 1, 9};

                Arrays.sort(messyNumbers);
            ```

        + Tìm kiếm trong mảng

            ```java
            import java.util.Arrays;

            int[] sortedNumbers = {10, 20, 30, 40, 50};

            int index = Arrays.binarySearch(sortedNumbers, 30);
            ```

        + Sao chép mảng

            ```java
            int[] original = {1, 2, 3};
            int[] badCopy = original; // Đây KHÔNG phải là sao chép!
            int[] goodCopy = Arrays.copyOf(original, original.length);

            badCopy[0] = 99;
            // Lúc này, original[0] cũng bị đổi thành 99!

            goodCopy[0] = 55;
            // original[0] vẫn là 99, không bị ảnh hưởng.
            ```

            *Note*: Vì mảng là kiểu tham chiếu, bạn không thể sao chép chúng bằng phép gán = đơn thuần. Phép gán đó chỉ tạo ra một tham chiếu khác đến cùng một mảng.

## 3. Hàm

- **Mục đích**: Tái sử dụng, dễ quản lý

- **Cấu trúc**: `access_modifier static_keyword return_type methodNa(parameter1, parameter2, ...) {     // Thân phương thức: Nơi chứa các câu lệnh     return value; // (Nếu có) }`.

    - `access_modifier`: Xác định phạm vi truy cập, ai có thể gọi phương thức này
    - `static_keyword`: Từ khóa static cho phép gọi phương thức trực tiếp từ tên lớp mà không cần tạo đối tượng.
    - `return_type`: Là kiểu dữ liệu của giá trị mà phương thức sẽ "gửi lại" sau khi thực hiện xong. Nếu phương thức không trả về gì cả, ta dùng từ khóa void.

    - `methodName`: Tên bạn đặt cho phương thức. Quy tắc đặt tên thường là dùng động từ và bắt đầu bằng chữ thường.

    - `parameter`: Là các giá trị đầu vào mà bạn "đưa" cho phương thức để nó xử lý. Đây là phần không bắt buộc.

    - `return value`: Giá trị mà phương thức trả về, phải có kiểu dữ liệu khớp với return_type

# Code PTIT đã làm được 14 bài