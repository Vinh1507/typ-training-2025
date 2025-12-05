# Phần 1: CSS Cơ Bản

### CSS là gì?

CSS (Cascading Style Sheets) là ngôn ngữ dùng để định dạng và trình bày giao diện cho các phần tử HTML (màu sắc, font chữ, bố cục, khoảng cách,…).

-HTML định nghĩa nội dung, còn CSS định nghĩa hình thức hiển thị của nội dung đó.
Ví dụ:

<p style="color: blue;">Xin chào! Đây là CSS Inline</p>

### 3 cách tích hợp CSS vào HTML

Cách tích hợp Cách dùng Đặc điểm
Inline CSS Viết trực tiếp trong thẻ HTML bằng thuộc tính style="" Dễ hiểu nhưng khó bảo trì

- Internal CSS Viết trong thẻ <style> bên trong file HTML Dễ thử nghiệm, nhưng chỉ dùng cho 1 trang
- External CSS : Viết trong file .css riêng, liên kết bằng <link> Chuẩn, dễ bảo trì, dùng chung cho nhiều trang
  Ví dụ minh họa:

### Inline:

<h1 style="color: red;">Tiêu đề màu đỏ</h1>

### Internal:

<head>
  <style>
    h1 { color: red; }
  </style>
</head>

### External (Khuyến khích):

<link rel="stylesheet" href="style.css">
/* style.css */
h1 { color: red; }

### Cú pháp CSS cơ bản

selector {
property: value;
}

- selector: phần tử HTML cần định dạng
- property: thuộc tính CSS
- value: giá trị của thuộc tính đó

Ví dụ:

p {
color: blue;
font-size: 16px;
}

### 4. Các loại CSS Selector phổ biến

- Element selector

* Cú pháp: h1, p, div Chọn tất cả phần tử theo tên thẻ
* VD: p { color: red; }

- Class selector

* Cú pháp: .class-name Chọn phần tử có class tương ứng
* VD: .title { font-weight: bold; }

- ID selector

* Cú pháp: #id-name Chọn phần tử theo ID duy nhất
* VD: #main { background: yellow; }

- Grouping selector

* Cú pháp: h1, h2, h3 Áp dụng cùng style cho nhiều thẻ
* VD: h1, h2, h3 { color: green; }
