# Báo cáo Tuần 3
## 1. JavaScript là gì ?
- JavaScript là một ngôn ngữ lập trình kịch bản đa nền tảng và hướng đối tượng. Nó là một ngôn ngữ động, được thiết kế để tạo ra các trang web có tính tương tác cao (ví dụ: hoạt ảnh phức tạp, menu bật lên)
## 2. Biến
### 2.1 Cách khai báo
- Có ba kiểu khai báo:
    - `var`: Biến được khai báo có phạm vi hàm. Điều này có nghĩa là nó chỉ tồn tại bên trong hàm nó được khai báo. Nếu được khai báo bên ngoài, nó sẽ thành một biến toàn cục. Biến var có khả năng hoisting, tuy nhiên nó sẽ chỉ đưa được phần biến lên đầu chứ không thể đưa phần gán giá trị.

    - `let`: Dùng để khai báo một biến có thể thay đổi giá trị và có phạm vi trong khối lệnh mà nó được khai báo.

    - `const`: Dùng để khai báo một biến không thể gán lại giá trị. Biến const cũng có phạm vi là trong một khối lệnh
    Hai biến được khai báo bằng `let` và `const` cũng được hoisting, tuy nhiên chúng sẽ bị TDZ.
### 2.2 Các kiểu dữ liệu trong JS
- Có 7 kiểu dữ liệu nguyên thủy trong js:
    - `String`: Dùng để hiển thị văn bản. 
        ```JavaScript
            let ten = "Tung";
        ```
    - `Number`: Dùng cho cả số nguyên và số thập phân
        ```JavaScript
            let tuoi = 30;
            let diem = 9.5;
        ```
    - `Boolean`: Có hai giá trị `true` hoặc `false`
        ```JavaScript
            let isSigned = true;
        ```
    - `undefined`: Một biến đã được khai báo nhưng chưa được gán giá trị sẽ có kiểu là undefined
        ```JavaScript
            let viDu;
        ```
    - `null`: Đại diện cho sự "không có giá trị" một cách **cố ý**. Lập trình viên thường chủ động gán `null` cho một biến để chỉ ra rằng nó không có giá trị
        ```js
            let xe = null;
        ```
    - `BigInt`: Dùng để hiển thỉ các số nguyên cực kì lớn vượt quá kiểu `Number`. Giá trị của `BigInt` được viết bằng cách thêm chữ n ở cuối.
        ```js
            let num = 98223940328049374n;
        ```
    - `Symbol`: Dùng để tạo các giá trị duy nhất và bất biến, thường dùng làm khóa cho thuộc tính của object để tránh xung đột
        ```js
            let id = Symbol("mota");
        ```
- Kiểu dữ liệu tham chiếu
    - `Object`: 
        - Dùng để lưu trữ các bộ sưu tập dữ liệu phức tạp, bao gồm các cặp key - value.
        - Object có thể thay đổi
        ```js
            let nguoi = { ten: "Tung", tuoi: "20" }
        ```
    - Các kiểu dữ liệu phổ biến khác mà bạn thường gặp thực chất cũng là các dạng đặc biệt của `Object`:
        - `Array`: Một loại object đặc biệt dùng để lưu trữ một danh sách các giá trị theo thứ tự
        ```js
            let mauSac = ["đỏ", "vàng", "xanh"];
        ```
        - `Function`: Cũng là một loại object đặc biệt có thể được gọi để thực thi một khối mã.
        ```js
            function chao() { console.log("Xin chào!"); }
        ```
    - Đối với object, khi gán giá trị sẽ là gán theo kiểu tham chiếu
        ```js
            let obj1 = { ten: "Tung", tuoi: "20" }
            let obj2 = obj1
            // Bản chất là obj2 và obj1 cùng trỏ về một object trong bộ nhớ
            // Tức là khi thay đổi giá trị obj2 thì giá trị obj1 cũng sẽ thay đổi theo
        ```
