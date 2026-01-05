const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'demo-secret-key';

const authenticateToken = (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Không có token, vui lòng đăng nhập'
      });
    }

    jwt.verify(token, JWT_SECRET, (err, decoded) => {
      if (err) {
        if (err.name === 'TokenExpiredError') {
          return res.status(401).json({
            success: false,
            message: 'Token đã hết hạn'
          });
        }
        return res.status(403).json({
          success: false,
          message: 'Token không hợp lệ'
        });
      }

      req.user = decoded;
      next();
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Lỗi xác thực',
      error: error.message
    });
  }
};

const authorize = (...roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: 'Chưa xác thực'
      });
    }

    if (!roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        message: 'Bạn không có quyền truy cập tài nguyên này'
      });
    }

    next();
  };
};

module.exports = {
  authenticateToken,
  authorize
};

