# Tìm hiểu về React

## State
- State là một React Hook mà cho phép thêm biến trạng thái vào trong component của mình.

Cú pháp:

```js
const [state, setState] = useState(initialState)
```

State là một tập hợp các dữ liệu có thể thay đổi, được quản lý nội bộ bởi chính Component đó. State đại diện cho trạng thái của Component tại một thời điểm cụ thể trong vòng đời của nó. Bất kỳ sự thay đổi nào đối với State đều sẽ kích hoạt quá trình re-render để cập nhật giao diện người dùng cho khớp với dữ liệu mới.

## Khi nào component re-render ?

Có 3 trường hợp khiến component re-render

- **State thay đổi**: Khi gọi hàm `setState`
- **Props thay đổi**: Khi component cha đưa ra chỉ thị mới
- Khi component cha re-render: Component con cũng sẽ re-render theo

## State bất biến

- Trong React, không bao giờ được thay đổi trực tiếp từ State
- Khi muốn thay đổi, chúng ta sử dụng `setState` để thay đổi. Tuy nhiên bản chất của việc sử dụng này là chúng ta sinh ra một bản sao mới thay vì là thay đổi cái cũ
- Ngoài ra, chúng ta dùng `...spread` khi cập nhật trạng thái của một object hoặc là array.

## State Lifting (Nâng trạng thái lên)

- Nguyên lý: Nếu Component A và Component B cần dùng chung một dữ liệu, ta phải cắt dữ liệu đó khỏi A và B, chuyển nó lên Component Cha gần nhất của cả hai. Sau đó cha sẽ truyền dữ liệu xuống cho 2 con A và B qua Props.

- Ví dụ minh họa:

    - Cha: `GiaDinh`
    - Con 1: `AnhTrai`
    - Con 2: `EmGai`

- Mục tiêu: Khi `AnhTrai` tiêu tiền, màn hình `EmGai` phải cập nhật số dư mới

- Bước 1: Viết Component cho Con 1

```js
const AnhTrai = (props) => {
    return (
        <div style={{ border: '1px solid blue', padding: '10px', margin: '10px' }}>
            <h3>Anh Trai</h3>
            <p>Số dư anh thấy: {props.tien} VNĐ</p>
            <button onClick={() => props.hamTieuTien(10000)}>
                Tiêu 10.000 mua trà sữa
            </button>  
        </div>
    );
};
```

- Bước 2: Viết Components cho Con 2
```js
const EmGai = (props) => {
    return (
        <div style={{ border: '1px solid pink', padding: '10px', margin: '10px' }}>
            <h3>Em Gái</h3>
            <p>Em nhìn thấy số dư còn lại là: <b>{props.tien} VNĐ</b></p>
        </div>
    );
};
```

- Bước 3: Viết Components cha

Lifting Up State được khai báo tại đây
```js
import React, { useState } from 'react';

const GiaDinh = () => {
    const [soDu, setSoDu] = useState(100000);

    const xuLyTieuTien = (soTienTieu) => {
        setSoDu(soDu - soTienTieu);
    };

    return (
        <div style={{ border: '2px solid black', padding: '20px' }}>
            <h1>Gia Đình (Quản lý két sắt)</h1>
            <p>Két sắt tổng: {soDu} VNĐ</p>
        
            <div style={{ display: 'flex' }}>
                <AnhTrai tien={soDu} hamTieuTien={xuLyTieuTien} />

                <EmGai tien={soDu} />
            </div>
        </div>
    );
};

export default GiaDinh;
```

## UseEffect

- useEffect là một Hook cho phép thực hiện các Side Effects bên trong Fucntional Components.
- Nó được thiết kế để đồng bộ hóa các hệ thống bên ngoài mà bản thân React không kiểm soát được.
- useEffect chạy bất đồng bộ và được kích hoạt sau khi React đã cập nhật DOM và vẽ giao diện, đảm bảo không chặn quá trình hiển thị UI của người dùng

- Dependency Array của useEffect chính là tham số thứ hai. Nó có vai trò là kiểm tra trước khi cho phép đoạn code bên trong useEffect chạy. 

### Có 3 trường hợp kiểm tra hàm useEffect

#### **Trường hợp 1**: Không có mảng nào cả

```js
useEffect(() => {
    console.log("Tung dep trai");
});
```