## 3. Vòng lặp
### 3.1 Vòng lặp `for`
- Là vòng lặp phổ biến nhất được sử dụng khi biết trước số lần lặp
```js
for (let i = 0; i < 5; i++) {
    console.log(i);
}
```
### 3.2 Vòng lặp `while`
- Sử dụng khi bạn lặp dựa trên một điều kiện mà không **biết trước số lần lặp**. Vòng lặp sẽ tiếp tục chừng nào điều kiện còn là true.
```js
let n = 0;

while (n < 3) {
    console.log(n);
    n++;
}
```
### 3.3 Vòng lặp `do...while`
- Giống hệt `while`, nhưng có một khác biệt quan trọng: Vòng lặp `do...while` luôn chạy ít nhất một lần, vì nó kiểm tra điều kiện sau khi chạy khối mã (thay vì trước như while).
```js
let m = 5;

do {
    console.log(m);
    m++;
} while (m < 3);

// Kết quả:
// 5
```
### 3.4 Vòng lặp `for...of`
- Dùng để duyệt qua các giá trị của các đối tượng có thể lặp như Array hoặc String. Đây là cách lặp qua mảng rất được ưa chuộng.
```js
const mauSac = ["đỏ", "vàng", "xanh"];

for (const mau of mauSac) {
    console.log(mau);
}
// Kết quả:
// "đỏ"
// "vàng"
// "xanh"

const ten = "An";

for (const kyTu of ten) {
    console.log(kyTu);    
}
// Kết quả:
// "A"
// "n"
```
### 3.5 Vòng lặp `for..in`
Dùng để duyệt qua các keys hoặc properties của một Object.
```js
const nguoi = {
    ten: "Alice",
    tuoi: 30,
    ngheNghiep: "Kỹ sư"
};

for (const key in nguoi) {
    console.log(key); // In ra tên của "key"
    console.log(nguoi[key]); // In ra "giá trị" tương ứng
}
// Kết quả:
// "ten"
// "Alice"
// "tuoi"
// 30
// "ngheNghiep"
// "Kỹ sư"
```
## 4. Rẽ nhánh
### 4.1 Lệnh `if`
- Đây là dạng cơ bản nhất. Khối mã bên trong if chỉ chạy nếu điều kiện là true.
```js
let tuoi = 20;

if (tuoi >= 18) {
    console.log("Bạn đã đủ tuổi trưởng thành.");
}
```
### 4.2 Lệnh `if...else`
Dùng khi bạn muốn thực thi một hành động nếu điều kiện đúng, và một hành động khác nếu điều kiện sai.
```js
let nhietDo = 15;

if (nhietDo > 25) {
    console.log("Trời nóng, bật điều hòa.");
} else {
    console.log("Trời mát/lạnh, không cần điều hòa.");    
}
```
### 4.3 Lệnh `if...else if...else`
Dùng khi bạn có nhiều điều kiện cần kiểm tra. JavaScript sẽ kiểm tra từng điều kiện từ trên xuống dưới và sẽ thực thi khối mã đầu tiên mà nó gặp true, sau đó bỏ qua tất cả các khối còn lại.
```js
let diem = 7.5;

if (diem >= 8) {
    console.log("Học sinh Giỏi");
} else if (diem >= 6.5) {
    console.log("Học sinh Khá"); // (7.5 >= 6.5) là true
} else if (diem >= 5) {
    console.log("Học sinh Trung bình");
} else {
    console.log("Học sinh Yếu");
}
// Kết quả: "Học sinh Khá"
// (Nó dừng lại ngay khi điều kiện else if đầu tiên đúng)
```
### 4.4 Lệnh `switch`
`switch` là một giải pháp thay thế gọn gàng cho chuỗi `if...else if...else` khi bạn cần so sánh một biến duy nhất với nhiều giá trị cụ thể.

`switch`: Biến bạn muốn kiểm tra.

`case value:`: Một giá trị cụ thể để so sánh.

`break;`: break dùng để thoát khỏi `switch` sau khi một `case` được thực thi. Nếu không có `break`, mã sẽ "rơi" xuống và thực thi cả các `case` bên dưới.

`default:`: Giống như `else`, khối này sẽ chạy nếu không có `case` nào khớp.
```js
let thu = "Thứ Hai";

switch (thu) {
    case "Thứ Hai":
        console.log("Bắt đầu tuần làm việc.");
        break;
    case "Thứ Ba":
    case "Thứ Tư":
    case "Thứ Năm":
        console.log("Ngày làm việc bình thường.");
        break;
    case "Thứ Sáu":
        console.log("Sắp cuối tuần rồi!");
        break;
    case "Thứ Bảy":
    case "Chủ Nhật":
        console.log("Ngày nghỉ cuối tuần.");
        break;
    default:
        console.log("Giá trị không hợp lệ.");
}
// Kết quả: "Bắt đầu tuần làm việc."
```
### 4.5 Toán tử 3 ngôi
Đây là cách viết siêu ngắn cho một lệnh `if...else` đơn giản.

