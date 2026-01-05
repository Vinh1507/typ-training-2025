const bcrypt = require('bcryptjs');

// Dữ liệu users mẫu (trong thực tế sẽ lưu trong database)
let users = [
  {
    id: 1,
    username: 'admin',
    email: 'admin@example.com',
    password: bcrypt.hashSync('admin123', 10), // Mật khẩu đã hash
    role: 'admin',
    createdAt: new Date()
  },
  {
    id: 2,
    username: 'user1',
    email: 'user1@example.com',
    password: bcrypt.hashSync('user123', 10),
    role: 'user',
    createdAt: new Date()
  }
];

// Refresh tokens (trong thực tế nên lưu trong database)
let refreshTokens = [];

module.exports = {
  users,
  refreshTokens
};

