const cache = require('../services/cache.service');
const productModel = require('../models/product.model');

// lay tat ca products
async function getAll(req, res) {
  const cacheKey = 'products:all';
  
  // check cache
  const cached = await cache.get(cacheKey);
  if (cached) {
    console.log('[Product] Cache hit:', cacheKey);
    return res.json({ data: cached, source: 'cache' });
  }
  
  // cache miss
  console.log('[Product] Cache miss:', cacheKey);
  const products = productModel.getAll();
  
  // luu cache 5 phut
  await cache.set(cacheKey, products, 300);
  
  res.json({ data: products, source: 'db' });
}

// lay product theo id
async function getById(req, res) {
  const { id } = req.params;
  const cacheKey = `product:${id}`;
  
  // check cache
  const cached = await cache.get(cacheKey);
  if (cached) {
    console.log('[Product] Cache hit:', cacheKey);
    return res.json({ data: cached, source: 'cache' });
  }
  
  console.log('[Product] Cache miss:', cacheKey);
  const product = productModel.getById(id);
  
  if (!product) {
    // cache null de tranh cache penetration
    await cache.set(cacheKey, null, 60);
    return res.status(404).json({ message: 'Product not found' });
  }
  
  // random TTL de tranh cache avalanche (5-6 phut)
  const ttl = 300 + Math.floor(Math.random() * 60);
  await cache.set(cacheKey, product, ttl);
  
  res.json({ data: product, source: 'db' });
}

// lay products theo category
async function getByCategory(req, res) {
  const { category } = req.params;
  const cacheKey = `products:category:${category}`;
  
  const cached = await cache.get(cacheKey);
  if (cached) {
    console.log('[Product] Cache hit:', cacheKey);
    return res.json({ data: cached, source: 'cache' });
  }
  
  console.log('[Product] Cache miss:', cacheKey);
  const products = productModel.getByCategory(category);
  
  await cache.set(cacheKey, products, 300);
  
  res.json({ data: products, source: 'db' });
}

// cap nhat product - invalidate cache
async function update(req, res) {
  const { id } = req.params;
  const data = req.body;
  
  const product = productModel.update(id, data);
  
  if (!product) {
    return res.status(404).json({ message: 'Product not found' });
  }
  
  // invalidate cache
  await cache.del(`product:${id}`);
  await cache.del('products:all');
  
  // xoa cache category neu co
  if (product.category) {
    await cache.del(`products:category:${product.category}`);
  }
  
  console.log('[Product] Cache invalidated for id:', id);
  
  res.json({ data: product, message: 'Updated and cache cleared' });
}

// xem cache info
async function getCacheInfo(req, res) {
  const { id } = req.params;
  const cacheKey = `product:${id}`;
  
  const exists = await cache.exists(cacheKey);
  const ttl = await cache.ttl(cacheKey);
  
  res.json({
    key: cacheKey,
    exists: exists === 1,
    ttl: ttl
  });
}

module.exports = { getAll, getById, getByCategory, update, getCacheInfo };

