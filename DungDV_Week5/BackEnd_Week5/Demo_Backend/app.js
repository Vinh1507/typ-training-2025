require('dotenv').config();
const express = require('express');
const { connect } = require('./src/config/redis');
const productRoutes = require('./src/routes/product.routes');
const leaderboardRoutes = require('./src/routes/leaderboard.routes');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// cors
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  if (req.method === 'OPTIONS') return res.sendStatus(200);
  next();
});

app.get('/', (req, res) => {
  res.json({
    message: 'Redis Cache Demo API',
    endpoints: {
      products: {
        getAll: 'GET /api/products',
        getById: 'GET /api/products/:id',
        getByCategory: 'GET /api/products/category/:category',
        getCacheInfo: 'GET /api/products/:id/cache-info',
        update: 'PUT /api/products/:id'
      },
      leaderboard: {
        getTop: 'GET /api/leaderboard/top?limit=10',
        getPlayer: 'GET /api/leaderboard/player/:playerId',
        getAround: 'GET /api/leaderboard/around/:playerId?range=2',
        updateScore: 'POST /api/leaderboard/score',
        seed: 'POST /api/leaderboard/seed',
        reset: 'DELETE /api/leaderboard/reset'
      }
    }
  });
});

app.use('/api/products', productRoutes);
app.use('/api/leaderboard', leaderboardRoutes);

app.use((req, res) => {
  res.status(404).json({ message: 'Route not found' });
});

// connect redis truoc khi start server
connect().then(() => {
  app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
  });
});

