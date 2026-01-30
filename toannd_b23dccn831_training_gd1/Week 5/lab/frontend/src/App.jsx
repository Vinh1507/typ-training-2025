import React from 'react'
import BooksTable from './components/BooksTable'

export default function App() {
  return (
    <div className="app-root">
      <header className="app-header">
        <div>
          <h1>Bookshop</h1>
          <p className="subtitle">Danh sách sách hiện có trong hệ thống</p>
        </div>
      </header>

      <main className="container">
        <div className="card">
          <BooksTable />
        </div>
      </main>

      <footer className="app-footer">Frontend demo • Fetch /api/books</footer>
    </div>
  )
}