- Nó sẽ chạy mỗi khi render
- Nếu trong đây gọi `setState`, Component sẽ render lại -> Effect chạy lại -> `setState` -> Render -> Infinitie Loop..
- Chỉ nên dùng khi muốn mọi thứ xảy ra trên component

#### **Trường hợp 2**: Chứa mảng rỗng
```js
useEffect(() => {
    console.log("Tung dep trai");
}, []);
```

- Chỉ chạy duy nhất 1 lần sau khi Component được gắn vào giao diện.
- Dùng khi:
    - Gọi API lấy dữ liệu ban đầu
    - Đăng ký sự kiện
    - Khởi tạo các thư viện bên thứ 3

#### **Trường hợp 3**: Array có giá trị
```js
useEffect(() => {
    console.log("Ai roi cung khac");
}, [userID, filter]);
```

- Chạy lại nếu và chỉ nếu các biến một trong các biến trong mảng thay đổi giá trị so với lần render trước.


#### Ngoài ra

- React so sánh các biến trong useEffect bằng toán tử `===`
- Cần chú ý đến việc sử dụng dependency, nếu ta sử dụng Object, rất có thể nếu truyền vào một object có nội dung y hệt nhưng useEffect vẫn chạy, lý do là vì nó sẽ so sánh tham chiếu chứ không so sánh nội dung bên trong. Dẫn đến việc bản chất ta đang so sánh 2 địa chỉ trong bộ nhớ. Vì vậy nó đã khác so với lần render trước đó nên nó sẽ chạy lại.

### Cleanup Function

- Trong React, nó là một cơ chế teardown. Được sử dụng để giải phóng tài nguyên, hủy bỏ các đăng ký, hoặc dọn dẹp các tác vụ phụ đã được thiết lập trong lần render trước đó nhằm ngăn chặn **Memory Leaks** và **Race Conditions**.

- Thời điểm Cleanup Function chạy:
    - Trước khi Effect chạy lại lần tiếp theo (nhằm mục đích dọn dẹp tàn dư của lần render trước).
    - Ngay trước khi Components bị Unmount (bị xóa khỏi UI).

### Các patterns phổ biến nhất

#### Parttern A: Sự kiện DOM

- Nếu lắng nghe sự kiện chuột hoặc bàn phím mà không có dọn dẹp, mỗi lần component render lại, sẽ tạo ra một sự kiện mới. Sau 10 lần render sẽ có 10 hàm chạy cùng một lúc -> Giật lag

```js
useEffect(() => {
    const handleResize = () => {
        console.log("Kich thuoc man hinh:", window.innerWidth);
    };

    window.addEventListener('resize', handleResize);

    return () => {
        window.removeEventListener('resize', handleResize);
    };
}, []);
```

#### Pattern B: Subscriptions

- Tương tự Event, nếu dùng `setInterval` mà không xóa nó đi, cái đồng hồ sẽ chạy ngầm bên dưới, ngay cả khi em đã chuyển sang trang khác.

```js
useEffect(() => { 
    const timerId = setinterval(() => {
        console.log("+1 giây nhé");
    }, 1000);

    return () => {
        clearInterval(timerId);
        console.log("Da dung dong ho !");
    };
}, []);
```

#### Pattern C: Fetching data và Race Conditions

- Vấn đề phổ biến: Khi vào trang `User/1`, mạng lag, dữ liệu chưa trả data về ta đã chuyển sang `User/2`.

    - Effect 2 chạy: Gọi API user 2.
    - Lúc này, API user 1 mới phản hồi xong -> Nó cập nhật State -> Hiển thị thông tin User 1 đè lên User 2. Đây chính là Race Conditions.

- Cách khắc phục: Ta sẽ sử dụng một `flag` nhằm bỏ qua kết quả cũ

```js
useEffect(() => {
    let isCancelled = false;

    const fetchData = async () => {
        const response = fetch(`http://api/user/${userId}`);
        const data = await response.json();

        if (!isCancelled)
            setUser(data);
    };

    return () => {
        // Huy ham
        isCancelled = true;
    };
}, [userId]);
```

## useMemo

### useMemo là gì ?

Là một hook trong React giúp lưu trữ (cache) kết quả một phép tính toán

- Nó giúp React nhớ kết quả của lần tính trước

- Nó chỉ tính toán lại khi các biến đầu vào (dependencies) thay đổi.

- Mục đích: Tránh thực hiện các việc nặng mỗi khi component bị re-render.

### Cú pháp

```js
const vinhTung = useMemo(deptrai, mangPhuThuoc);
```

- `deptrai` là một hàm được tính toán trả về kết quả và lưu cho biến `vinhTung`.
- `mangPhuThuoc`: là danh sách biến mà phép tính phụ thuộc vào.
- Giống hệt Effect:
    - Nếu biến trong dependencies thay đổi thì hàm sẽ chạy lại.
    - Nếu biến không thay đổi -> React sẽ trả về lại giá trị cũ đã được lưu ở lần chạy trước đó.

Ví dụ đơn giản: 
```js
import { useMemo } from 'react';

