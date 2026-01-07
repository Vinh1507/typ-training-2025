## 1. Message Queue
- **Khái niệm**: là hệ thống trung gian giúp các dịch vụ gửi và nhận thông diệp bất đồng bộ qua hàng đợi (queue). MQ giúp giảm độ phụ thuộc giữa các dịch vụ, tăng khả năng mở rộng, độ tin cậy và xử lí dữ liệu hiệu quả

- **Lý do sử dụng MQ**:
   * Giảm độ phụ thuộc: Các hệ thống có thể giao tiếp mà không cần đồng bộ.
   * Xử lí bất đồng bộ: Các tác vụ có thể thực hiện sau mà không ảnh hưởng tới hệ thôgns
   * Khả năng chịu lỗi cao: Thông điệp vẫn tồn tại trong queue nếu dịch vụ gặp sự cố
   * Điều tiết lưu lượng: Quản lý tải dữ liệu khi có lượng yêu cầu lớn

- **Các loại Message Queue**:
   * RabbitMQ:
      * Dựa trên AMQP, hỗ trọ nhiều giao thức
      * Tính năng: routing, clustering, xác nhận và retry
      * Sử dụng: Giao tiếp giữa microservices
   * Kafka
      * Hệ thống phân tán, xử lí lượng lớn dữ liệu
      * Tính năng: High throughput, low latency, lưu trữ lâu dài
      * Sử dụng: Xử lí luồng dữ liệu lớn, phân tích thời gian thực
   * Amazon SQS
      * Dịch vụ quản lý của AWS
      * Tính năng: Dễ sử dụng, tích hợp AWS
      * Sử dụng: Giao tiếp giữa dịch vụ AWS
   * ActiveMQ
      * Hỗ trợ nhiêu giao thức (AMQP, MQTT, OpenWire)
      * Tính năng: Transactional messages, clustering
      * Sử dụng: Ứng dụng với nhiều giao thức và cần tính năng phân tán
   * Redis Pub/Sub
      * In-memory, tốc độ cao
      * Sử dụng: Các ứng dụng nhẹ, thông báo real-time

--- 

## 2. Implement - Sử dụng RabiitMQ
1. **Kiến trúc cơ bản**
   ```java
   Producer -> Exchange -> Queue -> Consumer
   ```
   - Producer: Service gửi thông điệp vào RabbitMQ
   - Exchange: Thành phần định tuyến message theo rule. Gồm 4 loại: direct, topic, fanout, headers
   - Queue: Lưu message tạm thời trước khi consumer xử lý
   - Consumer: Service nhận và xử lý message

2. **Use case**: Ghi log sự kiện khi thêm sinh viên
- Không ghi log trực tiếp trong API, log sẽ được đẩy vào RabbitMQ và một consumer xử lý nền

- Flow:
   * API (POST) /api/students -> Tạo sinh viên
   * Gửi message ""Sent to MQ: " + message" vào RabbitMQ
   * Consumer nhận message và in ra màn hình

- File implement:
   * RabbitMQConfig.java -> Cấu hình queue + exchange + binding
   * MessageProducer.java -> Gửi message
   * MQController -> test gửi message
   * StudentMessageConsumer.java -> Nhận message

---

## 3. Lưu ý khi sử dụng MQ
- **Idempotency (Tránh xử lí lặp lại)**: MQ có thể gửi message trùng, vì RabbitMQ hỗ trợ at-least-once delivery
- **Duplicate message (Tin nhắn bị gửi lại)**: RabbitMQ có thể xử lí nhiều lần khi consumer crash, timeout xử lí. Giải pháp dùng manual ACK,...
- **Message durability (Khả năng chịu lỗi)**: Nếu RabbitMQ khởi động lại thì message có thể mất
   * Cách xử lí:
      * Queue: durable = true
      * Message: deliveryMode = 2
      * Exchange: durable
- **Dead letter queue**: Nếu message lỗi không xử lí được -> Cần đẩy sang DLQ
- **Monitoring & Scaling**: Giám sát queue và consumer