Cú pháp: `dieu_kien ? gia_tri_neu_dung : gia_tri_neu_sai`
```js
// Dùng if...else
let tuoi = 20;
let trangThai;
if (tuoi >= 18) {
    trangThai = "Trưởng thành";
} else {
    trangThai = "Vị thành niên";
}
// trangThai là "Trưởng thành"

// Dùng toán tử ba ngôi (Ternary Operator)
let trangThaiNganGon = (tuoi >= 18) ? "Trưởng thành" : "Vị thành niên";
// trangThaiNganGon cũng là "Trưởng thành"
```
## 5. Hàm
### 5.1 Hàm là gì ?
- Hàm là một trong những khối xây dựng cơ bản và quan trọng nhất trong JavaScript. Về cơ bản, nó là một khối mã có thể tái sử dụng, được thiết kế để thực hiện một tác vụ cụ thể.

- Các thành phần chính của hàm: 

    - Parameters (Tham số): Là các biến được liệt kê trong phần định nghĩa hàm.

    - Arguments (Đối số): Là các giá trị mà bạn truyền vào khi "gọi" hàm.

    - `return` (Trả về): Là từ khóa dùng để trả kết quả từ hàm ra bên ngoài. Khi gặp `return`, hàm sẽ dừng thực thi ngay lập tức. Nếu không có return, hàm sẽ tự động trả về undefined.

    ```js
    // 'a' và 'b' là parameters (tham số)
    function tinhTong(a, b) {
        let ketQua = a + b;
        return ketQua; // Trả kết quả ra ngoài
    }

    // Gọi hàm
    // 5 và 3 là arguments (đối số)
    let tong = tinhTong(5, 3);

    console.log(tong); // In ra 8
    ```
### 5.2 Các cách khai báo hàm trong JS
- Function Declaration:
    
    - Đây là cách khai báo "truyền thống".

        - Cú pháp: Bắt đầu bằng từ khóa function theo sau là tên hàm.

        - Đặc điểm: Bị Hoisting. Nghĩa là JavaScript sẽ đưa khai báo này lên đầu phạm vi, cho phép bạn gọi hàm trước khi nó được định nghĩa.
    
    ```js
    chao(); // "Xin chào!"

    function chao() {
        console.log("Xin chào!");
    }
    ```
- Function Expression (Biểu thức Hàm)

    - Đây là cách gán một hàm (thường là vô danh) cho một biến.

        - Cú pháp: Dùng `let`, `const`, `var` để khai báo một biến và gán nó bằng một function.

        - Đặc điểm: Không bị Hoisting. Bạn phải định nghĩa hàm trước khi gọi nó (giống như biến thông thường).

    ```js
    // tamBiet(); // Lỗi! TypeError: tamBiet is not a function

    const tamBiet = function() {
        console.log("Tạm biệt!");
    };

    // Phải gọi sau khi định nghĩa
    tamBiet(); // "Tạm biệt!"
    ```

- Arrow Function (Hàm mũi tên)
    
    - Cú pháp: Dùng dấu mũi tên =>.

    - Đặc điểm 1 (Ngắn gọn):

        - Nếu hàm chỉ có 1 tham số, có thể bỏ ().

        - Nếu hàm chỉ có 1 dòng lệnh và return, có thể bỏ {} và return.

    - Đặc điểm 2 (Nâng cao): Hàm mũi tên không có `this` của riêng nó. Nó sẽ "mượn" `this` của context (phạm vi) bên ngoài nơi nó được định nghĩa. Điều này rất hữu ích trong các phương thức của object hoặc callback.

    ```js
    // Cách viết đầy đủ
    const nhanDoi = (x) => {
        return x * 2;
    };

    // Viết gọn
    const nhanDoiNganGon = x => x * 2;

    console.log(nhanDoi(5));        // 10
    console.log(nhanDoiNganGon(5)); // 10
    ```