function sum({ a, b }) {
    const ketQua = useMemo(() => {
        console.log("Dang tinh toan");
        return a * b;
    }, [a, b]);

    return <div>Ket qua: {ketQua}</div>;
}
```

### Memoization là gì ?

Là một kỹ thuật tối ưu hóa nhằm tăng tốc độ ứng dụng bằng cách lưu lại kết quả của các phép tính tốn kém và trả về kết quả đã lưu khi các đầu vào không thay đổi.

### Khi nào dùng `useMemo`

- Trong React, mặc định mỗi khi Component re-render, **mọi dòng code** trong component đó sẽ chạy lại từ đầu.

- Ta sẽ dùng `useMemo` trong 2 trường hợp chính: 
    - **Trường hợp A: Tính toán lại các logic nặng**
        - Khi có một hàm dữ liệu xử lý phức tạp
    - **Trường hợp B: Giữ ổn định tham chiếu**
        - Dùng để tránh render thừa ở các component con khi props không thay đổi

### Dependency Array trong `useMemo`

- Cách hoạt động giống như `useEffect`, chỉ khác mục đích sử dụng

## useCallback


### Khái niệm
- useCallback giống như useMemo, nhưng thay vì lưu kết quả của phép tính, nó lưu lại tham chiếu của hàm qua các lần render, trừ khi các biến trong dependency thay đổi

- Cú pháp:
```js
const vinhTung = useCallback(deptrai, mangPhuThuoc);
```

### Sự kết hợp của `React.memo` và `useCallback`

- `React.memo` là một Higher Order Component (HOC) giúp tối ưu hóa component bằng cách tránh render lại khi props không thay đổi

```js
import React from 'react';

const Con = React.memo(function Con({ onBam }) {
  console.log("Con đã render!");
  return <button onClick={onBam}>Bấm con đi</button>;
});
```

- Con chỉ re-render khi props thay đổi

- Vấn đề là: với `onBam` ta đã truyền vào một hàm, mỗi lần render lại ta sẽ tạo ra một hàm mới

- Mỗi hàm được coi là một object. Khi truyền vào props, React sẽ so sánh tham chiếu của object này với tham chiếu trước đó. Nếu tham chiếu thay đổi -> React sẽ coi là props thay đổi -> Con sẽ re-render

- Vậy làm thế nào để tránh re-render thừa của con ? Rất đơn giản ta sẽ dùng `useCallback` để đóng băng tham chiếu của hàm

```js
import React, { useCallback, useState } from 'react';

function Cha() {
    const [count, setCount] = useState(0);

    const handleBam = useCallback(() => {
        console.log("Bam");
    }, []);

    return (
        <div>
            <h1>Cha đếm: {count}</h1>
            <button onClick={() => setCount(count + 1)}>Bấm</button>
            <Con onBam={handleBam} />
        </div>
    );
}
```

- Con nhận props `onBam` cũ và `React.memo` sẽ coi là props không thay đổi -> Con sẽ không re-render

### Khi nào dùng `useCallback`

Nên dùng khi:
    - Truyền hàm vào component con mà được đọc bởi `React.memo`
    - Hàm đó được dùng làm dependency của `useEffect` của một hook khác

## React Tree Root

### ReactDOM.createRoot(container)

- Là phương thức khởi tạo, dùng để tạo ra một React Root cho một DOM Element cụ thể

- React Root đóng vai trò là môi trường quản lý cao nhất, cho phép kích hoạt các tính năng Concurrent của React. Nó chịu trách nhiệm quản lý quy trình render và cập nhật DOM thật bên trong container đó.

### Component Tree

- Là một cấu trúc dữ liệu dạng cây đại diện cho giao diện người dùng.

- Mỗi node trong cây đại diện cho một component React hoặc một Element. Dữ liệu chảy đơn chiều từ nút cha xuống nút con.

### Root vs Component Tree

- Root là nút gốc của cây component tree. Nó đại diện cho React Root đã được tạo ra thông qua `ReactDOM.createRoot(container)`

- Root chịu trách nhiệm quản lý quy trình render và cập nhật DOM thật bên trong container

- Component Tree là nội dung. Là thực thể động, liên tục thay đổi, sinh ra và mất đi bên trong root.

```js
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

