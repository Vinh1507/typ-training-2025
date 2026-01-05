import { useRef, useState, useEffect } from 'react'

function UseRefDemo() {
  const [count, setCount] = useState(0)
  const inputRef = useRef(null)
  const countRef = useRef(0)
  const prevRef = useRef()
  
  const focusInput = () => {
    inputRef.current?.focus()
  }
  
  const incRef = () => {
    countRef.current += 1
    console.log('Ref:', countRef.current)
  }
  
  useEffect(() => {
    prevRef.current = count
  }, [count])
  
  return (
    <div className="section">
      <h2>5. useRef</h2>
      
      <div className="card">
        <h3>Truy cap DOM</h3>
        <input ref={inputRef} className="input" placeholder="Focus vao day" />
        <button className="button" onClick={focusInput}>Focus</button>
      </div>
      
      <div className="card">
        <h3>Mutable Reference</h3>
        <p>State: {count}</p>
        <p>Ref: {countRef.current}</p>
        <button className="button" onClick={() => setCount(count + 1)}>Tang State</button>
        <button className="button" onClick={incRef}>Tang Ref (ko re-render)</button>
      </div>
      
      <div className="card">
        <h3>Luu Previous Value</h3>
        <p>Hien tai: {count}</p>
        <p>Truoc do: {prevRef.current ?? '-'}</p>
        <button className="button" onClick={() => setCount(count + 1)}>Tang</button>
      </div>
    </div>
  )
}

export default UseRefDemo
