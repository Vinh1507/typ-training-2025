# Báo cáo FE tuần 2

## Mục lục

## Phần 1: CSS cơ bản

### 1. Hiểu CSS và cách tích hợp

#### 1.1 Khái niệm
- Là một ngôn ngữ được sử dụng để mô tả giao diện và định dạng của một tài liệu viết bằng HTML.
- Hiểu đơn giản thì nếu coi HTML là một bộ xương, CSS sẽ là phần da bọc bên ngoài bộ xương ấy.

#### 1.2 Vai trò
- Vai trò chính là tách biệt nội dung khỏi phần trình bày. 
- Điều này giúp cho mã nguồn được sạch sẽ và dễ bảo quản. Khi cần chỉnh sửa thì ta chỉ cần chỉnh ở CSS chứ không cần động đến HTML.

#### 1.3 Cách tích hợp

Có 3 cách để tích hợp CSS vào một trang web:
- 1.3.1 Inline CSS (Nội tuyến)
Sử dụng thuộc tính `style` trực tiếp bên trong thẻ HTML.
  - Chỉ sử dụng cho một phần tử duy nhất
  - Ví dụ:
  ```HTML
  <p style="color: blue; font-size: 16px;">Vĩnh Tùng đẹp trai.</p>
  ```
  - Tuy nhiên vẫn có nhược điểm là khó bảo trì, dễ lộn xộn, không thể tái sử dụng
- 1.3.2 Internal CSS
Đặt mã bên trong thẻ `<style>` nằm trong phần `<head>` của tệp HTML.
    - Cách dùng: Chỉ áp dụng cho một trang HTML duy nhất
    - Ví dụ:
    ```HTML
    <head>
        <style>
            p {
                color: green;
            }
            h1 {
                font-family: Arial, sans-serif; 
            }
        </style>
    </head>
    <body>
        <h1>Vĩnh Tùng cute font Arial</h1>
        <p>Vĩnh Tùng ngon zai chữ màu xanh.</p>
    </body>
    ```
- 1.3.3 External CSS
Viết một file riêng có đuôi `.css` (thường là `style.css`) là liên kết với HTML bằng thẻ `<link>` trong phần `<head>`.

    - Có thể dùng cho nhiều file HTML, dể bảo quản, dễ sử dụng.
    - Ví dụ:
    ```HTML
    // file HTML
    <head>
    <link rel="stylesheet" href="style.css">
    </head>
    ```

    ```CSS
    // file CSS
    body {
        background-color:rgb(182, 41, 173);
    }
    p {
        color: #333;    
    }
    ```
#### 1.4 Cú pháp CSS
- Selector (bộ chọn): Nhắm đến các phần tử HTML mà ta muốn chọn
    - Ví dụ: `<h1>` Chọn tất cả có thẻ h1. `.class` Chọn tất cả các class có tên là `class`. `#id-name` chọn tất cả các id có tên là id-name.
- Khối khai báo: Nằm bên trong ngoặc nhọn `{...}` chứa một hoặc nhiều khai báo.
- Khai báo: Bao gồm một thuộc tính và một giá trị phân biệt bằng dấu `:`
    - Thuộc tính: Tên của một loại thuộc tính mình muốn thay đổi (`color`, `font-size`, `background-color`)
    - Giá trị: Giá trị mà mình muốn gán cho thuộc tính đó (ví dụ: `red`, `20px`, `#ffffff`).
#### 1.5 Sử dụng External CSS
Lý do sử dụng External vì: 
- Dễ bảo trì: Chỉ cần chỉnh sửa lại một tệp `.css` duy nhất để thay đổi giao diện của toàn bộ trang web.
- Tái sử dụng: Cùng một tệp có thể sử dụng lại ở nhiều file HTML.
- Hiệu suất: Trình duyệt lưu chữ __cache__ sau lần tải đầu tiên. Khi người dùng truy cập các trang khác, nó sẽ tải nhanh hơn.

---

## Báo cáo thực hành

##### File `index.html`:
- Nhìn chung cấu trúc các thẻ trong html không có gì thay đổi so với tuần đầu
- Thẻ `<html>` bao gồm thẻ `<head>` và `<body>`
- Thẻ `head`:
    - Bao gồm các thẻ cơ bản và có `<title>`
