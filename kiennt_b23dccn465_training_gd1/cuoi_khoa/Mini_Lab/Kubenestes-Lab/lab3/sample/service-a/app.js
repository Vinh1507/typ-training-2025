const express = require('express');
const app = express();
const PORT = 3001;

app.get('/', (req, res) => {
  res.json({
    service: 'Service A',
    message: 'Hello from Service A!',
    timestamp: new Date().toISOString(),
    hostname: process.env.HOSTNAME
  });
});

app.listen(PORT, () => {
  console.log(`Service A running on port ${PORT}`);
});
