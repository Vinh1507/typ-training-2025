const express = require('express');
const router = express.Router();
const authController = require('../controllers/auth.controller');
const { authenticateToken } = require('../middlewares/auth.middleware');

// Đăng ký
router.post('/register', authController.register);

// Đăng nhập
router.post('/login', authController.login);

// Kiểm tra JWT (cần token)
router.get('/verify', authenticateToken, authController.verifyToken);

// Refresh token
router.post('/refresh', authController.refreshAccessToken);

// Đăng xuất
router.post('/logout', authController.logout);

module.exports = router;

