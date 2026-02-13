# Phần 2: Các thẻ HTML phổ biến

## ✍️ 1. Văn bản (Text)
Các thẻ cơ bản dùng để hiển thị và định dạng văn bản trong HTML.

### 🧱 Thẻ tiêu đề (Heading): `<h1>` → `<h6>`
- Dùng để tạo các **tiêu đề** có mức độ quan trọng giảm dần từ `h1` (lớn nhất) đến `h6` (nhỏ nhất).
```html
<h1>Tiêu đề chính</h1>
<h2>Tiêu đề phụ</h2>
<h3>Tiêu đề nhỏ hơn</h3>
```

### 📄 Thẻ đoạn văn: `<p>`
- Dùng để tạo một **đoạn văn bản**.
```html
<p>Đây là một đoạn văn bản mô tả nội dung.</p>
```

### 🧩 Thẻ `span` và `div`
- `<span>`: nhóm **văn bản nhỏ**, dùng để định dạng nội tuyến (inline).
- `<div>`: nhóm **khối nội dung lớn**, thường dùng cho bố cục (block).
```html
<div class="section">
  <h2>Giới thiệu</h2>
  <p>Tôi là <span class="highlight">Nguyễn Văn A</span>, một lập trình viên web.</p>
</div>
```

---

## 🔗 2. Liên kết & Hình ảnh

### 🌐 Thẻ `<a>` – Tạo liên kết
- Dùng để tạo **liên kết đến trang web khác hoặc trong cùng trang**.
```html
<a href="https://www.google.com" target="_blank">Truy cập Google</a>
```

### 🖼️ Thẻ `<img>` – Chèn hình ảnh
- Dùng để **hiển thị hình ảnh minh họa**.
```html
<img src="avatar.jpg" alt="Ảnh đại diện" width="150">
```

---

## 📋 3. Danh sách (List)

### 🔸 Danh sách không thứ tự – `<ul>` và `<li>`
```html
<ul>
  <li>HTML</li>
  <li>CSS</li>
  <li>JavaScript</li>
</ul>
```

### 🔹 Danh sách có thứ tự – `<ol>` và `<li>`
```html
<ol>
  <li>Bước 1: Mở VS Code</li>
  <li>Bước 2: Tạo file index.html</li>
  <li>Bước 3: Viết mã HTML</li>
</ol>
```

---

## 📊 4. Bảng (Table)

- Dùng để **trình bày dữ liệu dạng hàng và cột**.

```html
<table border="1">
  <tr>
    <th>Tên</th>
    <th>Nghề nghiệp</th>
  </tr>
  <tr>
    <td>Nguyễn Văn A</td>
    <td>Lập trình viên</td>
  </tr>
  <tr>
    <td>Trần Thị B</td>
    <td>Thiết kế UI/UX</td>
  </tr>
</table>
```

---

## 🧾 5. Form (Biểu mẫu)

- Dùng để **nhập dữ liệu từ người dùng**.

```html
<form action="/submit" method="POST">
  <label for="name">Họ và tên:</label><br>
  <input type="text" id="name" name="name" placeholder="Nhập tên của bạn"><br><br>

  <label for="email">Email:</label><br>
  <input type="email" id="email" name="email" placeholder="Nhập email"><br><br>

  <label for="message">Nội dung:</label><br>
  <textarea id="message" name="message" rows="4" cols="30"></textarea><br><br>

  <button type="submit">Gửi</button>
</form>
```

---

## ✅ Tóm tắt nhanh

| Nhóm thẻ | Mục đích | Ví dụ |
|-----------|-----------|--------|
| `h1` → `h6`, `p`, `div`, `span` | Hiển thị và nhóm văn bản | Tiêu đề, đoạn, nhóm nội dung |
| `a`, `img` | Liên kết và hình ảnh | Gắn link, chèn ảnh minh họa |
| `ul`, `ol`, `li` | Danh sách | Gạch đầu dòng hoặc số thứ tự |
| `table`, `tr`, `td` | Bảng dữ liệu | Thông tin học vấn, kinh nghiệm |
| `input`, `textarea`, `button` | Biểu mẫu nhập liệu | Gửi thông tin người dùng |
