const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { users, refreshTokens } = require('../models/user.model');
const { sendEmailMessage } = require('../services/emailQueue.service');

const JWT_SECRET = process.env.JWT_SECRET || 'demo-secret-key';
const JWT_REFRESH_SECRET = process.env.JWT_REFRESH_SECRET || 'demo-refresh-secret-key';
const ACCESS_TOKEN_EXPIRY = '15m';
const REFRESH_TOKEN_EXPIRY = '7d';

// Đăng ký
const register = async (req, res) => {
  try {
    const { username, email, password, role = 'user' } = req.body;

    if (!username || !email || !password) {
      return res.status(400).json({
        success: false,
        message: 'Username, email và password là bắt buộc'
      });
    }

    const existingUser = users.find(u => u.email === email || u.username === username);
    if (existingUser) {
      return res.status(400).json({
        success: false,
        message: 'Email hoặc username đã tồn tại'
      });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newUser = {
      id: users.length + 1,
      username,
      email,
      password: hashedPassword,
      role: role === 'admin' ? 'admin' : 'user',
      createdAt: new Date()
    };

    users.push(newUser);

    // gui email thong bao (bat dong bo)
    sendEmailMessage(newUser.email, newUser.username, 'welcome')
      .then(result => {
        if (result.success) {
          console.log('[Auth] Email queued for:', newUser.email);
        }
      })
      .catch(err => {
        console.error('[Auth] Queue error:', err.message);
      });

    res.status(201).json({
      success: true,
      message: 'Đăng ký thành công',
      data: {
        id: newUser.id,
        username: newUser.username,
        email: newUser.email,
        role: newUser.role
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
};

// Đăng nhập
const login = async (req, res) => {
  try {
    const { email, password } = req.body;

    if (!email || !password) {
      return res.status(400).json({
        success: false,
        message: 'Email và password là bắt buộc'
      });
    }

    const user = users.find(u => u.email === email);
    if (!user) {
      return res.status(401).json({
        success: false,
        message: 'Email hoặc password không đúng'
      });
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      return res.status(401).json({
        success: false,
        message: 'Email hoặc password không đúng'
      });
    }

    const accessToken = jwt.sign(
      {
        userId: user.id,
        email: user.email,
        role: user.role
      },
      JWT_SECRET,
      { expiresIn: ACCESS_TOKEN_EXPIRY }
    );

    const refreshToken = jwt.sign(
      {
        userId: user.id,
        email: user.email
      },
      JWT_REFRESH_SECRET,
      { expiresIn: REFRESH_TOKEN_EXPIRY }
    );

    refreshTokens.push(refreshToken);

    res.json({
      success: true,
      message: 'Đăng nhập thành công',
      data: {
        user: {
          id: user.id,
          username: user.username,
          email: user.email,
          role: user.role
        },
        accessToken,
        refreshToken
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
};

// Verify token
const verifyToken = (req, res) => {
  try {
    const token = req.headers.authorization?.split(' ')[1];

    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Không có token'
      });
    }

    const decoded = jwt.verify(token, JWT_SECRET);

    res.json({
      success: true,
      message: 'Token hợp lệ',
      data: decoded
    });
  } catch (error) {
    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({
        success: false,
        message: 'Token đã hết hạn'
      });
    }
    if (error.name === 'JsonWebTokenError') {
      return res.status(401).json({
        success: false,
        message: 'Token không hợp lệ'
      });
    }
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
};

// Refresh token
const refreshAccessToken = (req, res) => {
  try {
    const { refreshToken } = req.body;

    if (!refreshToken) {
      return res.status(400).json({
        success: false,
        message: 'Refresh token là bắt buộc'
      });
    }

    if (!refreshTokens.includes(refreshToken)) {
      return res.status(403).json({
        success: false,
        message: 'Refresh token không hợp lệ'
      });
    }

    const decoded = jwt.verify(refreshToken, JWT_REFRESH_SECRET);
    const user = users.find(u => u.id === decoded.userId);
    
    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User không tồn tại'
      });
    }

    const newAccessToken = jwt.sign(
      {
        userId: user.id,
        email: user.email,
        role: user.role
      },
      JWT_SECRET,
      { expiresIn: ACCESS_TOKEN_EXPIRY }
    );

    res.json({
      success: true,
      message: 'Làm mới token thành công',
      data: {
        accessToken: newAccessToken
      }
    });
  } catch (error) {
    if (error.name === 'TokenExpiredError') {
      const index = refreshTokens.indexOf(req.body.refreshToken);
      if (index > -1) {
        refreshTokens.splice(index, 1);
      }
      return res.status(401).json({
        success: false,
        message: 'Refresh token đã hết hạn, vui lòng đăng nhập lại'
      });
    }
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
};

// Logout
const logout = (req, res) => {
  try {
    const { refreshToken } = req.body;

    if (refreshToken) {
      const index = refreshTokens.indexOf(refreshToken);
      if (index > -1) {
        refreshTokens.splice(index, 1);
      }
    }

    res.json({
      success: true,
      message: 'Đăng xuất thành công'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'Lỗi server',
      error: error.message
    });
  }
};

module.exports = {
  register,
  login,
  verifyToken,
  refreshAccessToken,
  logout
};

