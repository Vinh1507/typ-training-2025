# 🗓️ Tuần 1 - HTML: Xây khung trang web

---

## 🎯 MỤC TIÊU
- Hiểu cấu trúc cơ bản của một trang HTML.  
- Sử dụng được các thẻ HTML phổ biến (văn bản, hình ảnh, danh sách, bảng, form).  
- Nắm vững khái niệm **Semantic HTML** để bố cục trang hợp lý, rõ ràng.  
- Thực hành tạo **trang giới thiệu cá nhân (Profile Page)** hoàn chỉnh.

---

## 📖 KIẾN THỨC LÝ THUYẾT

### 🧱 1. Cấu trúc cơ bản của HTML5

```html
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tiêu đề trang</title>
  </head>
  <body>
    <!-- Nội dung hiển thị -->
  </body>
</html>

#Giải thích:

## <!DOCTYPE html>: Khai báo tài liệu HTML5.
## <html>: Thẻ gốc bao toàn bộ trang.
## <head>: Chứa meta, tiêu đề, liên kết CSS/JS.
## <body>: Chứa nội dung chính hiển thị trên trình duyệt.


### 🧱 2. Các thẻ HTML phổ biến

Nhóm thẻ	        |       Mục đích         	        |   Thẻ sử dụng
Văn bản             |	Tiêu đề, đoạn, nhóm nội dung    |	<h1> → <h6>, <p>, <span>, <div>
Liên kết & hình ảnh |	Tạo link, chèn ảnh      	    |<a>, <img>
Danh sách           |	Trình bày liệt kê       	    |<ul>, <ol>, <li>
Bảng dữ liệu        |Hiển thị thông tin theo hàng, cột  |	<table>, <tr>, <th>, <td>
Biểu mẫu	        |Nhập và gửi dữ liệu	            |<form>, <input>, <label>, <button>, <textarea>, <select>

#### 2.1. <h1> → <h6> — Heading (Tiêu đề)

Mục đích: Phân cấp tiêu đề trong trang, h1 quan trọng nhất, h6 nhỏ nhất.
Thuộc tính thường dùng: id, class, style, aria-* nếu cần.
Ví dụ:

<h1>Trang cá nhân của tôi</h1>
<h2>Giới thiệu</h2>
<h3>Kinh nghiệm</h3>

#### 2.2 <p> — Paragraph (Đoạn văn)

Mục đích: Bao 1 đoạn văn bản.
Thuộc tính: class, id, style.
Ví dụ:

<p>Đây là đoạn văn giới thiệu bản thân. Tôi học lập trình web và thích front-end.</p>

#### 2.3 <div> — Division (Khối)

Mục đích: Nhóm các phần tử thành khối; không mang ý nghĩa semantic.
Thuộc tính: id, class, style, role (nếu dùng cho accessibility).
Ví dụ:

<div class="card">
  <h3>Tên dự án</h3>
  <p>Mô tả dự án ...</p>
</div>



#### 2.4. <span> — Inline container

Mục đích: Nhóm nội dung inline nhỏ (để style hoặc JS).
Thuộc tính: class, id, style.
Ví dụ:
<p>Xin chào, tôi là <span class="name">An</span>.</p>

### 2.5. <a> — Anchor (Liên kết)

Mục đích: Tạo link đến URL, anchor nội bộ, hoặc dùng với mailto:/tel:.
Thuộc tính quan trọng: href, target, rel, title, download.
Ví dụ:

<a href="https://github.com/username" target="_blank" rel="noopener noreferrer">GitHub</a>
<a href="#contact">Đi tới phần Liên hệ</a>
<a href="mailto:nguyena@example.com">Gửi email</a>

#### 2.6. <img> — Image
Mục đích: Hiển thị hình ảnh.
Thuộc tính quan trọng: src, alt, width, height, srcset, sizes, loading.
Ví dụ:

<img src="avatar.jpg" alt="Ảnh đại diện Nguyễn A" width="200" loading="lazy">

#### 2.7. <ul>, <ol>, <li> — Danh sách
Mục đích: Liệt kê các mục; ul không thứ tự, ol có thứ tự.
Ví dụ:

<ul>
  <li>HTML</li>
  <li>CSS</li>
  <li>JavaScript</li>
</ul>

<ol>
  <li>Bước 1</li>
  <li>Bước 2</li>
</ol>

#### 2.8. <table>, <thead>, <tbody>, <tfoot>, <tr>, <th>, <td>, <caption> — Bảng dữ liệu
Mục đích: Trình bày dữ liệu dạng hàng/cột (không dùng để layout).
Cấu trúc mẫu:

<table>
  <caption>Danh sách sinh viên</caption>
  <thead>
    <tr><th>STT</th><th>Tên</th><th>Lớp</th></tr>
  </thead>
  <tbody>
    <tr><td>1</td><td>Nguyễn A</td><td>B21</td></tr>
  </tbody>
  <tfoot>
    <tr><td colspan="3">Tổng: 1</td></tr>
  </tfoot>
</table>

#### 2.9. <form> — Biểu mẫu

Mục đích: Thu thập dữ liệu người dùng và gửi lên server.
Thuộc tính: action, method (get/post), enctype (ví dụ multipart/form-data cho file).
Ví dụ cơ bản:

<form action="/send" method="post">
  <!-- inputs -->
</form>

#### 2.10. <label> — Nhãn cho input

Mục đích: Liên kết text mô tả với input; cải thiện accessibility.
Cách dùng: for="id_input" hoặc bọc input bên trong label.

<label for="email">Email</label>
<input id="email" name="email" type="email">

<!-- hoặc -->
<label>Họ tên <input name="name" type="text"></label>

#### 2.11. <input> — Ô nhập dữ liệu (nhiều loại)

Mục đích: Nhập dữ liệu ngắn.
Một số type thường dùng & lưu ý:
type="text": chuỗi ký tự.
type="password": mật khẩu (ẩn ký tự).
type="email": trình duyệt validate email.
type="tel": số điện thoại (không validate format tự động).
type="number": chỉ số, có min, max, step
type="url": URL.
type="checkbox": chọn nhiều.
type="radio": chọn 1 trong nhóm (cùng name).
type="file": upload file (multiple để chọn nhiều).
type="hidden": trường ẩn.
type="submit" / button / reset.

Ví dụ:

<input type="text" id="name" name="name" required minlength="2">
<input type="email" id="email" name="email" required>
<input type="number" name="age" min="0" max="120">
<input type="checkbox" id="agree" name="agree">
<label for="agree">Tôi đồng ý</label>

#### 2.12. <textarea> — Ô nhập nhiều dòng

Mục đích: Nhập văn bản dài (tin nhắn, mô tả).
Thuộc tính: rows, cols, placeholder, maxlength, required, name, id.
Ví dụ:

<label for="message">Tin nhắn</label>
<textarea id="message" name="message" rows="6" required></textarea>

#### 2.13. <select> & <option> — Dropdown

Mục đích: Cho người dùng chọn 1 (hoặc nhiều) giá trị từ danh sách.
Thuộc tính: multiple, size.
Ví dụ:

<label for="country">Quốc gia</label>
<select id="country" name="country" required>
  <option value="">-- Chọn --</option>
  <option value="vn">Việt Nam</option>
  <option value="us">United States</option>
</select>


#### 2.14. <button> — Nút

Mục đích: Nút thực hiện hành động (submit, reset, JS).
Loại: type="submit" (submit form), type="reset", type="button" (JS).
Ví dụ:

<button type="submit">Gửi</button>
<button type="button" onclick="openModal()">Mở modal</button>

#### 2.15. Các thẻ inline semantic khác: <strong>, <em>, <small>, <mark>, <code>

Mục đích & ví dụ:

<strong> — nhấn mạnh về mặt ý nghĩa (thường trình bày in đậm).
<em> — nhấn mạnh ngữ điệu (nghiêng).
<small> — text nhỏ, chú thích.
<mark> — đánh dấu (highlight).
<code> — đoạn mã inline.
<p><strong>Quan trọng:</strong> Đọc kỹ hướng dẫn.</p>
<p>Đoạn mã: <code>const a = 1;</code></p>


#### 2.16. <figure> & <figcaption> — Ảnh có chú thích

Mục đích: Bọc ảnh và mô tả ảnh.
<figure>
  <img src="chart.png" alt="Biểu đồ doanh thu">
  <figcaption>Hình 1: Doanh thu theo quý</figcaption>
</figure>

#### 2.17. <meta> (trong <head>) — Thẻ meta quan trọng

Một vài meta phổ biến:

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="Mô tả trang, tốt cho SEO">
<meta name="robots" content="index, follow">

### 🧭 3. Semantic HTML

Semantic HTML giúp chia trang web thành các phần có ý nghĩa rõ ràng, hỗ trợ SEO và khả năng truy cập.

Thẻ Semantic     |	Ý nghĩa
<header>     	 |Phần đầu trang hoặc bài viết
<nav>            |	Thanh điều hướng, menu liên kết
<section>    	 |Phân chia nội dung theo chủ đề
<article>       |	Bài viết hoặc nội dung độc lập
<footer>        |	Phần chân trang, thông tin liên hệ

Ví dụ:

<header>
  <h1>Nguyễn Đức Anh</h1>
  <nav>
    <a href="#about">Giới thiệu</a>
    <a href="#skills">Kỹ năng</a>
    <a href="#contact">Liên hệ</a>
  </nav>
</header>


### 🧠 THỰC HÀNH – TẠO TRANG GIỚI THIỆU CÁ NHÂN (PROFILE PAGE)
##🔧 Yêu cầu:

Tạo cấu trúc HTML chuẩn (<!DOCTYPE html>, <head>, <body>).

Hiển thị thông tin cá nhân, ảnh đại diện, kỹ năng, học vấn, form liên hệ.

Sử dụng thẻ semantic để bố cục rõ ràng.



