const express = require("express");
const axios = require("axios");
const app = express();
const PORT = 3000;

app.get("/", (req, res) => {
  res.send(`
    <!DOCTYPE html>
    <html>
    <head>
      <title>Microservice Demo</title>
      <style>
        body {
          font-family: Arial, sans-serif;
          max-width: 800px;
          margin: 50px auto;
          padding: 20px;
          background-color: #f0f0f0;
        }
        .container {
          background-color: white;
          padding: 30px;
          border-radius: 10px;
          box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
          color: #333;
          text-align: center;
        }
        .button-container {
          display: flex;
          gap: 20px;
          justify-content: center;
          margin: 30px 0;
        }
        button {
          padding: 15px 30px;
          font-size: 16px;
          border: none;
          border-radius: 5px;
          cursor: pointer;
          transition: all 0.3s;
        }
        .btn-a {
          background-color: #4CAF50;
          color: white;
        }
        .btn-a:hover {
          background-color: #45a049;
        }
        .btn-b {
          background-color: #2196F3;
          color: white;
        }
        .btn-b:hover {
          background-color: #0b7dda;
        }
        #result {
          margin-top: 30px;
          padding: 20px;
          background-color: #f9f9f9;
          border-radius: 5px;
          border: 1px solid #ddd;
          min-height: 50px;
        }
        .loading {
          color: #666;
          font-style: italic;
        }
        .error {
          color: #d32f2f;
        }
        .success {
          color: #388e3c;
        }
      </style>
    </head>
    <body>
      <div class="container">
        <h1>Microservice Demo</h1>
        <p style="text-align: center; color: #666;">Click a button to call a microservice</p>
        
        <div class="button-container">
          <button class="btn-a" onclick="callServiceA()">Call Service A</button>
          <button class="btn-b" onclick="callServiceB()">Call Service B</button>
        </div>
        
        <div id="result"></div>
      </div>
      
      <script>
        async function callServiceA() {
          const resultDiv = document.getElementById('result');
          resultDiv.innerHTML = '<p class="loading">Calling Service A...</p>';
          
          try {
            const response = await fetch('/api/service-a');
            const data = await response.json();
            resultDiv.innerHTML = '<p class="success"><strong>Service A Response:</strong><br>' + JSON.stringify(data, null, 2) + '</p>';
          } catch (error) {
            resultDiv.innerHTML = '<p class="error">Error calling Service A: ' + error.message + '</p>';
          }
        }
        
        async function callServiceB() {
          const resultDiv = document.getElementById('result');
          resultDiv.innerHTML = '<p class="loading">Calling Service B...</p>';
          
          try {
            const response = await fetch('/api/service-b');
            const data = await response.json();
            resultDiv.innerHTML = '<p class="success"><strong>Service B Response:</strong><br>' + JSON.stringify(data, null, 2) + '</p>';
          } catch (error) {
            resultDiv.innerHTML = '<p class="error">Error calling Service B: ' + error.message + '</p>';
          }
        }
      </script>
    </body>
    </html>
  `);
});

app.get("/api/service-a", async (req, res) => {
  try {
    const response = await axios.get("http://service-a:3001/");
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error: "Failed to call Service A" });
  }
});

app.get("/api/service-b", async (req, res) => {
  try {
    const response = await axios.get("http://service-b:3002/");
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error: "Failed to call Service B" });
  }
});

app.listen(PORT, () => {
  console.log(`Frontend service running on port ${PORT}`);
});
