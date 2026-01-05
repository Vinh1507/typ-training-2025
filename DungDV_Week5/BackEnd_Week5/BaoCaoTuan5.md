# Caching & Redis

## 1. Caching là gì?

Caching là kỹ thuật lưu tạm dữ liệu vào bộ nhớ nhanh (RAM) để truy xuất nhanh hơn thay vì phải query từ database mỗi lần.

Ví dụ: user request thông tin sản phẩm, thay vì query DB (chậm), ta check cache trước. Nếu có thì trả về luôn, không có thì mới query DB rồi lưu vào cache.

```
Request -> Check Cache -> Có? -> Trả về
                      -> Không? -> Query DB -> Lưu cache -> Trả về
```

## Tại sao cần Caching/Redis?

- **Nhanh hơn**: Redis lưu trên RAM, đọc/ghi nhanh hơn DB rất nhiều (microseconds vs milliseconds)
- **Giảm tải DB**: nhiều request giống nhau thì chỉ cần query DB 1 lần
- **Scalable**: có thể scale Redis cluster để xử lý nhiều request hơn

Khi nào nên dùng:
- Dữ liệu đọc nhiều, ít thay đổi (product info, user profile...)
- Dữ liệu tính toán mất thời gian (aggregations, rankings...)
- Session storage

## Các chiến lược Caching

### Cache-Aside (Lazy Loading)

Phổ biến nhất. App tự quản lý cache:

```
1. Check cache
2. Nếu miss -> query DB -> lưu cache
3. Nếu hit -> trả về từ cache
```

Ưu điểm: đơn giản, chỉ cache dữ liệu được request
Nhược điểm: request đầu tiên chậm (cold start)

### Write-Through

Ghi vào cache và DB cùng lúc:

```
1. Ghi vào cache
2. Cache ghi vào DB
```

Ưu điểm: data luôn consistent
Nhược điểm: chậm khi write

### Write-Behind (Write-Back)

Ghi vào cache trước, sau đó async ghi vào DB:

```
1. Ghi vào cache
2. Sau 1 thời gian -> batch write vào DB
```

Ưu điểm: write nhanh
Nhược điểm: có thể mất data nếu cache die trước khi sync

### Read-Through

Tương tự cache-aside nhưng cache tự động load từ DB khi miss.

---

## 2. Redis Data Types

### String

Kiểu cơ bản nhất, lưu text hoặc number:

```
SET user:1 "john"
GET user:1
SETEX session:abc 3600 "user_data"  // có TTL
```

### Hash

Lưu object, giống map/dictionary:

```
HSET user:1 name "john" age 25
HGET user:1 name
HGETALL user:1
```

### List

Danh sách, có thể dùng làm queue:

```
LPUSH queue:email "msg1"
RPOP queue:email
LRANGE queue:email 0 -1
```

### Set

Tập hợp, không trùng lặp:

```
SADD tags:post1 "nodejs" "redis"
SMEMBERS tags:post1
SISMEMBER tags:post1 "nodejs"
```

### Sorted Set

Set có score, dùng cho ranking/leaderboard:

```
ZADD leaderboard 100 "player1" 200 "player2"
ZREVRANGE leaderboard 0 9  // top 10
ZSCORE leaderboard "player1"
```

---

## 3. Implement

### Cấu trúc project

```
Demo_Backend/
├── src/
│   ├── config/
│   │   └── redis.js        // kết nối redis
│   ├── services/
│   │   └── cache.service.js // helper functions
│   ├── controllers/
│   │   ├── product.controller.js
│   │   └── leaderboard.controller.js
│   └── routes/
├── app.js
└── package.json
```

### Kết nối Redis

```javascript
// config/redis.js
const redis = require('redis');

const client = redis.createClient({
  url: process.env.REDIS_URL || 'redis://localhost:6379'
});

client.on('error', err => console.log('Redis error:', err));
client.on('connect', () => console.log('Redis connected'));

module.exports = client;
```

### Cache Service

