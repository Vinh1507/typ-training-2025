# Styling Cơ Bản

## Typography (Kiểu chữ)

### Thuộc tính:

- font-family:

* Chức năng: Chọn font chữ (font-family: 'Segoe UI', sans-serif);

- font-size :

* Chức năng:Kích thước chữ (font-size: 18px);

- font-weight:

* Chức năng:Độ đậm nhạt (normal, bold) (font-weight: bold);

- color :

* Chức năng:Màu chữ (color: #000);

- text-align:

* Chức năng:Căn lề văn bản (left, center, right) (text-align: center);

- line-height:

* Chức năng:Khoảng cách giữa các dòng (line-height: 1.6);

💡 Ví dụ:
p {
font-family: "Roboto", sans-serif;
font-size: 16px;
font-weight: 400;
color: #222;
text-align: justify;
line-height: 1.5;
}

## Background & Colors (Màu sắc và nền)

#### Thuộc tính

- background-color:
  -Màu nền background-color: #f4f4f4;
- background-image:

* Hình nền background-image: url('bg.jpg');

- background-size:

* Kích thước hình nền background-size: cover;

- background-position:

* Vị trí hiển thị background-position: center;

- background-repeat:

* Lặp lại hình nền background-repeat: no-repeat;
  Ví dụ:
  body {
  background-color: #eef2f5;
  background-image: url("banner.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  }

## Box Model (Mô hình hộp)

Mỗi phần tử HTML là một hộp gồm 4 lớp:
margin → border → padding → content

- width, height
  Kích thước phần tử width: 300px; height: 150px;
- padding:
  Khoảng cách bên trong giữa nội dung và viền padding: 20px;
- margin:
  Khoảng cách giữa phần tử và xung quanh margin: 10px 0;
- border:
  Viền của phần tử border: 2px solid #4a90e2;
- box-sizing:
  Cách tính kích thước tổng thể box-sizing: border-box;

Ví dụ:
.card {
width: 300px;
height: 200px;
padding: 20px;
margin: 10px auto;
border: 1px solid #ddd;
box-sizing: border-box;
background-color: white;
}
