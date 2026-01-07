## 1. Biến (Variable)
- **Khái niệm**: Dùng lưu trữ giá trị dữ liệu trong bộ nhớ
- **Khai báo biến**:
```js
   var x = 10; // phạm vi function
   let y = 20; // phạm vi block
   const z = 40; // Hằng số không thay đổi được giá trị
```

---
## 2. Cấu trúc điều khiển
#### 2.1 Vòng lặp (Loops)
- for, while, do...while:
```js
   let a = 1;
   for(let i = 0; i < 5; i++){
      console.log(a);
      a++;
   }
```
- for...of: Duyệt qua phần tử của mảng hoặc iterable
```js
let arr = [1, 2, 3, 4, 5]
   for(let item of arr){
      console.log(item);
   }
```
- for...in: Duyệt qua thuộc tính của object
```js
   const person = {
   name: "John",
   age: 30,
   city: "New York"
   };

   for (let key in person) {
   console.log(key + ": " + person[key]);
   }
```

#### 2.2 Rẽ nhánh
- if/else
```js
   if(a > b) console.log("abcxyz");
   else console.log("mck");
```
- switch
```js
   switch(color){
      case 'red': console.log('red'); break;
      default: console.log('different color');
   }
```
## 3. Hàm
- Hàm là khối mã thực hiện một nhiệm vụ cụ thể
```js
   function chao(name) {
  console.log("Xin chào " + name);
   }
   chao("Nguyễn Văn A");
```
## 4. Mảng
- Lưu nhiều giá trị trong một biến
```js
   let array = [1, 2, 3, 4, 5];
   for(let i of array){
      console.log(i);
   }
```
## 5. Xâu
- Là chuỗi kí tự
```js
   let name = "Đặng Hồng Lâm"
   console.log(name.length);
   console.log(name.toUpperCase());
```
## 6. Callback & Callback Hell
- **Callback**: Hàm được truyền vào hàm khác để gọi lại sau.
- **Ví dụ**:
```js
   function xinChao(name, callback) {
      console.log("Xin chào " + name);
      callback();
   }

   xinChao("An", () => {
      console.log("Hoàn tất lời chào!");
   });
```
- **Callback Hell**: Xảy ra khi có quá nhiều callback lồng nhau, thường là khi ta phải thực hiện nhiều tác vụ nối tiếp nhau, ví dụ gọi API, đọc file, hoặc đợi dữ liệu,...
```js
   getUser(function(user) {
      getPosts(user.id, function(posts) {
         getComments(posts[0].id, function(comments) {
            console.log("Bình luận đầu tiên:", comments[0]);
         });
      });
   });
```
- Giải pháp cho callback hell này dùng promise hoặc async/await

## 7. OOP
- Object là thực thể của class
- Class đóng gói thực thể
```js
   class Person {
      constructor(name, age) {
         this.name = name;
         this.age = age;
      }
      inten() {
         console.log(this.name);
      }
   }

   let a = new Person("An", 20);
   a.inten();
```
## 8. DOM (Document Object Model)
- DOM cho phép JavaScript tương tác với HTML
```js
   <p id="demo">Xin chào</p>
   <script>
   document.getElementById("demo").innerText = "Hello!";
   </script>
```

## 9. JSON
- JSON là kiểu dữ liệu dùng để trao đổi giữa frontend và backend
```js
   let user = { name: "A", age: 20 };
   let json = JSON.stringify(user);
   console.log(json);

   let obj = JSON.parse(json);
   console.log(obj.name);
```

## 10. Promise, async, await
- **Promise**: Giúp xử lý tác vụ bất đồng bộ (asynchronous) gọn gàng hơn callback
```js
   let promise = new Promise((resolve, reject) => {
      setTimeout(() => resolve("Done"), 1000);
   });

   promise.then(result => console.log(result));
```
- **Promise.all**: Chạy nhiều promise song song

```js
   Promise.all([p1, p2, p3]).then(results => console.log(results));
```
- **Async/await**: Cú pháp dễ đọc hơn
```js
   async function run() {
   let result = await promise;
   console.log(result);
   }
   run();
```

## 11. Fetch (Gọi API)
- Dùng để lấy dữ liệu từ server
```js
   fetch('https://sky.shiiyu.moe/stats/Vivibee1414/Mango#Gear')
      .then(response => response.json())
      .then(data => console.log(data));
```
- Có thể kết hợp với async/await
```js
   async function getUsers() {
      let res = await fetch('https://sky.shiiyu.moe/stats/Vivibee1414/Mango#Gear');
      let data = await res.json();
      console.log(data);
   }
   getUsers();
```





