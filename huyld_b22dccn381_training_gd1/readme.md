# High-Performance QR Code Login System

> **Hệ thống đăng nhập bằng mã QR bảo mật, hiệu năng cao, sử dụng cơ chế khóa giao dịch Atomic trên Redis.**

Dự án này là một giải pháp Backend/Frontend cho tính năng đăng nhập không mật khẩu (Passwordless) thông qua mã QR. Hệ thống được thiết kế để xử lý tranh chấp dữ liệu (Race Conditions) và đã được kiểm thử chịu tải (Load Testing) với hàng ngàn người dùng đồng thời.

---

## Giao diện & Luồng hoạt động

| 1. Tạo mã QR | 2. Quét & Xác nhận (Mobile) | 3. Đăng nhập thành công |
| :---: | :---: | :---: |
| ![QR Gen](/uploads/anh1.png) | ![Mobile Confirm](/uploads/anh2.png) | ![Login Success](/uploads/anh3.png) |
| *User nhập username, hệ thống sinh QR (TTL 15s)* | *Mobile quét mã và gửi xác thực* | *PC tự động chuyển trang khi trạng thái thay đổi* |

---

## 🛠 Công nghệ sử dụng

### Backend
- **Java Spring Boot 3**: Xây dựng RESTful API.
- **Spring Data Redis**: Tương tác với Redis Cache.
- **Redis (Docker)**: Lưu trữ phiên giao dịch (Transaction) tạm thời với TTL (Time-To-Live).
- **Cơ chế Atomic/Optimistic Locking**: Sử dụng `WATCH`, `MULTI`, `EXEC` để đảm bảo tính toàn vẹn dữ liệu, ngăn chặn đăng nhập trùng lặp.

### Frontend
- **ReactJS**: Library xây dựng giao diện.
- **Ant Design (Antd)**: UI Framework (QRCode, Result, Button).
- **Axios**: Xử lý HTTP Request.

### Testing & DevOps
- **k6**: Công cụ Load Testing (Kiểm thử chịu tải).
- **Docker**: Container hóa Redis.

---

## Báo cáo Kiểm thử chịu tải (Load Testing)

Hệ thống đã được kiểm thử chịu tải (Stress Test) ngay trên môi trường Localhost (chạy đồng thời cả Client tạo tải, Server xử lý và Database).

### 1. Cấu hình bài Test (k6)
Giả lập lượng người dùng ảo (Virtual Users - VUs) tăng dần lên **7,000 users** trong vòng 1 phút.

![k6 Config](/uploads/anhtest1.png)

### 2. Tài nguyên hệ thống (Task Manager)
Trong quá trình test, CPU và Memory hoạt động ở mức cao do phải gánh cả tool test và server.
- **CPU**: ~82% 
- **Memory**: ~93% (RAM 8GB hàn chết)
*(Lưu ý: Trong môi trường Production khi tách rời Server và Client test, hiệu năng sẽ còn cao hơn nhiều)*.

![System Resources](/uploads/anhtest2.png)

### 3. Kết quả (Result)
- **Tổng Request**: 146,190 requests.
- **Thông lượng (Throughput)**: ~2,064 requests/giây.
- **Tỷ lệ thành công**: **100%** (Không có request nào bị timeout hay lỗi 500/404).
- **User đồng thời**: Chịu tải thành công **7,000 VUs**.

![k6 Result](/uploads/anhtest3.png)

---

## Phân tích điểm nghẽn (Bottleneck Analysis)

Mặc dù hệ thống chịu được tải cao và không có lỗi, nhưng log backend cho thấy dấu hiệu của **"Thắt nút cổ chai" (Bottleneck)**.

![Backend Logs](/uploads/anhtest4.png)

**Hiện tượng:**
- Server in ra hàng loạt UUID cùng một lúc (burst logs).
- Đây là dấu hiệu của việc **Thread Pool** của Tomcat bị quá tải nhất thời. Các request phải xếp hàng (Queue) chờ xử lý do mô hình **HTTP Polling** (Gửi request liên tục mỗi 2s để kiểm tra trạng thái).

**Vấn đề của mô hình hiện tại:**
- Với 7,000 users, mỗi user gửi request mỗi 2 giây -> Server phải hứng **3,500 request/giây** chỉ để trả lời câu hỏi "Tôi đăng nhập xong chưa?".
- Điều này gây lãng phí tài nguyên mạng và CPU rất lớn.

---

## Hướng cải thiện (Future Roadmap)

Để giải quyết vấn đề nghẽn cổ chai và tối ưu hóa tài nguyên cho hàng triệu người dùng, giải pháp tiếp theo là:

### Chuyển đổi sang WebSocket (Real-time Communication)

Thay vì Client phải hỏi Server liên tục (Polling), chúng ta sẽ thiết lập một kênh kết nối 2 chiều (Duplex) bền vững.

1.  **Client** subscribe vào topic `/topic/login/{uuid}`.
2.  **Server** khi nhận được xác nhận từ Mobile -> Bắn event vào Message Broker.
3.  **Client** nhận tín hiệu ngay lập tức (Real-time) và chuyển trang.

**Lợi ích:**
- Giảm lượng Request thừa xuống **0**.
- Độ trễ phản hồi thấp nhất (Latency < 10ms).
- Tiết kiệm tài nguyên CPU cho việc xử lý handshake liên tục.

---
## Những việc chưa hoàn tất
- Chưa hosting được website lên Internet 
- Vấn đề: Vercel chỉ hosting được Frontend, trong khi để các thiết bị khác (dùng mạng khác) quét và gửi request được đến hệ thống thì bắt buộc phải hosting cả backend.
- Việc đang làm: Tìm hiểu cách hosting cả hệ thống. Đang bắt đầu tìm hiểu về cloud
- Mục tiêu cần làm: Hosting được toàn bộ hệ thống lên Internet một cách thành công.