## useRef

### Khái niệm

- useRef là một hook trong React giúp tạo ra một object chứa giá trị tham chiếu (reference) đến một giá trị. Đối tượng này có một thuộc tính duy nhất là `.current`, được tạo với giá trị ban đầu truyền vào.

    - Đối tượng `ref` này tồn tại xuyên suốt vòng đời của Component. React đảm bảo trả về cùng một tham chiếu trong mỗi lần render.

    - Việc thay đổi giá trị của thuộc tính `.current` không gây ra re-render Component. Đây là điểm khác biệt so với `useState`

### Cú pháp
```js
const ref = useRef(initialValue);
```

### Sử dụng khi nào ?

- Thường được sử dụng cho 2 mục đích chính

- Trường hợp 1: Truy cập trực tiếp vào DOM thật
    - Đây là việc bỏ qua các Virtual DOM để điều khiển các phần tử HTML trực tiếp

```js
import React, { useRef } from 'react';

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

- Trường hợp 2: Giữ ổn định tham chiếu

```js
import React, { useState, useEffect, useRef } from 'react';

function DemSoLanRender() {
  const [count, setCount] = useState(0);
  
  const renderCount = useRef(0);

  useEffect(() => {
    renderCount.current = renderCount.current + 1;
    console.log(`Đã render lần thứ: ${renderCount.current}`);
  });

  return (
    <div>
      <h1>Count: {count}</h1>
      <button onClick={() => setCount(count + 1)}>Tăng Count</button>
    </div>
  );
}
```

### Công dụng

#### Lưu trữ qua các lần Render

Đôi khi muốn lưu một biến số nào đó mà không muốn nó bị reset khi component re-render

Ví dụ trong đồng hồ đếm giây:

```js
function DongHoBamGio() {
  const [count, setCount] = useState(0);
  
  const timerIdRef = useRef(null);

  const start = () => {
    if (timerIdRef.current) return;

    timerIdRef.current = setInterval(() => {
      setCount(prev => prev + 1);
    }, 1000);
  };

  const stop = () => {
    clearInterval(timerIdRef.current);
    timerIdRef.current = null;
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

#### Lưu trữ giá trị cũ

- Chúng ta có kỹ thuật cập nhật Ref sau khi Render

```js
function SoSanhGiaTri() {
  const [count, setCount] = useState(0);
  
  const prevCountRef = useRef();

  useEffect(() => {
    prevCountRef.current = count;
  }, [count]);

  return (
    <div>
      <h1>Hiện tại: {count}</h1>
      <h2>Lần trước: {prevCountRef.current}</h2>
      
      <button onClick={() => setCount(count + 1)}>Tăng</button>
    </div>
  );
}
```

## useContext

### Khái niệm

- Context là một cơ chế Dependency Injection được tích hợp sẵn trong React. Nó cho phép chia sẻ dữ liệu được coi là toàn cục cho một cây Component mà không cần phải truyền qua props một cách thủ công qua từng cấp bậc trung gian

- Nhằm mục đích để giải quyết vấn đề prop drilling

- Tóm lại:
    - Nếu sử dụng props cho Component con thì phải truyền qua từng cấp bậc trung gian, ví dụ từ Component cha -> Component trung gian -> Component con
    - Nếu sử dụng Context thì không cần truyền qua các cấp bậc trung gian, Component con có thể truy cập trực tiếp vào dữ liệu

### Các bước để sử dụng

Bước 1: Tạo Context

```js
import { createContext } from "react";

export const ThemeContext = createContext('light');
```

Bước 2: Cung cấp Context

Context object tạo một Component Provider. Nó có nhiệm vụ bao bọc lấy khu vực muốn phủ sóng.

```js
function App() {
  return (
    <ThemeContext.Provider value="dark">
      <NutBam />
    </ThemeContext.Provider>
  );
}
```

Bước 3: Sử dụng Context

```js
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

### Khi nào nên sử dụng ?

- Dữ liệu mang tính toàn cục hoặc được sử dụng bởi nhiều Component ở các nhánh khác nhau trong cây:


## Component Patterns

### Khái niệm

- Là nghệ thuật sắp xếp code sao cho sạch, dễ tái sử dụng và dễ bảo trì.

#### Phần 1: Composition

- là một mô hình thiết kế nền tảng trong React. Nó cho phép xây dựng các giao diện người dùng phức tạp bằng cách kết hợp nhiều Component nhỏ, độc lập và đơn giản lại với nhau.

- Composition giải quyết 2 bài toán lớn:
    - Containment: Một component bao bọc bên ngoài không biết trước bên trong sẽ chứa nội dung gì
    - Specialization: Biến một component chung chung thành một component cụ thể

##### Kỹ thuật Containment
Đây là kỹ thuật phổ biến. Nó sẽ làm một cái hộp thoại hoặc một cái thẻ. Nội dung bên trong nó sẽ thay đổi

```js
function CaiHop(props) {
  return (
    <div style={{ border: '2px solid blue', padding: '10px', borderRadius: '8px' }}>
      {props.children} 
    </div>
  );
}

function App() {
  return (
    <div>
      {/* Lần 1: Nhét chữ vào hộp */}
      <CaiHop>
        <h1>Xin chào</h1>
        <p>Đây là một đoạn văn bản.</p>
      </CaiHop>

      <br />

      {/* Lần 2: Nhét cả một cái Form vào hộp */}
      <CaiHop>
        <label>Nhập tên:</label>
        <input type="text" />
        <button>Gửi</button>
      </CaiHop>
    </div>
  );
}
```

#### Specialization

- Là kỹ thuật cho phép biến một component chung chung thành một component cụ thể.

```js
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

### Phần 2: Uncontrolled Components

#### Khái niệm

- Là mô hình quay về thời kỳ HTML cổ điển, nơi mà DOM tự quản lý trạng thái của chính nó.

- React không theo dõi giá trị của input, thay vào đó ta sẽ lấy giá trị từ DOM khi cần thiết thông qua `ref`.

- Phù hợp khi:
    - Form đơn giản, không cần validation real-time
    - Tích hợp với thư viện bên thứ 3 không thuộc React
    - Không cần kiểm soát chặt chẽ dữ liệu nhập vào

#### Ví dụ minh họa

```js
import React, { useRef } from 'react';

function FormUncontrolled() {
  const inputRef = useRef(null);

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Giá trị nhập:", inputRef.current.value);
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>Nhập tên của bạn:</label>
      <input type="text" ref={inputRef} defaultValue="Tung" />
      <button type="submit">Gửi</button>
    </form>
  );
}
```

#### Đặc điểm

- **Ưu điểm**:
    - Code ngắn gọn hơn
    - Không cần State và xử lý onChange
    - Phù hợp với các form đơn giản

- **Nhược điểm**:
    - Không kiểm soát được giá trị real-time
    - Khó validate trong khi nhập liệu
    - Không phù hợp với logic phức tạp

### Phần 3: Controlled Components

#### Khái niệm

- Là mô hình mà React hoàn toàn kiểm soát giá trị của input thông qua State.

- Mỗi lần người dùng nhập, giá trị mới được cập nhật vào State và React sẽ hiển thị lại giá trị đó.

- Đây là cách tiếp cận được khuyến nghị trong React vì nó cho phép kiểm soát và validate dữ liệu một cách linh hoạt.

#### Ví dụ minh họa

```js
import React, { useState } from 'react';

function FormControlled() {
  const [name, setName] = useState("Tung");

  const handleChange = (e) => {
    setName(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Giá trị nhập:", name);
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>Nhập tên của bạn:</label>
      <input 
        type="text" 
        value={name} 
        onChange={handleChange} 
      />
      <p>Bạn đang nhập: {name}</p>
      <button type="submit">Gửi</button>
    </form>
  );
}
```

#### Đặc điểm

- **Ưu điểm**:
    - Kiểm soát hoàn toàn giá trị input
    - Dễ dàng validate trong khi nhập liệu
    - Có thể format dữ liệu real-time
    - Dễ đồng bộ hóa với các component khác

- **Nhược điểm**:
    - Code dài hơn so với Uncontrolled
    - Cần quản lý State cho mỗi input
    - Có thể gây re-render nhiều hơn


## Link Demo
https://youtu.be/Dh6dUMffLtI