## 6. Mảng
### 6.1 Mảng là gì ?
Mảng trong JavaScript là một kiểu đối tượng đặc biệt, được dùng để lưu trữ một danh sách các giá trị theo thứ tự. Bạn có thể coi nó như một cái tủ có nhiều ngăn, mỗi ngăn chứa một giá trị.

### 6.2 Cách tạo và thay đổi mảng
- Cách phổ biến và đơn giản nhất là dùng dấu ngoặc vuông `[]`

```js
// Một mảng rỗng
const mangRong = [];

// Một mảng chứa các chuỗi
const traiCay = ["Táo", "Cam", "Chuối"];

// Một mảng chứa các con số
const diemSo = [10, 8, 9.5];

// Một mảng chứa nhiều kiểu dữ liệu
const honHop = ["Xin chào", 100, true, { ten: "An" }];
```

- Truy cập và thay đổi phần tử

```js
const traiCay = ["Táo", "Cam", "Chuối"];

// Truy cập phần tử
console.log(traiCay[0]); // "Táo"
console.log(traiCay[1]); // "Cam"
console.log(traiCay[2]); // "Chuối"

// Thay đổi phần tử
traiCay[1] = "Xoài";
console.log(traiCay); // ["Táo", "Xoài", "Chuối"]
```

### 6.3 Thuộc tính và phương thức phổ biến
Mảng có rất nhiều phương thức tích hợp sẵn để thao tác dữ liệu.
- `.length`: Trả về số lượng phần tử trong mảng
```js
console.log(traiCay.length); // 3
```
- `.push(item)`: Thêm một hoặc nhiều phần tử vào cuối mảng.
```js
traiCay.push("Dâu tây");
console.log(traiCay); // ["Táo", "Xoài", "Chuối", "Dâu tây"]
```
- `.pop()`: Xóa phần tử cuối cùng khỏi mảng và trả về phần tử đó.
```js
traiCay.pop();
console.log(traiCay); // ["Táo", "Xoài", "Chuối"]
```
- `.unshift(item)`: Thêm một hoặc nhiều phần tử vào đầu mảng.
```js
traiCay.unshift("Lê");
console.log(traiCay); // ["Lê", "Táo", "Xoài", "Chuối"]
```
- .shift(): Xóa phần tử đầu tiên khỏi mảng (và trả về phần tử đó).
```js
traiCay.shift();
console.log(traiCay); // ["Táo", "Xoài", "Chuối"]
```
- `.splice(startIndex, deleteCount, ...items)`: Một phương thức cho phép thêm/xóa/thay thế phần tử ở vị trí bất kỳ.
```js
// Xóa 1 phần tử tại index 1
traiCay.splice(1, 1); // Xóa "Xoài"
console.log(traiCay); // ["Táo", "Chuối"]

// Thêm "Cam" và "Nho" vào index 1 (không xóa gì)
traiCay.splice(1, 0, "Cam", "Nho");
console.log(traiCay); // ["Táo", "Cam", "Nho", "Chuối"]
```
- Các phương thức lặp mà Chúng không làm thay đổi mảng ban đầu, mà thường trả về một giá trị mới (mảng mới, giá trị đơn lẻ...).
    - forEach(callback): 

        - Mục đích: Thay thế vòng lặp for hoặc for...of khi chỉ muốn thực thi một hành động cho mỗi phần tử, không cần trả về mảng mới.

        - Không trả về gì (undefined).

        ```js
        const ten = ["An", "Bình", "Cường"];

        // Cách dùng forEach (hiện đại)
        ten.forEach(function(item, index) {
            console.log(`Chào ${item} (chỉ số ${index})`);
        });

        // Viết gọn bằng Arrow Function
        ten.forEach((item) => console.log(`Chào ${item}`));
        ```

    - map(callback):

        - Mục đích: biến đổi mỗi phần tử của mảng và tạo ra một mảng mới với các phần tử đã biến đổi đó.

        - Trả về: Một mảng mới có cùng độ dài với mảng ban đầu.

        ```js
        const so = [1, 2, 3, 4];

        const soBinhPhuongMap = so.map(function(num) {
            return num * num;
        });
        // soBinhPhuongMap là [1, 4, 9, 16]

        // Viết gọn
        const soBinhPhuongMap = so.map(num => num * num);
        ```

    - filter(callback): 

        - Mục đích: Khi muốn lọc/chọn các phần tử từ mảng dựa trên một điều kiện và tạo ra một mảng mới chỉ chứa các phần tử thỏa mãn.

        - Trả về: Một mảng mới chứa các phần tử đủ điều kiện (có thể ngắn hơn mảng ban đầu).

        ```js
        const so = [1, 2, 3, 4, 5, 6];

        const soChanFilter = so.filter(function(num) {
            return num % 2 === 0; // Trả về true nếu muốn giữ lại
        });
        // soChanFilter là [2, 4, 6]

        // Viết gọn
        const soChanFilter = so.filter(num => num % 2 === 0);
        ```

    - reduce(callback, initialValue): 

        - Mục đích: "Giảm" mảng về một giá trị duy nhất. Đây là phương thức mạnh mẽ nhất. Có thể dùng nó để tính tổng, tìm giá trị lớn nhất, đếm, hoặc thậm chí xây dựng một object mới.

        - Nó nhận 2 đối số: một hàm callback và một giá trị khởi tạo.

        - Hàm callback có 2 tham số quan trọng: accumulator (biến tích lũy) và currentValue (phần tử hiện tại).

        ```js
        const so = [1, 2, 3, 4, 5];

        // 'acc' là biến tích lũy (accumulator), 'curr' là phần tử hiện tại (current)
        // 0 là giá trị khởi tạo cho 'acc'
        const tong = so.reduce(function(acc, curr) {
            return acc + curr;
        }, 0); 
        // tong là 15

        // Viết gọn
        const tong = so.reduce((acc, curr) => acc + curr, 0);
        ```
