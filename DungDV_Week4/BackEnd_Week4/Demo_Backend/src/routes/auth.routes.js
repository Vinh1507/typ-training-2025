const express = require('express');
const router = express.Router();
const authController = require('../controllers/auth.controller');
const { authenticateToken } = require('../middlewares/auth.middleware');

router.post('/register', authController.register);
router.post('/login', authController.login);
router.get('/verify', authenticateToken, authController.verifyToken);
router.post('/refresh', authController.refreshAccessToken);
router.post('/logout', authController.logout);

module.exports = router;

