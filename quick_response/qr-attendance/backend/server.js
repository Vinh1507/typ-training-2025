const express = require('express');
const QRCode = require('qrcode');
const crypto = require('crypto');
const fs = require('fs');
const path = require('path');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

const attendanceLog = [];
let currentQRData = null;

const LOG_FILE = path.join(__dirname, 'data', 'attendance.log');

if (!fs.existsSync(path.join(__dirname, 'data'))) {
  fs.mkdirSync(path.join(__dirname, 'data'));
}

function generateQRToken() {
  return crypto.randomBytes(16).toString('hex');
}

function writeLog(entry) {
  const logEntry = `${new Date().toISOString()} | ${JSON.stringify(entry)}\n`;
  fs.appendFileSync(LOG_FILE, logEntry);
}

async function generateQRCode() {
  const token = generateQRToken();
  const QR_URL = process.env.QR_BASE_URL || `http://localhost:${PORT}`;
  const checkinURL = `${QR_URL}/checkin?token=${token}`;
  
  const qrImage = await QRCode.toDataURL(checkinURL, {
    width: 400,
    margin: 2,
    color: {
      dark: '#000000',
      light: '#FFFFFF'
    }
  });

  currentQRData = {
    token,
    url: checkinURL,
    qrImage,
    createdAt: Date.now(),
    expiry: Date.now() + 31 * 1000 // 31s
  };

  return currentQRData;
}

generateQRCode().then(() => {
  console.log('Initial QR code generated');
});

app.get('/api/qr', async (req, res) => {
  try {
    if (!currentQRData || Date.now() > currentQRData.expiry) {
      await generateQRCode();
    }
    res.json(currentQRData);
  } catch (err) {
    res.status(500).json({ error: 'Error generating QR code' });
  }
});

app.post('/api/qr/generate', async (req, res) => {
  try {
    const newQR = await generateQRCode();
    res.json(newQR);
  } catch (err) {
    res.status(500).json({ error: 'Error generating QR code' });
  }
});

app.get('/api/logs', (req, res) => {
  const limit = parseInt(req.query.limit) || 10;
  const logs = attendanceLog.slice(-limit).reverse();
  
  res.json({
    total: attendanceLog.length,
    logs: logs.map((log, idx) => ({
      id: attendanceLog.length - idx,
      ...log
    }))
  });
});

app.get('/api/stats', (req, res) => {
  const now = Date.now();
  const last24h = attendanceLog.filter(
    log => now - new Date(log.timestamp).getTime() < 86400000
  ).length;

  res.json({
    total: attendanceLog.length,
    today: last24h,
    qrValid: currentQRData && now < currentQRData.expiry
  });
});

app.post('/api/checkin', (req, res) => {
  const { token } = req.body;
  const clientIP = req.ip || req.connection.remoteAddress;
  const timestamp = new Date().toISOString();

  // Validate token
  if (!currentQRData || token !== currentQRData.token) {
    return res.status(400).json({ 
      success: false,
      error: 'Invalid token' 
    });
  }
  if (Date.now() > currentQRData.expiry) {
    return res.status(400).json({ 
      success: false,
      error: 'Token expired' 
    });
  }

  const entry = {
    timestamp,
    ip: clientIP,
    token,
    userAgent: req.get('user-agent') || 'Unknown'
  };

  attendanceLog.push(entry);
  writeLog(entry);

  console.log(`Check-in: ${timestamp} | IP: ${clientIP}`);

  res.json({
    success: true,
    message: 'Check-in successful',
    timestamp
  });
});

app.get('/qr', async (req, res) => {
  try {
    if (!currentQRData || Date.now() > currentQRData.expiry) {
      await generateQRCode();
    }
    
    res.send(`
      <!DOCTYPE html>
      <html>
      <head>
        <title>QR Code - Attendance</title>
        <style>
          body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          .container {
            background: white;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            text-align: center;
          }
          h1 { color: #333; margin-bottom: 10px; }
          .subtitle { color: #666; margin-bottom: 30px; }
          img {
            border: 3px solid #667eea;
            border-radius: 10px;
            padding: 10px;
            background: white;
          }
          .info {
            margin-top: 20px;
            padding: 15px;
            background: #f0f0f0;
            border-radius: 10px;
            font-size: 14px;
            color: #555;
          }
          .token {
            font-family: monospace;
            color: #667eea;
            font-weight: bold;
          }
        </style>
      </head>
      <body>
        <div class="container">
          <h1>Điểm danh QR Code</h1>
          <p class="subtitle">Quét mã để điểm danh</p>
          <img src="${currentQRData.qrImage}" alt="QR Code" />
          <div class="info">
            <p><strong>Token:</strong> <span class="token">${currentQRData.token}</span></p>
            <p><strong>URL:</strong> ${currentQRData.url}</p>
          </div>
        </div>
      </body>
      </html>
    `);
  } catch (err) {
    res.status(500).send('Error generating QR code');
  }
});

