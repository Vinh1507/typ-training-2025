const jwt = require('jsonwebtoken');

const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key-change-in-production';

// 4. Middleware xác thực JWT
const authenticateToken = (req, res, next) => {
  try {
    // Lấy token từ header
    const authHeader = req.headers.authorization;
    const token = authHeader && authHeader.split(' ')[1]; // Format: "Bearer TOKEN"

    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Không có token, vui lòng đăng nhập'
      });
    }

    // Verify token
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

      // Lưu thông tin user vào request để dùng ở các route tiếp theo
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

// 5. Middleware phân quyền dựa trên role
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

