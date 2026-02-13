# Phần 4: Responsive Design

## Media Queries

Media Queries giúp giao diện thích ứng với kích thước màn hình (desktop, tablet, mobile).

- Cú pháp:
  @media screen and (max-width: 768px) {
  /_ CSS áp dụng cho màn hình nhỏ hơn hoặc bằng 768px _/
  }

@media screen and (min-width: 1024px) {
/_ CSS áp dụng cho màn hình từ 1024px trở lên _/
}

- Breakpoints phổ biến:

320px -> điện thoại nhỏ

768px -> tablet

1024px -> laptop nhỏ

1200px -> màn hình lớn (desktop)

## Responsive Units (Đơn vị tương đối):

- px: cố định

* không thay đổi theo màn hình

- % :Phần trăm
  -Tương đối với phần tử cha
- em: Theo font-size của phần tử cha

* Dễ bị ảnh hưởng nếu lồng nhiều cấp

- rem: Theo font-size của phần tử gốc (html)

* Ổn định, thường dùng cho layout

- vw :1% chiều rộng màn hình (viewport width)

* Linh hoạt cho thiết kế ngang

- vh: 1% chiều cao màn hình (viewport height)

* Linh hoạt cho thiết kế dọc
