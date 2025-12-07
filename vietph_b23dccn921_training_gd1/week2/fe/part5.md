# Phần 5: CSS Effects và Animations

## CSS Transitions

Dùng để **chuyển đổi mượt mà** giữa các trạng thái (hover, active, focus...).

**Cú pháp:**
transition: property duration timing-function;

**Thuộc tính chính:**

- `transition-property`: Thuộc tính áp dụng (ví dụ: color, transform)
- `transition-duration`: Thời gian thực hiện (vd: 0.3s)
- `transition-timing-function`: Kiểu chuyển động (`ease`, `linear`, `ease-in-out`)

**Ví dụ:**

button {
background-color: blue;
transition: background-color 0.3s ease;
}
button:hover {
background-color: red;
}

## CSS Transforms

Dùng để **biến đổi phần tử** (dịch chuyển, xoay, phóng to, nghiêng...).

**Các hàm phổ biến:**

- `translate(x, y)`=> Di chuyển phần tử
- `scale(x, y)` => Thay đổi kích thước
- `rotate(deg)` => Xoay phần tử
- `skew(x, y)` => Nghiêng phần tử

**Ví dụ:**
.box {
transform: rotate(15deg) scale(1.2);
}

## CSS Animations

Cho phép **tạo hiệu ứng chuyển động phức tạp**.

**Thành phần:**

- `@keyframes`: Định nghĩa các bước chuyển động
- `animation-name`: Tên animation
- `animation-duration`: Thời gian chạy
- `animation-iteration-count`: Số lần lặp (`infinite` để lặp mãi)
- `animation-direction`: Hướng (`normal`, `reverse`, `alternate`)

**Ví dụ:**
@keyframes move {
from { transform: translateX(0); }
to { transform: translateX(100px); }
}

.box {
animation-name: move;
animation-duration: 2s;
animation-iteration-count: infinite;
animation-direction: alternate;
}
