# Giai đoạn 4: JavaScript - Chuẩn bị cho ReactJS

## 1. Các kiến thức cơ bản

### Biến (Variables)
Biến dùng để lưu trữ dữ liệu trong JavaScript. Có 3 cách khai báo:

```javascript
var name = "Dung";        // Cách cũ, phạm vi function
let age = 20;            // Phạm vi block, có thể thay đổi
const PI = 3.14;         // Phạm vi block, không thể thay đổi
```

### Vòng lặp (Loops)
Dùng để lặp lại một đoạn code nhiều lần:

```javascript
// Vòng lặp for
for (let i = 0; i < 5; i++) {
    console.log(i);
}

// Vòng lặp while
let i = 0;
while (i < 5) {
    console.log(i);
    i++;
}

// Vòng lặp for...of (cho mảng)
const arr = [1, 2, 3];
for (let item of arr) {
    console.log(item);
}
```

### Rẽ nhánh (Conditionals)
Dùng để thực hiện các hành động khác nhau dựa trên điều kiện:

```javascript
// if...else
if (age >= 18) {
    console.log("Đã trưởng thành");
} else {
    console.log("Chưa trưởng thành");
}

// switch
switch (day) {
    case 1:
        console.log("Thứ 2");
        break;
    case 2:
        console.log("Thứ 3");
        break;
    default:
        console.log("Khác");
}
```

### Hàm (Functions)
Hàm là một khối code có thể tái sử dụng:

```javascript
// Hàm thông thường
function greet(name) {
    return "Xin chào " + name;
}

// Arrow function
const greet = (name) => {
    return "Xin chào " + name;
}

// Arrow function rút gọn
const add = (a, b) => a + b;
```

### Mảng (Arrays)
Mảng dùng để lưu nhiều giá trị:

```javascript
const fruits = ["Táo", "Chuối", "Cam"];

// Truy cập phần tử
console.log(fruits[0]); // "Táo"

// Thêm phần tử
fruits.push("Nho");

// Các phương thức để in và thay đổi giá trị dãy
fruits.forEach(item => console.log(item));
const numbers = [1, 2, 3].map(x => x * 2); // [2, 4, 6]
const filtered = [1, 2, 3, 4].filter(x => x > 2); // [3, 4]
```

### Xâu (Strings)
Xâu là chuỗi ký tự:

```javascript
const name = "JavaScript";
const message = `Xin chào ${name}`; 

// Các phương thức thường dùng
name.length;              // Độ dài
name.toUpperCase();       // Chữ hoa
name.toLowerCase();       // Chữ thường
name.includes("Script");  // Kiểm tra có chứa
name.split("");           // Chia thành mảng
```

## 2. Callback và Callback Hell

### Callback là gì?
Callback là một hàm được truyền vào hàm khác như một tham số, và sẽ được gọi sau khi một tác vụ hoàn thành.

```javascript
// Ví dụ đơn giản
function doSomething(callback) {
    console.log("Đang làm gì đó...");
    callback(); // Gọi hàm callback
}

doSomething(function() {
    console.log("Đã xong!");
});
```

### Callback Hell
Khi có nhiều callback lồng nhau, code trở nên khó đọc và khó bảo trì:

```javascript
// Ví dụ callback hell
getData(function(a) {
    getMoreData(a, function(b) {
        getMoreData(b, function(c) {
            getMoreData(c, function(d) {
                // Code rất khó đọc!
            });
        });
    });
});
```

**Giải pháp:** Dùng Promise hoặc async/await (sẽ học ở phần sau).

## 3. OOP: Class, Object, Reference

### Object (Đối tượng)
Object là tập hợp các thuộc tính và phương thức:

```javascript
// Tạo object
const person = {
    name: "Dung",
    age: 20,
    greet: function() {
        return "Xin chào, tôi là " + this.name;
    }
};

// Truy cập thuộc tính
console.log(person.name);
console.log(person.greet());
```

### Class (Lớp)
Class là bản thiết kế để tạo object:

```javascript
class Person {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }
    
    greet() {
        return `Xin chào, tôi là ${this.name}, ${this.age} tuổi`;
    }
}

// Tạo object từ class
const person1 = new Person("Dung", 20);
console.log(person1.greet());
```

### Reference (Tham chiếu)
Trong JavaScript, object và array được truyền bằng tham chiếu, không phải giá trị:

