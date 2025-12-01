// 1. Imports
import React, { useState } from 'react'; // 'useState' will store what the user types
import axios from 'axios';               // 'axios' will call your backend
import { QRCode } from 'antd';

// 2. Define where your Spring Boot backend is running
const API_URL = 'http://localhost:8080/api/login';

// 3. This is your main App component
function App() {
  
  // 4. Create a 'state' to hold the username
  const [username, setUsername] = useState('');

  // 5. Create a 'state' to hold the data we get back from the backend
  const [qrData, setQrData] = useState(null);

  /**
   * 6. This function runs when the user clicks the "Login" button
   */
  const handleLogin = async () => {
    if (!username) {
      alert('Please enter a username');
      return;
    }

    try {
      // 7. Send the username to your Spring Boot controller
      // This makes a POST request to http://localhost:8080/api/login/initiate
      const response = await axios.post(`${API_URL}/initiate`, {
        username: username // This becomes the 'payload' Map in your controller
      });

      // 8. If successful, log it and save the response data
      console.log('Backend response:', response.data);
      setQrData(response.data); // Save the {qrUrl, tx} object

    } catch (error) {
      console.error('Error calling backend:', error);
      alert('Failed to connect to backend. Is it running?');
    }
  };

  // 9. This is the HTML (JSX) to display
  return (
    <div style={{ padding: '50px', textAlign: 'center' }}>
      <h1>QR Code Login</h1>

      {
        /* 10. This is a basic "if/else" in React */
        
        /* IF we have NOT logged in (qrData is null)... */
        !qrData ? (
          <div>
            <p>Enter your username to begin:</p>
            <input 
              type="text"
              placeholder="Your username"
              value={username}
              onChange={(e) => setUsername(e.target.value)} // Update state on every key-press
              style={{ padding: '10px', fontSize: '16px' }}
            />
            <button 
              onClick={handleLogin} // Call our function on click
              style={{ padding: '10px', fontSize: '16px', marginLeft: '10px' }}
            >
              Get Login Code
            </button>
          </div>
        ) : (
          /* ELSE (if qrData EXISTS)... show the data we got back */
          <div>
            <h2>Login initiated for: {username}</h2>
            <p>We received a QR URL from the backend:</p>
            <div>
              <QRCode
                value={qrData.qrUrl}
                size={256}
              />
            </div>
            <p>(Next step: we will turn this text into a real QR code)</p>
          </div>
        )
      }

    </div>
  );
}

export default App;