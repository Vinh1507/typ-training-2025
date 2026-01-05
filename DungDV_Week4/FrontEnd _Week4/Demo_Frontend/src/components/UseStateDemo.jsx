import { useState } from 'react'

function Counter() {
  const [count, setCount] = useState(0)
  
  return (
    <div className="card">
      <h3>Counter: {count}</h3>
      <button className="button" onClick={() => setCount(count + 1)}>+</button>
      <button className="button" onClick={() => setCount(count - 1)}>-</button>
      <button className="button" onClick={() => setCount(0)}>Reset</button>
    </div>
  )
}

function UserForm() {
  const [user, setUser] = useState({ name: '', email: '' })
  
  const handleChange = (field, value) => {
    setUser({ ...user, [field]: value })
  }
  
  return (
    <div className="card">
      <h3>Form (state voi object)</h3>
      <input
        className="input"
        placeholder="Ten"
        value={user.name}
        onChange={(e) => handleChange('name', e.target.value)}
      />
      <input
        className="input"
        placeholder="Email"
        value={user.email}
        onChange={(e) => handleChange('email', e.target.value)}
      />
      <p>Ten: <span className="highlight">{user.name}</span></p>
      <p>Email: <span className="highlight">{user.email}</span></p>
    </div>
  )
}

function Display({ count }) {
  return <p>Gia tri tu cha: <span className="highlight">{count}</span></p>
}

function UseStateDemo() {
  const [sharedCount, setSharedCount] = useState(0)
  
  return (
    <div className="section">
      <h2>1. useState</h2>
      
      <h3>Counter</h3>
      <Counter />
      
      <h3>State voi Object</h3>
      <UserForm />
      
      <h3>State Lifting</h3>
      <div className="card">
        <p>State o component cha, truyen xuong con</p>
        <button className="button" onClick={() => setSharedCount(sharedCount + 1)}>
          Tang: {sharedCount}
        </button>
        <Display count={sharedCount} />
      </div>
    </div>
  )
}

export default UseStateDemo
