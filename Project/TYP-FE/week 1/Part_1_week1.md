## Phần 1: Cấu trúc cơ bản của một tài liệu HTML

### `<!DOCTYPE html>`
- Khai báo loại tài liệu là **HTML5**.  
- Giúp trình duyệt hiểu cách phân tích và hiển thị nội dung đúng chuẩn.  
- Luôn được đặt ở **dòng đầu tiên** của file HTML.

---

### `<html>`
- Là **thẻ gốc (root element)** của toàn bộ tài liệu HTML.  
- Tất cả các thành phần khác đều phải nằm trong thẻ này.  
- Thường có thuộc tính `lang` (ví dụ: `<html lang="vi">`) để xác định ngôn ngữ của trang.

---

### `<head>`
- Chứa **thông tin meta** và **các phần không hiển thị trực tiếp** trên trình duyệt.  
- Bên trong thường có:  
  - `<meta>` → Khai báo mã hóa ký tự, cài đặt viewport, mô tả trang...  
  - `<title>` → Tiêu đề hiển thị trên tab trình duyệt.  
  - `<link>` → Liên kết đến file CSS hoặc icon.  
  - `<script>` → Chèn hoặc liên kết file JavaScript.

---

### `<body>`
- Chứa **nội dung chính của trang web**, phần người dùng có thể nhìn thấy.  
- Có thể bao gồm:
  - Văn bản, hình ảnh, liên kết, bảng, danh sách, biểu mẫu, v.v.  
  - Các thẻ cấu trúc như `<header>`, `<section>`, `<article>`, và `<footer>`.  
- Đây là nơi bạn thiết kế và bố cục nội dung mà người dùng tương tác trực tiếp.