```javascript
// services/cache.service.js
const redis = require('../config/redis');

// get với auto parse JSON
async function get(key) {
  const data = await redis.get(key);
  if (!data) return null;
  try {
    return JSON.parse(data);
  } catch {
    return data;
  }
}

// set với TTL (seconds)
async function set(key, value, ttl = 3600) {
  const data = typeof value === 'object' ? JSON.stringify(value) : value;
  if (ttl) {
    await redis.setEx(key, ttl, data);
  } else {
    await redis.set(key, data);
  }
}

async function del(key) {
  await redis.del(key);
}

module.exports = { get, set, del };
```

### Use Case 1: Cache Product Info

```javascript
// controllers/product.controller.js
const cache = require('../services/cache.service');
const products = require('../models/product.model');

async function getProduct(req, res) {
  const { id } = req.params;
  const cacheKey = `product:${id}`;
  
  // check cache
  const cached = await cache.get(cacheKey);
  if (cached) {
    console.log('Cache hit:', cacheKey);
    return res.json({ data: cached, source: 'cache' });
  }
  
  // cache miss - query "DB"
  console.log('Cache miss:', cacheKey);
  const product = products.find(p => p.id === parseInt(id));
  
  if (!product) {
    // cache null để tránh cache penetration
    await cache.set(cacheKey, null, 60);
    return res.status(404).json({ message: 'Not found' });
  }
  
  // lưu cache với TTL 5 phút
  await cache.set(cacheKey, product, 300);
  
  res.json({ data: product, source: 'db' });
}
```

### Use Case 2: Leaderboard (Sorted Set)

```javascript
// controllers/leaderboard.controller.js
const redis = require('../config/redis');

const LEADERBOARD_KEY = 'game:leaderboard';

// thêm/cập nhật score
async function updateScore(req, res) {
  const { playerId, score } = req.body;
  
  await redis.zAdd(LEADERBOARD_KEY, {
    score: score,
    value: playerId
  });
  
  res.json({ message: 'Score updated' });
}

// lấy top N players
async function getTopPlayers(req, res) {
  const limit = parseInt(req.query.limit) || 10;
  
  const top = await redis.zRangeWithScores(
    LEADERBOARD_KEY,
    0,
    limit - 1,
    { REV: true }
  );
  
  const result = top.map((item, index) => ({
    rank: index + 1,
    playerId: item.value,
    score: item.score
  }));
  
  res.json({ data: result });
}

// lấy rank của player
async function getPlayerRank(req, res) {
  const { playerId } = req.params;
  
  const rank = await redis.zRevRank(LEADERBOARD_KEY, playerId);
  const score = await redis.zScore(LEADERBOARD_KEY, playerId);
  
  if (rank === null) {
    return res.status(404).json({ message: 'Player not found' });
  }
  
  res.json({
    playerId,
    rank: rank + 1,
    score
  });
}
```

---

## 4. Lưu ý khi dùng Cache

### TTL (Time to Live)

Đặt thời gian hết hạn phù hợp:
- Dữ liệu thay đổi thường xuyên: TTL ngắn (1-5 phút)
- Dữ liệu ít thay đổi: TTL dài (1 giờ - 1 ngày)
- Session: theo thời gian session

```javascript
// random TTL để tránh cache avalanche
const baseTTL = 300;
const randomTTL = baseTTL + Math.floor(Math.random() * 60);
await cache.set(key, data, randomTTL);
```

### Cache Invalidation

Xóa cache khi dữ liệu thay đổi:

```javascript
async function updateProduct(id, data) {
  await db.update(id, data);
  await cache.del(`product:${id}`);  // xóa cache
}
```

### Cache Avalanche

Nhiều cache hết hạn cùng lúc -> request dồn vào DB.

Cách xử lý:
- Random TTL (không set cùng 1 giá trị)
- Warm up cache trước khi hết hạn
- Rate limiting

### Cache Penetration

Request dữ liệu không tồn tại -> luôn miss cache -> query DB vô ích.

Cách xử lý:
- Cache null value với TTL ngắn
- Bloom filter để check trước

```javascript
if (!data) {
  // cache null 1 phút
  await cache.set(key, null, 60);
  return res.status(404).json({ message: 'Not found' });
}
```

### Cache Stampede

Nhiều request cùng lúc khi cache miss -> tất cả đều query DB.

Cách xử lý:
- Mutex/lock: chỉ 1 request được query DB
- Cache warming: refresh trước khi hết hạn
