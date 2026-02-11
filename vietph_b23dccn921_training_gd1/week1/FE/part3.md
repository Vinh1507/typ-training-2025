# Phần 3: Semantic HTML

## 🧠 Giới thiệu
**Semantic HTML** (HTML ngữ nghĩa) là cách sử dụng các thẻ HTML có **ý nghĩa rõ ràng về nội dung**, giúp **con người và trình duyệt hiểu cấu trúc trang web** tốt hơn.

Thay vì chỉ dùng `<div>` cho mọi thứ, HTML5 cung cấp các thẻ semantic giúp phân tách nội dung **theo ngữ cảnh** — điều này giúp **SEO tốt hơn**, **code dễ đọc hơn** và **truy cập dễ dàng hơn**.

---

## 🧭 1. `<header>` — Phần đầu trang hoặc phần mở đầu nội dung
- Dùng để chứa logo, tiêu đề trang, hoặc thanh điều hướng đầu trang.
- Mỗi trang có thể có **nhiều header**, ví dụ: header cho toàn trang và header cho từng bài viết.

```html
<header>
  <h1>Blog Lập Trình Web</h1>
  <p>Chia sẻ kiến thức lập trình và công nghệ</p>
</header>
```

---

## 🗺️ 2. `<nav>` — Khu vực điều hướng (Navigation)
- Dùng để chứa **các liên kết điều hướng** như menu, thanh công cụ, hoặc danh mục.
- Giúp người dùng dễ dàng di chuyển giữa các phần của trang hoặc các trang khác.

```html
<nav>
  <ul>
    <li><a href="#home">Trang chủ</a></li>
    <li><a href="#about">Giới thiệu</a></li>
    <li><a href="#contact">Liên hệ</a></li>
  </ul>
</nav>
```

---

## 📄 3. `<article>` — Nội dung độc lập
- Đại diện cho **một phần nội dung hoàn chỉnh và độc lập**, ví dụ như một bài viết, tin tức, hoặc bài blog.
- Có thể chứa tiêu đề, hình ảnh, văn bản, và footer riêng.

```html
<article>
  <h2>Giới thiệu về HTML5</h2>
  <p>HTML5 là phiên bản mới nhất của ngôn ngữ HTML, hỗ trợ nhiều thẻ semantic mới.</p>
</article>
```

---

## 📚 4. `<section>` — Phân chia nội dung
- Dùng để **chia nhỏ nội dung** thành các phần logic khác nhau trong một trang hoặc bài viết.
- Mỗi section thường có **tiêu đề riêng (`<h2>` hoặc `<h3>`)**.

```html
<section>
  <h2>Kinh nghiệm làm việc</h2>
  <p>Tôi đã có hơn 3 năm kinh nghiệm phát triển web với React và Node.js.</p>
</section>
```

---

## ⚙️ 5. `<footer>` — Phần chân trang
- Dùng để chứa **thông tin bổ sung**, chẳng hạn như bản quyền, liên hệ, hoặc liên kết mạng xã hội.
- Giống như header, có thể có nhiều footer trong cùng một trang (ví dụ: một cho trang chính và một cho mỗi bài viết).

```html
<footer>
  <p>&copy; 2025 - Thiết kế bởi Nguyễn Văn A</p>
  <p>Email: nguyenvana@example.com</p>
</footer>
```

---

## ✅ Tóm tắt Semantic HTML

| Thẻ Semantic | Mô tả | Ví dụ sử dụng |
|---------------|--------|----------------|
| `<header>` | Phần đầu trang hoặc phần mở đầu nội dung | Logo, tiêu đề, slogan |
| `<nav>` | Khu vực điều hướng | Menu liên kết |
| `<article>` | Nội dung độc lập, tự chứa | Bài viết, tin tức |
| `<section>` | Phân chia nội dung theo chủ đề | Mục thông tin, phần bài viết |
| `<footer>` | Phần chân trang | Bản quyền, thông tin liên hệ |

---

## 💡 Ví dụ hoàn chỉnh
```html
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Trang Semantic HTML</title>
</head>
<body>
  <header>
    <h1>Trang Cá Nhân Của Tôi</h1>
  </header>

  <nav>
    <ul>
      <li><a href="#about">Giới thiệu</a></li>
      <li><a href="#skills">Kỹ năng</a></li>
      <li><a href="#contact">Liên hệ</a></li>
    </ul>
  </nav>

  <section id="about">
    <h2>Giới thiệu</h2>
    <p>Tôi là lập trình viên front-end yêu thích ReactJS và thiết kế UI/UX.</p>
  </section>

  <article>
    <h2>Bài viết mới</h2>
    <p>Hôm nay tôi học về Semantic HTML – cách viết HTML có ý nghĩa và chuẩn SEO hơn!</p>
  </article>

  <footer>
    <p>&copy; 2025 Nguyễn Văn A | Email: nguyenvana@example.com</p>
  </footer>
</body>
</html>
```
