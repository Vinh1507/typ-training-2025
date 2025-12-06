import { useState, useCallback, memo } from 'react'

const ChildButton = memo(({ onClick, label }) => {
  console.log('Child rendered:', label)
  
  return (
    <div className="card" style={{ background: '#e8f5e9' }}>
      <h4>{label}</h4>
      <button className="button" onClick={onClick}>Click</button>
      <p style={{ fontSize: '12px', color: '#666' }}>
        Check console de xem re-render
      </p>
    </div>
  )
})

function UseCallbackDemo() {
  const [count, setCount] = useState(0)
  const [other, setOther] = useState(0)
  
  // ko dung useCallback - tao function moi moi lan render
  const handleNormal = () => {
    console.log('Normal click')
  }
  
  // dung useCallback - function ko doi
  const handleCallback = useCallback(() => {
    console.log('Callback click')
  }, [])
  
  return (
    <div className="section">
      <h2>4. useCallback</h2>
      
      <div className="card">
        <h3>Demo voi React.memo</h3>
        <p>Count: {count} | Other: {other}</p>
        
        <button className="button" onClick={() => setCount(count + 1)}>Tang Count</button>
        <button className="button" onClick={() => setOther(other + 1)}>Tang Other</button>
        
        <div style={{ marginTop: '20px' }}>
          <ChildButton label="Ko dung useCallback" onClick={handleNormal} />
          <ChildButton label="Co useCallback" onClick={handleCallback} />
        </div>
        
        <p style={{ fontSize: '12px', marginTop: '10px' }}>
          Tang Other: child ko useCallback se re-render, child co useCallback thi ko
        </p>
      </div>
    </div>
  )
}

export default UseCallbackDemo
