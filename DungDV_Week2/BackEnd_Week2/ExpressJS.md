# Báo Cáo: Framework Express.js

## 1. Giới Thiệu Về Express.js

Express.js là framework web phổ biến nhất cho Node.js, giúp xây dựng ứng dụng web và API một cách đơn giản và nhanh chóng.

### Ưu điểm:
- Đơn giản, dễ học
- Linh hoạt, không ép buộc cấu trúc
- Hiệu suất cao
- Middleware mạnh mẽ
- Cộng đồng lớn

### Nhược điểm:
- Cần tự tổ chức code
- Cần tự implement nhiều tính năng bảo mật

---

## 2. Khởi Tạo Project với Express.js

### Bước 1: Khởi tạo project
```bash
npm init
```

### Bước 2: Cài đặt Express.js
```bash
npm install express
npm install --save-dev nodemon
```

### Bước 3: Tạo file app.js
```javascript
const express = require('express');
const app = express();
const PORT = 3000;

app.get('/', (req, res) => {
  res.send('Hello World!');
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

### Bước 4: Cấu hình package.json
```json
{
  "scripts": {
    "start": "node app.js",
    "dev": "nodemon app.js"
  }
}
```

### Bước 5: Chạy server
```bash
npm run dev
```

---

## 3. Cấu Trúc Thư Mục

```
my-express-app/
├── node_modules/
├── src/
│   ├── controllers/  
│   ├── models/        
│   ├── routes/        
│   ├── middlewares/   
│   └── config/       
├── .env               
├── .gitignore
├── app.js             
└── package.json
```
---

## 4. Vai Trò Các Thư Mục

### 4.1. Models
- **Vai trò**: Lưu trữ dữ liệu (có thể là mảng hoặc kết nối database)

**Ví dụ**: `Library/src/models/book.model.js`
```javascript
let books = [
  { id: 1, title: 'Lập trình Node.js', author: 'Nguyễn Văn A', year: 2023 },
  { id: 2, title: 'Học Express.js', author: 'Trần Thị B', year: 2024 }
];

module.exports = books;
```

### 4.2. Controllers
- **Vai trò**: Xử lý logic nghiệp vụ, xử lý request và trả về response

**Ví dụ**: `Library/src/controllers/book.controller.js`
```javascript
const books = require('../models/book.model');

// Lấy tất cả sách
const getAllBooks = (req, res) => {
  res.json({ 
    success: true, 
    data: books 
  });
};

// Lấy sách theo ID
const getBookById = (req, res) => {
  const { id } = req.params;
  const book = books.find(b => b.id === parseInt(id));
  
  if (!book) {
    return res.status(404).json({ 
      success: false, 
      message: 'Book not found' 
    });
  }
  
  res.json({ success: true, data: book });
};

// Thêm sách mới
const createBook = (req, res) => {
  const { title, author, year } = req.body;
  
  const newBook = {
    id: books.length + 1,
    title, author, year
  };
  
  books.push(newBook);
  res.status(201).json({ success: true, data: newBook });
};

module.exports = { getAllBooks, getBookById, createBook };
```

### 4.3. Routes
- **Vai trò**: Định nghĩa các endpoint API (URL và HTTP method)

**Ví dụ**: `Library/src/routes/book.routes.js`
```javascript
const express = require('express');
const router = express.Router();
const bookController = require('../controllers/book.controller');

router.get('/', bookController.getAllBooks);
router.get('/:id', bookController.getBookById);
router.post('/', bookController.createBook);
router.put('/:id', bookController.updateBook);
router.delete('/:id', bookController.deleteBook);

module.exports = router;
```

### 4.4. File chính (app.js)
- **Vai trò**: Khởi tạo Express app, kết nối routes, start server

**Ví dụ**: `Library/app.js`
```javascript
const express = require('express');
const bookRoutes = require('./src/routes/book.routes');

const app = express();
const PORT = 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ message: 'Welcome to Library API' });
});

app.use('/api/books', bookRoutes);

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
```
---

## 5. Xây Dựng RESTful API

### Khái niệm REST API & HTTP Methods

| Method | Mục đích | Ví dụ |
|--------|----------|-------|
| GET | Lấy dữ liệu | `GET /api/books` |
| POST | Thêm dữ liệu | `POST /api/books` |
| PUT/PATCH | Cập nhật dữ liệu | `PUT /api/books/1` |
| DELETE | Xóa dữ liệu | `DELETE /api/books/1` |

### Cách định nghĩa Routes

**Mapping URL đến hàm xử lý:**
```javascript
app.use('/api/books', bookRoutes);
```

### Xử lý Request

**Đọc dữ liệu từ:**
- **Path Variable**: `req.params.id` (VD: `/users/123`)
- **Query Param**: `req.query.page` (VD: `/users?page=2`)
- **Request Body**: `req.body` (JSON payload)

**Ví dụ thực tế từ `Library`:**
```javascript
// GET /api/books/:id
router.get('/:id', (req, res) => {
  const { id } = req.params; 
  // Xử lý logic...
});

// GET /api/books?page=2
router.get('/', (req, res) => {
  const { page } = req.query;  
  // Xử lý logic...
});

// POST /api/books
router.post('/', (req, res) => {
  const { title, author } = req.body;
  // Xử lý logic...
});
```

### Xử lý Response

**Trả về JSON và HTTP Status Code:**
```javascript
// Success
res.status(200).json({ success: true, data: books });

// Created
res.status(201).json({ success: true, data: newBook });

// Not Found
res.status(404).json({ success: false, message: 'Book not found' });

// Bad Request
res.status(400).json({ success: false, message: 'Invalid data' });

// Server Error
res.status(500).json({ success: false, message: 'Server error' });
```

### Ví dụ API hoàn chỉnh từ `Library`

```javascript
// GET http://localhost:3000/api/books
// Response: { success: true, data: [...] }

// GET http://localhost:3000/api/books/1
// Response: { success: true, data: { id: 1, title: "..." } }

// POST http://localhost:3000/api/books
// Body: { "title": "New Book", "author": "Author", "year": 2024 }
// Response: { success: true, data: { id: 3, ... } }

// PUT http://localhost:3000/api/books/1
// Body: { "title": "Updated", "author": "Author", "year": 2024 }

// DELETE http://localhost:3000/api/books/1
// Response: { success: true, message: "Book deleted" }
```

---

## 6.File .env

File `.env` chứa các biến môi trường (thông tin nhạy cảm).

**Lưu ý**: Không commit file .env lên Git!

### Cài đặt
```bash
npm install dotenv
```

### Ví dụ file .env
```env
PORT=3000
DB_HOST=localhost
DB_NAME=myapp
DB_USER=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
```

### Sử dụng trong code
```javascript
require('dotenv').config();

const PORT = process.env.PORT || 3000;
const DB_HOST = process.env.DB_HOST;
```

### File .gitignore
```gitignore
node_modules/
.env
*.log
```

---

## 7. Package.json

### Ví dụ package.json cơ bản
```json
{
  "name": "library-api",
  "version": "1.0.0",
  "description": "API quản lý thư viện đơn giản",
  "main": "app.js",
  "scripts": {
    "start": "node app.js",
    "dev": "nodemon app.js"
  },
  "keywords": ["express", "api", "library"],
  "author": "DungDV - B23DCKH031",
  "license": "MIT",
  "dependencies": {
    "express": "^4.18.2"
  },
  "devDependencies": {
    "nodemon": "^3.0.2"
  }
}
```

### Link video demo phần 1: https://youtu.be/4_b0ByVvoPc