## 7. Callback
### 7.1 Callback là gì ?
- Callback, hay "hàm gọi lại", là một hàm được truyền vào một hàm khác dưới dạng đối số (argument), với mục đích là hàm này sẽ được gọi sau khi một tác vụ nào đó hoàn thành.

- Nói một cách đơn giản, đó là cách bạn nói với một hàm khác: "Này, hãy làm việc A. Khi nào làm xong, hãy gọi cái hàm B này."

    - Hàm A là hàm nhận callback.

    - Hàm B chính là callback.

    Ví dụ với `setTimeout`:
    ```js
    console.log("1. Bắt đầu đặt hàng...");

    // Hàm 'function()' chính là callback
    // Nó được truyền vào setTimeout để được gọi lại SAU 2 giây.
    setTimeout(function() { 
        console.log("3. Pizza đã đến!"); // Hàm này được gọi lại sau
    }, 2000); // 2000ms = 2 giây

    console.log("2. Tiếp tục làm việc nhà trong khi chờ...");
    ```
### 7.2 Callback Hell
#### 7.2.1 Khái niệm và vấn đề
- `Callback Hell` xảy ra khi có nhiều tác vụ bất đồng bộ phụ thuộc lẫn nhau (tác vụ 2 phải chờ tác vụ 1 xong, tác vụ 3 phải chờ tác vụ 2 xong,...).

- Khi đó, bạn phải lồng các callback vào bên trong nhau, tạo ra một cấu trúc mã rất sâu và khó đọc, thường được gọi là "Pyramid of Doom".

- Ví dụ về Callback Hell: Giả sử cần làm các bước: 1. Lấy thông tin user (mất 1s) -> 2. Lấy bài đăng của user đó (mất 1s) -> 3. Lấy bình luận của bài đăng đó (mất 1s).

    ```js
    // Bắt đầu lồng nhau...
    getUser(1, function(user) { // Tác vụ 1: Lấy user
        console.log("Đã lấy user:", user.name);
        
        // Lồng callback thứ 2
        getPosts(user.id, function(posts) { // Tác vụ 2: Lấy bài đăng
            console.log("Đã lấy bài đăng:", posts[0].title);
            
            // Lồng callback thứ 3
            getComments(posts[0].id, function(comments) { // Tác vụ 3: Lấy bình luận
                console.log("Đã lấy bình luận:", comments[0].text);
            
            });
        });
    });
    ```

- Vấn đề:

    - Khó đọc: Mã bị thụt vào quá sâu, đi sang bên phải mãi mãi.

    - Khó bảo trì: Thêm một bước mới hoặc thay đổi logic là một cơn ác mộng.

    - Xử lý lỗi: Rất phức tạp. Phải xử lý lỗi ở mỗi cấp độ lồng nhau.

