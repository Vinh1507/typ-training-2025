const express = require('express');
const router = express.Router();
const bookController = require('../controllers/book.controller.protected');
const { authenticateToken, authorize } = require('../middlewares/auth.middleware');

router.use(authenticateToken);

router.get('/', bookController.getAllBooks);
router.get('/:id', bookController.getBookById);
router.post('/', authorize('admin'), bookController.createBook);
router.put('/:id', authorize('admin'), bookController.updateBook);
router.delete('/:id', authorize('admin'), bookController.deleteBook);

module.exports = router;

