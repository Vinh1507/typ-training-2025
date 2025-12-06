# React - Core Concepts

## 1. useState

Hook cơ bản nhất để quản lý state trong component:

```javascript
import { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0);
  
  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Tăng</button>
    </div>
  );
}
```

Lưu ý: state là immutable, ko được thay đổi trực tiếp mà phải dùng setter function.

```javascript
// sai
items.push(4);

// đúng
setItems([...items, 4]);
```

### State Lifting

Khi nhiều component cần dùng chung state thì đưa state lên component cha:

```javascript
function App() {
  const [count, setCount] = useState(0);
  
  return (
    <div>
      <Counter count={count} setCount={setCount} />
      <Display count={count} />
    </div>
  );
}
```

---

## 2. useEffect

Dùng để xử lý side effects như gọi API, set timer,...

### Dependency array

```javascript
// chạy 1 lần khi mount
useEffect(() => {
  console.log('mounted');
}, []);

// chạy mỗi khi count thay đổi
useEffect(() => {
  console.log('count changed');
}, [count]);

// chạy mỗi lần render (tránh dùng cái này)
useEffect(() => {
  console.log('rendered');
});
```

### Cleanup function

Dọn dẹp khi component unmount:

```javascript
useEffect(() => {
  const timer = setInterval(() => {
    console.log('tick');
  }, 1000);
  
  return () => clearInterval(timer);
}, []);
```

---

## 3. useMemo

Cache kết quả tính toán, chỉ tính lại khi dependency thay đổi:

```javascript
const total = useMemo(() => {
  return items.reduce((sum, item) => sum + item.price, 0);
}, [items]);
```

Dùng khi tính toán nặng thôi, ko cần dùng cho mọi thứ.

---

## 4. useCallback

Tương tự useMemo nhưng cho function. Thường dùng kết hợp với React.memo:

```javascript
const handleClick = useCallback(() => {
  console.log('clicked');
}, []);

// child component sẽ ko re-render nếu onClick ko đổi
const Child = React.memo(({ onClick }) => {
  return <button onClick={onClick}>Click</button>;
});
```

---

## 5. useRef

Lưu giá trị mà ko gây re-render, hoặc để truy cập DOM:

```javascript
// truy cập DOM
const inputRef = useRef(null);
inputRef.current.focus();

// lưu giá trị
const countRef = useRef(0);
countRef.current = 10; // ko re-render
```

---

## 6. useContext

Truyền data qua nhiều component mà ko cần props drilling:

```javascript
const ThemeContext = createContext();

function App() {
  const [theme, setTheme] = useState('light');
  
  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      <Header />
    </ThemeContext.Provider>
  );
}

function Header() {
  const { theme } = useContext(ThemeContext);
  return <div className={theme}>Header</div>;
}
```

Dùng cho những thứ global như theme, auth, language,...

---

## 7. React Tree

Từ React 18 dùng createRoot:

```javascript
import { createRoot } from 'react-dom/client';

const root = createRoot(document.getElementById('root'));
root.render(<App />);
```

Component tree là cấu trúc phân cấp các component từ root xuống.

---

## 8. Component Patterns

### Composition

Dùng children prop để truyền content:

```javascript
function Card({ title, children }) {
  return (
    <div className="card">
      <h2>{title}</h2>
      {children}
    </div>
  );
}

<Card title="Title">
  <p>Content</p>
</Card>
```

### Controlled vs Uncontrolled

Controlled - state do React quản lý:
```javascript
const [value, setValue] = useState('');
<input value={value} onChange={(e) => setValue(e.target.value)} />
```

Uncontrolled - state do DOM quản lý:
```javascript
const inputRef = useRef();
<input ref={inputRef} />
// lấy giá trị: inputRef.current.value
```

Controlled dùng khi cần validate realtime, uncontrolled cho form đơn giản.
