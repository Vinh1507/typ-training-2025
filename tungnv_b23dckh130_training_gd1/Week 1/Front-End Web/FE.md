# Báo cáo tóm tắt: HTML Cơ bản

## Phần 1: Cấu trúc cơ bản của trang HTML

Cấu trúc chuẩn của một tài liệu HTML5 là nền tảng của mọi trang web. Nó xác định loại tài liệu và các thành phần cốt lõi chứa đựng nội dung.

- **`<!DOCTYPE html>`**: Luôn đặt ở dòng đầu tiên, khai báo cho trình duyệt biết đây là tài liệu HTML5.

- **`<html>`**: Thẻ gốc, bao bọc toàn bộ nội dung của trang web.

- **`<head>`**: Chứa các thông tin meta (siêu dữ liệu) về trang. Nội dung trong thẻ này không hiển thị trực tiếp, bao gồm:
  - **`<title>`**: Tiêu đề của trang (hiển thị trên tab trình duyệt).
  - Liên kết đến các tệp CSS (để tạo kiểu) hoặc JavaScript (để tạo hành vi).
  - Các thẻ `<meta>` (ví dụ: bộ ký tự, viewport cho thiết bị di động).

- **`<body>`**: Chứa tất cả nội dung chính sẽ hiển thị trực tiếp trên trình duyệt, chẳng hạn như văn bản, hình ảnh, video, và liên kết.

---

## Phần 2: Các thẻ HTML phổ biến

Đây là các thẻ được sử dụng thường xuyên nhất để xây dựng nội dung trang web.

### Văn bản

- **`<h1>` đến `<h6>`**: Các thẻ tiêu đề (heading). `<h1>` là quan trọng nhất (thường là tiêu đề chính) và `<h6>` là ít quan trọng nhất.

- **`<p>`**: Định nghĩa một đoạn văn bản.

- **`<span>`**: Dùng để nhóm các phần tử inline (trong dòng) hoặc một phần nhỏ văn bản, thường để áp dụng CSS.

- **`<div>`**: Thẻ khối (block) dùng để nhóm các nội dung lớn lại với nhau, chủ yếu phục vụ cho việc tạo bố cục và tạo kiểu.

### Liên kết & Hình ảnh

- **`<a>`**: Tạo siêu liên kết. Thuộc tính quan trọng nhất là `href`.

- **`<img>`**: Nhúng hình ảnh vào trang. Các thuộc tính bắt buộc là `src` và `alt`.

### Danh sách

- **`<ul>`**: Tạo danh sách không có thứ tự (thường là dấu chấm đầu dòng).

- **`<ol>`**: Tạo danh sách có thứ tự (dùng số, chữ cái).

- **`<li>`**: Đại diện cho một mục trong danh sách (dùng bên trong `<ul>` hoặc `<ol>`).

### Bảng (Table)

- **`<table>`**: Thẻ bao bọc toàn bộ cấu trúc bảng.

- **`<tr>`**: Định nghĩa một hàng trong bảng.

- **`<td>`**: Định nghĩa một ô dữ liệu bên trong một hàng.

### Form (Biểu mẫu)

- **`<input>`**: Thẻ nhập liệu đa năng nhất (văn bản, mật khẩu, checkbox, radio...) được xác định bởi thuộc tính `type`.

- **`<label>`**: Cung cấp nhãn mô tả cho một thẻ `<input>`, giúp cải thiện khả năng truy cập.

- **`<button>`**: Tạo ra một nút bấm (ví dụ: nút "Gửi" biểu mẫu).

- **`<select>`**: Tạo một danh sách thả xuống.

- **`<textarea>`**: Tạo một vùng nhập văn bản lớn, cho phép nhập nhiều dòng.

---

## Phần 3: Semantic HTML (HTML ngữ nghĩa)

HTML ngữ nghĩa sử dụng các thẻ mô tả rõ ràng ý nghĩa và chức năng của nội dung bên trong nó. Điều này giúp cải thiện cấu trúc, hỗ trợ SEO (Tối ưu hóa công cụ tìm kiếm) và tăng khả năng truy cập cho người dùng sử dụng trình đọc màn hình.

- **`<header>`**: Đại diện cho phần đầu của trang hoặc một phần nội dung. Thường chứa logo, tiêu đề chính, hoặc menu điều hướng.

- **`<nav>`**: Dành riêng cho khu vực chứa các liên kết điều hướng chính của trang (menu).

- **`<article>`**: Định nghĩa một khối nội dung độc lập, hoàn chỉnh và có thể tái phân phối (ví dụ: một bài đăng blog, một bài báo).

- **`<section>`**: Phân nhóm các nội dung có liên quan logic với nhau (ví dụ: một chương, một tab nội dung).

- **`<footer>`**: Đại diện cho phần chân trang của trang hoặc một phần nội dung. Thường chứa thông tin bản quyền, liên hệ, hoặc các liên kết phụ.
