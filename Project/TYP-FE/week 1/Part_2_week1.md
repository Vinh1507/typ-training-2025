# Phần 2: Các thẻ HTML phổ biến

## 1. Văn bản (Text)

### Mô tả
Được dùng để hiển thị nội dung văn bản, tiêu đề, đoạn văn, hoặc nhóm nội dung trong một trang web. Đây là phần nền tảng giúp người dùng đọc được thông tin.

### Các thẻ thông dụng
- `<h1>` → `<h6>`: Các thẻ tiêu đề, từ lớn nhất (`<h1>`) đến nhỏ nhất (`<h6>`)
- `<p>`: Thẻ đoạn văn bản

### Ví dụ

```html
<div class="intro">
    <h2>Xin chào!</h2>
    <p>Đây là phần giới thiệu ngắn gọn về tôi.</p>
</div>
```

## 2. Liên kết và Hình ảnh (Links & Images)

### Mô tả
Dùng để tạo liên kết đến các trang web khác hoặc chèn hình ảnh minh họa vào nội dung.

### Các thẻ thông dụng

`<a>`: Tạo liên kết đến trang web hoặc vị trí khác trong cùng trang.
Ví dụ:
```html
<a href="https://ptit.edu.vn" target="_blank">Trang chủ PTIT</a>


<img>: Hiển thị hình ảnh.
Ví dụ:

<img src="avatar.jpg" alt="Ảnh đại diện" width="150">
```

## 3. Danh sách (Lists)

### Mô tả
Dùng để hiển thị các mục theo dạng danh sách, giúp nội dung rõ ràng và có tổ chức.

### Các thẻ thông dụng

- `<ul>`: Danh sách không có thứ tự (unordered list)
- `<ol>`: Danh sách có thứ tự (ordered list)
- `<li>`: Mỗi mục trong danh sách

Ví dụ:
```html
<h3>Kỹ năng</h3>
<ul>
  <li>Lập trình Java</li>
  <li>Phát triển Web</li>
  <li>Phân tích dữ liệu</li>
</ul>

<h3>Các bước thực hiện</h3>
<ol>
  <li>Tạo file HTML</li>
  <li>Viết cấu trúc cơ bản</li>
  <li>Thêm nội dung vào trang</li>
</ol>
```
## 4. Bảng (Table)

### Mô tả
Dùng để trình bày dữ liệu dạng bảng — giúp thể hiện thông tin có cấu trúc rõ ràng theo hàng và cột.

### Các thẻ thông dụng

- `<table>`: Khai báo bảng
- `<tr>`: Hàng trong bảng (table row)
- `<td>`: Ô dữ liệu trong bảng (table data)
- `<th>`: Ô tiêu đề cột (table header)

Ví dụ:
``` html
<h3>Học vấn</h3>
<table border="1">
  <tr>
    <th>Năm</th>
    <th>Trường</th>
    <th>Chuyên ngành</th>
  </tr>
  <tr>
    <td>2022 - nay</td>
    <td>PTIT</td>
    <td>Công nghệ phần mềm</td>
  </tr>
</table>
```
## 5. Biểu mẫu (Forms)

### Mô tả
Dùng để thu thập thông tin từ người dùng, như họ tên, email, hoặc nội dung liên hệ.

### Các thẻ thông dụng

- `<form>`: Khai báo một biểu mẫu
- `<label>`: Gắn nhãn cho ô nhập liệu
- `<input>`: Tạo ô nhập dữ liệu (text, email, checkbox, radio,...)
- `<textarea>`: Ô nhập văn bản dài
- `<button>`: Nút bấm gửi hoặc thực hiện hành động
- `<select>` và `<option>`: Tạo danh sách chọn

Ví dụ:
``` html
<h3>Liên hệ</h3>
<form>
  <label for="name">Họ và tên:</label><br>
  <input type="text" id="name" name="name"><br><br>

  <label for="email">Email:</label><br>
  <input type="email" id="email" name="email"><br><br>

  <label for="message">Nội dung:</label><br>
  <textarea id="message" name="message" rows="4" cols="30"></textarea><br><br>

  <button type="submit">Gửi</button>
</form>
```
