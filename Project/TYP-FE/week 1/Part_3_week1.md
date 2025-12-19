# 🧱 Phần 3: Semantic HTML
---

## 1. Hiểu về Semantic HTML

**Semantic HTML** (HTML ngữ nghĩa) là cách viết mã HTML có **ý nghĩa rõ ràng** về mặt nội dung, giúp:
- Trình duyệt và công cụ tìm kiếm hiểu cấu trúc trang web tốt hơn.  
- Dễ đọc, dễ bảo trì hơn cho lập trình viên.  

Các thẻ semantic **mô tả vai trò nội dung** thay vì chỉ định dạng, khác với các thẻ không ngữ nghĩa như `<div>` hay `<span>`.

---

## 2. Các thẻ Semantic HTML thông dụng

| Thẻ | Ý nghĩa | Mô tả |
|-----|----------|--------|
| `<header>` | Phần đầu trang hoặc phần mở đầu nội dung | Thường chứa logo, tiêu đề, hoặc menu điều hướng |
| `<nav>` | Thanh điều hướng (navigation) | Dùng cho các liên kết menu chính hoặc phụ |
| `<article>` | Nội dung độc lập | Thường là bài viết, tin tức, hoặc một khối nội dung tách biệt |
| `<section>` | Một phần nội dung có chủ đề riêng | Dùng để nhóm các đoạn nội dung có liên quan |
| `<footer>` | Phần chân trang | Chứa thông tin liên hệ, bản quyền, hoặc liên kết mạng xã hội |

---

## 3. Ví dụ minh họa

```html
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Trang cá nhân</title>
</head>
<body>

  <header>
    <h1>Nguyễn Trường Giang</h1>
    <p>Sinh viên chuyên ngành Công nghệ phần mềm</p>
  </header>

  <nav>
    <ul>
      <li><a href="#about">Giới thiệu</a></li>
      <li><a href="#skills">Kỹ năng</a></li>
      <li><a href="#contact">Liên hệ</a></li>
    </ul>
  </nav>

  <main>
    <section id="about">
      <h2>Giới thiệu</h2>
      <p>Tôi yêu thích lập trình web và các công nghệ trí tuệ nhân tạo (AI).</p>
    </section>

    <section id="skills">
      <h2>Kỹ năng</h2>
      <article>
        <h3>Lập trình Web</h3>
        <p>Sử dụng HTML, CSS, JavaScript và framework ReactJS.</p>
      </article>
      <article>
        <h3>Phát triển Backend</h3>
        <p>Kinh nghiệm với Java Spring Boot và cơ sở dữ liệu MySQL.</p>
      </article>
    </section>
  </main>

  <footer>
    <p>Liên hệ: giangptit@example.com</p>
    <p>&copy; 2025 Nguyễn Trường Giang</p>
  </footer>

</body>
</html>
```