#### 7.2.2 Giải pháp
Có 2 cơ chế để giải quyết `Callback Hell`
##### 1. Promise
Promises cho phép "làm phẳng" kim tự tháp bằng cách sử dụng .then() để nối chuỗi các tác vụ.
```js
// Thay vì lồng, chúng ta "nối chuỗi"
getUser(1)
.then(user => {
    console.log("Đã lấy user:", user.name);
    return getPosts(user.id); // Trả về Promise tiếp theo
})
.then(posts => {
    console.log("Đã lấy bài đăng:", posts[0].title);
    return getComments(posts[0].id); // Trả về Promise tiếp theo
})
.then(comments => {
    console.log("Đã lấy bình luận:", comments[0].text);
})
.catch(error => {
    // Xử lý TẤT CẢ lỗi ở một nơi duy nhất!
    console.error("Đã xảy ra lỗi:", error);
});
```
##### 2. Sử dụng Promise
Đây là cách tốt nhất và dễ đọc nhất. Nó cho phép viết mã bất đồng bộ trông giống như mã đồng bộ (viết tuần tự từ trên xuống). Dùng await để "chờ" một Promise hoàn thành.

```js
async function layDuLieu() {
  try {
    // Viết tuần tự, như code bình thường!
    const user = await getUser(1);
    console.log("Đã lấy user:", user.name);

    const posts = await getPosts(user.id);
    console.log("Đã lấy bài đăng:", posts[0].title);

    const comments = await getComments(posts[0].id);
    console.log("Đã lấy bình luận:", comments[0].text);

  } catch (error) {
    // Xử lý TẤT CẢ lỗi ở một nơi duy nhất!
    console.error("Đã xảy ra lỗi:", error);
  }
}

// Gọi hàm async
layDuLieu();
```

## 8. Class
### 8.1 Class là gì ?
Nó là một bản thiết kế, một khuôn mẫu để tạo ra các đối tượng. Bản thiết kế này sẽ mô tả chi tiết một ngôi nhà chung chung này giống như thế nào và nó bao gồm:
    - Thuộc tính
    - Phương thức

```js
// Đây là BẢN THIẾT KẾ (Class)
class NgoiNha {
    constructor(mauSonNgoaiThat) {
        this.mauSon = mauSonNgoaiThat; // Thuộc tính
        this.soPhongNgu = 4;
        this.cuaDangMo = false;
    }

    // Phương thức
    moCua() {
        this.cuaDangMo = true;
        console.log(`Ngôi nhà màu ${this.mauSon} đã mở cửa.`);
    }
}
```

### 8.2 Object
- `Object` là một thể hiện cụ thể được tạo ra từ bản thiết kế (Class).

- Có thể xây được nhiều `object` từ một `class`

- Một `object` tồn tại độc lập và có trạng thái riêng của nó.

```js
// Sử dụng bản thiết kế (Class) để xây nhà (Object)
// 'new' chính là hành động "xây dựng"

const nhaCuaAn = new NgoiNha("Xanh"); 

const nhaCuaBinh = new NgoiNha("Đỏ");

nhaCuaAn.moCua(); // "Ngôi nhà màu Xanh đã mở cửa."
nhaCuaBinh.moCua(); // "Ngôi nhà màu Đỏ đã mở cửa."

console.log(nhaCuaAn.mauSon); // "Xanh"
console.log(nhaCuaBinh.mauSon); // "Đỏ"
```

### 8.3 Reference
- Là địa chỉ con trỏ trỏ đến vị trí của đối tượng đó trong bộ nhớ

- Khi gán một object bằng một object khác thì bản chất là sao chép lại địa chỉ của object gán vào. Vậy khi object được gán thay đổi bất kì thứ gì thì object gán cũng sẽ thay đổi theo

## 9. DOM
### 9.1 Khái niệm
DOM là viết tắt của Document Object Model (Mô hình Đối tượng Tài liệu).

DOM là một "cây cầu" nối giữa file HTML và ngôn ngữ JavaScript.
### 9.2 Cấu trúc
DOM tổ chức tất cả các phần tử thành một cấu trúc cây phả hệ (cha-con, anh-em). Mọi thứ trong DOM đều là một "Nút" (Node).

