// du lieu mau - trong thuc te se la database
const products = [
  { id: 1, name: 'iPhone 15', price: 25000000, category: 'phone', stock: 50 },
  { id: 2, name: 'Samsung S24', price: 22000000, category: 'phone', stock: 30 },
  { id: 3, name: 'MacBook Pro', price: 45000000, category: 'laptop', stock: 20 },
  { id: 4, name: 'Dell XPS 15', price: 35000000, category: 'laptop', stock: 15 },
  { id: 5, name: 'AirPods Pro', price: 5000000, category: 'accessory', stock: 100 },
  { id: 6, name: 'Apple Watch', price: 12000000, category: 'watch', stock: 40 },
  { id: 7, name: 'iPad Pro', price: 28000000, category: 'tablet', stock: 25 },
  { id: 8, name: 'Sony WH-1000XM5', price: 8000000, category: 'accessory', stock: 35 }
];

function getAll() {
  return products;
}

function getById(id) {
  return products.find(p => p.id === parseInt(id));
}

function getByCategory(category) {
  return products.filter(p => p.category === category);
}

function update(id, data) {
  const index = products.findIndex(p => p.id === parseInt(id));
  if (index === -1) return null;
  
  products[index] = { ...products[index], ...data };
  return products[index];
}

module.exports = { products, getAll, getById, getByCategory, update };

