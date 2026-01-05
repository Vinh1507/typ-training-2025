const bcrypt = require('bcryptjs');

// Dữ liệu users mẫu
let users = [
  {
    id: 1,
    username: 'admin',
    email: 'admin@example.com',
    password: bcrypt.hashSync('admin123', 10),
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

// Refresh tokens
let refreshTokens = [];

module.exports = {
  users,
  refreshTokens
};

