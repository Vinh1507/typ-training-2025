
const books = require('../models/book.model');

const getAllBooks = (req, res) => {
  res.json({ 
    success: true, 
    count: books.length,
    data: books 
  });
};

const getBookById = (req, res) => {
  const { id } = req.params;
  const book = books.find(b => b.id === parseInt(id));
  
  if (!book) {
    return res.status(404).json({ 
      success: false, 
      message: 'Book not found' 
    });
  }
  
  res.json({ 
    success: true, 
    data: book 
  });
};

const createBook = (req, res) => {
  const { title, author, year } = req.body;
  
  if (!title || !author) {
    return res.status(400).json({
      success: false,
      message: 'Title and Author are required'
    });
  }
  
  const newBook = {
    id: books.length + 1,
    title,
    author,
    year: year || null
  };
  
  books.push(newBook);
  
  res.status(201).json({ 
    success: true, 
    message: 'Book created successfully',
    data: newBook 
  });
};

const updateBook = (req, res) => {
  const { id } = req.params;
  const { title, author, year } = req.body;
  
  const bookIndex = books.findIndex(b => b.id === parseInt(id));
  
  if (bookIndex === -1) {
    return res.status(404).json({ 
      success: false, 
      message: 'Book not found' 
    });
  }
  
  books[bookIndex] = {
    id: parseInt(id),
    title: title || books[bookIndex].title,
    author: author || books[bookIndex].author,
    year: year !== undefined ? year : books[bookIndex].year
  };
  
  res.json({ 
    success: true, 
    message: 'Book updated successfully',
    data: books[bookIndex] 
  });
};

const deleteBook = (req, res) => {
  const { id } = req.params;
  const bookIndex = books.findIndex(b => b.id === parseInt(id));
  
  if (bookIndex === -1) {
    return res.status(404).json({ 
      success: false, 
      message: 'Book not found' 
    });
  }
  
  books.splice(bookIndex, 1);
  
  res.json({ 
    success: true, 
    message: 'Book deleted successfully' 
  });
};

module.exports = {
  getAllBooks,
  getBookById,
  createBook,
  updateBook,
  deleteBook
};

