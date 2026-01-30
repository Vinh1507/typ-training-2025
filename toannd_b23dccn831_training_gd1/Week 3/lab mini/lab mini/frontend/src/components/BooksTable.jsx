import React, { useEffect, useState } from 'react'

function normalizeResponse(payload) {
  // Support both flat array responses and nested { data: { books: [...] } }
  if (!payload) return null
  if (Array.isArray(payload)) return payload
  if (Array.isArray(payload.data)) return payload.data
  if (payload.data && Array.isArray(payload.data.books)) return payload.data.books
  if (Array.isArray(payload.books)) return payload.books
  return null
}

export default function BooksTable() {
  const [books, setBooks] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let mounted = true

    async function fetchBooks() {
      setLoading(true)
      setError(null)

      const candidates = ['/api/v1/books', '/api/books', 'http://localhost:8080/api/v1/books', 'http://localhost:8080/api/books']

      for (const url of candidates) {
        try {
          const res = await fetch(url)
          if (!res.ok) throw new Error(`HTTP ${res.status}`)
          const json = await res.json()
          const arr = normalizeResponse(json)
          if (arr && mounted) {
            setBooks(arr)
            setLoading(false)
            return
          }
        } catch (err) {
          // try next candidate
        }
      }

      if (mounted) {
        setError('Không thể lấy dữ liệu từ server (đã thử vài endpoint).')
        setLoading(false)
      }
    }

    fetchBooks()

    return () => { mounted = false }
  }, [])

  if (loading) return <div className="empty">Đang tải dữ liệu...</div>
  if (error) return <div className="empty text-red-600">{error}</div>
  if (!Array.isArray(books) || books.length === 0) return <div className="empty">Không có sách nào</div>

  return (
    <div className="p-4">
      <div className="flex items-center justify-between mb-4">
        <div className="text-sm text-gray-600">Tổng: <span className="font-medium text-gray-800">{books.length}</span></div>
        <div className="text-sm">
          <button className="inline-flex items-center gap-2 px-3 py-1.5 rounded-md bg-primary-600 text-white text-sm hover:bg-primary-700">Làm mới</button>
        </div>
      </div>

      <table className="table w-full">
        <thead>
          <tr>
            <th className="th bg-gradient-to-r from-primary-600 to-primary-500 text-white">ID</th>
            <th className="th bg-gradient-to-r from-primary-600 to-primary-500 text-white">Name</th>
          </tr>
        </thead>
        <tbody className="bg-white">
          {books.map((b, idx) => (
            <tr key={b.id ?? JSON.stringify(b)} className={`hover:bg-primary-50 ${idx % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}>
              <td className="td w-24 font-medium text-gray-800">{b.id ?? '-'}</td>
              <td className="td">{b.name ?? b.title ?? '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
