## 1. State trong React

### useState

* Hook dùng để quản lý state cục bộ trong function component.
* Cú pháp:

```jsx
const [value, setValue] = useState(initialValue);
```

### State bất biến (Immutability)

* Không được thay đổi trực tiếp:

```js
state.count++ ❌
setState({...state, count: state.count + 1}) ✅
```

* React dựa vào shallow comparison để phát hiện thay đổi; nếu mutate trực tiếp thì component không re-render.

### State Lifting

* Khi nhiều component cần dùng chung 1 state, đưa state lên component cha và truyền xuống qua props.

### Khi nào component re-render lại:

Component re-render khi:

1. State thay đổi
2. Props thay đổi
3. Context thay đổi
4. Component cha re-render, component con cũng re-render (trừ khi dùng React.memo, useMemo, useCallback).

---

## 2. Dependency Array trong React Hooks

### useEffect với [] rỗng

```jsx
useEffect(() => {}, []);
```

* Chạy một lần duy nhất sau khi component mount.
* Dùng cho: fetch data lần đầu, subscribe WebSocket, thiết lập event listener.

### useEffect có dependency

```jsx
useEffect(() => {}, [a, b]);
```

* Chạy lại mỗi khi bất kỳ giá trị nào trong array (a,b) thay đổi.
* React so sánh bằng shallow comparison.

### useEffect không có dependency

```jsx
useEffect(() => {});
```

* Chạy sau mỗi lần render.
* Hiếm dùng; có thể gây lặp vô hạn nếu setState trong effect.

---

## 3. useEffect nâng cao

### Cleanup Function

Dùng để dọn dẹp: remove event listener, unsubscribe, clear timer.

```jsx
useEffect(() => {
  const id = setInterval(...);

  return () => clearInterval(id);
}, []);
```

### Các pattern phổ biến

#### Fetching Data

```jsx
useEffect(() => {
  fetchData();
}, []);
```

#### Subscriptions

```jsx
useEffect(() => {
  socket.on("message", handler);

  return () => socket.off("message", handler);
}, []);
```

#### Event Listeners

```jsx
useEffect(() => {
  window.addEventListener("resize", handleResize);
  return () => window.removeEventListener("resize", handleResize);
}, []);
```

---

## 4. useMemo – Memoization cho giá trị

### Sử dụng khi:

* Khi có tính toán nặng và phức tạp.
* Khi cần tránh tạo lại giá trị không cần thiết.

### Cú pháp

```jsx
const result = useMemo(() => heavyCompute(a), [a]);
```

## 5. useCallback – Memoize function

### Cú pháp

```jsx
const handleClick = useCallback(() => {
    doSomething();
}, [value]);
```

### Sử dụng khi:

* Khi truyền function xuống component con có React.memo.
* Khi function nằm trong dependency của useEffect hoặc useMemo.

### Kết hợp với React.memo

```jsx
const Child = React.memo(({ onClick }) => {...});
```

---

## 6. React Tree Root

### ReactDOM.createRoot()

```jsx
const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
```

### Component Tree

* Là cấu trúc lồng nhau các component bên trong App.
* React dùng reconciliation để xác định phần nào cần cập nhật.

### Root vs Component Tree

| Root                      | Component Tree                        |
| ------------------------- | ------------------------------------- |
| Điểm bắt đầu của ứng dụng | Cấu trúc các component bên trong root |
| Tạo bằng createRoot()     | Render bằng root.render(App)          |

---

## 7. useRef – Mutable Reference

### Công dụng:

1. Truy cập trực tiếp vào phần tử DOM

```jsx
const inputRef = useRef();
```

2. Lưu giá trị qua nhiều lần render nhưng không gây re-render

```jsx
const count = useRef(0);
```

3. Lưu giá trị trước đó (previous value)

```jsx
const prev = useRef(value);
useEffect(() => { prev.current = value; });
```

---

## 8. useContext – Context API

### Context gồm 3 phần

1. createContext()
2. Provider – cung cấp dữ liệu
3. useContext() – truy cập dữ liệu

### Sử dụng khi:

* Khi props drilling gây rối (truyền quá nhiều tầng).
* Khi nhiều component cần chung một state:

  * theme
  * ngôn ngữ

---

## 9. Component Patterns

### Composition (Thành phần hợp thành)

* Composition là cách React cho phép bạn kết hợp nhiều component lại với nhau để xây dựng UI linh hoạt, tái sử dụng được và dễ mở rộng.
* React ưu tiên Composition hơn Inheritance (kế thừa).

### Sử dụng khi:
* Tái sử dụng component
* Giảm lặp lại mã
* Dễ kết hợp nhiều phần UI với nhau
* Tạo cấu trúc component rõ ràng và linh hoạt

```jsx
<Box>
  <Header />
  <Content />
</Box>
```

### Controlled Components

* Input được quản lý bằng state.

```jsx
<input value={value} onChange={e => setValue(e.target.value)} />
```

### Uncontrolled Components

* Dùng useRef để lấy giá trị.

```jsx
const inputRef = useRef();
<input ref={inputRef} />
```