```javascript
const arr1 = [1, 2, 3];
const arr2 = arr1; // arr2 tham chiếu đến arr1
arr2.push(4);
console.log(arr1); // [1, 2, 3, 4] - arr1 cũng thay đổi!

// Copy mảng để tránh thay đổi
const arr3 = [...arr1]; // Spread operator
```

## 4. DOM (Document Object Model)

DOM là cách JavaScript tương tác với HTML:

```javascript
// Chọn phần tử
const element = document.getElementById("myId");
const elements = document.querySelectorAll(".myClass");

// Thay đổi nội dung
element.textContent = "Nội dung mới";
element.innerHTML = "<strong>HTML mới</strong>";

// Thay đổi style
element.style.color = "red";
element.style.backgroundColor = "blue";

// Thêm/xóa class
element.classList.add("active");
element.classList.remove("inactive");

// Tạo phần tử mới
const newDiv = document.createElement("div");
newDiv.textContent = "Phần tử mới";
document.body.appendChild(newDiv);

// Lắng nghe sự kiện
element.addEventListener("click", function() {
    console.log("Đã click!");
});
```

## 5. JSON (JavaScript Object Notation)

JSON là định dạng trao đổi dữ liệu phổ biến:

```javascript
// Object JavaScript
const person = {
    name: "Dung",
    age: 20,
    city: "Hà Nội"
};

// Chuyển thành JSON string
const jsonString = JSON.stringify(person);
console.log(jsonString); // '{"name":"Dung","age":20,"city":"Hà Nội"}'

// Chuyển JSON string thành object
const jsonObj = JSON.parse(jsonString);
console.log(jsonObj.name); // "Dung"
```

## 6. Promise, Promise.all, async/await

### Promise
Promise giúp xử lý code bất đồng bộ dễ đọc hơn callback:

```javascript
// Tạo Promise
const myPromise = new Promise((resolve, reject) => {
    setTimeout(() => {
        const success = true;
        if (success) {
            resolve("Thành công!");
        } else {
            reject("Thất bại!");
        }
    }, 1000);
});

// Sử dụng Promise
myPromise
    .then(result => console.log(result))
    .catch(error => console.log(error));
```

### Promise.all
Chạy nhiều Promise cùng lúc và đợi tất cả hoàn thành:

```javascript
const promise1 = fetch('/api/data1');
const promise2 = fetch('/api/data2');
const promise3 = fetch('/api/data3');

Promise.all([promise1, promise2, promise3])
    .then(results => {
        console.log("Tất cả đã hoàn thành:", results);
    })
    .catch(error => {
        console.log("Có lỗi:", error);
    });
```

### async/await
Cú pháp hiện đại để làm việc với Promise, code dễ đọc hơn:

```javascript
// Hàm async
async function fetchData() {
    try {
        const response = await fetch('/api/data');
        const data = await response.json();
        console.log(data);
    } catch (error) {
        console.log("Lỗi:", error);
    }
}

fetchData();
```

## 7. Fetch API

Fetch dùng để gửi request HTTP và nhận response:

```javascript
// GET request
fetch('https://api.example.com/data')
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => console.log("Lỗi:", error));

// POST request
fetch('https://api.example.com/data', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        name: "Dung",
        age: 20
    })
})
    .then(response => response.json())
    .then(data => console.log(data));

// Dùng với async/await
async function getData() {
    try {
        const response = await fetch('https://api.example.com/data');
        const data = await response.json();
        return data;
    } catch (error) {
        console.log("Lỗi:", error);
    }
}
```

## 8. Code JS cho giao diện của tuần trước

Áp dụng JavaScript để làm các giao diện HTML/CSS của tuần trước trở nên tương tác và động. Các file JavaScript được tạo trong folder `FrontEnd_Week3`:

### 8.1. Image Gallery (`BaiTapCSS/ImageGallery/script.js`)

Thêm tương tác cho image gallery:

```javascript
// Lắng nghe click trên gallery items
const galleryItems = document.querySelectorAll('.gallery-item');
galleryItems.forEach(item => {
    item.addEventListener('click', function() {
        const img = this.querySelector('img');
        showImageModal(img.src, img.alt); // Hiển thị ảnh lớn trong modal
    });
    
    // Hiệu ứng hover
    item.addEventListener('mouseenter', function() {
        this.style.transform = 'scale(1.05)';
    });
});
```

**Tính năng:**
- Click vào ảnh để xem ảnh lớn trong modal
- Hiệu ứng hover với scale
- Đóng modal bằng click hoặc phím ESC

### 8.2. Portfolio Form (`Pofolio/script.js`)

