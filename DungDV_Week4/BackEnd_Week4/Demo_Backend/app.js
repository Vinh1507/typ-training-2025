require('dotenv').config();
const express = require('express');
const bookRoutes = require('./src/routes/book.routes');
const authRoutes = require('./src/routes/auth.routes');
const bookProtectedRoutes = require('./src/routes/book.routes.protected');

const app = express();
const PORT = process.env.PORT || 3000;

// CORS middleware - Cho phép Frontend gọi API
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', 'http://localhost:5173');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization');
  if (req.method === 'OPTIONS') {
    return res.sendStatus(200);
  }
  next();
});

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ 
    message: 'Welcome to Library API with JWT Authentication - Demo Backend',
    endpoints: {
      public: {
        getAll: 'GET /api/books',
        getById: 'GET /api/books/:id'
      },
      auth: {
        register: 'POST /api/auth/register',
        login: 'POST /api/auth/login',
        verify: 'GET /api/auth/verify (requires token)',
        refresh: 'POST /api/auth/refresh',
        logout: 'POST /api/auth/logout'
      },
      protected: {
        getAll: 'GET /api/books-protected',
        getById: 'GET /api/books-protected/:id',
        create: 'POST /api/books-protected (admin only)',
        update: 'PUT /api/books-protected/:id (admin only)',
        delete: 'DELETE /api/books-protected/:id (admin only)'
      }
    },
    note: 'Data is stored in memory array. Will be lost after server restart!'
  });
});

// Public routes
app.use('/api/books', bookRoutes);

// Auth routes
app.use('/api/auth', authRoutes);

// Protected routes
app.use('/api/books-protected', bookProtectedRoutes);

app.use((req, res) => {
  res.status(404).json({ 
    success: false,
    message: 'Route not found' 
  });
});

app.listen(PORT, () => {
  console.log(`Backend Server running on http://localhost:${PORT}`);
  console.log(`API Documentation: http://localhost:${PORT}`);
  console.log(`Note: Data is stored in memory - will be lost after restart`);
});

module.exports = app;

