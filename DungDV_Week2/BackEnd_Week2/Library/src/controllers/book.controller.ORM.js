
const Book = require('../models/book.model.ORM');

const getAllBooks = async (req, res) => {
  try {
    const books = await Book.findAll({
      order: [['id', 'ASC']]
    });
    
    res.json({ 
      success: true, 
      count: books.length,
      data: books 
    });
  } catch (error) {
    res.status(500).json({ 
      success: false, 
      message: error.message 
    });
  }
};

const getBookById = async (req, res) => {
  try {
    const { id } = req.params;
    const book = await Book.findByPk(id);
    
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
  } catch (error) {
    res.status(500).json({ 
      success: false, 
      message: error.message 
    });
  }
};

const createBook = async (req, res) => {
  try {
    const { title, author, year } = req.body;
    
    if (!title || !author) {
      return res.status(400).json({
        success: false,
        message: 'Title and Author are required'
      });
    }
    
    const newBook = await Book.create({
      title,
      author,
      year: year || null
    });
    
    res.status(201).json({ 
      success: true, 
      message: 'Book created successfully',
      data: newBook 
    });
  } catch (error) {
    if (error.name === 'SequelizeValidationError') {
      return res.status(400).json({
        success: false,
        message: 'Validation error',
        errors: error.errors.map(e => e.message)
      });
    }
    
    res.status(500).json({ 
      success: false, 
      message: error.message 
    });
  }
};

const updateBook = async (req, res) => {
  try {
    const { id } = req.params;
    const { title, author, year } = req.body;
    
    const book = await Book.findByPk(id);
    
    if (!book) {
      return res.status(404).json({ 
        success: false, 
        message: 'Book not found' 
      });
    }
    
    await book.update({
      title: title || book.title,
      author: author || book.author,
      year: year !== undefined ? year : book.year
    });
    
    res.json({ 
      success: true, 
      message: 'Book updated successfully',
      data: book 
    });
  } catch (error) {
    if (error.name === 'SequelizeValidationError') {
      return res.status(400).json({
        success: false,
        message: 'Validation error',
        errors: error.errors.map(e => e.message)
      });
    }
    
    res.status(500).json({ 
      success: false, 
      message: error.message 
    });
  }
};

const deleteBook = async (req, res) => {
  try {
    const { id } = req.params;
    
    const book = await Book.findByPk(id);
    
    if (!book) {
      return res.status(404).json({ 
        success: false, 
        message: 'Book not found' 
      });
    }
    
    await book.destroy();
    
    res.json({ 
      success: true, 
      message: 'Book deleted successfully' 
    });
  } catch (error) {
    res.status(500).json({ 
      success: false, 
      message: error.message 
    });
  }
};

module.exports = {
  getAllBooks,
  getBookById,
  createBook,
  updateBook,
  deleteBook
};