- Nút gốc (Root Node): Là document.

- Nút phần tử (Element Node): Là các thẻ HTML (ví dụ: <html>, <body>, <div>, p). Đây là loại nút sẽ thao tác nhiều nhất.

- Nút văn bản (Text Node): Là nội dung văn bản bên trong một thẻ.

- Nút thuộc tính (Attribute Node): Là các thuộc tính như `class="..."`, `id="..."`, `src="..."`.

- Ví dụ:
```html
<html>
  <head>
    <title>Trang web của tôi</title>
  </head>
  <body>
    <h1>Tiêu đề chính</h1>
    <p>Một đoạn văn.</p>
  </body>
</html>
```

Cây DOM của nó sẽ trông như thế này:

document

    <html> (con của document)

        <head> (con của <html>)

            <title> (con của <head>)

                "Trang web của tôi" (Text Node, con của <title>)

        <body> (con của <html>, anh em với <head>)

            <h1> (con của <body>)

                "Tiêu đề chính" (Text Node, con của <h1>)

            <p> (con của <body>, anh em với <h1>)

                "Một đoạn văn." (Text Node, con của <p>)

### 9.3 Làm việc với DOM
Toàn bộ việc thao tác với DOM đều xoay quanh 4 hành động chính:

**I. Tìm kiếm và truy cập**

Trước khi có thể thay đổi bất cứ thứ gì, phải chọn được nó.

`document.getElementById("idCuaBan")`: Chọn 1 phần tử duy nhất có id (rất nhanh).

`document.getElementsByClassName("tenClass")`: Trả về một HTMLCollection (giống mảng) gồm tất cả các phần tử có class đó.

`document.getElementsByTagName("p")`: Trả về một HTMLCollection gồm tất cả các thẻ `<p>`.

`document.querySelector(selector)`: Chọn 1 phần tử đầu tiên mà nó tìm thấy khớp với CSS selector.

`document.querySelector("#idCuaBan")` (giống getElementById)

`document.querySelector(".tenClass")` (chỉ lấy phần tử đầu tiên có class này)

`document.querySelector("div p.tenClass")`

`document.querySelectorAll(selector)`: Chọn TẤT CẢ các phần tử khớp với selector.

Trả về một **NodeList** (giống mảng, bạn có thể `forEach` nó).

`document.querySelectorAll(".tenClass")` (lấy tất cả)

`document.querySelectorAll("ul li")` (lấy tất cả các `<li>` bên trong `<ul>`)

Ví dụ:
```js
// Chọn <h1> duy nhất
let tieuDe = document.querySelector("h1");

// Chọn tất cả các đoạn văn
let dsDoanVan = document.querySelectorAll("p");
```

**II. Thay đổi và thao tác**

Khi đã chọn được phần tử và lưu vào một biến, bạn có thể thay đổi nó:

**1. Thay đổi Nội dung (Text Content):**

- element.textContent = "Nội dung mới";

    - An toàn nhất. Chỉ thay đổi text, bỏ qua mọi thẻ HTML.

- element.innerHTML = "<b>Nội dung in đậm</b>";

    - Cho phép chèn cả HTML.

**2. Thay đổi Thuộc tính (Attributes):**

- Cách trực tiếp:

    - `element.id = "idMoi";`

    - `imgElement.src = "anh-moi.jpg";`

    - `aElement.href = "https://google.com";`

- Cách tổng quát (`setAttribute`):

    - `element.setAttribute("ten-thuoc-tinh", "gia-tri-moi");`

    - `element.getAttribute("ten-thuoc-tinh");`

**3. Thay đổi Kiểu dáng (Styling):**

- Cách trực tiếp:

    - `element.style.color = "red";`

    - `element.style.backgroundColor = "blue";` (lưu ý: `background-color` -> `backgroundColor`)

- Cách tốt nhất: Thao tác với `class`.

    - `element.classList.add("tenClassMoi")`;

    - `element.classList.remove("tenClassCu")`;

    - `element.classList.toggle("classDeBatTat")`;

