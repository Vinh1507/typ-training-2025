require('dotenv').config();
const express = require('express');
const bookRoutes = require('./src/routes/book.routes.ORM');

const app = express();
const PORT = process.env.PORT || 3000;

const sequelize = require('./config/database');

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ 
    message: 'Welcome to Library API (After ORM - Using Sequelize + MySQL)',
    endpoints: {
      getAll: 'GET /api/books',
      getById: 'GET /api/books/:id',
      create: 'POST /api/books',
      update: 'PUT /api/books/:id',
      delete: 'DELETE /api/books/:id'
    },
    note: 'Data is stored in MySQL database. Will persist after server restart!'
  });
});

app.use('/api/books', bookRoutes);

app.use((req, res) => {
  res.status(404).json({ 
    success: false,
    message: 'Route not found' 
  });
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
  console.log(`Database: ${process.env.DB_NAME || 'library_db'}`);
});

module.exports = app;


