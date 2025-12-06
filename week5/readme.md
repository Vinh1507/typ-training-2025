# Week 5: Caching & Redis

## 1. Tìm hiểu về Caching & Redis

### Caching là gì?

### Vì sao cần Caching/Redis?

### Các chiến lược Caching phổ biến?

### Kiểu dữ liệu cơ bản trong Redis?

## 2. Implement

### Tích hợp Redis vào project hiện tại

1. Cài đặt Redis (docker/local/cloud) và thư viện client tương ứng (Node: `redis`, Java: `lettuce`/`jedis`, Python: `redis-py`).
2. Tạo module cấu hình kết nối (URI, auth, retry policy...).
3. Viết helper/service để thao tác với Redis (get/set/delete, TTL...).

### Use case mẫu

- **Cache thông tin User/Sản phẩm:**
  - Khi request, kiểm tra cache → nếu có trả về ngay.
  - Nếu không, truy vấn DB → lưu vào cache với TTL hợp lý.
- **Bảng xếp hạng (Sorted Set):**
  - Lưu score với `ZADD`, lấy top N bằng `ZREVRANGE`.

### Lưu ý khi dùng Cache

- **TTL (Time to Live):** đặt thời gian hết hạn phù hợp để dữ liệu không bị stale.
- **Cache Invalidation:** xác định rõ khi nào cần xóa/refresh cache.
- **Cache Avalanche:** tránh hết hạn đồng loạt (dùng random TTL, phân tán).
- **Cache Penetration:** dữ liệu không tồn tại bị request liên tục → dùng cache null hoặc Bloom Filter.


## Output

- Trình bày phần lý thuyết.
- Demo use case đã implement (log, Postman, màn hình web...).

## Tham khảo

- [Redis Documentation](https://redis.io/docs/latest/)
- [Caching Strategies Cheat Sheet](https://github.com/donnemartin/system-design-primer#caching)
- [Redis Data Types](https://redis.io/docs/latest/develop/data-types/)
- [Cache-Aside Pattern - Microsoft Docs](https://learn.microsoft.com/azure/architecture/patterns/cache-aside)