import { useState, useRef } from 'react'

function Card({ title, children }) {
  return (
    <div className="card" style={{ border: '2px solid #3498db' }}>
      <h3>{title}</h3>
      {children}
    </div>
  )
}

function ControlledInput() {
  const [value, setValue] = useState('')
  
  return (
    <div className="card">
      <h3>Controlled</h3>
      <input
        className="input"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="Nhap..."
      />
      <p>Value: {value}</p>
      <p style={{ fontSize: '12px' }}>State do React quan ly</p>
    </div>
  )
}

function UncontrolledInput() {
  const inputRef = useRef(null)
  const [display, setDisplay] = useState('')
  
  return (
    <div className="card">
      <h3>Uncontrolled</h3>
      <input ref={inputRef} className="input" placeholder="Nhap..." />
      <button className="button" onClick={() => setDisplay(inputRef.current.value)}>
        Lay gia tri
      </button>
      <p>Value: {display}</p>
      <p style={{ fontSize: '12px' }}>State do DOM quan ly</p>
    </div>
  )
}

function ComponentPatternsDemo() {
  return (
    <div className="section">
      <h2>7. Component Patterns</h2>
      
      <div className="card">
        <h3>Composition</h3>
        <Card title="Card Component">
          <p>Noi dung truyen qua children</p>
          <p>Co the them gi tuy y</p>
        </Card>
      </div>
      
      <ControlledInput />
      <UncontrolledInput />
    </div>
  )
}

export default ComponentPatternsDemo
