# Phần 1: Cấu trúc cơ bản của trang HTML

1. Hiểu cấu trúc chuẩn của trang HTML

- `<!DOCTYPE html>`: Khai báo loại tài liệu (HTML5).
- `<html>`: Thẻ gốc của tài liệu HTML.
- `<head>`: Chứa thông tin meta, tiêu đề trang, liên kết CSS, JS.
- `<body>`: Chứa nội dung chính hiển thị trên trình duyệt.

```html
<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <title>Document</title>
   <link rel="stylesheet" href="">
</head>
<body>
   
</body>
</html>
```

---

# Phần 2: Các thẻ HTML phổ biến

2. Văn bản
   - h1 -> h6, p, span, div
```
<!-- Tạo tiêu đề theo cấp độ 1 đến 6 -->
    <h1></h1>
    <h2></h2>
    <h3></h3>

 <!-- Tạo văn bản -->
    <p></p>

<!-- Nhóm nội dung, bố cục -->
    <span></span>
    <div></div>
```
3. Liên kết & hình ảnh
   - a, img
```
    <a href="abc.xyz">Hình ảnh</a>
    <img src="abc.xyz" alt="Hình ảnh">
```
4. Danh sách
   - ul, ol, li
```
    <ul>
        <li>a</li>
        <li>b</li>
        <li>c</li>
    </ul>
    <ol>
        <li>x</li>
        <li>y</li>
    </ol>
```
5. Bảng
   - table, tr, td
```
    <table>
        <thead>
            <tr>
                <td></td>
            </tr>
        </thead>
    </table>
```
6. Biểu mẫu (Form)
   - input, label, button, select, textarea
```
    <label for="name">Nội dung: </label>
    <input type="text" id="name" class="name">

    <label for="textarea">Nội dung: </label>
    <textarea name="textarea" id="textarea"></textarea>
```
---

# Phần 3: Semantic HTML

7. Hiểu và sử dụng các thẻ semantic
   - header: Phần đầu trang hoặc phần mở đầu nội dung.
   - nav: Khu vực điều hướng, menu liên kết.
   - article: Nội dung độc lập, ví dụ như bài viết.
   - section: Phân chia nội dung thành các phần rõ ràng.
   - footer: Phần chân trang, chứa thông tin liên hệ, bản quyền.

---

# Phần 4: Thực hành tạo trang giới thiệu cá nhân
   - Chi tiết tại [MyProfile.html](MyProfile.html)