```js
// Giả sử có: <h1 id="tieuDe">Chào</h1>
let tieuDe = document.querySelector("#tieuDe");

// 1. Thay đổi text
tieuDe.textContent = "Chào DOM!"; // Giờ <h1> là "Chào DOM!"

// 2. Thay đổi style bằng class
// (Giả sử trong CSS có .tieu-de-do { color: red; })
tieuDe.classList.add("tieu-de-do"); // Giờ <h1> có màu đỏ
```

**III. Tạo và Xóa**
Có thể tạo các phần tử và thêm chúng vào trang

**1. Tạo (Create)**

- `let newElement = document.createElement("tenThe");` (ví dụ: `document.createElement("p")`)

**2. Thêm (Append)**

- `parentElement.appendChild(newElement);` (thêm `newElement` làm con cuối cùng của `parentElement`)

- `parentElement.prepend(newElement);` (thêm làm con đầu tiên)

**3. Xóa (Remove)**

- Cách cũ: `parentElement.removeChild(childElement);`

- Cách mới: `elementCanXoa.remove();`

Ví dụ: Tạo một mã `<li>` mới thêm vào `<ul>`

```html
<ul id="danhSach">
    <li>Mục 1</li>
</ul>
```

```js
// 1. Chọn cha
let danhSach = document.querySelector("#danhSach");

// 2. Tạo phần tử mới
let mucMoi = document.createElement("li");

// 3. Thêm nội dung cho nó
mucMoi.textContent = "Mục 2";

// 4. Gắn nó vào cây DOM (thêm vào <ul>)
danhSach.appendChild(mucMoi);

// Kết quả: <ul> giờ sẽ có 2 <li>
```

**IV. Phản hồi sự kiện (DOM Events)

Phần cuối cùng và quan trọng nhất: Làm cho trang web có "tương tác". DOM cho phép "lắng nghe" các sự kiện xảy ra trên các phần tử.

- **Sự kiện là gì?** Là bất cứ hành động nào xảy ra

    - `click` (người dùng click chuột)

    - `mouseover` / `mouseout` (di chuột qua/ra)

    - `keydown` / `keyup` (nhấn/nhả phím)

    - `submit` (gửi một form)

    - `DOMContentLoaded` (trang đã tải xong HTML)

- Cú pháp: `element.addEventListener('tenSuKien', hamCallback);`

    - `tenSuKien`: Tên sự kiện (ví dụ: `'click'`)

    - `hamCallback`: Hàm sẽ được gọi lại khi sự kiện đó xảy ra.

## 10. JSON
JSON là viết tắt của JavaScript Object Notation.

Về cơ bản, nó là một định dạng văn bản (text) được dùng để lưu trữ và vận chuyển dữ liệu.

### 10.1 Cú pháp của JSON
JSON được "lấy cảm hứng" từ cú pháp đối tượng (object) của JavaScript, nhưng nó đơn giản hơn và nghiêm ngặt hơn.

- JSON chỉ có 2 kiểu cấu trúc:

    - 1. Đối tượng: Một tập hợp các cặp khóa/giá trị. Được bao bọc bởi dấu ngoặc nhọn `{}`.

    - 2. Mảng: Một danh sách các giá trị được sắp xếp. Được bao bọc bởi dấu ngoặc vuông `[]`.

#### Các quy tắc trong JSON

1. Key PHẢI là chuỗi: Tất cả các key bắt buộc phải là một chuỗi và bắt buộc phải nằm trong dấu ngoặc kép "".

- *JavaScript Object*: { ten: "An", 'tuoi': 25 } (hợp lệ)

- *JSON*: { "ten": "An", "tuoi": 25 } (chỉ thế này mới hợp lệ)

2. Value được phép: Giá trị có thể là:

- `string` (bắt buộc trong dấu ngoặc kép "")

- `number` (số nguyên hoặc số thập phân)

- `object` (một đối tượng JSON khác)

- `array` (một mảng JSON khác)

- `boolean` (`true` hoặc `false` - không có ngoặc kép)

- `null` (không có ngoặc kép)

3. Những thứ KHÔNG được phép:

- Không có hàm (Function). JSON là chỉ để chứa dữ liệu, không chứa hành vi.

- Không có biến undefined.

- Không có chú thích (Comment) (không có `//` hay `/* */`).

- Không có dấu phẩy cuối: `{ "ten": "An", }` (Dấu phẩy cuối sau "An" là sai trong JSON).

