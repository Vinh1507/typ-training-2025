# Phần 1: Cấu trúc cơ bản của trang HTML
1. Hiểu cấu trúc chuẩn của trang HTML
- `<!DOCTYPE html>`: Khai báo loại tài liệu (HTML5).
- `<html>`: Thẻ gốc của tài liệu HTML.
- `<head>`: Chứa thông tin meta, tiêu đề trang, liên kết CSS, JS.  
Ví dụ:
```
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống kê hành vi vi phạm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
```
- `<body>`: Chứa nội dung chính hiển thị trên trình duyệt.
---
# Phần 2: Các thẻ HTML phổ biến
2. Văn bản

- Sử dụng các thẻ: h1 → h6, p, span, div.
    - `<h1>` → `<h6>`: là lần lượt các loại header, theo thứ tự nhỏ dần.
    - `<p>` : thẻ paragraph. Ở đây bạn sẽ viết những dòng chữ bình thường
    - `<div>` : thẻ khối, dùng để nhóm lại các phần tử với nhau.
- Tạo tiêu đề, đoạn văn bản, nhóm nội dung, và bố cục cơ bản.
3. Liên kết & hình ảnh

- Sử dụng thẻ: a, img.  
    - `<a>`: tạo 1 đường dẫn.  
    ví dụ: `<a href="link"> text </a>`
    - `<img>`: chèn 1 hình ảnh vào trang web  
    ví dụ: `<img src="link.jpg" alt="Alter text">`
- Tạo liên kết đến trang khác hoặc chèn hình ảnh minh họa.
4. Danh sách

- Sử dụng thẻ: ul, ol, li.
- Tạo danh sách có thứ tự hoặc không thứ tự.  
    - `<ul>`: (unordered list): tạo danh sách không có thứ tự (thường dùng dấu chấm tròn làm đánh dấu)
    - `<ol>`: (ordered list): tạo danh sách có thứ tự (thường dùng số, chữ cái hoặc số La Mã)
    - `<li>`: (list) xác định mục trong danh sách  
    Ví dụ: `<ol><li>Bước 1</li></ol>`
5. Bảng (Table)

- Sử dụng thẻ: table, tr, td.
- Trình bày dữ liệu dạng bảng, cấu trúc hàng và cột.
```
<table>   //Khai báo bảng
    <thead>     //Nhóm các hàng tiêu đề
        <tr>    //1 hàng
            <th>STT</th>
            <th>Hành vi vi phạm</th>
        </tr>
    </thead>
    <tbody>     //Nhóm các hàng dữ liệu chính
        <tr>
            <td></td>
            <td></td>
        </tr>
    </tbody>
    <tfoot>     //Nhóm các hàng chân bảng (tổng kết, ghi chú)
        <td> Tổng số vụ vi phạm </td>
        <td> 247 </td>
    </tfoot>
```

6. Form (Biểu mẫu)

- Sử dụng thẻ: input, label, button, select, textarea.
- Tạo biểu mẫu nhập liệu cơ bản (nhập tên, email, nội dung...).
- Ví dụ: loginForm:
```
<form action="/login_handler" method="POST">    <!-- Khai báo form và chứa tất cả các phần tử trong form -->
    
    <h2>Đăng nhập Hệ thống</h2>

    <div>
        <label for="username">Tên đăng nhập:</label><br>        <!-- Nhãn cho trường input -->
        <input 
            type="text" 
            id="username" 
            name="username" 
            placeholder="Nhập email hoặc tên đăng nhập" 
            required
        >
        <!-- Trường nhập liệu (type: text/mail/password/...) -->
    </div>
    
    <div>
        <label for="password">Mật khẩu:</label><br>
        <input 
            type="password" 
            id="password" 
            name="password" 
            placeholder="Nhập mật khẩu" 
            required
        >
    </div>

    <button type="submit" style="margin-top: 15px;">
        Đăng nhập
    </button>
</form>
```
---
# Phần 3: Semantic HTML
7. Hiểu và sử dụng các thẻ semantic  
a. Thẻ semantic là gì?  
- Semantic HTML là những thẻ HTML mô tả ý nghĩa của nội dung mà chúng chứa, thay vì chỉ dùng div
- Ví dụ:  
    - `<header>`: ở đây là ta biết nó là phần chứa nội dung giới thiệu (nằm ở đầu trang hoặc phần mở đầu nội dung)
    - `<nav>`: khu vực điều hướng, chứa những menu liên kết
    - `<article>`: Nội dung độc lập, ví dụ như bài viết.
    - `<section>`: Phân chia nội dung thành các phần rõ ràng.
    - `<footer>`: Phần chân trang, chứa thông tin liên hệ, bản quyền.

# Phần 4: Thực hành - Code profile page
Visit [My profile](profilePage.htmlprofilePage.html) for more information.


