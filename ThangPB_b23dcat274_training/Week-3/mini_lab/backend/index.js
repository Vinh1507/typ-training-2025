const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");
const bodyParser = require("body-parser");

const app = express();
app.use(cors());
app.use(bodyParser.json());

// MySQL connection config
const dbConfig = {
  host: process.env.DB_HOST || "db",
  user: process.env.DB_USER || "appuser",
  password: process.env.DB_PASSWORD || "pass123",
  database: process.env.DB_NAME || "appdb",
  port: 3306,
};

const MAX_RETRIES = 10;   // số lần thử kết nối
const RETRY_DELAY = 3000; // ms, 3 giây
let attempt = 0;

let db; // sẽ gắn connection vào biến này

function connectWithRetry() {
  attempt++;
  db = mysql.createConnection(dbConfig);

  db.connect((err) => {
    if (err) {
      console.error(`Attempt ${attempt} - Database connection failed:`, err.message);
      if (attempt < MAX_RETRIES) {
        console.log(`Retrying in ${RETRY_DELAY / 1000} seconds...`);
        setTimeout(connectWithRetry, RETRY_DELAY);
      } else {
        console.error("All attempts failed. Exiting.");
        process.exit(1);
      }
    } else {
      console.log("Connected to MySQL!");

      // Create table if not exists
      db.query(
        `CREATE TABLE IF NOT EXISTS apptb (
          id INT AUTO_INCREMENT PRIMARY KEY,
          name VARCHAR(255) NOT NULL
        )`
      );
    }
  });
}

// Bắt đầu kết nối
connectWithRetry();

// Routes

app.get("/users", (req, res) => {
  db.query("SELECT * FROM apptb", (err, results) => {
    if (err) return res.status(500).send(err);
    res.json(results);
  });
});

app.post("/users", (req, res) => {
  const { name } = req.body;
  db.query("INSERT INTO apptb (name) VALUES (?)", [name], (err, result) => {
    if (err) return res.status(500).send(err);
    res.json({ id: result.insertId, name });
  });
});

app.put("/users/:id", (req, res) => {
  const { id } = req.params;
  const { name } = req.body;
  db.query("UPDATE apptb SET name=? WHERE id=?", [name, id], (err) => {
    if (err) return res.status(500).send(err);
    res.json({ id, name });
  });
});

app.delete("/users/:id", (req, res) => {
  const { id } = req.params;
  db.query("DELETE FROM apptb WHERE id=?", [id], (err) => {
    if (err) return res.status(500).send(err);
    res.json({ message: "Deleted successfully" });
  });
});

app.listen(3001, () => console.log("Backend running on port 3001"));