require('dotenv').config();
const express = require('express');
const bookRoutes = require('./src/routes/book.routes');
const authRoutes = require('./src/routes/auth.routes');
const bookProtectedRoutes = require('./src/routes/book.routes.protected');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ 
    message: 'Welcome to Library API with JWT Authentication',
    endpoints: {
      // Public endpoints
      public: {
        getAll: 'GET /api/books',
        getById: 'GET /api/books/:id'
      },
      // Auth endpoints
      auth: {
        register: 'POST /api/auth/register',
        login: 'POST /api/auth/login',
        verify: 'GET /api/auth/verify (requires token)',
        refresh: 'POST /api/auth/refresh',
        logout: 'POST /api/auth/logout'
      },
      // Protected endpoints (requires token)
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

// Public routes (không cần đăng nhập)
app.use('/api/books', bookRoutes);

// Auth routes
app.use('/api/auth', authRoutes);

// Protected routes (cần đăng nhập)
app.use('/api/books-protected', bookProtectedRoutes);

app.use((req, res) => {
  res.status(404).json({ 
    success: false,
    message: 'Route not found' 
  });
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
  console.log(`Using memory array - data will be lost after restart`);
});

module.exports = app;


