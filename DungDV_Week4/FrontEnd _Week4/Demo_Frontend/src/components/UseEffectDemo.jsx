import { useState, useEffect } from 'react'

function MountDemo() {
  const [mounted, setMounted] = useState(false)
  
  useEffect(() => {
    console.log('Component mounted')
    setMounted(true)
  }, [])
  
  return (
    <div className="card">
      <h3>useEffect voi [] - chay 1 lan</h3>
      <p>Mounted: {mounted ? 'Yes' : 'Loading...'}</p>
    </div>
  )
}

function CounterEffect() {
  const [count, setCount] = useState(0)
  const [effectRun, setEffectRun] = useState(0)
  
  useEffect(() => {
    console.log('Count changed:', count)
    setEffectRun(prev => prev + 1)
  }, [count])
  
  return (
    <div className="card">
      <h3>useEffect voi [count]</h3>
      <p>Count: <span className="highlight">{count}</span></p>
      <p>Effect chay: <span className="highlight">{effectRun}</span> lan</p>
      <button className="button" onClick={() => setCount(count + 1)}>Tang</button>
    </div>
  )
}

function TimerDemo() {
  const [seconds, setSeconds] = useState(0)
  const [running, setRunning] = useState(false)
  
  useEffect(() => {
    if (!running) return
    
    const timer = setInterval(() => {
      setSeconds(prev => prev + 1)
    }, 1000)
    
    return () => {
      clearInterval(timer)
      console.log('Timer cleared')
    }
  }, [running])
  
  return (
    <div className="card">
      <h3>useEffect voi Cleanup</h3>
      <p>Timer: <span className="highlight">{seconds}</span>s</p>
      <button className="button" onClick={() => setRunning(!running)}>
        {running ? 'Stop' : 'Start'}
      </button>
    </div>
  )
}

function FetchDemo() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(false)
  
  useEffect(() => {
    setLoading(true)
    fetch('/api/books')
      .then(res => res.json())
      .then(result => {
        setData(result)
        setLoading(false)
      })
      .catch(err => {
        console.error(err)
        setLoading(false)
      })
  }, [])
  
  return (
    <div className="card">
      <h3>Fetch Data</h3>
      {loading && <p>Loading...</p>}
      {data && (
        <div>
          <p>Loaded {data.count || data.data?.length || 0} books</p>
        </div>
      )}
    </div>
  )
}

function UseEffectDemo() {
  return (
    <div className="section">
      <h2>2. useEffect</h2>
      <MountDemo />
      <CounterEffect />
      <TimerDemo />
      <FetchDemo />
    </div>
  )
}

export default UseEffectDemo
