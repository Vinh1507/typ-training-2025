const express = require('express');
const app = express();
const PORT = 3002;

app.get('/', (req, res) => {
  const currentTime = new Date();
  res.json({
    service: 'Service B',
    message: 'Current time from Service B',
    time: currentTime.toTimeString(),
    date: currentTime.toDateString(),
    timestamp: currentTime.toISOString(),
    hostname: process.env.HOSTNAME
  });
});

app.listen(PORT, () => {
  console.log(`Service B running on port ${PORT}`);
});
