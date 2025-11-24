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

<br>

## Phần 2: Các thẻ HTML phổ biến

### 2. Văn bản

#### `<h1>` đến `<h6>`
- Định nghĩa các **cấp độ tiêu đề** (headings) cho nội dung.
- `<h1>` là tiêu đề quan trọng nhất, thường dùng cho tiêu đề chính của trang.
- `<h6>` là tiêu đề ít quan trọng nhất.
- Giúp tạo cấu trúc phân cấp cho tài liệu, quan trọng cho **SEO** và khả năng truy cập.

---

#### `<p>`
- Định nghĩa một **đoạn văn bản** (paragraph).
- Trình duyệt sẽ tự động thêm một khoảng trống (margin) trước và sau mỗi thẻ `<p>`.
- Dùng để chứa các khối văn bản nội dung chính.

---

#### `<span>`
- Là một thẻ **container nội tuyến (inline)**.
- Dùng để nhóm một phần nhỏ của văn bản hoặc nội dung mà không làm ngắt dòng.
- Thường được sử dụng để áp dụng **CSS** (ví dụ: đổi màu, font chữ) cho một vài từ cụ thể.

---

#### `<div>`
- Là một thẻ **container khối (block-level)**.
- Dùng để nhóm các thành phần HTML lớn hơn lại với nhau.
- Thường được sử dụng làm "hộp" chính để **tạo bố cục (layout)** cho trang web (ví dụ: chia cột, tạo header, footer, content).
- Luôn bắt đầu trên một dòng mới và chiếm toàn bộ chiều rộng có sẵn.

<br>

### 3. Liên kết & hình ảnh

#### `<a>`
- Là thẻ **liên kết** (anchor).
- Dùng để tạo liên kết đến các trang web khác, các file, hoặc các vị trí khác trong cùng một trang.
- Thuộc tính quan trọng nhất là `href` (hyperlink reference), chỉ định URL đích.
- Ví dụ: `<a href="https://www.google.com">Đây là liên kết</a>`
- Thuộc tính `target="_blank"` thường được dùng để mở liên kết trong một tab mới.

---

#### `<img>`
- Dùng để **chèn hình ảnh** vào trang web.
- Đây là thẻ tự đóng (không có thẻ `</img>`).
- Yêu cầu thuộc tính `src` (source) để chỉ định đường dẫn (URL) đến file ảnh.
- **Bắt buộc** phải có thuộc tính `alt` (alternative text) để cung cấp mô tả văn bản cho hình ảnh, hỗ trợ trình đọc màn hình (cho người khiếm thị) và SEO.
- Ví dụ: `<img src="logo.png" alt="Logo của công ty">`

<br>

### 4. Danh sách

#### `<ul>`
- Viết tắt của **Unordered List** (Danh sách không có thứ tự).
- Tạo một danh sách mà các mục thường được hiển thị bằng dấu đầu dòng (ví dụ: chấm tròn, hình vuông).
- Thẻ này chỉ chứa trực tiếp các thẻ `<li>`.

---

#### `<ol>`
- Viết tắt của **Ordered List** (Danh sách có thứ tự).
- Tạo một danh sách mà các mục được đánh số thứ tự (ví dụ: 1, 2, 3... hoặc a, b, c...).
- Thẻ này cũng chỉ chứa trực tiếp các thẻ `<li>`.

---

#### `<li>`
- Viết tắt của **List Item** (Mục trong danh sách).
- Dùng để định nghĩa từng mục riêng lẻ trong một danh sách.
- Thẻ `<li>` phải được đặt bên trong thẻ cha là `<ul>` hoặc `<ol>`.

### 5. Bảng (Table)

#### `<table>`
- Là thẻ **container** bao bọc toàn bộ nội dung của một bảng.
- Dùng để tổ chức và trình bày dữ liệu dạng lưới (hàng và cột).

---

#### `<tr>`
- Viết tắt của **Table Row** (Hàng của bảng).
- Dùng để định nghĩa một hàng mới bên trong bảng.
- Bên trong `<tr>` sẽ chứa các ô dữ liệu `<td>` hoặc ô tiêu đề `<th>`.

---

#### `<th>`
- Viết tắt của **Table Header** (Tiêu đề bảng).
- Định nghĩa một ô **tiêu đề** cho một cột hoặc một hàng.
- Nội dung bên trong `<th>` thường được **in đậm** và **căn giữa** theo mặc định.
- Giúp tăng khả năng truy cập và làm rõ ý nghĩa của cột/hàng.

---

#### `<td>`
- Viết tắt của **Table Data** (Dữ liệu bảng).
- Định nghĩa một ô chứa dữ liệu thông thường trong bảng.
- Phải được đặt bên trong thẻ `<tr>`.

<br>

### 6. Form (Biểu mẫu)

#### `<form>`
- Là thẻ **container** bao bọc tất cả các yếu tố của một biểu mẫu (các trường nhập liệu, nhãn, nút bấm).
- Thuộc tính `action` chỉ định URL sẽ xử lý dữ liệu khi biểu mẫu được gửi đi.
- Thuộc tính `method` chỉ định phương thức HTTP để gửi dữ liệu (thường là `GET` hoặc `POST`).

---

