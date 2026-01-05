# Backend API Demo

API backend với JWT Authentication và Message Queue (RabbitMQ).

## Cài đặt

```bash
cd Demo_Backend
npm install
```

Tạo file `.env`:

```env
JWT_SECRET=demo-secret-key
JWT_REFRESH_SECRET=demo-refresh-secret-key
PORT=3000
RABBITMQ_URL=amqp://localhost
```

## Chạy RabbitMQ

Docker:
```bash
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
```

UI quản lý: http://localhost:15672 (guest/guest)

## Chạy Server

Terminal 1 - API:
```bash
npm run dev
```

Terminal 2 - Email Worker:
```bash
npm run worker
```

## API

### Public
- `GET /api/books` - lấy tất cả sách
- `GET /api/books/:id` - lấy sách theo id

### Auth
- `POST /api/auth/register` - đăng ký
- `POST /api/auth/login` - đăng nhập
- `GET /api/auth/verify` - kiểm tra token
- `POST /api/auth/refresh` - refresh token
- `POST /api/auth/logout` - đăng xuất

### Protected (cần token)
- `GET /api/books-protected` - lấy sách
- `POST /api/books-protected` - tạo sách (admin)
- `PUT /api/books-protected/:id` - sửa sách (admin)
- `DELETE /api/books-protected/:id` - xóa sách (admin)

## Tài khoản test

- Admin: admin@example.com / admin123
- User: user1@example.com / user123

## Message Queue

Khi user đăng ký thành công thì gửi message vào queue, worker xử lý gửi email ở background.

Flow:
1. User đăng ký -> API response ngay
2. Message vào queue
3. Worker lấy message xử lý
4. Email được gửi

## Cấu trúc

```
Demo_Backend/
├── src/
│   ├── models/
│   ├── controllers/
│   ├── services/
│   │   └── emailQueue.service.js
│   ├── middlewares/
│   └── routes/
├── workers/
│   └── emailWorker.js
├── app.js
└── package.json
```
