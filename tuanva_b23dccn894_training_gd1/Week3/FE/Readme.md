# Báo cáo Kiến Thức JavaScript Cơ Bản

## 1. Biến (Variable)

**Lý thuyết:**  
Biến là vùng nhớ dùng để lưu trữ dữ liệu mà chương trình có thể truy cập và thay đổi trong quá trình thực thi.  
JavaScript có ba cách khai báo biến:

- `var`: khai báo biến có phạm vi toàn cục hoặc phạm vi trong hàm. Tuy nhiên, `var` có thể bị **hoisting** (kéo lên đầu phạm vi) và không có phạm vi khối, nên ít được dùng trong ES6 trở lên.
- `let`: khai báo biến có phạm vi khối `{}`. Đây là cách phổ biến nhất hiện nay.
- `const`: dùng để khai báo hằng số – giá trị không thể thay đổi sau khi gán.

**Ví dụ:**

```javascript
let name = "Tuan";
const PI = 3.14;
var age = 20;
console.log(name, PI, age);
```

## 2. Vòng lặp (Loop)

**Lý thuyết:**  
Vòng lặp giúp tự động hóa việc lặp đi lặp lại một khối lệnh. JavaScript hỗ trợ nhiều loại vòng lặp:

- `for`: lặp với điều kiện khởi tạo, điều kiện kiểm tra và bước tăng.
- `while`: lặp khi điều kiện còn đúng.
- `do...while`: thực thi ít nhất một lần trước khi kiểm tra điều kiện.
- `for...of`: duyệt qua các phần tử của mảng hoặc chuỗi.
- `for...in`: duyệt qua các thuộc tính của đối tượng.

**Ví dụ:**

```javascript
for (let i = 0; i < 5; i++) {
  console.log("Lần thứ", i);
}
```

## 3. Rẽ nhánh (Conditional)

**Lý thuyết:**  
Rẽ nhánh cho phép chương trình lựa chọn hướng thực thi dựa trên điều kiện logic (`true` hoặc `false`). Các cấu trúc chính:

- `if` – kiểm tra điều kiện.
- `else if` – kiểm tra thêm điều kiện khác nếu cái trước sai.
- `else` – chạy khi tất cả điều kiện đều sai.
- `switch` – so sánh nhiều giá trị với một biểu thức.

**Ví dụ:**

```javascript
let score = 80;
if (score >= 90) console.log("Xuất sắc");
else if (score >= 70) console.log("Khá");
else console.log("Trung bình");
```

## 4. Hàm (Function)

**Lý thuyết:**  
Hàm là khối mã được đặt tên, có thể được gọi lại nhiều lần. Giúp giảm lặp lại và tăng khả năng tái sử dụng.  
Có 3 cách định nghĩa:

1. **Function Declaration:**
   ```javascript
   function sum(a, b) {
     return a + b;
   }
   ```
2. **Function Expression:**
   ```javascript
   const sum = function (a, b) {
     return a + b;
   };
   ```
3. **Arrow Function:** (ngắn gọn, không có `this` riêng)
   ```javascript
   const sum = (a, b) => a + b;
   ```

**Ví dụ:**

```javascript
function sum(a, b) {
  return a + b;
}
console.log(sum(3, 5));
```

## 5. Mảng (Array)

**Lý thuyết:**  
Mảng lưu trữ danh sách các phần tử theo thứ tự. Có thể chứa nhiều kiểu dữ liệu khác nhau.  
Các phương thức thường dùng:

- `push()`, `pop()` – thêm/xóa phần tử ở cuối.
- `shift()`, `unshift()` – thêm/xóa phần tử ở đầu.
- `map()`, `filter()`, `reduce()` – xử lý và tạo ra mảng mới.
- `forEach()` – duyệt qua từng phần tử mà không trả giá trị.

**Ví dụ:**

```javascript
let numbers = [1, 2, 3, 4];
let doubled = numbers.map((n) => n * 2);
console.log(doubled);
```

## 6. Xâu (String)

