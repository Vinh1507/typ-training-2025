const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.static('.')); // phục vụ index.html và app.js

app.listen(PORT, () => {
  console.log(`Frontend chạy tại http://localhost:${PORT}`);
});
