## Báo cáo Backend Week 4

# I. Tìm hiểu về Message Queue

1. 
    - Khái niệm: `Message Queue - MQ` là một cơ chế trung gian cho 
    phép
    các service giao tiếp với nhau thông qua việc gửi và nhận message
    một cách bất đồng bộ.
    - Hiểu 1 cách đơn giản Queue (hàng đợi) chứa Message(thông tin)
    giống như hòm thư

    - Một hệ thống Message Queue thường có những thành phần sau:
        - `Message`: Thông tin được gửi có thể là text, binary hoặc
        JSON
        - `Message Queue`: Nơi chứa những message, cho phép producer 
        và consumer có thể trao đổi với nhau
        - `Producer`: Service tạo ra message và đưa message vào 
        message queue 
        - `Consumer`: Service nhận message từ message queue và xử lý
        - `Channel`: Là cơ chế truyền thông tin giữa Producer và 
        Consumer thông qua Message Queue (Một số MQ không có)
        - `Broker`: Xử lý message và quản lý message queue để đảm bảo 
        Producer và Consumer truyền thông tin được cho nhau. Broker 
        giúp định tuyến thông tin, quản lý tình trạng của hành đợi, 
        và đảm bảo rằng thông tin được chuyển giao đúng các
        - Một service có thể vừa là producer và consumer

        - ![Minh họa](images/img1.png)

    - Hoạt động theo mô hình `Producer -> Queue -> Consumer`
        - Điểm cốt lõi của mô hình là nó tách biệt các service, 
        Producer không cần chở Consumer xử lý.
        
        - ![Minh họa](images/img2.png)

        - Ví dụ: Gửi email đăng ký tài khoản, hệ thống cần gửi email xác nhận
            - Khi không sử dụng MQ: Producer và Consumer gắn chặt với nhau, Producer(Service đăng ký) phải tự gửi email, xử lý 
            xong toàn bộ mọi việc rồi mới trả về kết 
            
            - Khi sử dụng MQ: Producer và Consumer tách rời nhau, 
            Producer(Auth Service) chỉ cần đẩy 1 message vào queue, 
            không chờ Consumer gửi email mà nó sẽ phản hồi lại ngay 
            cho User ví dụ : "Đăng ký thành công! Vui lòng kiểm tra 
            email". Consumer (Email Service) có thể lấy message từ 
            queue bất cứ lúc nào, xử lý gửi email riêng không ảnh 
            hưởng tới request của 
            
2. Những lý do cần sử dụng MQ
    - Ưu điểm:
        - `Bất đồng bộ`: Message Queue hỗ trợ truyền thông điệp giữa các thành phần mà không đòi hỏi chúng phải chờ đợi nhau. Nó giúp cải thiện hiệu suất và tăng tính mở rộng của hệ thống

        - `Chia nhỏ hệ thống`: Các service chỉ giao tiếp qua mesage -> giảm sự phụ thuộc

        - `Chống quá tải hệ thống`: Khi lượng request tăng cao MQ đóng vai trò như 1 lớp đệm để không làm cho hệ thống sập, nó chỉ giao cho consumer 1 số lượng request trong khả năng sử lý của DB còn lại sẽ chờ ở trong queue 

        - `Đảm bảo an toàn dữ liệu`: Do message được lưu trong queue, khi 1 service xử lý nhưng bị lỗi, ta không lo mất data vì có thể thấy message trong queue ra và retry

    - Những Message Queue phổ biến:
        - `RabbitMQ`
        - `Kafka`
        - `Redis Stream`
        - `AWS SQS`
        - `MSMQ - Microsoft Message Queuing`
        - `RocketMQ`

# II. Implement
 
1. RabbitMQ
    - RabbitMQ là message broker hỗ trợ AMQP - Advance Message 
    Queuing Protocol giao thức tiêu chuẩn để truyền message

    - ![Minh họa](images/img1.png)

    - Kiến trúc của `RabbitMQ`:
        - `Producer`: Ứng dụng gửi message vào RabbitMQ
        - `Consumer`: Ứng dụng nhận và xử lý message
        - `Queue`: Nơi message được lưu trữ tạm thời
        - `Message`: Dữ liệu được truyền từ producer -> consumer
        - `Connection`: Một kết TCP giữa ứng dụng và RabbitMQ broker
        - `Channel`: Một kết nối ảo trong 1 Connection, việc
        publishing hoặc consuming từ một queue đều được thực hiện trên channel.

        - `Exchange`: Là nơi nhận message được publish tử Producer và đẩy chúng vào queue dự trên quy tắc của từng loại Exchange

        - `Bingding`: Đảm bảo nhận nhiệm vụ liên kết giữa Exchane và Queue

        - `Routing Key`: Một key mà Exchange dựa vào đó để quyết định cách để định tuyến message đến queue. Hiểu nôm na, Routing key là địa chỉ dành cho 
        
        - `AMQP - Advance Message Queuing Protocol`: Giao thức tiêu chuẩn để truyền message

        - `User`: Tài khoản truy cập RabbitMQ, gán quyền theo vhost

        - `Virtual host - vhost`: Không gian logic để tách queue/exchange giữa nhiều ứng dụng khác nhau

        - ![Minh họa](images/img3.png)

    - Các loại Exchange:
        - Direct Exchange: 
            - Định tuyến message đến queue dựa trên sự 
            khớp chính xác giữa routing key của message và routing key 
            của queue binding. Là loại exchange đơn giản nhất, thường dùng gửi đến đúng 1 key, nhưng nhiều nhiều queue dùng 1
            routing key => nó cũng trở thành multicast

            - Cách hoạt động:
                - Queue được blind tới Direct Exchane với 1 routing key K
                - Producer gửi message với routing key R vào exchange
                - Exchange so sánh R và K:
                    - Nếu R = K: message được chuyển vào queue đó
                    - Nếu R != K: queue sẽ không được nhận
            
        - Fanout Exchange:
            - Gửi message đến tất cả các queue được blind, bỏ qua
            routing key. 
            - Cách hoạt động:
                - 1 Fanout Exchange có thể bind với nhiều queue
                - Khi producer gửi message ==> tất cả queue được
                blind đều nhận được 

        - Topic Exchange:
            - Định tuyến message dựa trên pattern khớp giữa routing   key của message và blind key của queue. Dùng cho routing phức tạp, linh hoạt hơn direct exchange
            - Có thể sử dụng ký tự đại diện: * - khớp 1 phần tử trong 
            routing key; # - khớp 0 hoặc nhiều phần tử
            - Cách hoạt động:
                - Queue bind với topic exchange bằng 1 pattern
                - Message gửi đến exchange với routing key
                - Nếu routing key khớp với pattern ==> message được chuyển vào queue

        - Headers Exchange:
            - Định tuyến dựa trên header trong message, không dựa vào routing key. Phù hợp khi routing dựa trên nhiều thuộc tính phức tạp
            - Cách hoạt động:
                - Queue bind với header exchange bằng 1 hoặc nhiều cặp key-value
                - Message gửi kèm header
                - Nếu header của message khớp với header queue => 
                message được gửi vào queue

        
