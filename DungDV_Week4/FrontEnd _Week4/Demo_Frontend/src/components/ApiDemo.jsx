import { useState, useEffect } from 'react'

const API_URL = 'http://localhost:3000/api'

function ApiDemo() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(false)
  const [user, setUser] = useState(null)
  const [token, setToken] = useState(localStorage.getItem('token') || '')
  
  const fetchBooks = async () => {
    setLoading(true)
    try {
      const res = await fetch(`${API_URL}/books`)
      const data = await res.json()
      setBooks(data.data || [])
    } catch (err) {
      console.error(err)
    }
    setLoading(false)
  }
  
  const handleLogin = async () => {
    try {
      const res = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: 'admin@example.com',
          password: 'admin123'
        })
      })
      
      const data = await res.json()
      if (data.success) {
        setToken(data.data.accessToken)
        setUser(data.data.user)
        localStorage.setItem('token', data.data.accessToken)
        alert('Login thanh cong!')
      } else {
        alert('Login that bai')
      }
    } catch (err) {
      alert('Loi: ' + err.message)
    }
  }
  
  const fetchProtected = async () => {
    if (!token) {
      alert('Chua login')
      return
    }
    
    setLoading(true)
    try {
      const res = await fetch(`${API_URL}/books-protected`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      
      if (res.status === 401) {
        alert('Token het han')
        setToken('')
        localStorage.removeItem('token')
        return
      }
      
      const data = await res.json()
      setBooks(data.data || [])
    } catch (err) {
      console.error(err)
    }
    setLoading(false)
  }
  
  useEffect(() => {
    fetchBooks()
  }, [])
  
  return (
    <div className="section">
      <h2>8. API Demo</h2>
      
      <div className="card">
        <h3>Goi API tu React</h3>
        
        {!user ? (
          <div>
            <p>Chua login</p>
            <button className="button" onClick={handleLogin}>
              Login (admin@example.com)
            </button>
          </div>
        ) : (
          <div>
            <p>User: {user.username} ({user.role})</p>
          </div>
        )}
        
        <div style={{ marginTop: '15px' }}>
          <button className="button" onClick={fetchBooks}>Public API</button>
          <button className="button" onClick={fetchProtected}>Protected API</button>
        </div>
        
        {loading && <p>Loading...</p>}
        
        {books.length > 0 && (
          <div style={{ marginTop: '15px' }}>
            <h4>Books ({books.length}):</h4>
            <ul>
              {books.map(book => (
                <li key={book.id}>{book.title} - {book.author}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  )
}

export default ApiDemo
