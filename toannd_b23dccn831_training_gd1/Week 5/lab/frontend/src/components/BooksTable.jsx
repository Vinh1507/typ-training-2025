import React, { useEffect, useState } from 'react'

function normalizeResponse(payload) {
  if (!payload) return null
  if (Array.isArray(payload)) return payload
  if (Array.isArray(payload.data)) return payload.data
  if (payload.data && Array.isArray(payload.data.books)) return payload.data.books
  if (Array.isArray(payload.books)) return payload.books
  return null
}

export default function BooksTable() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  async function fetchBooks() {
    setLoading(true)
    setError(null)
    try {
      const res = await fetch('/api/books')
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const json = await res.json()
      const arr = normalizeResponse(json)
      if (!arr) throw new Error('Invalid response format')
      setBooks(arr)
    } catch (err) {
      setError(err.message || String(err))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchBooks() }, [])

  return (
    <div>
      <div className="table-header">
        <div className="count">Tổng: <strong>{books.length}</strong></div>
        <div>
          <button className="btn" onClick={fetchBooks}>Làm mới</button>
        </div>
      </div>

      {loading && <div className="empty">Đang tải dữ liệu...</div>}
      {error && <div className="empty" style={{color:'crimson'}}>Lỗi: {error}</div>}

      {!loading && !error && books.length === 0 && <div className="empty">Không có sách nào</div>}

      {!loading && !error && books.length > 0 && (
        <div style={{overflowX:'auto'}}>
          <table>
            <thead>
              <tr>
                <th style={{textAlign:'left'}}>ID</th>
                <th style={{textAlign:'left'}}>Tên sách</th>
              </tr>
            </thead>
            <tbody>
              {books.map((b, idx) => (
                <tr key={b.id ?? idx}>
                  <td>{b.id ?? '-'}</td>
                  <td>{b.name ?? b.title ?? '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
