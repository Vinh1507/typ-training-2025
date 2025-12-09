# Week 4

## 1. Tìm hiểu về Message Queue 

### 1.1 Message Queue là gì?

- Message Queue (Hàng đợi tin nhắn) là một mô hình giao tiếp bất đồng bộ giữa các thành phần trong hệ thống (services, applications). Thay vì gọi trực tiếp nhau (như REST API), các thành phần gửi tin nhắn vào một hàng đợi, và thành phần khác sẽ lấy tin nhắn từ hàng đợi đó để xử lý.

### 1.2 Tại sao cần dùng Message Queue?

- Decoupling : Giảm sự phục thuộc giữa các thành phần trong hệ thống : Producer không cần biết Consumer là ai, đang ở đâu hay có đang hoạt động không

- Async : Xử lý bất đồng bộ : Giúp hệ thống phản hồi nhanh hơn. VD : Khi đặt đơn hàng thành công, server sẽ trả về trang redirect tiếp theo ngay lập tức (tùy thuộc vào việc BE cho redirect đến đâu), còn việc gửi email cảm ơn sẽ được đẩy vào MQ để xử lý sau.

- Scalability : Tăng khả năng mở rộng : Có thể dễ dàng thêm nhiều Consumer để xử lý tin nhắn trong hàng đợi nếu tải cao

- Reliability : Tăng độ tin cậy : Nếu Consumer lỗi, tin nhắn vẫn nằm trong Queue và sẽ được tiếp tục xử lý khi Consumer hoạt động trở lại

- Traffic Spikes : Chống quá tải : MQ đóng vai trò như bộ đệm khi lượng request tăng lên đột biến, giúp hệ thống không bị sập


### 1.3 Có những loại Message Queue nào ?

- RabbitMQ : Phổ biến, dễ dùng, hỗ trợ nhiều giao thức. Phù hợp cho các hệ thống transactional

- Kafka : Hiệu năng cực cao, xử lý stream dữ liệu lớn (logging, tracking)

- ActiveMQ : Lâu đời, hỗ trợ Java Message Service (JMS)

- Amazon SQS : Dịch vụ Message Queue của AWS

## 2. RabbitMQ và kiến trúc:

- RabbitMQ tích hợp rất tốt với Spring Boot và phù hợp với use case em sử dụng là gửi email

- Kiến trúc cơ bản của RabbitMQ :

RabbitMQ hoạt động dựa trên giao thức AMQP (Advanced Message Queuing Protocol), gồm các thành phần chính:

1. Producer : Ứng dụng gửi tin nhắn

2. Exchange : Nơi nhận tin nhắn từ Producer và quyết định đẩy tin nhắn vào Queue nào dựa trên quy tắc routing key
Có các loại Exchange sau :
    
    - Direct Exchange : Gửi đến Queue có routing key khớp
    
    - Fanout Exchange : Gửi đến tất cả các Queue được bind với nó 

    - Topic Exchange : Gửi đến Queue có routing key khớp với pattern (vd như là order.*)

3. Queue : Nơi lưu trữ tin nhắn chờ xử lý

4. Consumer : Ứng dụng nhận tin nhắn từ Queue và xử lý tin nhắn

5. Binding : Liên kết giữa Exchange và Queue thông qua routing key

Luồng cơ bản : Producer -> Exchange -> (Binding) -> Queue -> Consumer

## 3. Mở rộng

1. Idempotency (Tính nhất quán)

- Idempotency là tính chất của một hành động hoặc yêu cầu, đảm bảo rằng nó có thể được thực hiện nhiều lần mà không gây ra hiệu ứng phụ hoặc kết quả không mong muốn.

- Trong ngữ cảnh của RabbitMQ, idempotency có thể được áp dụng khi gửi tin nhắn đến Queue.

- Nếu một tin nhắn đã được gửi và xử lý thành công, việc gửi lại tin nhắn đó không gây ra hiệu ứng phụ hoặc kết quả không mong muốn.

VD với bonjour Email : Nếu nhận đc 2 tin nhắn yêu cầu gửi mail cho cùng 1 người giả sử là buianhduc@gmail.com, thì hệ thống chỉ nên gửi 1 mail

-> Giải pháp : Trước khi gửi, kiểm tra trong bảng email logs xem tin nhắn đó đã được gửi chưa . Nếu rồi -> bỏ qua luôn

2. Duplicate Message (Trùng lặp tin nhắn)

- Duplicate message xảy ra khi một tin nhắn được gửi đến Queue và được xử lý nhiều lần.

- Trong ngữ cảnh của RabbitMQ, duplicate message có thể xảy ra do các nguyên nhân sau:

    - Lỗi trong Consumer khiến tin nhắn không được xử lý thành công
    - Lỗi trong Exchange khiến tin nhắn không được gửi đến Queue
    - Lỗi trong Queue khiến tin nhắn không được xử lý thành công
    - Lỗi trong Consumer khiến tin nhắn không được xử lý thành công

3. Dead Letter Queue (Tin nhắn chết)

- Dead letter queue là một queue đặc biệt trong RabbitMQ, được sử dụng để lưu trữ tin nhắn không thể được xử lý thành công

- Trong ngữ cảnh của RabbitMQ, dead letter queue có thể được áp dụng khi gửi tin nhắn đến Queue

- Nếu một tin nhắn không thể được xử lý thành công, nó sẽ được chuyển đến dead letter queue (Cấu hình DLQ để chuyển các tin nhắn lỗi sang 1 hàng đợi riêng sau số lần retry nhất định, sau đó admin có thể ktra thủ công)