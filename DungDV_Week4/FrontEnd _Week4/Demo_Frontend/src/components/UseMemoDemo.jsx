import { useState, useMemo } from 'react'

function UseMemoDemo() {
  const [numbers] = useState([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
  const [filter, setFilter] = useState('')
  const [count, setCount] = useState(0)
  
  // tinh tong binh phuong - chi tinh lai khi numbers thay doi
  const total = useMemo(() => {
    console.log('Calculating...')
    return numbers.reduce((sum, n) => sum + n * n, 0)
  }, [numbers])
  
  const filtered = useMemo(() => {
    if (!filter) return numbers
    return numbers.filter(n => n.toString().includes(filter))
  }, [numbers, filter])
  
  return (
    <div className="section">
      <h2>3. useMemo</h2>
      
      <div className="card">
        <h3>Memoized Calculation</h3>
        <p>Tong binh phuong: <span className="highlight">{total}</span></p>
        <p>Thay doi filter, tong ko tinh lai (check console)</p>
      </div>
      
      <div className="card">
        <h3>Filtered List</h3>
        <input
          className="input"
          placeholder="Loc so..."
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
        />
        <p>Ket qua: {filtered.join(', ')}</p>
        
        <button className="button" onClick={() => setCount(count + 1)}>
          Re-render ({count})
        </button>
      </div>
    </div>
  )
}

export default UseMemoDemo