app.get('/checkin', (req, res) => {
  const { token } = req.query;
  const clientIP = req.ip || req.connection.remoteAddress;
  const timestamp = new Date().toISOString();

  // Validate token
  if (!currentQRData || token !== currentQRData.token) {
    return res.status(400).send(`
      <!DOCTYPE html>
      <html>
      <head>
        <title>Invalid Token</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          body {
            font-family: Arial, sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            background: #f44336;
          }
          .container {
            background: white;
            padding: 40px;
            border-radius: 20px;
            text-align: center;
          }
          h1 { color: #f44336; }
        </style>
      </head>
      <body>
        <div class="container">
          <h1>Token không hợp lệ</h1>
          <p>Vui lòng quét lại mã QR</p>
        </div>
      </body>
      </html>
    `);
  }

  // Check expiry
  if (Date.now() > currentQRData.expiry) {
    return res.status(400).send(`
      <!DOCTYPE html>
      <html>
      <head>
        <title>Expired Token</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          body {
            font-family: Arial, sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            margin: 0;
            background: #ff9800;
          }
          .container {
            background: white;
            padding: 40px;
            border-radius: 20px;
            text-align: center;
          }
          h1 { color: #ff9800; }
        </style>
      </head>
      <body>
        <div class="container">
          <h1>Mã QR đã hết hạn</h1>
          <p>Vui lòng quét mã mới</p>
        </div>
      </body>
      </html>
    `);
  }

  // Log attendance
  const entry = {
    timestamp,
    ip: clientIP,
    token,
    userAgent: req.get('user-agent') || 'Unknown'
  };

  attendanceLog.push(entry);
  writeLog(entry);

  console.log(`Check-in: ${timestamp} | IP: ${clientIP}`);

  // Success page
  res.send(`
    <!DOCTYPE html>
    <html>
    <head>
      <title>Điểm danh thành công</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <style>
        body {
          font-family: Arial, sans-serif;
          display: flex;
          align-items: center;
          justify-content: center;
          min-height: 100vh;
          margin: 0;
          background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }
        .container {
          background: white;
          padding: 40px;
          border-radius: 20px;
          box-shadow: 0 20px 60px rgba(0,0,0,0.3);
          text-align: center;
          max-width: 400px;
        }
        .checkmark {
          font-size: 80px;
          color: #11998e;
          margin-bottom: 20px;
          animation: pop 0.5s ease-out;
        }
        @keyframes pop {
          0% { transform: scale(0); }
          50% { transform: scale(1.2); }
          100% { transform: scale(1); }
        }
        h1 { color: #333; margin-bottom: 10px; }
        .info {
          margin-top: 20px;
          padding: 15px;
          background: #f0f0f0;
          border-radius: 10px;
          font-size: 14px;
          color: #555;
          text-align: left;
        }
        .info p { margin: 8px 0; }
      </style>
    </head>
    <body>
      <div class="container">
        <div class="checkmark"></div>
        <h1>Điểm danh thành công!</h1>
        <p>Bạn đã được ghi nhận tham dự</p>
        <div class="info">
          <p><strong>Thời gian:</strong> ${new Date(timestamp).toLocaleString('vi-VN')}</p>
          <p><strong>IP:</strong> ${clientIP}</p>
        </div>
      </div>
    </body>
    </html>
  `);
});

app.get('/logs', (req, res) => {
  res.json({
    total: attendanceLog.length,
    logs: attendanceLog
  });
});

app.get('/', (req, res) => {
  res.redirect('/qr');
});

app.listen(PORT, () => {
  console.log(`
QR Attendance Server running!
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
QR Code:  http://localhost:${PORT}/qr
API:      http://localhost:${PORT}/api/qr
Logs:     http://localhost:${PORT}/api/logs
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  `);
});