#### `<input>`
- Là thẻ **quan trọng nhất** trong biểu mẫu, dùng để tạo ra nhiều loại trường nhập liệu khác nhau.
- Thuộc tính `type` quyết định kiểu của ô nhập liệu:
  - `type="text"`: Ô nhập văn bản một dòng.
  - `type="password"`: Ô nhập mật khẩu (che dấu ký tự).
  - `type="email"`: Ô nhập email (có xác thực cơ bản).
  - `type="checkbox"`: Ô chọn, cho phép chọn nhiều tùy chọn.
  - `type="radio"`: Ô chọn, chỉ cho phép chọn một tùy chọn trong một nhóm (cùng `name`).
  - `type="submit"`: Một nút để gửi biểu mẫu.

---

#### `<label>`
- Định nghĩa một **nhãn** mô tả cho một yếu tố biểu mẫu (như `<input>`, `<textarea>`).
- Tăng khả năng truy cập: Khi người dùng nhấp vào `<label>`, con trỏ sẽ tự động tập trung vào trường nhập liệu tương ứng.
- Liên kết với `input` thông qua thuộc tính `for`, giá trị của `for` phải bằng với `id` của `input`.
- Ví dụ: `<label for="ten">Tên:</label> <input type="text" id="ten">`

---

#### `<button>`
- Định nghĩa một **nút bấm** có thể nhấp được.
- Có thể đặt nội dung bên trong (văn bản, hình ảnh...) linh hoạt hơn `<input type="submit">`.
- Có thể có thuộc tính `type` (ví dụ: `type="submit"`, `type="reset"`, `type="button"`).

---

#### `<select>`
- Dùng để tạo một **danh sách thả xuống (dropdown list)**.
- Bên trong `<select>` sẽ chứa nhiều thẻ `<option>`.

---

#### `<option>`
- Định nghĩa một **tùy chọn** bên trong thẻ `<select>`.
- Mỗi `<option>` là một mục mà người dùng có thể chọn.
- Thuộc tính `value` chỉ định giá trị sẽ được gửi đi khi biểu mẫu được gửi.
- Ví dụ: `<option value="hn">Hà Nội</option>`

---

#### `<textarea>`
- Định nghĩa một trường **nhập văn bản nhiều dòng** (ví dụ: ô bình luận, mô tả chi tiết).
- Có thể điều chỉnh kích thước thông qua thuộc tính `rows` (số hàng) và `cols` (số cột).

<br>

## Phần 3: Semantic HTML

### 7. Hiểu và sử dụng các thẻ semantic

*(Semantic HTML là việc sử dụng các thẻ HTML đúng với ý nghĩa ngữ nghĩa của chúng. Thay vì chỉ dùng `<div>` và `<span>` cho mọi thứ, chúng ta dùng các thẻ có ý nghĩa rõ ràng để mô tả cấu trúc nội dung.)*

---

#### `<header>`
- Đại diện cho phần **đầu trang** hoặc phần giới thiệu (mở đầu) của một phần nội dung.
- Thường chứa logo, tiêu đề chính của trang (ví dụ: `<h1>`), và thanh điều hướng (`<nav>`).
- Một trang có thể có nhiều thẻ `<header>` (ví dụ: một cho toàn trang, một cho mỗi bài viết `<article>`).

---

#### `<nav>`
- Viết tắt của **Navigation** (Điều hướng).
- Dùng để bao bọc một nhóm các **liên kết điều hướng chính** của trang web (ví dụ: menu chính).
- Giúp trình duyệt và trình đọc màn hình hiểu đây là khu vực điều hướng.

---

#### `<main>`
- Định nghĩa **nội dung chính, độc nhất** của tài liệu.
- Nội dung bên trong `<main>` phải là duy nhất cho trang đó, không bao gồm các phần lặp lại như sidebar, navigation, footer.
- **Chỉ nên có một** thẻ `<main>` trong mỗi tài liệu.

---

#### `<section>`
- Định nghĩa một **phần (section)** riêng biệt trong tài liệu.
- Dùng để nhóm các nội dung có liên quan về mặt logic (ví dụ: một chương, một tab "Giới thiệu", một tab "Liên hệ" trên cùng một trang).
- Thường nên có một tiêu đề (ví dụ: `<h2>`) bên trong mỗi `<section>`.

---

#### `<article>`
- Định nghĩa một phần **nội dung độc lập, trọn vẹn** (self-contained).
- Nội dung này phải có ý nghĩa khi được đọc riêng lẻ, tách biệt khỏi phần còn lại của trang web.
- Ví dụ điển hình: một bài đăng blog, một bài báo, một bình luận của người dùng.

---

#### `<aside>`
- Định nghĩa nội dung **bên lề**, có liên quan gián tiếp đến nội dung chính xung quanh nó.
- Thường được sử dụng cho **thanh bên (sidebar)**, chứa các liên kết liên quan, quảng cáo, hoặc thông tin bổ sung.

---

#### `<footer>`
- Đại diện cho phần **chân trang** của tài liệu hoặc của một phần nội dung.
- Thường chứa thông tin tác giả, thông tin bản quyền, liên kết liên hệ, sơ đồ trang web (sitemap).
- Tương tự như `<header>`, một trang có thể có nhiều `<footer>`.