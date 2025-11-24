# Báo Cáo: Tích Hợp Database với ORM

> **Lưu ý:** Báo cáo này sử dụng **Sequelize** làm ORM chính

---

## 1. Tìm hiểu về ORM

### ORM là gì?

**ORM** (Object-Relational Mapping) là kỹ thuật ánh xạ giữa đối tượng trong code và bảng trong database. Thay vì viết SQL, bạn dùng code JavaScript để thao tác với database.

**Ví dụ:**

SQL thuần:
```sql
SELECT * FROM books WHERE id = 1;
```

Dùng ORM:
```javascript
Book.findByPk(1);
```

### Lợi ích chính

- Không cần viết SQL thủ công
- Tự động chống SQL Injection
- Code dễ đọc và bảo trì
- Dễ chuyển đổi giữa các loại database

---

## 2. Sequelize - ORM cho Express.js

### Sequelize là gì?

**Sequelize** là ORM (Object-Relational Mapping) phổ biến nhất cho NodeJS/Express.js, cho phép làm việc với database thông qua JavaScript thay vì viết SQL thuần.

### Đặc điểm chính

- ORM lâu đời và ổn định nhất cho NodeJS
- Hỗ trợ nhiều loại database: MySQL, PostgreSQL, SQLite, MSSQL
- API đơn giản, dễ học cho người mới
- Cộng đồng lớn, tài liệu phong phú

### Ưu điểm

- Tài liệu chi tiết, nhiều ví dụ  
- Ổn định, ít bug  
- Dễ học, phù hợp beginner  
- Hỗ trợ đầy đủ tính năng CRUD  
- Migration system mạnh mẽ  
- Validation tích hợp sẵn  

### Nhược điểm

- API hơi cũ so với ORM mới  
- TypeScript support chưa tốt bằng TypeORM/Prisma  
- Một số query phức tạp vẫn phải dùng raw SQL  

### Các ORM khác 

**TypeORM:** Phù hợp cho TypeScript, sử dụng Decorators  
**Prisma:** ORM mới nhất, type-safety tốt nhất nhưng cộng đồng nhỏ hơn  

**Lý do chọn Sequelize:**
- Dễ học nhất cho người mới
- Tài liệu và cộng đồng lớn
- Phù hợp với JavaScript/Express.js

---

## 3. Định nghĩa Schema với Sequelize

### 3.1. Model là gì?

Trong Sequelize, **Model** là đại diện của một bảng trong database.

Model định nghĩa:
- Tên bảng
- Các cột (columns)
- Kiểu dữ liệu của mỗi cột
- Ràng buộc (constraints)
- Quan hệ với các model khác

### 3.2. Cách định nghĩa Model

```javascript
const { DataTypes } = require('sequelize');

const Book = sequelize.define('Book', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  title: {
    type: DataTypes.STRING,
    allowNull: false
  },
  author: {
    type: DataTypes.STRING,
    allowNull: false
  },
  year: {
    type: DataTypes.INTEGER
  }
});
```

### 3.3. Giải thích các thuộc tính

| Thuộc tính | Ý nghĩa |
|------------|---------|
| `DataTypes.INTEGER` | Kiểu số nguyên |
| `DataTypes.STRING` | Kiểu chuỗi (VARCHAR) |
| `DataTypes.TEXT` | Kiểu text dài |
| `DataTypes.DATE` | Kiểu ngày tháng |
| `DataTypes.BOOLEAN` | Kiểu boolean (true/false) |
| `primaryKey: true` | Đánh dấu là khóa chính |
| `autoIncrement: true` | Tự động tăng |
| `allowNull: false` | Bắt buộc phải có giá trị |
| `defaultValue` | Giá trị mặc định |

### 3.4. Các kiểu dữ liệu Sequelize

| SQL Type | Sequelize DataType | Ví dụ |
|----------|-------------------|-------|
| INT | `DataTypes.INTEGER` | 123 |
| VARCHAR(255) | `DataTypes.STRING` | "Hello" |
| TEXT | `DataTypes.TEXT` | "Long text..." |
| DATE | `DataTypes.DATE` | new Date() |
| BOOLEAN | `DataTypes.BOOLEAN` | true/false |
| DECIMAL(10,2) | `DataTypes.DECIMAL(10,2)` | 99.99 |
| FLOAT | `DataTypes.FLOAT` | 3.14 |

### 3.5. Validation trong Model

Sequelize hỗ trợ validation ngay trong model:

```javascript
const Book = sequelize.define('Book', {
  title: {
    type: DataTypes.STRING,
    allowNull: false,
    validate: {
      notEmpty: true,
      len: [3, 100]
    }
  },
  year: {
    type: DataTypes.INTEGER,
    validate: {
      min: 1900,
      max: 2100
    }
  }
});
```

---

## 4. Cấu hình kết nối Database trong .env

### 4.1. Tại sao dùng .env?

File `.env` chứa các biến môi trường (environment variables) như:
- Thông tin kết nối database
- API keys
- Secret keys

**Lợi ích:**
- Bảo mật (không commit lên Git)
- Dễ thay đổi giữa các môi trường (dev/staging/production)
- Tách biệt config và code

### 4.2. Cấu trúc file .env

**Ví dụ file .env:**

```env
# Application
PORT=3000
NODE_ENV=development

# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=library_db
DB_USER=root
DB_PASSWORD=your_password
DB_DIALECT=mysql

# JWT (nếu có authentication)
JWT_SECRET=your_secret_key_here
JWT_EXPIRATION=24h
```


### 4.3. Cách sử dụng .env với Sequelize

**Bước 1:** Cài đặt dotenv
```bash
npm install dotenv
```

**Bước 2:** Load biến môi trường
```javascript
require('dotenv').config();

const { Sequelize } = require('sequelize');

const sequelize = new Sequelize(
  process.env.DB_NAME,
  process.env.DB_USER,
  process.env.DB_PASSWORD,
  {
    host: process.env.DB_HOST,
    dialect: process.env.DB_DIALECT,
    port: process.env.DB_PORT
  }
);
```
### 4.4. Kiểm tra kết nối

Test xem kết nối database có thành công không:

```javascript
sequelize.authenticate()
  .then(() => {
    console.log('Database connected successfully!');
  })
  .catch(err => {
    console.error('Unable to connect:', err);
  });
```

### 4.5. File .gitignore

Nhớ thêm vào `.gitignore`:

```gitignore
node_modules/
.env
.env.local
.env.*.local
*.log
```

---

**Link video demo:** https://youtu.be/1dKcUNyOhfA
