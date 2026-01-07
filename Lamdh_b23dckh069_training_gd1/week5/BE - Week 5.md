## 1. Caching
- Caching là kỹ thuật lưu tạm dữ liệu ở một nơi truy cập nhanh (RAM, bộ nhớ trung gian) để không phải tính toán hay truy vấn lại nhiều lần
- Luồng hoạt động:
   * Không dùng caching: Client -> Server -> Database
   * Dùng caching: Client -> Cache -> Database (Truy vấn chỉ khi cần)
- Ví dụ:
   * Kết quả truy vấn danh sách sản phẩm
   * Thông tin user đăng nhập

---

## 2. Vì sao cần Caching/Redis?
- Tăng hiệu năng:
   * RAM nhanh hơn database rất nhiều
   * Giảm thời gian phản hồi
- Giảm tải database: Database không phải xử lý cùng một truy vấn lặp đi lặp lại
- Tăng khả năng mở rộng: Ứng dụng chịu được nhiều user hơn
- Redis phù hợp vì:
   * Lưu dữ liệu trong RAM
   * Tốc độ đọc và ghi nhanh (tính bằng micro-seconds đến mili-seconds)
   * Hỗ trợ nhiều kiểu dữ liệu

---

## 3. Các chiến lược caching phổ biến
1. Cache Aside (Lazy Loading)
- Luồng hoạt động:
   * App kiểm tra cache
   * Nếu hit thì tiếp tục. Nếu miss thì truy vấn database
   * Lưu kết quả vào cache
- Dễ triển khai nhưng lần đầu sẽ load chậm

2. Write Through
- Khi ghi dữ liệu -> Ghi cả cache và database
- Ưu điểm: Dữ liệu luôn nhất quán
- Nhược điểm: Ghi chậm

3. Write Behind - Write Back
- Ghi vào cache trước, database cập nhật sau (async)
- Ưu điểm: Nhanh
- Nhược điểm: Có rủi ro mất dữ liệu nếu cache chết

4. Read Through
- Cache tự động lấy dữ liệu từ database khi thiếu
- Ưu điểm: Code gọn
- Nhược điểm: Phụ thuộc hệ thống cache

5. Time To Live - TTL
- Cache tự hết hạn sau một khoảng thời gian nhất định

---

## 4. Kiểu dữ liệu cơ bản trong redis
1. String
- Lưu text, số, JSON dạng string
```bash
   SET name "DangHongLam"
   GET name
```

2. List
- Danh sách có thứ tự
- Dùng cho queue, history
```bash
   LPUSH tasks "task1"
   RPOP tasks
```

3. Set
- Tập hợp không trùng lặp
- Không có thứ tự
```bash
   SADD users 1 2 3
```

4. Hash
- Giống Object / dictionary
- Hay dùng để lưu profile của người dùng
```bash
   HSET user:1 name "DangHongLam" age 20
```

5. Sorted Set
- Set + điểm số
- Dùng cho ranking, leaderboard
```bash
   ZADD score 100 "Lam"
```

