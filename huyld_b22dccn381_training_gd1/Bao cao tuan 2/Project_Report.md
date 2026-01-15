# Báo cáo dự án: Quét mã QR

## Phân tích cụ thể Project

1. Nghiệp vụ.  
- Dự án sẽ là: **Dự án đăng nhập bằng mã QR.**

- Luồng nghiệp vụ:  

| Bước | Mô tả chi tiết |
| :--- | :--- |
| Nhập Username|Người dùng truy cập Login webpage, nhập username và nhấn Login|
|Tạo mã QR|Backend sinh ra transaction, lưu vào Redis (cache) 1 transaction_object với TTL=15s (ứng với mỗi 1 mã QR) và trả về 1 đường dẫn URL|
|Hiển thị QR|Hệ thống nhận URL, chuyển thành mã QR và hiển thị. Cứ mỗi 15s sẽ tự động interval đổi mã và lại gen ra QR mới.|
|User quét QR bằng điện thoại|User dùng smartphone quét QR --> Hiện URL xác thực (chính là URL được trả về ban nãy)|
|Truy cập vào URL bằng điện thoại|Khi bấm vào URL, trình duyệt của điện thoại user sẽ truy cập trang web, gọi 1 GET API lấy thông tin giao dịch|
|Hiển thị giao dịch|- Nếu hết TTL hoặc transaction đã bị xóa --> Hiển thị "QR expired". **<br>**  - Nếu transaction_object vẫn còn tồn tại trong Redis và chưa bị xóa --> Trang web hiển thị giao diện xác thực: **<br>** Logging in as: username **<br>** QR expired in: 7s **<br>** [Yes]      [No]|
|User chọn Yes/No| trình duyệt gửi 1 POST API lên hệ thống để xác nhận.|
|Backend xác thực|Backend kiểm tra tính hợp lệ của của transaction trong Redis. **<br>** - Chưa hết hạn **<br>** - Chưa được xử lý **<br>** --> Nếu hợp lệ, cập nhật trạng thái transaction. **<br>** Sau khi xử lý, transaction phải bị khóa trạng thái.|
|Phản hồi tới các thiết bị|Các thiết bị đều hiển thị: "Login successfull"|

## Tài liệu - giải pháp.
### Vấn đề 1: Tạo ra 1 bản ghi transaction object ứng với mỗi phiên đăng nhập.
- Lưu vào Redis: truy xuất cho nhanh, do TTL ngắn chỉ sống trong 1 phiên ngắn nên sẽ không lưu vào database.
- Định dạng:  
```
{ 
    "tx": "uuid",  
    "username": "huy",  
    "status": "WAITING",    //WAITING/APPROVE/EXPIRED  
    "createdAt": "10:00:00",
    "expiredAt": "10:00:15",
}
```

### Vấn đề 2: Gen ra mã QR
- Cần 1 cơ chế sinh mã QR từ URL 
- (Tìm hiểu về thư viện/module làm việc này)
- Từ khóa: Shorten-URL

### Vấn đề 3: Quản lý mã QR
- Thực ra không cần quản lý mã QR bởi vì QR chỉ là 1 ảnh thể hiện của 1 transaction object
- 1 transaction object ứng với 1 phiên đăng nhập (tồn tại 15s)
- Từ transaction object --> tạo ra 1 URL (chính là nội dung của mã QR)
- Sau 15s, transaction object sẽ hết hạn và bị xóa đi trong Cache, --> tạo ra 1 transaction object mới và 1 mã QR mới.
- **Lưu ý**: Do khi 1 user gửi yêu cầu Login, trong quá trình đó sẽ có nhiều transaction object được tạo ra ứng với nhiều mã QR được tạo ra cho 1 user.  
Nhưng do chỉ có 1 id_transaction duy nhất validate tại 1 thời điểm --> tránh bị xử lý trùng/nhầm.

### Vấn đề 4: xử lý lượng request cao
- Với mỗi user: cứ 15s lại có 1 request tạo QR(= 1 request tạo transaction object + 1 request tạo QR, và nhiều request khác) --> Khi hàng ngàn user thì cực kỳ nhiều request.
- Có 2 cách xử lý:
    - Load shedding: khi hệ thống sắp quá tải, loại bỏ bớt request.
    - Rate limit: giới hạn số request/s ngay từ đầu.
- Do Rate limit hạn chế request (phù hợp chống Dos DDos) nhưng khiến hệ thống bị chậm, có thể miss request ngay từ đầu. Do bản chất là 1 user đã có nhiều request sẵn (không phải spam) nên sẽ sử dụng Load shedding.

### Vấn đề 5: Cơ chế đồng bộ trạng thái đăng nhập.
- Sau khi nhận request login từ người dùng, hệ thống sẽ tạo 1 web service (ws) đến server để lắng nghe.
- Khi người dùng gửi request xác thực (Yes) thì backend sẽ broadcast đến tất cả những thiết bị còn lại ứng với user.

- **Lưu ý**: Tạo web service thì không nên spawn thread. Do việc spawn thread khá mất thười gian. --> 1 thread spawn + chạy + cleanup (mà nếu chạy ít quá thì thười gian spawn với dọn quá thời gian chạy.)  
Mà việc Login này thì thời gian chạy không quá lâu.

### Vấn đề 6: Deploy trang web lên Internet.
- Để smartphone có thể quét mã QR, rồi truy cập đường liên kết --> trang web cần được chạy trên Internet chứ không phải localhost.
- Giải pháp: sử dụng Vercell, tính theo số lượng request.

## Công nghệ lựa chọn
1. Frontend
- React + Antd (Ant Design: 1 thư viện UI cung cấp các component có sẵn), hiển thị giao diện.
- Sinh QR từ URL: có thể tìm hiểu qrcode.react hoặc qrcodejs hoặc 1 module sinh mã QR có sẵn.
- Tự động đổi QR mỗi 15s: hàm setInterval() trong JavaScript
- WebSocket client: lắng nghe real-time.

2. Backend
- Sử dụng ngôn ngữ Java
- Spring FrameWork: SpringBoot, Spring Web, Spring WebSocket, Spring Data Reids
- Jackson: (JSON serialization/deserialization)
- Maven

3. Redis
- Sử dụng cục bộ (local): --> Dùng Docker

4. Deployment
- Vercel

5. Công cụ khác:
- Postman: để test API thủ công



