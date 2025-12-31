# Exception Handling & File I/O

## 1. Exception Handling (Xử lý ngoại lệ)

### 1.1 Định nghĩa

- Ngoại lệ là một sự kiện bất thường xảy ra trong quá trình thực thi chương trình, làm gián đoạn luồng xử lý bình thường của các câu lệnh.

### 1.2 Phân loại

| Đặc điểm | Checked Exception | Unchecked Exception |
| :--- | :--- | :--- |
|**Đ/n**| Ngoại lệ được kiểm tra tại thời điểm biên dịch. | Ngoại lệ xảy ra tại thời điểm chạy, thường do lỗi logic. |
|**Bắt buộc xử lý**|**Có**. Compiler sẽ báo lỗi nếu bạn không dùng try-catch hoặc throws.|**Không**. Compiler không bắt buộc xử lý.|

### 1.3 Các khối lệnh xử lí

- `try`: Chứa đoạn code có thể gây ra lỗi.
- `catch`: Bắt và xử lý lỗi của `try`.
- `finally`: Luôn chạy dù có lỗi hay không.

vd:
```java
public class Main {
    public static void main(String[] args) {
        TaiKhoanNganHang tk = new TaiKhoanNganHang();

        try {
            System.out.println("Đang thực hiện giao dịch...");
            tk.rutTien(1000.0);
        } 
        catch (SoDuKhongDuException e) {
            System.out.println("BẮT ĐƯỢC NGOẠI LỆ: " + e.getMessage());
        } 
        finally {
            System.out.println("Kết thúc phiên giao dịch (Rút thẻ)");
        }
    }
}
```

### 1.4 `throw` và `throws`

- `throw`: Dùng để ném ra một ngoại lệ cụ thể trong thân hàm.

- `throws`: Dùng trên khai báo hàm để báo hiệu rằng hàm này có thể ném ra ngoại lệ đó (đẩy trách nhiệm xử lý cho hàm gọi nó).

vd:
```java
class TaiKhoanNganHang {
    private double soDu = 500.0;

    public void rutTien(double soTienRut) throws SoDuKhongDuException {
        
        if (soTienRut > soDu) {
            throw new SoDuKhongDuException("Lỗi: Số dư " + soDu + " không đủ để rút " + soTienRut);
        }

        soDu -= soTienRut;
        System.out.println("-> Rút thành công: " + soTienRut);
    }
}
```

### 1.5 Custom Exception (tự định nghĩa ngoại lệ)

- Có thể tạo ngoại lệ riêng bằng cách kế thừa Exception (cho Checked) hoặc RuntimeException (cho Unchecked) để mô tả lỗi nghiệp vụ cụ thể.

### 1.6 Vai trò

- Tránh crash ứng dụng: Giúp phần mềm không bị tắt đột ngột khi gặp lỗi.

- Trải nghiệm người dùng: Hiển thị thông báo lỗi dễ hiểu thay vì in ra dòng mã lỗi kỹ thuật (stack trace).

- Debugging: Giúp ghi log lỗi chính xác để lập trình viên sửa chữa.