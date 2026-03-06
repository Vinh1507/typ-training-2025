import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import { message } from 'antd'
import 'antd/dist/reset.css';
import { App as AntApp } from 'antd'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <AntApp>
        <App />
      </AntApp>
    </BrowserRouter>
  </StrictMode>,
)

// Configure global antd message (ensures visibility and consistent behavior)
message.config({ top: 64, duration: 2, maxCount: 1, zIndex: 9999 })
