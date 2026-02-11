const express = require('express');
const bookRoutes = require('./src/routes/book.routes');

const app = express();
const PORT = 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ 
    message: 'Welcome to Library API (Before ORM - Using Array)',
    endpoints: {
      getAll: 'GET /api/books',
      getById: 'GET /api/books/:id',
      create: 'POST /api/books',
      update: 'PUT /api/books/:id',
      delete: 'DELETE /api/books/:id'
    },
    note: 'Data is stored in memory array. Will be lost after server restart!'
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
  console.log(`Using memory array - data will be lost after restart`);
});

module.exports = app;


