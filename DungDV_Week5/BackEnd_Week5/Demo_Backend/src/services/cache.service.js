const { client } = require('../config/redis');

// get va tu dong parse JSON
async function get(key) {
  try {
    const data = await client.get(key);
    if (!data) return null;
    
    try {
      return JSON.parse(data);
    } catch {
      return data;
    }
  } catch (err) {
    console.log('[Cache] Get error:', err.message);
    return null;
  }
}

// set voi TTL (seconds), default 1 gio
async function set(key, value, ttl = 3600) {
  try {
    const data = typeof value === 'object' ? JSON.stringify(value) : String(value);
    
    if (ttl) {
      await client.setEx(key, ttl, data);
    } else {
      await client.set(key, data);
    }
    return true;
  } catch (err) {
    console.log('[Cache] Set error:', err.message);
    return false;
  }
}

// xoa key
async function del(key) {
  try {
    await client.del(key);
    return true;
  } catch (err) {
    console.log('[Cache] Del error:', err.message);
    return false;
  }
}

// xoa nhieu key theo pattern
async function delPattern(pattern) {
  try {
    const keys = await client.keys(pattern);
    if (keys.length > 0) {
      await client.del(keys);
    }
    return keys.length;
  } catch (err) {
    console.log('[Cache] DelPattern error:', err.message);
    return 0;
  }
}

// check key ton tai
async function exists(key) {
  try {
    return await client.exists(key);
  } catch (err) {
    return false;
  }
}

// lay TTL con lai
async function ttl(key) {
  try {
    return await client.ttl(key);
  } catch (err) {
    return -1;
  }
}

module.exports = { get, set, del, delPattern, exists, ttl };

