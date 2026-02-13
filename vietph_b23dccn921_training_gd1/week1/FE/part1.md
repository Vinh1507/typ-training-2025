# Phần 1: Cấu trúc chuẩn của trang HTML

## 🧩 1. `<!DOCTYPE html>`
- Dòng đầu tiên trong tài liệu HTML.
- Khai báo cho trình duyệt biết đây là **tài liệu HTML5**.
- Giúp trình duyệt hiển thị nội dung đúng theo tiêu chuẩn mới nhất.

## 🏷️ 2. `<html>`
- Là **thẻ gốc (root element)** của toàn bộ tài liệu HTML.
- Tất cả nội dung của trang web (phần `head` và `body`) đều nằm bên trong thẻ này.
- Có thể khai báo thuộc tính `lang` để xác định ngôn ngữ của tài liệu, ví dụ:
  ```html
  <html lang="vi">
  ```

## 🧠 3. `<head>`
- Chứa các **thông tin meta (metadata)** của trang web, không hiển thị trực tiếp ra màn hình.
- Bao gồm:
  - `<title>`: Tiêu đề của trang, hiển thị trên tab trình duyệt.
  - `<meta charset="UTF-8">`: Khai báo bảng mã ký tự.
  - `<link>`: Liên kết đến file CSS hoặc favicon.
  - `<script>`: Nối file JavaScript bên ngoài (nếu có).

Ví dụ:
```html
<head>
  <meta charset="UTF-8">
  <title>Trang Giới Thiệu Cá Nhân</title>
  <link rel="stylesheet" href="style.css">
</head>
```

## 💻 4. `<body>`
- Chứa **nội dung chính** hiển thị trên trang web: văn bản, hình ảnh, nút bấm, biểu mẫu, v.v.
- Người dùng sẽ nhìn thấy phần nội dung bên trong `body` trên trình duyệt.

Ví dụ:
```html
<body>
  <h1>Xin chào!</h1>
  <p>Đây là trang web đầu tiên của tôi.</p>
</body>
```

---

## ✅ Tóm tắt cấu trúc hoàn chỉnh
```html
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8">
    <title>Tiêu đề trang</title>
    <link rel="stylesheet" href="style.css">
  </head>
  <body>
    <h1>Nội dung chính của trang</h1>
    <p>Đây là phần thân hiển thị cho người dùng.</p>
  </body>
</html>
```

---

📘 **Tóm lại:**
| Thành phần | Chức năng chính |
|-------------|-----------------|
| `<!DOCTYPE html>` | Khai báo loại tài liệu HTML5 |
| `<html>` | Bao toàn bộ nội dung trang |
| `<head>` | Chứa thông tin về tài liệu (meta, title, CSS, JS) |
| `<body>` | Hiển thị nội dung chính của trang |
