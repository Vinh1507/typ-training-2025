# ## 1. Message Queue là gì?

Message Queue là hệ thống cho phép các ứng dụng gửi và nhận messages với nhau theo bất đồng bộ., giúp các service giao tiếp với nhau thông qua hàng đợi thay vì gọi trực tiếp.

Messages sẽ được lưu trong queue cho đến khi được xử lý xong.
**Sử dụng MQ vì một số lý do:**

* Giảm tải cho API -tránh sử lý quá nhiều trong cùng 1 request
* Tránh mất dữ liệu
* Tách biệt các service -Service A gửi message xong , Service B xử lý khi rảnh, 2 service hoàn toàn độc lập nhau
* Dễ mở rộng (thêm worker không ảnh hưởng logic chính)

---
# ## 2. Các loại Message Queue phổ biến

- **RabbitMQ**: phổ biến, dễ dùng, Routing linh hoạt, topic exchange mạnh
- **Kafka**: xử lý lượng data lớn, real-time
- **Redis Stream**: Nhẹ, đơn giản, chạy chung Redis
- **Amazon SQS**: dịch vụ do AWS quản lý, khoong cần tự quản lý

Tùy thuộc vào từng dự án mà sử dựng kiến trúc cho phù hợp
# ## 3. Implement MQ vào Post-Service để?

Khi user tạo hoặc xóa bài viết, có nhiều tác vụ nền cần xử lý:

* Gửi thông báo đến follower
* Cập nhật Analytics
* Xóa file media liên quan
* Lưu log hành vi
* Trigger AI service (recommendation)

Những tác vụ này **không nên xử lý trong API**, nếu không sẽ làm request mất 500–1500ms hoặc hơn.

-> MQ giúp API **nhanh và nhẹ**, còn việc nặng thì worker xử lý sau.

---

# ## 4. Kiến Trúc MQ áp dụng vào dự án đang học tập

```
 ┌──────────────┐
 │ Post Service │
 └──────┬───────┘
        │ publish event
        ▼
 ┌──────────────────────┐
 │ RabbitMQ Exchange    │  (facebook_events)
 │       type: topic    │
 └────────┬─────────────┘
         │ routing key
 ┌───────┼──────────────┬──────────────────────┐
 │       │              │                      │
 ▼       ▼              ▼                      ▼
Queue A Queue B     Queue C                Queue D
(post.created) (post.deleted)  (analytics.*)   (media.*)

Consumers:
- Notification Service
- Media Worker
- Analytics Worker
- Logging Worker
```

---

# ## 5. Use Case thực tế trong Post Service

## ### 5.1. Khi tạo bài viết

Code:

```js
const post = await postModel.create({...});
await publishPostEvent("post.created", {
  postId: post._id.toString(),
  userId,
  content: post.content
});
await invalidatePostCache(req);
```

Flow:

```
User → API createPost → DB → MQ publish(post.created) → cache clear → Response
```

Consumer nhận `post.created` có thể:

* Gửi thông báo
* Cập nhật số lượng bài viết
* Chạy AI suggestion
* Log hành vi

---

## ### 5.2. Khi xóa bài viết

```js
await publishPostEvent("post.deleted", {
  postId: post._id.toString(),
  userId,
  media: post.media
});
```

Flow:

```
API deletePost → DB delete → MQ publish(post.deleted) → clear cache → Response
```

Worker xử lý:

* Xóa file media
* Xóa log liên quan
* Cập nhật analytics

---

# ## 6. Mã nguồn MQ Module – Giải thích chi tiết

```js
import amqp from 'amqplib';
import logger from './logger.js';
import dotenv from "dotenv";
dotenv.config();

let connection = null;
let channel = null;

const EXCHANGE_NAME = 'facebook_events';
```

### ✔ `connection` và `channel` được giữ dạng singleton

→ tránh tạo lại mỗi lần publish → tối ưu hiệu năng.

---

## ### 6.1. Kết nối RabbitMQ

```js
export const connectToRabbitMQ = async () => {
  try {
    connection = await amqp.connect(process.env.RABBITMQ_URL);
    channel = await connection.createChannel();

    await channel.assertExchange(EXCHANGE_NAME, 'topic', { durable: false });
    logger.info("Connected to RabbitMQ");

    return channel;
  } catch (error) {
    logger.error(`Error connecting to RabbitMQ: ${error.message}`);
    throw error;
  }
};
```

## ### 6.2. Publish message

```js
export const publishPostEvent = async (routingKey, message) => {
  if (!channel) {
    await connectToRabbitMQ();
  }

  channel.publish(
    EXCHANGE_NAME,
    routingKey,
    Buffer.from(JSON.stringify(message))
  );

  logger.info(`Published event to ${routingKey}`);
};
```

### Giải thích:

* Kiểm tra nếu chưa có channel → tạo mới
* Gửi message dạng buffer
* Sử dụng routing key như:

  * `post.created`
  * `post.updated`
  * `post.deleted`

---

# ## 7. Cần chú ý khi sử dụng MQ

## ### 7.1. Duplicate Message (trùng message)

RabbitMQ là **at-least-once**, nghĩa là luôn có khả năng gửi message **lặp**.
→ Vì thế có thể gửi message nhiều lần nếu consumer chưa ack.
 Consumer cần idempotent:

```js
if (await redis.get(`processed:${postId}`)) {
  return ch.ack(msg);
}
await redis.set(`processed:${postId}`, 1);
```

---

## ### 7.2. Message mất khi worker chết

Giải pháp:

* `ack`/`nack`
* retry
* dead-letter queue (DLQ)

---

## ### 7.3. Không bảo đảm thứ tự

Nếu có nhiều consumer thì thứ tự xử lý sẽ có thể không còn đúng theo thứ tự nữa.

Giải pháp:

* 1 consumer/queue
* hoặc sắp xếp bằng timestamp trong DB

