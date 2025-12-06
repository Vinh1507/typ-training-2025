import { useContext } from 'react'
import { ThemeContext } from '../contexts/ThemeContext'

function DeepChild() {
  const { theme, toggleTheme } = useContext(ThemeContext)
  
  return (
    <div className="card" style={{ 
      background: theme === 'light' ? '#fff' : '#333',
      color: theme === 'light' ? '#333' : '#fff'
    }}>
      <h3>Deep Child (cap 3)</h3>
      <p>Theme: {theme}</p>
      <p>Nhan theme tu Context, ko can props</p>
      <button className="button" onClick={toggleTheme}>Doi Theme</button>
    </div>
  )
}

function MiddleComponent() {
  return (
    <div className="card" style={{ background: '#f5f5f5' }}>
      <h3>Middle (cap 2)</h3>
      <p>Component nay ko can props theme</p>
      <DeepChild />
    </div>
  )
}

function UseContextDemo() {
  const { theme } = useContext(ThemeContext)
  
  return (
    <div className="section">
      <h2>6. useContext</h2>
      
      <div className="card">
        <h3>Context API</h3>
        <p>Theme: {theme}</p>
        <p style={{ fontSize: '12px' }}>
          Theme truyen tu Provider xuong DeepChild ma ko can props
        </p>
      </div>
      
      <MiddleComponent />
    </div>
  )
}

export default UseContextDemo
