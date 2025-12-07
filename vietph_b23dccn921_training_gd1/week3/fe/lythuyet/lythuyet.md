## 1. Biến
- Biến là “hộp chứa dữ liệu” trong lập trình — dùng để lưu trữ giá trị (số, chữ, mảng, đối tượng,…).
```js
let x = 5;      // có thể thay đổi giá trị
const PI = 3.14; // hằng số , không  thể thay đổi giá trị
var y = 10;
````

---

## 2. Cấu trúc điều khiển
### Rẽ nhánh

```js
if (x > 5) console.log("Lớn hơn 5");
else console.log("Không lớn hơn 5");

switch (day) {
  case 1: console.log("Thangs 1"); break;
  case 2: console.log("Tháng 2"); break;
  default: console.log("Tháng khác");
}
```

### Vòng lặp
- Vòng lặp là cấu trúc giúp lặp lại một khối lệnh nhiều lần, đến khi điều kiện nào đó không còn đúng.
```js
for (let i = 0; i < 5; i++) console.log(i);
while (x > 0) { x--; }
```

---

## 3. Hàm (Function)
- Hàm là một khối mã (code block) thực hiện một nhiệm vụ cụ thể, có thể tái sử dụng nhiều lần.
```js
function sum(a, b) {
  return a + b;
}

const sum = (a, b) => a - b; // arrow function
```

---

## 4. Mảng (Array)
- Là danh sách các giá trị, được lưu trữ trong một biến duy nhất.
- Mỗi phần tử có chỉ số (index) bắt đầu từ 0.
```js
const arr = [1, 2, 3];
arr.push(4);       // [1,2,3,4]
arr.map(x => x*2); // [2,4,6,8]
arr.filter(x => x%2===0); // [2,4]
```

---

## 5. Xâu (String)
- Là chuỗi các ký tự, thường được dùng để lưu trữ văn bản, dữ liệu chó thể chứa cả kí tự , số , kí tự đặc biệt
```js
let s = "Hello JS";
console.log(s.length);   // 8
console.log(s.toUpperCase()); // "HELLO JS"
console.log(s.includes("JS")); // true
```

---

## 6. Callback

- Là hàm được truyền vào một hàm khác như là đối số, và được gọi lại sau khi nhiệm vụ hoàn tất.
-> Thường dùng trong lập trình bất đồng bộ (asynchronous programming).
```js
function fetchData(callback) {
  setTimeout(() => {
    console.log("Lấy dữ liệu xong!");
    callback();
  }, 1000);
}

fetchData(() => console.log("Hiển thị dữ liệu!"));
```

### Callback hell 
“Callback Hell” xảy ra khi bạn lồng quá nhiều hàm callback bên trong nhau, khiến code rối, khó đọc, khó bảo trì.
```js
doA(() => {
  doB(() => {
    doC(() => console.log("Xong!"));
  });
});
```

---

## 7. Lập trình hướng đối tượng (OOP)
- Là lập trình hướng đối tượng, tổ chức chương trình quanh đối tượng (object) – bao gồm thuộc tính (property) và phương thức (method).
```js
class Person {
  constructor(name, age) {
    this.name = name;
    this.age = age;
  }
  sayHi() {
    console.log(`Hi, I'm ${this.name}`);
  }
}

const p1 = new Person("Việt", 21);
p1.sayHi();
```

> * **Class**: khuôn mẫu
> * **Object**: đối tượng tạo ra từ class
> * **Reference**: gán theo địa chỉ, không phải bản sao

---

## 8. DOM (Document Object Model)

> -DOM là cách trình duyệt biểu diễn cấu trúc của trang web (HTML) dưới dạng một cây đối tượng (object tree) mà JavaScript có thể truy cập và thao tác.
```js
const btn = document.querySelector("button");
btn.addEventListener("click", () => {
  document.body.style.background = "lightblue";
});
```

---

## 9. JSON (JavaScript Object Notation)
- JSON là định dạng dữ liệu (data format) nhẹ, dùng để trao đổi thông tin giữa máy khách (client) và máy chủ (server).
- JSON không phải là ngôn ngữ lập trình, mà là chuỗi văn bản (string) tuân theo cú pháp rất giống Object trong JavaScript.
```js
const obj = { name: "Việt", age: 21 };
const json = JSON.stringify(obj); // object → JSON string
{
  "name": "Việt",
  "age": 21,
}
const back = JSON.parse(json);    // JSON → object
```

---

## 10. Promise
- Promise là một đối tượng biểu diễn một thao tác bất đồng bộ (asynchronous) — tức là kết quả sẽ có trong tương lai, chứ không có ngay lập tức.

Promise có 3 trạng thái (states):

+ pending — đang chờ xử lý

+ fulfilled — hoàn thành thành công (resolve)

+ rejected — thất bại (reject)
->  Giúp xử lý bất đồng bộ (asynchronous)

```js
const p = new Promise((resolve, reject) => {
  setTimeout(() => resolve("Thành công!"), 1000);
});

p.then(msg => console.log(msg)).catch(err => console.error(err));
```

### Promise.all
- Dùng để chờ nhiều Promise chạy song song → chỉ tiếp tục khi tất cả đều hoàn tất.
```js
Promise.all([
  fetch("/api/user"),
  fetch("/api/posts")
]).then(res => console.log("Xong tất cả!"));
```

---

## 11. async / await
- async/await là cú pháp mới hơn của Promise, giúp viết code bất đồng bộ trông như đồng bộ, dễ đọc và dễ bảo trì.

+ async biến một hàm thành hàm bất đồng bộ, tự động trả về Promise.

+ await dùng bên trong hàm async, tạm “dừng” cho đến khi Promise hoàn tất.
-> Cú pháp dễ đọc hơn Promise

```js
async function getData() {
  try {
    const res = await fetch("https://jsonplaceholder.typicode.com/users");
    const data = await res.json();
    console.log(data);
  } catch (err) {
    console.error(err);
  }
}

getData();