- Thẻ `<body>`:
    - Phần đầu là thẻ `<header>` để định nghĩa header cho trang web.
    - Bên trong bao gồm một thẻ `<div>` có tên class là container để chứa các thẻ con bên trong
    - Đầu tiên sẽ có các thẻ `<nav>` để điều hướng trang, bên trong đó gồm các `<ul>` và `<li>` để tạo list chứa các link điều hướng
    - Tiếp đến là các thẻ `<section>` tương ứng với các đầu mục trong thẻ `<nav>`
    - `<section>`:

        - `id=introduction`: Trong phần này sẽ bao gồm các thẻ `<img>`, `<h1>` và `<p>`. Nhìn chung là những thẻ cơ bản dùng để viết văn bản, dùng đường liên kết ảnh để giới thiệu bản thân.
        - `id=skills`: Ở đây là phần dành cho kỹ năng. Em sẽ sử dụng các thẻ `<h2>`, `<ul>`, `<li>`. Mục đích là để liệt kê tất cả các kỹ năng và sở thích mình có. Ngoài ra để cho có khoảng cách đẹp hơn, em có sử dụng `inline CSS` để chèn thêm lề vào.
        - `id=education`: Đây là phần em sẽ sử dụng để viết tất cả kinh nghiệm và học vấn của mình. Các thẻ sử dụng: `<table>`, `<thead>`, `<tbody>`, `<tr>`, `<th>`, `<td>`. Tất cả mục đích là để căn chỉnh bảng và đưa ra các thông tin về học vấn và kinh nghiệm.
        - `id=contact`: Phần này dùng để tạo form liên hệ. Sử dụng thẻ `<form>` bao ngoài, bên trong có các thẻ `<label>` và `<input>` để nhập liệu. `required` để bắt buộc người dùng phải điền đầy đủ thông tin.
    - Cuối cùng là thẻ `<footer>`:
        - Bên trong có một `<div class="footer-content">` chứa 3 phần footer-section:
            - Phần 1 "Về tôi": Giới thiệu ngắn gọn về bản thân
            - Phần 2 "Liên kết": Danh sách các link điều hướng nội bộ trang web
            - Phần 3 "Social Media": Các link mạng xã hội như GitHub, Facebook, Instagram
        - Phần `<div class="footer-bottom">`: Hiển thị bản quyền và thông tin cuối trang.

- Cấu trúc thẻ:
```HTML
<html>
    <head>
    </head>
    <body>
        <div>
            <header>......</header>
            <nav>
                <ul>
                    <li><a href="">......</a></li>
                </ul>
            </nav>        
            <section id="introduction">
                <img>
                <h1>....</h1>
                <p>.....</p>
            </section>

            <section id="skills">
                <h2>....</h2>
                <ul>
                    <li>....</li>
                </ul>
            </section>

            <section id="education">
                <h2>.....</h2>
                
                <table>
                    <thead>
                        <tr>
                            <th>.....</th>
                        </tr>
                    </thead>
                    
                    <tbody>
                        <tr>
                            <td>....</td>
                        </tr>
                    </tbody>
                </table>
            </section>

            <section id="contact">
                <h2></h2>
                
                <form>
                    <label>.....</label>
                    <input>
                    <button>.....</button>
                </form>
            </section>

            <footer>
                <div>
                    <div>
                        <h3>....</h3>
                        <p>.....</p>
                    </div>
                    
                    <div>
                        <h3>.....</h3>
                        <ul>
                            <li><a>......</a></li>
                        </ul>
                    </div>
                    
                    <div>
                        <h3>......</h3>
                        <div>
                            <a>......</a>
                        </div>
                    </div>
                </div>
                
                <div>
                    <p>......</p>
                </div>
            </footer>
        </div>
    </body>
</html>

```
---

##### File `styles.css`:

**1. Cài đặt toàn cục**
```CSS
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

html {
    scroll-behavior: smooth;
}

body {
    font-family: 'Segoe UI', Arial, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #333;
    line-height: 1.6;
    padding: 20px;
}
```

- Sử dụng `* {}` để reset margin, padding về 0 và `box-sizing: border-box` cho tất cả phần tử, giúp việc tính toán kích thước dễ dàng hơn.
- `html { scroll-behavior: smooth; }` giúp khi click vào các link điều hướng sẽ có hiệu ứng cuộn mượt mà.
- `body` sử dụng font-family `Segoe UI`, background gradient màu tím-xanh.

