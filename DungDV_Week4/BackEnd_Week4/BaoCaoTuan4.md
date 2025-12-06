# Message Queue

## 1. Message Queue là gì

Message Queue là hệ thống cho phép các ứng dụng gửi và nhận messages với nhau theo kiểu bất đồng bộ. Hiểu đơn giản thì nó giống như hộp thư vậy, người gửi bỏ thư vào, người nhận lấy ra sau, ko cần 2 bên phải online cùng lúc.

Messages sẽ được lưu trong queue cho đến khi được xử lý xong.

## Tại sao dùng Message Queue?

Có mấy lý do chính:

1. **Tách biệt các service** - Service A gửi message xong thì kệ nó, Service B rảnh thì xử lý, 2 cái này độc lập nhau

2. **Xử lý bất đồng bộ** - Ko cần đợi xong mới response, user nhận phản hồi nhanh hơn

3. **Giảm tải** - Xử lý theo batch chứ ko dồn hết một lúc

4. **Ko mất data** - Message được lưu lại, nếu service down thì sau này xử lý lại

Ví dụ thực tế: user đăng ký xong thì gửi email chào mừng. Thay vì gửi ngay (chậm) thì đẩy vào queue, worker xử lý sau.

## Có những loại MQ nào

- **RabbitMQ**: phổ biến, dễ dùng, có UI quản lý
- **Kafka**: xử lý data lớn, real-time, nhưng phức tạp hơn
- **Redis**: đơn giản nhanh nhưng ko reliable bằng
- **Amazon SQS**: dịch vụ AWS, ko cần tự quản lý

Tùy dự án mà chọn. Project nhỏ thì dùng Redis hoặc RabbitMQ, lớn thì Kafka.

---

## 2. Implement với RabbitMQ

### Kiến trúc cơ bản

```
Producer -> Exchange -> Queue -> Consumer
```

- **Producer**: app gửi message đi
- **Exchange**: nhận message rồi route đến queue 
- **Queue**: chỗ lưu message
- **Consumer**: app lấy message ra xử lý

Luồng hoạt động:
1. Producer gửi message
2. Exchange route đến queue phù hợp
3. Message nằm chờ trong queue
4. Consumer lấy ra xử lý
5. Xong thì ACK, message bị xóa khỏi queue

### Code ví dụ

Cài đặt:
```bash
npm install amqplib
```

Producer - gửi message:

```javascript
const amqp = require('amqplib');

async function sendEmailMessage(userEmail, userName) {
  const connection = await amqp.connect('amqp://localhost');
  const channel = await connection.createChannel();
  
  const queue = 'email_queue';
  await channel.assertQueue(queue, { durable: true });
  
  const message = JSON.stringify({
    email: userEmail,
    name: userName,
    type: 'welcome'
  });
  
  channel.sendToQueue(queue, Buffer.from(message), {
    persistent: true
  });
  
  console.log('Message sent:', message);
  
  setTimeout(() => connection.close(), 500);
}
```

Consumer - xử lý message:

```javascript
const amqp = require('amqplib');

async function startWorker() {
  const connection = await amqp.connect('amqp://localhost');
  const channel = await connection.createChannel();
  
  const queue = 'email_queue';
  await channel.assertQueue(queue, { durable: true });
  
  channel.prefetch(1); // xử lý 1 message 1 lúc
  
  console.log('Waiting for messages...');
  
  channel.consume(queue, async (msg) => {
    const data = JSON.parse(msg.content.toString());
    console.log('Received:', data);
    
    try {
      await sendEmail(data.email, data.name);
      channel.ack(msg); // xong thì ack
    } catch (err) {
      channel.nack(msg, false, true); // lỗi thì đưa lại vào queue
    }
  });
}
```

Dùng trong controller:

```javascript
const register = async (req, res) => {
  // đăng ký user xong...
  
  // gửi vào queue thay vì gửi email ngay
  await sendEmailMessage(user.email, user.username);
  
  res.json({ success: true, message: 'Đăng ký thành công' });
};
```

---

## 3. Các vấn đề cần chú ý

### Duplicate message

Message có thể bị xử lý nhiều lần nếu consumer crash trước khi ACK. Cách xử lý là dùng messageId để check đã xử lý chưa:

```javascript
const processed = new Set();

channel.consume(queue, async (msg) => {
  const msgId = msg.properties.messageId;
  
  if (processed.has(msgId)) {
    channel.ack(msg);
    return; // skip
  }
  
  await processMessage(msg);
  processed.add(msgId);
  channel.ack(msg);
});
```

Trong production nên lưu vào database thay vì Set.

### Idempotency

Đảm bảo xử lý nhiều lần cũng cho kết quả giống nhau. Ví dụ: trước khi insert vào db thì check xem đã có chưa.

### Các vấn đề khác

- **Message ordering**: cần đúng thứ tự thì dùng single consumer
- **Dead letter queue**: queue chứa message lỗi, ko xử lý được
- **Retry**: retry khi lỗi nhưng giới hạn số lần
- **Monitoring**: theo dõi số message trong queue
