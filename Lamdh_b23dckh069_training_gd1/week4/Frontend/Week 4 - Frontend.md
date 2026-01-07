## 1. State trong React
- **useState**: Hook quản lý state trong Functional Component. Cập nhật state sẽ trigger re-render.
   ```jsx
   const [state, setState] = useState(initialValue);
   ```
- **State bất biến**: State không nên thay đổi trực tiếp mà phải dùng setState để cập nhật. Tránh việc React không phát hiện thay đổi và không render lại component.
- **State Lifting**: Khi nhiều component cần chia sẻ state thì đưa state lên component cha, từ đó truyền state xuống các component con qua props
- Component re-render khi state hoặc props thay đổi (useState sẽ trigger re-render mỗi khi giá trị state thay đổi)

--- 

## 2. Dependency Array
- **Dependency Array Rỗng []**: Chạy 1 lần sau lần render đầu tiên
- **Dependency Array có giá trị**: useEffect chạy mỗi khi giá trị trong dependency array thay đổi
   ```jsx
   useEffect(() => {
      //side effect
   }, [dep1, dep2]);
   ```
- **Không có Dependency Array**: useEffect sẽ chạy sau mỗi lần render.

---

## 3. useEffect
- **cleaup function**: Giúp dọn dẹp tác dụng phụ khi component unmount hoặc trước khi effect chạy lại
   ```jsx
   useEffect(() => {
   // setup effect
   return () => {
   // cleanup effect
   };
   }, []);
   ```
- **patterns**:
   * Fetching Data: Dùng useEffect để gọi API khi component mount
   * Subscriptions & Event Listeners: Cài đặt các subscription hoặc event listener và cleanup khi component unmount.

---

## 4. useMemo
- Dùng khi có những phép tính phức tạp và cần tối ưu hiệu suất cho các component con
- **Memoization**: useMemo dùng để ghi nhớ giá trị tính toán từ trước và chỉ tính lại khi các dependencies thay đổi
   ```jsx
   const memoizedValue = useMemo(() => computeExpensiveValue(a, b), [a, b]);
   ```
---

## 5. useCallback
- Dùng khi cần truyền function xuống component con mà không muốn làm trigger re-render cho component con đó
- **Memoizing Functions**: useCallback giúp nhớ các function để tránh tạo ra function mới mỗi lần render
   ```jsx
   const mem = useCallback(() => {/* function */}, [deps]);
   ```
 **Kêt hợp với React.momo**: useCallback kết hợp với React.memo giúp tránh việc re-render không cần thiết

---

 ## 6. React Tree Root
 - **ReactDOM.createRoot()**: Cách mới để khởi tạo ứng dụng React với hỗ trọ Concurrent Mode
   ```jsx
   ReactDOM.createRoot(document.getElementById('root')).render(<App />);
   ```
- **Component Tree**: Cấu trúc cây các component trong ứng dụng React, nơi mỗi component có thể là con hoặc cha của một component khác.
- **Root vs Component Tree:** Root là điểm khởi đầu của app react, còn component tree là cấu trúc các component con bên trong root

---

## 7. useRef
- **Mutable References**: useRef giúp lưu trũ các giá trị mà không trigger re-render
   ```jsx
   const myRef = useRef(initialValue);
   ```
- **Accessing DOM**: useRef có thể được dùng để tham chiếu đến DOM node và thực hiện các thao tác trực tiếp
- **Storing Previous Values**: useRef giúp lưu trữ các giá trị trước đó mà không làm trigger re-render.

---

## 8. useContext
- Dùng khi cần chia sẻ dữ liệu giữa các component mà không muốn dùng props
- **Context API**: Cung cấp một cách để truyền giá trị giữa các component mà không cần phải truyền qua props
- **Provider**: Cung cấp giá trị context cho các component con
   ```jsx
   <MyContext.Provider value={value}>
      <Component />  
   </MyContext.Provider>
   ```

---

## 9. Component Patterns
- **Compostition**: Kết hợp nhiều component nhỏ lại để tạo thành một component lớn hơn, dễ bảo trì và tái sử dụng
- **Controlled/Uncontrolled Components**:
   * Controlled: Component có state được kiểm soát bởi react (sử dụng useState)
   * Uncontrolled: Component sử dụng DOM để quản lý state mà không qua React (useRef)
