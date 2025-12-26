import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom'; 

createRoot(document.getElementById('root')).render(
  <StrictMode>

    <App />

  </StrictMode>,
)