**Lý thuyết:**  
Xâu (string) là dãy ký tự được bao bởi dấu nháy đơn `' '`, nháy kép `" "`, hoặc dấu backtick `` ` `` (template literal).  
Các hàm xử lý xâu phổ biến:

- `length`, `toUpperCase()`, `toLowerCase()`, `slice()`, `substring()`, `replace()`, `includes()`, `split()`.

**Ví dụ:**

```javascript
let text = "JavaScript";
console.log(text.slice(0, 4)); // "Java"
```

## 7. Callback và Callback Hell

**Lý thuyết:**  
Callback là hàm được truyền làm đối số vào hàm khác, dùng để xử lý tác vụ bất đồng bộ như đọc file, request API.  
**Callback Hell** xảy ra khi nhiều callback lồng nhau, làm mã khó đọc và bảo trì.

**Ví dụ:**

```javascript
function doTask(task, callback) {
  console.log("Đang thực hiện:", task);
  callback();
}
doTask("Học JS", function () {
  console.log("Xong!");
});
```

## 8. Lập trình hướng đối tượng (OOP)

**Lý thuyết:**  
JavaScript hỗ trợ lập trình hướng đối tượng thông qua `class` và `object`. Các khái niệm chính:

- **Class:** bản thiết kế (mẫu) cho đối tượng.
- **Object:** thể hiện cụ thể của class.
- **Reference:** khi gán object cho biến khác, chỉ có địa chỉ (tham chiếu) được sao chép.  
  OOP trong JavaScript hỗ trợ **kế thừa**, **đóng gói**, **đa hình**.

**Ví dụ:**

```javascript
class Person {
  constructor(name, age) {
    this.name = name;
    this.age = age;
  }
  introduce() {
    console.log("Tôi là", this.name);
  }
}
const p = new Person("Tuan", 21);
p.introduce();
```

## 9. DOM (Document Object Model)

**Lý thuyết:**  
DOM là mô hình cây biểu diễn toàn bộ cấu trúc HTML. JavaScript có thể truy cập và chỉnh sửa nội dung, thuộc tính và kiểu của các phần tử HTML.

Một số hàm thường dùng:

- `document.getElementById()`
- `document.querySelector()`
- `element.innerText`, `element.innerHTML`, `element.style`

**Ví dụ:**

```javascript
document.getElementById("demo").innerText = "Xin chào JS!";
```

## 10. JSON (JavaScript Object Notation)

**Lý thuyết:**  
JSON là định dạng trao đổi dữ liệu dạng văn bản, thường dùng giữa frontend và backend.  
Hai hàm quan trọng:

- `JSON.stringify(obj)`: chuyển object sang chuỗi JSON.
- `JSON.parse(json)`: chuyển chuỗi JSON thành object.

**Ví dụ:**

```javascript
let obj = { name: "Tuan", age: 21 };
let json = JSON.stringify(obj);
console.log(JSON.parse(json));
```

## 11. Promise, async/await, và Promise.all

**Lý thuyết:**  
Promise là đối tượng đại diện cho một thao tác bất đồng bộ, có 3 trạng thái:

- `pending`: đang xử lý
- `fulfilled`: hoàn thành
- `rejected`: thất bại

`async/await` giúp viết mã bất đồng bộ theo cú pháp tuần tự, dễ hiểu hơn.  
`Promise.all()` cho phép chạy nhiều Promise song song và đợi tất cả hoàn thành.

**Ví dụ:**

```javascript
function delay(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
async function run() {
  await delay(1000);
  console.log("Hoàn thành sau 1s");
}
run();
```

## 12. Fetch API

**Lý thuyết:**  
`fetch()` dùng để gửi yêu cầu HTTP (GET, POST, PUT, DELETE). Nó trả về một Promise chứa đối tượng `Response`, cho phép đọc dữ liệu dưới dạng JSON hoặc text.

**Ví dụ:**

```javascript
fetch("https://jsonplaceholder.typicode.com/posts/1")
  .then((res) => res.json())
  .then((data) => console.log(data))
  .catch((err) => console.error(err));
```