Xử lý form liên hệ và navigation:

```javascript
// Form validation
const contactForm = document.querySelector('#contact form');
contactForm.addEventListener('submit', function(e) {
    e.preventDefault();
    
    const name = document.getElementById('name').value.trim();
    const email = document.getElementById('email').value.trim();
    
    if (!validateForm(name, email, subject, message)) {
        return;
    }
    
    showNotification('Cảm ơn bạn đã liên hệ!', 'success');
    contactForm.reset();
});

// Smooth scroll cho navigation
navLinks.forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const targetElement = document.getElementById(targetId);
        targetElement.scrollIntoView({ behavior: 'smooth' });
    });
});
```

**Tính năng:**
- Validation form real-time
- Smooth scroll navigation
- Hiển thị thông báo thành công/lỗi
- Animation khi scroll đến section

### 8.3. Button States (`BaiTapCSS/ButtonStates/script.js`)

Thêm hiệu ứng cho các button:

```javascript
buttons.forEach(button => {
    button.addEventListener('click', function(e) {
        // Tạo ripple effect khi click
        const ripple = document.createElement('span');
        ripple.style.cssText = `
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.5);
            animation: ripple 0.6s ease-out;
        `;
        this.appendChild(ripple);
    });
});
```

**Tính năng:**
- Ripple effect khi click button
- Hover effect với transform
- Animation mượt mà

### 8.4. Responsive Navigation (`BaiTapCSS/ResponsiveNavigation/script.js`)

Xử lý navigation và active link:

```javascript
// Active link khi scroll
window.addEventListener('scroll', function() {
    let current = '';
    sections.forEach(section => {
        if (window.pageYOffset >= sectionTop - 200) {
            current = section.getAttribute('id');
        }
    });
    
    navItems.forEach(item => {
        item.classList.remove('active');
        if (item.getAttribute('href') === `#${current}`) {
            item.classList.add('active');
        }
    });
});
```

**Tính năng:**
- Tự động highlight link khi scroll
- Smooth scroll đến section
- Đóng menu khi click bên ngoài (mobile)

### Tổng kết

Các file JavaScript đã được tạo:
- `FrontEnd_Week3/BaiTapCSS/ImageGallery/script.js` - Tương tác với gallery
- `FrontEnd_Week3/Pofolio/script.js` - Form validation và navigation
- `FrontEnd_Week3/BaiTapCSS/ButtonStates/script.js` - Button interactions
- `FrontEnd_Week3/BaiTapCSS/ResponsiveNavigation/script.js` - Navigation logic

Các file này áp dụng các kiến thức đã học: DOM manipulation, event listeners, form validation, và animations.

## 9. Chuẩn bị cho ReactJS vào 1-2 tuần tới

Để học ReactJS hiệu quả, cần nắm vững các kiến thức JavaScript sau:

### Kiến thức cần thiết:

- **ES6+ Features:** Arrow functions, destructuring, spread operator, template literals
- **Array Methods:** map, filter, reduce, forEach (rất quan trọng trong React)
- **Functions:** Regular functions, arrow functions, higher-order functions
- **Objects & Arrays:** Cách thao tác và cập nhật (immutability)
- **Async Programming:** Promise, async/await (dùng nhiều trong React để fetch data)
- **DOM Manipulation:** Hiểu cách JavaScript tương tác với DOM (React làm việc tương tự nhưng theo cách khác - Virtual DOM)

### Lưu ý quan trọng:

- React sử dụng **JSX** (JavaScript XML) để viết code giống HTML nhưng thực chất là JavaScript
- React sử dụng **components** - các phần tử có thể tái sử dụng
- React sử dụng **state** và **props** để quản lý dữ liệu
- Cần hiểu rõ JavaScript cơ bản trước khi học React để tránh bối rối

### Ví dụ so sánh:

**JavaScript thuần:**
```javascript
const button = document.createElement('button');
button.textContent = 'Click me';
button.addEventListener('click', () => alert('Clicked!'));
document.body.appendChild(button);
```

**React (JSX):**
```javascript
function Button() {
    return <button onClick={() => alert('Clicked!')}>Click me</button>;
}
```

---

## Kết luận

JavaScript là ngôn ngữ quan trọng để phát triển web hiện đại. Nắm vững các kiến thức trên sẽ giúp học ReactJS dễ dàng hơn. Cần thực hành nhiều để thành thạo các khái niệm, đặc biệt là Promise, async/await, DOM manipulation và cách thao tác với mảng/object.
