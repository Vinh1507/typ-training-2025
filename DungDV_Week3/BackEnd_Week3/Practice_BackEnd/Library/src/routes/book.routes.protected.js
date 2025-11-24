const express = require('express');
const router = express.Router();
const bookController = require('../controllers/book.controller.protected');
const { authenticateToken, authorize } = require('../middlewares/auth.middleware');

// Tất cả routes đều cần đăng nhập
router.use(authenticateToken);

// User và Admin đều có thể xem
router.get('/', bookController.getAllBooks);
router.get('/:id', bookController.getBookById);

// Chỉ Admin mới có thể tạo, sửa, xóa
router.post('/', authorize('admin'), bookController.createBook);
router.put('/:id', authorize('admin'), bookController.updateBook);
router.delete('/:id', authorize('admin'), bookController.deleteBook);

module.exports = router;