**2. Container**
```CSS
.container {
    max-width: 1200px;
    margin: 0 auto;
    background: white;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
}

header {
    background: #2563eb;
    color: white;
    padding: 40px 20px;
    text-align: center;
    font-size: 2rem;
    font-weight: bold;
}
```

- `.container`: Giới hạn chiều rộng tối đa 1200px, căn giữa trang, bo góc 15px, thêm box-shadow (em có tham khảo tại https://getcssscan.com/css-box-shadow-examples).
- `header`: Nền màu xanh `#2563eb`, chữ trắng, padding rộng, font-size lớn để nổi bật hơn, căn giữa và in đậm chữ.

**3. Navigation**
```CSS
nav {
    background: white;
    position: sticky;
    top: 0;
    z-index: 100;
    box-shadow: rgba(0, 0, 0, 0.15) 0px 15px 25px;
}

nav ul {
    display: flex;
    list-style: none;
    margin: 0;
    padding: 0;
}

nav li {
    flex: 1;
    text-align: center;
}

nav a {
    display: block;
    padding: 20px;
    color: #333;
    text-decoration: none;
    font-weight: 600;
    transition: all 0.3s ease;
    border-bottom: 3px solid transparent;
}

nav a:hover {
    background: #2563eb;
    color: white;
    border-bottom: 3px solid #f4b028;
}
```
- Sử dụng `position: sticky` và `top: 0` để menu luôn dính ở đầu trang khi cuộn, `list-style: none` để loại bỏ dấu chấm.
- `display: flex` cho `<ul>` để các mục menu xếp ngang.
- Mỗi `<li>` có `flex: 1` để chia đều không gian, `text-align` để căn giữa.
- Hiệu ứng hover: Khi di chuột vào link, background đổi sang màu xanh và chữ chuyển thành màu trắng và border thêm màu vàng cam.

**4. Sections**
```CSS
section {
    padding: 50px 40px;
}

section h2 {
    color: #2563eb;
    font-size: 2rem;
    margin-bottom: 30px;
    text-align: center;
}

.section-desc {
    text-align: center;
    color: #666;
    margin-bottom: 40px;
}
```
- Mỗi `section` có padding 50px trên dưới và 40px cho trái phải.
- Tiêu đề `h2` của mỗi section có màu xanh chủ đạo, font-size 2rem, căn giữa. Mỗi tiêu đề sẽ cách 30px so với nội dụng để dễ nhìn.

**5. Section Introduction**
```CSS
.introduction {
    text-align: center;
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
}

@keyframes spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
}

.avatar {
    width: 200px;
    height: 200px;
    border-radius: 50%;
    object-fit: cover;
    border: 5px solid #2563eb;
    margin-bottom: 20px;
    transition: transform 0.3s;
}

.avatar:hover {
    transform: scale(1.05);
    animation: spin 8s linear infinite;
}

.introduction h1 {
    font-size: 2.5rem;
    color: #333;
    margin-bottom: 15px;
}

.intro-text {
    font-size: 1.1rem;
    color: #666;
    max-width: 700px;
    margin: 0 auto;
}
```
- Background gradient nhạt để tạo điểm nhấn nhẹ.
- `.avatar`: Ảnh đại diện hình tròn (border-radius 50%), kích thước 200x200px, viền xanh 5px, có hiệu ứng phóng to khi hover, ngoài ra em có làm thêm keyframes tạo chuyển động xoay tròn khi hover vào.
- Tiêu đề h1 cỡ lớn 2.5rem, đoạn text giới thiệu có max-width để dễ đọc hơn.

**6. Section Skills**
```CSS
.skills {
    background: #f9fafb;
}

.skills ul {
    list-style: none;
    max-width: 600px;
    margin: 20px auto;
}

.skills li {
    padding: 12px 15px;
    margin: 8px 0;
    background: white;
    border-radius: 8px;
    border-left: 4px solid #2563eb;
    transition: all 0.3s;
}

.skills li:hover {
    background: #e5e7eb;
    border-left: 4px solid #ee9631;
    transform: translateX(5px);
}
```
- Nền màu xám nhạt `#f9fafb`.
- Danh sách `<li>` không có bullet, mỗi item có nền trắng, bo góc, viền trái màu xanh.
- Hiệu ứng hover: Background đổi màu xám hơn, viền trái chuyển thành màu cam và dịch chuyển vị trí 5px theo chiều ngang.

**7. Section Education**
- Nền xám nhạt tương tự Skills.
- Bảng `<table>` có:
    - `border-collapse: collapse` để gộp viền
    - `thead` có background xanh, chữ trắng
    - `tbody` có hiệu ứng hover đổi màu nền
    - `over-flow: auto` để khi đoạn văn bản tràn ra sẽ có thanh cuộn
    - `.category` và `.time` được làm nổi bật bằng font-weight và màu sắc
    - Các link trong bảng có màu xanh, hover thì có gạch chân.

**8. Section Contact**
```CSS
.contact {
    background: white;
}

.contact form {
    max-width: 600px;
    margin: 0 auto;
    background: #f9fafb;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.contact label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    color: #333;
}

.contact input, textarea {
    width: 100%;
    padding: 12px;
    margin-bottom: 20px;
    border: 2px solid #e5e7eb;
    border-radius: 8px;
    font-size: 1rem;
    font-family: inherit;
    transition: border-color 0.3s;
}

.contact input:focus,
.contact textarea:focus {
    outline: none;
    border-color: #2563eb;
}

.contact textarea {
    min-height: 120px;
    resize: vertical;
}

.contact button {
    width: 100%;
    padding: 15px;
    background: #2563eb;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1.1rem;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s;
}

.contact button:hover {
    background: #1d4ed8;
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(37, 99, 235, 0.3);
}

.contact button:active {
    transform: translateY(0);
}
```
- Form được đặt trong khung có nền xám nhạt, bo góc, box-shadow.
- `label` có font-weight 600 để dễ đọc.
- `input` và `textarea` có:
    - Border 2px màu xám nhạt
    - Border-radius 8px để bo góc
    - Khi focus, viền đổi sang màu xanh chủ đạo
- Button gửi:
    - Nền màu xanh `#2563eb`, chữ trắng
    - Hover: Background tối hơn, nổi lên với box-shadow và translateY(-2px)
    - Active: Tạo hiệu ứng như vừa nhấn xuống

**9. Footer**
```CSS
footer {
    background: #1f2937;
    color: white;
    padding: 50px 40px 20px;
}

.footer-content {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 40px;
    margin-bottom: 30px;
}

.footer-section h3 {
    margin-bottom: 15px;
    color: white;
}

.footer-section p {
    color: rgba(255, 255, 255, 0.8);
}

.footer-section ul {
    list-style: none;
}

.footer-section ul li {
    margin-bottom: 8px;
}

.footer-section a {
    color: rgba(255, 255, 255, 0.8);
    text-decoration: none;
    transition: color 0.3s;
}

.footer-section a:hover {
    color: white;
}

.social-links {
    display: flex;
    gap: 15px;
}

.social-links a {
    background: rgba(255, 255, 255, 0.1);
    padding: 8px 15px;
    border-radius: 5px;
    transition: background 0.3s;
}

.social-links a:hover {
    background: #2563eb;
}

.footer-bottom {
    text-align: center;
    padding-top: 20px;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-bottom p {
    color: rgba(255, 255, 255, 0.6);
}
```
- Nền tối `#1f2937`, chữ trắng.
- `.footer-content`: Dùng CSS Grid với 3 cột bằng nhau.
- Các `.footer-section` chứa:
    - Tiêu đề h3
    - Danh sách liên kết hoặc mô tả
    - `.social-links`: Display flex với các link mạng xã hội, có background semi-transparent, hover đổi sang màu xanh.
- `.footer-bottom`: Border-top phân cách, chứa thông tin bản quyền.

**10. Responsive Design**
```CSS
@media (max-width: 768px) {
    body {
        padding: 10px;
    }

    header {
        font-size: 1.5rem;
        padding: 30px 15px;
    }

    nav ul {
        flex-wrap: wrap;
    }

    nav li {
        flex: 1 1 50%;
    }

    nav a {
        padding: 15px;
    }

    section {
        padding: 40px 25px;
    }

    section h2 {
        font-size: 1.8rem;
    }

    table {
        font-size: 0.9rem;
    }

    th, td {
        padding: 10px;
    }

    .footer-content {
        grid-template-columns: 1fr;
        text-align: center;
    }

    .social-links {
        justify-content: center;
    }
}
```
- Body giảm padding xuống 10px.
- Header font-size nhỏ hơn.
- Menu: Các item chiếm 50% width mỗi hàng (2 items trên 1 hàng).
- Section padding giảm xuống.
- Bảng giảm font-size.
- Footer chuyển từ 3 cột sang 1 cột, căn giữa.

---