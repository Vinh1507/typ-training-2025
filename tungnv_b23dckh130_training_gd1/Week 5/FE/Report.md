# TỔNG HỢP KIẾN THỨC FRONTEND

- [TUẦN 1: HTML CƠ BẢN](#tuần-1-html-cơ-bản)
- [TUẦN 2: CSS CƠ BẢN](#tuần-2-css-cơ-bản)
- [TUẦN 3: JAVASCRIPT CƠ BẢN](#tuần-3-javascript-cơ-bản)
- [TUẦN 4: REACT CƠ BẢN](#tuần-4-react-cơ-bản)
- [REFRESH TOKEN](#refresh-token)
---

## TUẦN 1: HTML CƠ BẢN

### 1. Cấu trúc cơ bản của trang HTML

Cấu trúc chuẩn của một tài liệu HTML5 là nền tảng của mọi trang web:

- **`<!DOCTYPE html>`**: Khai báo cho trình duyệt biết đây là tài liệu HTML5
- **`<html>`**: Thẻ gốc, bao bọc toàn bộ nội dung của trang web
- **`<head>`**: Chứa các thông tin meta về trang (title, CSS, JavaScript, meta tags)
- **`<body>`**: Chứa tất cả nội dung hiển thị trực tiếp trên trình duyệt

### 2. Các thẻ HTML phổ biến

#### Văn bản
- **`<h1>` đến `<h6>`**: Các thẻ tiêu đề (h1 quan trọng nhất, h6 ít quan trọng nhất)
- **`<p>`**: Định nghĩa một đoạn văn bản
- **`<span>`**: Nhóm các phần tử inline hoặc một phần nhỏ văn bản
- **`<div>`**: Thẻ khối dùng để nhóm các nội dung lại, phục vụ cho việc tạo bố cục

#### Liên kết & Hình ảnh
- **`<a>`**: Tạo siêu liên kết (thuộc tính quan trọng: `href`)
- **`<img>`**: Nhúng hình ảnh (thuộc tính bắt buộc: `src`, `alt`)

#### Danh sách
- **`<ul>`**: Danh sách không có thứ tự (dấu chấm đầu dòng)
- **`<ol>`**: Danh sách có thứ tự (dùng số, chữ cái)
- **`<li>`**: Một mục trong danh sách

#### Bảng (Table)
- **`<table>`**: Thẻ bao bọc toàn bộ cấu trúc bảng
- **`<tr>`**: Định nghĩa một hàng trong bảng
- **`<td>`**: Định nghĩa một ô dữ liệu

#### Form (Biểu mẫu)
- **`<input>`**: Thẻ nhập liệu (type: text, password, checkbox, radio...)
- **`<label>`**: Cung cấp nhãn mô tả cho input
- **`<button>`**: Tạo ra một nút bấm
- **`<select>`**: Tạo một danh sách thả xuống
- **`<textarea>`**: Tạo vùng nhập văn bản nhiều dòng

### 3. Semantic HTML (HTML ngữ nghĩa)

HTML ngữ nghĩa sử dụng các thẻ mô tả rõ ràng ý nghĩa và chức năng của nội dung:

- **`<header>`**: Phần đầu của trang (logo, tiêu đề, menu điều hướng)
- **`<nav>`**: Khu vực chứa các liên kết điều hướng chính
- **`<article>`**: Nội dung độc lập, hoàn chỉnh (bài đăng blog, bài báo)
- **`<section>`**: Phân nhóm các nội dung có liên quan logic
- **`<footer>`**: Phần chân trang (thông tin bản quyền, liên hệ, liên kết phụ)

**Lợi ích**: Cải thiện cấu trúc, hỗ trợ SEO và tăng khả năng truy cập cho người dùng sử dụng trình đọc màn hình.

---

## TUẦN 2: CSS CƠ BẢN

### 1. CSS là gì?

CSS (Cascading Style Sheets) là ngôn ngữ được sử dụng để mô tả giao diện và định dạng của tài liệu HTML.

**Vai trò**: Tách biệt nội dung khỏi phần trình bày, giúp mã nguồn sạch sẽ và dễ bảo quản.

### 2. Cách tích hợp CSS

#### 2.1 Inline CSS
- Sử dụng thuộc tính `style` trực tiếp trong thẻ HTML
- Chỉ áp dụng cho một phần tử duy nhất

```html
<p style="color: blue; font-size: 16px;">Văn bản màu xanh.</p>
```

**Nhược điểm**: Khó bảo trì, dễ lộn xộn, không thể tái sử dụng

#### 2.2 Internal CSS
- Đặt mã CSS trong thẻ `<style>` trong phần `<head>`
- Chỉ áp dụng cho một trang HTML duy nhất

```html
<head>
    <style>
        p { color: green; }
    </style>
</head>
```

#### 2.3 External CSS (Khuyên dùng)
- Viết CSS trong file riêng có đuôi `.css`
- Liên kết với HTML bằng thẻ `<link>`

```html
<head>
    <link rel="stylesheet" href="style.css">
</head>
```

**Ưu điểm**: 
- Dễ bảo trì (chỉnh sửa 1 file CSS cho toàn bộ trang web)
- Tái sử dụng cho nhiều file HTML
- Hiệu suất tốt (trình duyệt cache file CSS)

### 3. Cú pháp CSS

```css
selector {
    property: value;
}
```

- **Selector**: Nhắm đến phần tử HTML muốn chọn (`h1`, `.class`, `#id`)
- **Property**: Thuộc tính muốn thay đổi (`color`, `font-size`, `background-color`)
- **Value**: Giá trị gán cho thuộc tính (`red`, `20px`, `#ffffff`)

### 4. Các khái niệm CSS quan trọng

#### 4.1 Box Model
Mọi phần tử HTML đều là một "hộp" gồm:
- **Content**: Nội dung
- **Padding**: Khoảng cách bên trong (giữa content và border)
- **Border**: Đường viền
- **Margin**: Khoảng cách bên ngoài (giữa phần tử và các phần tử khác)

#### 4.2 Display & Position
- **Display**: `block`, `inline`, `inline-block`, `flex`, `grid`
- **Position**: `static`, `relative`, `absolute`, `fixed`, `sticky`

#### 4.3 Flexbox
- Bố cục linh hoạt cho việc sắp xếp phần tử
- Thuộc tính chính: `display: flex`, `justify-content`, `align-items`, `flex-direction`

#### 4.4 CSS Grid
- Hệ thống lưới 2 chiều mạnh mẽ
- Thuộc tính chính: `display: grid`, `grid-template-columns`, `grid-template-rows`, `gap`

#### 4.5 Responsive Design
- Sử dụng **Media Queries** để tạo giao diện responsive

```css
@media (max-width: 768px) {
    .container {
        flex-direction: column;
    }
}
```

### 5. Các kỹ thuật CSS thực tế

#### 5.1 Sticky Navigation
```css
nav {
    position: sticky;
    top: 0;
    z-index: 100;
}
```

#### 5.2 Hover Effects
```css
button:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}
```

#### 5.3 Animations
```css
@keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}

.avatar:hover {
    animation: spin 8s linear infinite;
}
```

---

## TUẦN 3: JAVASCRIPT CƠ BẢN

### 1. JavaScript là gì?

JavaScript là một ngôn ngữ lập trình kịch bản đa nền tảng và hướng đối tượng, được thiết kế để tạo ra các trang web có tính tương tác cao.

### 2. Biến

#### 2.1 Cách khai báo

- **`var`**: Phạm vi hàm (function scope), có hoisting
- **`let`**: Phạm vi khối (block scope), có thể thay đổi giá trị
- **`const`**: Phạm vi khối, không thể gán lại giá trị

```javascript
var x = 10;      // Function scope
let y = 20;      // Block scope, có thể thay đổi
const z = 30;    // Block scope, không thể thay đổi
```

#### 2.2 Các kiểu dữ liệu

**Kiểu dữ liệu nguyên thủy (Primitive)**:
- **String**: Chuỗi văn bản (`"Xin chào"`)
- **Number**: Số nguyên và số thập phân (`42`, `3.14`)
- **Boolean**: `true` hoặc `false`
- **undefined**: Biến đã khai báo nhưng chưa gán giá trị
- **null**: Giá trị "rỗng" được gán cố ý
- **BigInt**: Số nguyên cực lớn (`123456789012345678901234567890n`)
- **Symbol**: Giá trị duy nhất và bất biến

**Kiểu dữ liệu tham chiếu (Reference)**:
- **Object**: Bộ sưu tập các cặp key-value
- **Array**: Danh sách các giá trị theo thứ tự
- **Function**: Khối mã có thể gọi để thực thi

### 3. Vòng lặp

#### 3.1 Vòng lặp `for`
```javascript
for (let i = 0; i < 5; i++) {
    console.log(i);
}
```

#### 3.2 Vòng lặp `while`
```javascript
let n = 0;
while (n < 3) {
    console.log(n);
    n++;
}
```

#### 3.3 Vòng lặp `do...while`
```javascript
let m = 5;
do {
    console.log(m);
    m++;
} while (m < 3);
// Luôn chạy ít nhất 1 lần
```

#### 3.4 Vòng lặp `for...of`
```javascript
const mauSac = ["đỏ", "vàng", "xanh"];
for (const mau of mauSac) {
    console.log(mau);
}
```

#### 3.5 Vòng lặp `for...in`
```javascript
const nguoi = { ten: "Alice", tuoi: 30 };
for (const key in nguoi) {
    console.log(key, nguoi[key]);
}
```

### 4. Rẽ nhánh

#### 4.1 Lệnh `if...else`
```javascript
if (tuoi >= 18) {
    console.log("Đủ tuổi trưởng thành");
} else {
    console.log("Vị thành niên");
}
```

#### 4.2 Lệnh `switch`
```javascript
switch (thu) {
    case "Thứ Hai":
        console.log("Bắt đầu tuần làm việc");
        break;
    case "Thứ Bảy":
    case "Chủ Nhật":
        console.log("Ngày nghỉ cuối tuần");
        break;
    default:
        console.log("Giá trị không hợp lệ");
}
```

#### 4.3 Toán tử 3 ngôi (Ternary Operator)
```javascript
let trangThai = (tuoi >= 18) ? "Trưởng thành" : "Vị thành niên";
```

### 5. Hàm

#### 5.1 Function Declaration
```javascript
function chao() {
    console.log("Xin chào!");
}
```

#### 5.2 Function Expression
```javascript
const tamBiet = function() {
    console.log("Tạm biệt!");
};
```

#### 5.3 Arrow Function
```javascript
const nhanDoi = x => x * 2;
const tinhTong = (a, b) => a + b;
```

**Đặc điểm Arrow Function**:
- Cú pháp ngắn gọn
- Không có `this` riêng (mượn `this` từ context bên ngoài)

### 6. Mảng (Array)

#### 6.1 Tạo và truy cập mảng
```javascript
const traiCay = ["Táo", "Cam", "Chuối"];
console.log(traiCay[0]); // "Táo"
console.log(traiCay.length); // 3
```

#### 6.2 Các phương thức phổ biến

**Thay đổi mảng gốc**:
- `push()`: Thêm phần tử vào cuối
- `pop()`: Xóa phần tử cuối
- `unshift()`: Thêm phần tử vào đầu
- `shift()`: Xóa phần tử đầu
- `splice()`: Thêm/xóa/thay thế phần tử ở vị trí bất kỳ

**Không thay đổi mảng gốc**:
- `forEach()`: Lặp qua từng phần tử
- `map()`: Biến đổi mỗi phần tử, trả về mảng mới
- `filter()`: Lọc các phần tử thỏa điều kiện
- `reduce()`: Giảm mảng về một giá trị duy nhất

```javascript
const so = [1, 2, 3, 4, 5];

// forEach - không trả về gì
so.forEach(num => console.log(num));

// map - trả về mảng mới
const soBinhPhuong = so.map(num => num * num); // [1, 4, 9, 16, 25]

// filter - lọc các phần tử
const soChan = so.filter(num => num % 2 === 0); // [2, 4]

// reduce - tính tổng
const tong = so.reduce((acc, curr) => acc + curr, 0); // 15
```

### 7. Callback

#### 7.1 Callback là gì?
Callback là một hàm được truyền vào hàm khác dưới dạng đối số, để được gọi sau khi một tác vụ hoàn thành.

```javascript
setTimeout(function() {
    console.log("Đã chờ 2 giây!");
}, 2000);
```

#### 7.2 Callback Hell
Xảy ra khi có nhiều tác vụ bất đồng bộ phụ thuộc lẫn nhau, tạo ra cấu trúc mã lồng nhau sâu.

**Giải pháp**:
- **Promise**: Sử dụng `.then()` để nối chuỗi các tác vụ
- **Async/Await**: Viết mã bất đồng bộ như mã đồng bộ

```javascript
// Promise
getUser(1)
    .then(user => getPosts(user.id))
    .then(posts => getComments(posts[0].id))
    .catch(error => console.error(error));

// Async/Await
async function layDuLieu() {
    try {
        const user = await getUser(1);
        const posts = await getPosts(user.id);
        const comments = await getComments(posts[0].id);
    } catch (error) {
        console.error(error);
    }
}
```

### 8. Class & Object

#### 8.1 Class
Class là bản thiết kế để tạo ra các đối tượng.

```javascript
class NgoiNha {
    constructor(mauSon) {
        this.mauSon = mauSon;
        this.soPhongNgu = 4;
    }

    moCua() {
        console.log(`Ngôi nhà màu ${this.mauSon} đã mở cửa.`);
    }
}
```

#### 8.2 Object
Object là thể hiện cụ thể được tạo ra từ Class.

```javascript
const nhaCuaAn = new NgoiNha("Xanh");
nhaCuaAn.moCua(); // "Ngôi nhà màu Xanh đã mở cửa."
```

#### 8.3 Reference
Khi gán object, bản chất là sao chép địa chỉ tham chiếu (reference), không phải giá trị.

```javascript
let obj1 = { ten: "Tung" };
let obj2 = obj1; // obj2 và obj1 cùng trỏ đến một object
obj2.ten = "An";
console.log(obj1.ten); // "An"
```

### 9. DOM (Document Object Model)

#### 9.1 DOM là gì?
DOM là "cây cầu" nối giữa file HTML và JavaScript, tổ chức tất cả các phần tử thành cấu trúc cây.

#### 9.2 Làm việc với DOM

**I. Tìm kiếm và truy cập**
```javascript
// Chọn phần tử
document.getElementById("idCuaBan");
document.querySelector(".tenClass");
document.querySelectorAll("ul li");
```

**II. Thay đổi và thao tác**
```javascript
// Thay đổi nội dung
element.textContent = "Nội dung mới";
element.innerHTML = "<b>Nội dung in đậm</b>";

// Thay đổi thuộc tính
element.id = "idMoi";
element.setAttribute("src", "anh-moi.jpg");

// Thay đổi kiểu dáng
element.style.color = "red";
element.classList.add("tenClassMoi");
element.classList.remove("tenClassCu");
element.classList.toggle("classDeBatTat");
```

**III. Tạo và Xóa**
```javascript
// Tạo phần tử mới
let newElement = document.createElement("p");
newElement.textContent = "Nội dung mới";

// Thêm vào DOM
parentElement.appendChild(newElement);
parentElement.prepend(newElement);

// Xóa phần tử
elementCanXoa.remove();
```

**IV. Phản hồi sự kiện**
```javascript
element.addEventListener('click', function() {
    console.log("Đã click!");
});

element.addEventListener('mouseover', function() {
    console.log("Di chuột qua!");
});
```

### 10. JSON (JavaScript Object Notation)

#### 10.1 JSON là gì?
JSON là một định dạng văn bản dùng để lưu trữ và vận chuyển dữ liệu.

#### 10.2 Quy tắc JSON
- Key PHẢI là chuỗi và nằm trong dấu ngoặc kép `""`
- Value có thể là: `string`, `number`, `object`, `array`, `boolean`, `null`
- KHÔNG được phép: `function`, `undefined`, `comment`, dấu phẩy cuối

```json
{
    "ten": "An",
    "tuoi": 25,
    "diaChi": {
        "thanh pho": "Ha Noi"
    },
    "soThich": ["đọc sách", "chơi game"]
}
```

---

## TUẦN 4: REACT CƠ BẢN

### 1. State

#### 1.1 State là gì?
State là một React Hook cho phép thêm biến trạng thái vào component.

```javascript
const [state, setState] = useState(initialState);
```

State là tập hợp các dữ liệu có thể thay đổi, được quản lý nội bộ bởi Component. Bất kỳ sự thay đổi nào đối với State đều sẽ kích hoạt quá trình re-render.

#### 1.2 Khi nào component re-render?
- **State thay đổi**: Khi gọi hàm `setState`
- **Props thay đổi**: Khi component cha đưa ra chỉ thị mới
- **Component cha re-render**: Component con cũng sẽ re-render theo

#### 1.3 State bất biến (Immutability)
- Không bao giờ thay đổi trực tiếp State
- Sử dụng `setState` để tạo bản sao mới
- Dùng spread operator `...` khi cập nhật object hoặc array

```javascript
// Sai
state.name = "Tung";

// Đúng
setState({ ...state, name: "Tung" });
```

### 2. State Lifting (Nâng trạng thái lên)

Khi Component A và Component B cần dùng chung một dữ liệu, ta phải chuyển dữ liệu lên Component Cha gần nhất, sau đó cha truyền xuống cho A và B qua Props.

```javascript
const GiaDinh = () => {
    const [soDu, setSoDu] = useState(100000);

    const xuLyTieuTien = (soTienTieu) => {
        setSoDu(soDu - soTienTieu);
    };

    return (
        <div>
            <AnhTrai tien={soDu} hamTieuTien={xuLyTieuTien} />
            <EmGai tien={soDu} />
        </div>
    );
};
```

### 3. useEffect

#### 3.1 useEffect là gì?
useEffect là một Hook cho phép thực hiện các Side Effects bên trong Functional Components.

```javascript
useEffect(() => {
    // Side effect code
}, [dependencies]);
```

#### 3.2 Ba trường hợp của useEffect

**Trường hợp 1: Không có dependency array**
```javascript
useEffect(() => {
    console.log("Chạy mỗi khi render");
});
// Có thể gây infinite loop
```

**Trường hợp 2: Dependency array rỗng**
```javascript
useEffect(() => {
    console.log("Chạy 1 lần sau khi component mount");
}, []);
// Dùng cho: Gọi API ban đầu, đăng ký sự kiện
```

**Trường hợp 3: Dependency array có giá trị**
```javascript
useEffect(() => {
    console.log("Chạy khi userID hoặc filter thay đổi");
}, [userID, filter]);
// Dùng cho: Lắng nghe sự thay đổi của biến cụ thể
```

#### 3.3 Cleanup Function
Cleanup Function được sử dụng để giải phóng tài nguyên, hủy bỏ các đăng ký.

```javascript
useEffect(() => {
    const handleResize = () => {
        console.log("Kích thước màn hình:", window.innerWidth);
    };

    window.addEventListener('resize', handleResize);

    // Cleanup function
    return () => {
        window.removeEventListener('resize', handleResize);
    };
}, []);
```

**Thời điểm Cleanup chạy**:
- Trước khi Effect chạy lại lần tiếp theo
- Ngay trước khi Component bị Unmount

### 4. useMemo

#### 4.1 useMemo là gì?
useMemo giúp lưu trữ (cache) kết quả của một phép tính toán để tránh thực hiện lại mỗi khi component re-render.

```javascript
const ketQua = useMemo(() => {
    console.log("Đang tính toán...");
    return a * b;
}, [a, b]);
```

#### 4.2 Memoization
Là kỹ thuật tối ưu hóa tăng tốc độ ứng dụng bằng cách lưu lại kết quả của các phép tính tốn kém.

#### 4.3 Khi nào dùng useMemo?
- **Tính toán logic nặng**: Khi có hàm xử lý dữ liệu phức tạp
- **Giữ ổn định tham chiếu**: Tránh render thừa ở component con

### 5. useCallback

#### 5.1 useCallback là gì?
useCallback lưu lại tham chiếu của hàm qua các lần render.

```javascript
const handleBam = useCallback(() => {
    console.log("Bấm");
}, []);
```

#### 5.2 Sự kết hợp React.memo và useCallback

**React.memo**: HOC giúp tối ưu component bằng cách tránh render lại khi props không thay đổi.

```javascript
const Con = React.memo(function Con({ onBam }) {
    return <button onClick={onBam}>Bấm tôi</button>;
});

function Cha() {
    const [count, setCount] = useState(0);

    // Đóng băng tham chiếu hàm
    const handleBam = useCallback(() => {
        console.log("Bấm");
    }, []);

    return (
        <div>
            <button onClick={() => setCount(count + 1)}>Tăng</button>
            <Con onBam={handleBam} />
        </div>
    );
}
```

#### 5.3 Khi nào dùng useCallback?
- Truyền hàm vào component con được bọc bởi `React.memo`
- Hàm được dùng làm dependency của `useEffect` hoặc hook khác

### 6. useRef

#### 6.1 useRef là gì?
useRef tạo ra một object chứa giá trị tham chiếu với thuộc tính `.current`.

```javascript
const ref = useRef(initialValue);
```

**Đặc điểm**:
- Tồn tại xuyên suốt vòng đời của Component
- Thay đổi `.current` không gây re-render

#### 6.2 Khi nào dùng useRef?

**Trường hợp 1: Truy cập trực tiếp vào DOM**
```javascript
function FormNhapLieu() {
    const inputRef = useRef(null);

    const handleFocus = () => {
        inputRef.current.focus();
    };

    return (
        <div>
            <input ref={inputRef} type="text" />
            <button onClick={handleFocus}>Focus vào ô này</button>
        </div>
    );
}
```

**Trường hợp 2: Lưu trữ giá trị qua các lần render**
```javascript
function DongHoBamGio() {
    const [count, setCount] = useState(0);
    const timerIdRef = useRef(null);

    const start = () => {
        timerIdRef.current = setInterval(() => {
            setCount(prev => prev + 1);
        }, 1000);
    };

    const stop = () => {
        clearInterval(timerIdRef.current);
    };

    return (
        <div>
            <h1>{count}s</h1>
            <button onClick={start}>Start</button>
            <button onClick={stop}>Stop</button>
        </div>
    );
}
```

### 7. useContext

#### 7.1 Context là gì?
Context là cơ chế Dependency Injection cho phép chia sẻ dữ liệu toàn cục cho cây Component mà không cần truyền qua props.

**Mục đích**: Giải quyết vấn đề prop drilling (truyền props qua nhiều cấp bậc).

#### 7.2 Các bước sử dụng Context

**Bước 1: Tạo Context**
```javascript
import { createContext } from "react";
export const ThemeContext = createContext('light');
```

**Bước 2: Cung cấp Context**
```javascript
function App() {
    return (
        <ThemeContext.Provider value="dark">
            <NutBam />
        </ThemeContext.Provider>
    );
}
```

**Bước 3: Sử dụng Context**
```javascript
import { useContext } from "react";
import { ThemeContext } from "./ThemeContext";

function NutBam() {
    const themeHienTai = useContext(ThemeContext);

    return (
        <button className={themeHienTai}>
            Bấm tôi
        </button>
    );
}
```

#### 7.3 Khi nào nên sử dụng?
- Dữ liệu mang tính toàn cục (theme, ngôn ngữ, user info)
- Được sử dụng bởi nhiều Component ở các nhánh khác nhau

### 8. Component Patterns

#### 8.1 Composition (Kết hợp)

**Containment**: Component bao bọc không biết trước nội dung bên trong.

```javascript
function CaiHop(props) {
    return (
        <div style={{ border: '2px solid blue', padding: '10px' }}>
            {props.children}
        </div>
    );
}

function App() {
    return (
        <CaiHop>
            <h1>Xin chào</h1>
            <p>Đây là một đoạn văn bản.</p>
        </CaiHop>
    );
}
```

**Specialization**: Biến component chung thành component cụ thể.

```javascript
function Button(props) {
    return (
        <button style={{ backgroundColor: props.color }}>
            {props.children}
        </button>
    );
}

function DeleteButton() {
    return (
        <Button color="red">
            Xóa ngay lập tức!
        </Button>
    );
}
```

#### 8.2 Uncontrolled Components

Component mà DOM tự quản lý trạng thái, lấy giá trị qua `ref`.

```javascript
function FormUncontrolled() {
    const inputRef = useRef(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Giá trị nhập:", inputRef.current.value);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" ref={inputRef} defaultValue="Tung" />
            <button type="submit">Gửi</button>
        </form>
    );
}
```

**Ưu điểm**: Code ngắn gọn, phù hợp với form đơn giản  
**Nhược điểm**: Không kiểm soát real-time, khó validate

#### 8.3 Controlled Components

React hoàn toàn kiểm soát giá trị của input thông qua State.

```javascript
function FormControlled() {
    const [name, setName] = useState("Tung");

    const handleChange = (e) => {
        setName(e.target.value);
    };

    return (
        <form>
            <input 
                type="text" 
                value={name} 
                onChange={handleChange} 
            />
            <p>Bạn đang nhập: {name}</p>
        </form>
    );
}
```

**Ưu điểm**: Kiểm soát hoàn toàn, dễ validate real-time  
**Nhược điểm**: Code dài hơn, cần quản lý State

### 9. React Tree Root

#### 9.1 ReactDOM.createRoot(container)
Phương thức khởi tạo để tạo React Root cho một DOM Element cụ thể.

```javascript
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

const container = document.getElementById('root');
const root = ReactDOM.createRoot(container);

root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
```

#### 9.2 Component Tree
- Cấu trúc dữ liệu dạng cây đại diện cho giao diện người dùng
- Mỗi node là một component React hoặc Element
- Dữ liệu chảy đơn chiều từ cha xuống con

---

## REFRESH TOKEN
### 1. Refresh Token là gì?
**Refresh Token** là một mã thông báo đặc biệt được sử dụng để lấy **Access Token** mới mà không cần đăng nhập lại.

- Các đặc điểm của Refresh Token:
    - Mục đích: Lấy Access Token mới mà không cần đăng nhập lại.
    - Thời gian hết hạn: Dài hơn Access Token (7-30 ngày).
    - Lưu trữ: httpOnly cookie.
    - Gửi kèm: Chỉ gửi khi cần làm mới token
    - Rủi ro bảo mật: Thấp vì gửi ít lần

### 2. Luồng hoạt động Refresh Token
1. **Đăng nhập**

    User --[Username/Password]---> Server

2. **Server trả về tokens**

    User <--[Access Token + Refresh Token]-- Server

3. **Truy cập API**

    User --[Header: Bearer Access Token]---> API

    API <--[Response]-- Server

4. **Access Token hết hạn**

    User --[Header: Bearer Access Token cũ (hết hạn)]---> API

    API <--[401 Unauthorized]-- Server

5. **Refresh Token**

    User --[refresh_token]---> Endpoint /refresh

    User <--[New Access Token + New Refresh Token]-- Server

6. **Tiếp tục truy cập**

    User --[Header: Bearer New Access Token]---> API

    API <--[Response]-- Server

### 3. Tại sao cần Refresh Token ?
1. Tăng bảo mật

- Access Token có thời gian hết hạn ngắn -> giảm thiểu rủi ro nếu bị đánh cắp
- Refresh Token lưu trong httpOnly cookie -> không thể truy cập từ JavaScript

2. Trải nghiệm người dùng tốt
- Người dùng không cần đăng nhập lại liên tục
- Tự động làm mới token trong background

3. Kiểm soát phiên làm việc
- Server có thể thu hồi refresh token khi cần
- Có thể force logout user từ xa

### 4. Implement Refresh Token

#### 1. Cấu trúc thư mục
```js
src/
├── api/
│   ├── axiosInstance.js    // Cấu hình axios với interceptor
│   └── authApi.js           // API calls cho authentication
├── utils/
│   └── tokenManager.js      // Quản lý tokens
└── context/
    └── AuthContext.js       // Context cho authentication
```

#### 2. Tạo Axios Instance với Interceptor

```javascript
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:5000/api',
    timeout: 10000,
});

axiosInstance.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem('access_token');
        if (accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const refreshToken = localStorage.getItem('refresh_token');
                const response = await axios.post(
                    'http://localhost:5000/api/auth/refresh',
                    { refresh_token: refreshToken }
                );
                const { access_token, refresh_token } = response.data;
                localStorage.setItem('access_token', access_token);
                localStorage.setItem('refresh_token', refresh_token);

                originalRequest.headers.Authorization = `Bearer ${access_token}`;
                return axiosInstance(originalRequest);
            } catch (refreshError) {
                localStorage.removeItem('access_token');
                localStorage.removeItem('refresh_token');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
```

#### 5.3 Sử dụng trong Component

```javascript
import { useState } from 'react';
import axiosInstance from '../api/axiosInstance';

function UserProfile() {
    const [userData, setUserData] = useState(null);

    const fetchUserData = async () => {
        try {
            const response = await axiosInstance.get('/user/profile');
            setUserData(response.data);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div>
            <button onClick={fetchUserData}>Load Profile</button>
            {userData && <div>{JSON.stringify(userData)}</div>}
        </div>
    );
}
```

### 6. Best Practices cho Refresh Token

#### 6.1 Bảo mật

**Nên làm:**
- Lưu Refresh Token trong **httpOnly Cookie** (không thể truy cập bằng JavaScript)
- Implement **token rotation** (mỗi lần refresh tạo token mới)
- Có cơ chế **revoke token** khi cần
- Giới hạn thời gian sống: Access Token (15-30 phút), Refresh Token (7-30 ngày)
- Lưu Refresh Token hash trong database, không lưu plain text

**Không nên:**
- Lưu Refresh Token trong localStorage (dễ bị XSS attack)
- Để Refresh Token có thời gian sống quá dài
- Không xử lý concurrent requests cần refresh token
- Không validate token trước khi sử dụng

#### 6.2 Token Rotation

```javascript
// Backend: Tạo token mới và blacklist token cũ
async function refreshAccessToken(oldRefreshToken) {
    // 1. Verify old refresh token
    const payload = verifyToken(oldRefreshToken);
    
    // 2. Tạo tokens mới
    const newAccessToken = generateAccessToken(payload.userId);
    const newRefreshToken = generateRefreshToken(payload.userId);
    
    // 3. Blacklist token cũ
    await blacklistToken(oldRefreshToken);
    
    // 4. Lưu token mới vào database
    await saveRefreshToken(payload.userId, newRefreshToken);
    
    return { newAccessToken, newRefreshToken };
}
```

#### 6.3 Xử lý Concurrent Requests

```javascript
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

axiosInstance.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                // Đợi token mới
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    return axiosInstance(originalRequest);
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            return new Promise((resolve, reject) => {
                refreshTokenAPI()
                    .then(newToken => {
                        processQueue(null, newToken);
                        resolve(axiosInstance(originalRequest));
                    })
                    .catch(err => {
                        processQueue(err, null);
                        reject(err);
                    })
                    .finally(() => {
                        isRefreshing = false;
                    });
            });
        }

        return Promise.reject(error);
    }
);
```

#### 6.4 Automatic Token Refresh

```javascript
// Tự động refresh trước khi token hết hạn
import { jwtDecode } from 'jwt-decode';

function useAutoRefreshToken() {
    useEffect(() => {
        const token = localStorage.getItem('access_token');
        if (!token) return;

        const decoded = jwtDecode(token);
        const expiresIn = decoded.exp * 1000 - Date.now();
        
        // Refresh 5 phút trước khi hết hạn
        const timeoutId = setTimeout(async () => {
            try {
                await authApi.refreshToken();
            } catch (error) {
                console.error('Auto refresh failed:', error);
            }
        }, expiresIn - 5 * 60 * 1000);

        return () => clearTimeout(timeoutId);
    }, []);
}
```

---

## TUẦN 5: UPLOAD FILE LÊN CLOUD STORAGE

### 1. Tổng quan về Cloud Storage

Cloud Storage là dịch vụ lưu trữ dữ liệu trên đám mây, cho phép lưu trữ và truy xuất file từ bất kỳ đâu qua Internet.

**Lợi ích:**
- Không cần quản lý server storage
- Tự động scale theo nhu cầu
- Độ tin cậy và bảo mật cao
- Tích hợp CDN để tải nhanh
- Có API dễ sử dụng

### 2. So sánh AWS S3, Cloudinary và MinIO

#### 2.1 AWS S3 (Amazon Simple Storage Service)

**S3 là gì ?**
- Dịch vụ object storage của Amazon Web Services
- Lưu trữ và truy xuất bất kỳ lượng dữ liệu nào
- Độ tin cậy 99.999999999%

**Ưu điểm:**
- Khả năng mở rộng không giới hạn
- Tích hợp tốt với AWS ecosystem
- Nhiều storage classes (Standard, Glacier...)
- Versioning và lifecycle management

**Nhược điểm:**
- Chi phí có thể cao khi scale
- Cấu hình phức tạp cho người mới
- Phụ thuộc vào vendor AWS

**Khi nào dùng:**
- Enterprise, scale lớn
- Đã sử dụng các dịch vụ AWS khác
- Cần độ tin cậy cực cao
- Budget không phải vấn đề

#### 2.2 Cloudinary

**Cloudinary là gì ?**
- Nền tảng quản lý media (ảnh, video) toàn diện
- Tích hợp sẵn CDN và image transformation
- Upload widget UI sẵn có

**Ưu điểm:**
- Dễ sử dụng, setup nhanh
- Tự động tối ưu hóa media (resize, format, quality)
- Real-time transformation URL-based

**Nhược điểm:**
- Chỉ tối ưu cho media files
- Chi phí cao khi vượt free tier
- Phụ thuộc vào third-party service

**Khi nào dùng:**
- App tập trung vào media (ảnh, video)
- Startup, cần MVP nhanh
- Cần tính năng transform, optimize sẵn có

#### 2.3 MinIO

**MinIO là gì ?**
- Object storage mã nguồn mở
- Tương thích 100% API với S3
- Self-hosted trên infrastructure riêng

**Ưu điểm:**
- Mã nguồn mở, miễn phí
- Tự kiểm soát hoàn toàn data
- Performance cao, low latency
- S3-compatible (dễ migrate)
- Phù hợp data nhạy cảm

**Nhược điểm:**
- Cần tự quản lý infrastructure
- Cần kiến thức DevOps
- Không có features nâng cao như Cloudinary
- Tự chịu trách nhiệm về backup, security

**Khi nào dùng:**
- Cần tự quản lý data (compliance, privacy)
- Data nhạy cảm không thể lên public cloud
- Có team DevOps
- Muốn tiết kiệm chi phí dài hạn

#### 2.4 Bảng so sánh

| Tiêu chí | AWS S3 | Cloudinary | MinIO |
|----------|--------|------------|-------|
| **Loại** | Managed Cloud | Managed Media | Self-hosted |
| **CDN** | CloudFront (riêng) | Tích hợp sẵn | Tự setup |
| **Transform** | Lambda function | URL-based | Không có |
| **API** | AWS SDK | REST + Widget | S3-compatible |
| **Use case** | General storage | Media-heavy apps | Private cloud |
