// backend/index.js
const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 8081;

app.use(cors()); // Cho phép request từ frontend
app.use(express.json());

const books = [
  { id: 1, name: 'Book A' },
  { id: 2, name: 'Book B' },
  { id: 3, name: 'Book C' },
];

app.get('/api/books', (req, res) => {
  res.json(books);
});

app.listen(PORT, () => {
  console.log(`Backend chạy tại http://localhost:${PORT}`);
});
