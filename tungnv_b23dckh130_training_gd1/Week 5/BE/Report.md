# Caching & Redis

## 1. Tìm hiểu về Caching & Redis
- Caching là kỹ thuật lưu trữ dữ liệu tạm thời trong một lớp lưu trữ tốc độ cao (thường là RAM), nhằm mục đích phục vụ các yêu cầu dữ liệu tương tự trong tương lai nhanh hơn so với việc truy xuất từ nguồn lưu trữ chính. Mục tiêu cốt lõi là:
    - Giảm độ trễ (latency): Rút ngắn thời gian phản hồi.
    - Tăng Throughput (lưu lượng): Xử lý được nhiều request hơn cùng lúc.
    - Giảm load (Tải): Giảm áp lực lên database chính.

## 2. Lý do phải sử dụng Caching/Redis
- Tốc độ: 
    - Database: Lưu trữ dữ liệu trên **Ổ cứng** (SSD/HĐD). Dù SSD có nhanh đến mấy, việc đọc ghi vấn tốn thời gian (I/O latency).
    - Redis (Cache): Lưu trữ dữ liệu trên RAM

-> Redis giúp phản hồi API nhanh hơn gấp trăm lần, mang lại trải nghiệm mượt mà cho người dùng.

- Bảo vệ Database, tránh sập nguồn:
    - Giả sử có 1000 request/s đến API, và API phải truy xuất database 10 lần cho mỗi request -> Tải database lên đến 10.000 request/s -> Sập database.
    - Nếu có Cache:
        - Lấy từ DB, lưu vào cache
        - Người thứ 2 đến người thứ 10.000: FastAPI lấy bài viết từ Redis ra và trả về.
        - DB sẽ phải chịu tải 1 request thay vì 10000.

- Chia sẻ dữ liệu giữa các Workers:
    - Vấn đề: Để tận dụng tối đa sức mạnh của CPU đa nhân, ta thường chạy FastAPI phía sau một Process Manager như Gunicorn hay Uvicorn.
    - Về mặt kỹ thuật, mỗi Worker là một OS Process riêng biệt.
    - Giải pháp: Để các Workers biết thông tin của nhau, chúng ta cần một trung tâm dữ liệu để Worker nào cũng có thể truy cập. Có 2 giải pháp chính:
        - Database: Nhưng tốc độ đọc ghi đĩa cứng vẫn chậm.
        - Redis: Là một In-Memory Database (Cơ sở dữ liệu trên RAM). Vì lưu trữ trên RAM nên tốc độ nó là nhanh nhất.

## 3. Các chiến lược Caching phổ biến
### 3.1 Tổng quan
Ta có công thức để đánh giá hiệu quả của Cache thông qua Cache Hit Ratio (Tỷ lệ trúng cache):
```
Cache Hit Ratio = Cache Hits / (Cache Hits + Cache Misses)
```
*Cache Hits: Số lần truy xuất từ Cache.*

*Cache Misses: Số lần truy xuất không từ Cache.*

**Mục tiêu**: Tối đa hóa tỉ lệ này

### 3.2 Các chiến lược Caching phổ biến

- Có 3 chiến lược cốt lõi thường dùng:

1. **Cache Aside Pattern (Lazy Loading)**:
- Đây là chiến lược phổ biến nhất vì tính an toàn và logic đơn giản. Ứng dụng đóng vai trò trung tâm, điều phối giữa Cache và Database.

- **Quy trình hoạt động**:
    - **Read**: FastAPI nhận Request -> Kiểm tra Cache
        - *Hit*: Trả về dữ liệu từ Cache
        - *Miss*: Hỏi Database -> Lấy dữ liệu -> Lưu vào Cache -> Trả về dữ liệu
    - **Write**: FastAPI cập nhật Database -> Xóa hoặc cập nhật key tương ứng trong Cache

- **Ưu điểm**:
    - *Resilient*: Nếu Cache sập, hệ thống vẫn chạy (nhưng chậm hơn) vì nó sẽ chọc thẳng vào database.
    - *Cost-effective*: Chỉ cần lưu trữ dữ liệu cần thiết. Dữ liệu ít dùng sẽ không bao giờ được lưu vào RAM.

- **Nhược điểm**:
    - *Latency ban đầu*: Lần đầu tiên request sẽ chậm vì phải đi 3 bước (App -> DB -> Cache)
    - *Stale Data (Dữ liệu cũ)*: Có một khoảng thời gian trễ giữa lúc DB thay đổi và lúc Cache được cập nhật.

- **Các trường hợp hay sử dụng**: Đa số các trường hợp đọc nhiều, dữ liệu phổ thông như Profile User, danh sách sản phẩm,...

2. **Write-Through Pattern (Chiến lược đồng bộ tuyệt đối)**:
- Ứng dụng sẽ coi Cache là kho lưu trữ chính. Khi ghi dữ liệu, nó sẽ ghi vào cache và cache sẽ chịu trách nhiệm ghi ngay lập tức vào database.

- **Quy trình hoạt động**:
    - **Write**: FastAPI -> Ghi vào Cache -> Ghi vào database -> Return success
    - **Read**: FastAPI -> Lấy dữ liệu từ Cache (luôn có dữ liệu mới nhất)

- **Ưu điểm**:
    - *Consistency*: Dữ liệu trong Cache luôn là dữ liệu mới nhất, gần như không bao giờ phải đọc phải dữ liệu cũ.
    - *Read Performance*: Read cực nhanh vì dữ liệu luôn sẵn sàng, không bao giờ bị "Miss" sau khi vừa Write xong.

- **Nhược điểm**:
    - *Write Lantency*: Ghi dữ liệu sẽ chậm hơn vì sẽ phải chờ ghi xong cả 2 nơi (Cache và Database).
    - *Lãng phí tài nguyên*: Nhiều dữ liệu được ghi vào nhưng có thể sẽ không bao giờ được đọc lại.

- **Các trường hợp hay sử dụng**: Hệ thống yêu cầu độ nhất quán cao, không chấp nhận dữ liệu cũ, ví dụ như số dư ví điện tử, trạng thái tồn kho thời gian thực,...

3. **Write-Behind Pattern (Write-Back) - Chiến lược tốc độ là trên hết**:
- Là phiên bản nâng cao và mạo hiểm hơn của Write-Through Pattern.

- **Quy trình hoạt động**:
    - **Write**: FastAPI -> Ghi vào Cache -> Trả về success ngay lập tức.
    - **Background Process**:Cache (hoặc một worker ngầm) sẽ từ từ gom các thay đổi và cập nhật xuống Database sau (Asynchronous).

- **Ưu điểm**:
    - *Write Performance*: Ghi dữ liệu rất nhanh vì chỉ cần ghi vào Cache, không cần chờ ghi xong Database.
    - *Giảm tải database*: Giả sử có nhiều cập nhật trong 1s. Write-Behind chỉ cần ghi vào database 1 lần cuối cùng.

- **Nhược điểm**:
    - *Rủi ro mất dữ liệu*: Nếu cache sập trước khi kịp ghi xuống database -> Toàn bộ dữ liệu chưa được cập nhật sẽ biến mất.
    - *Phức tạp*: Khó triển khai và debug nếu có lỗi.

- **Các trường hợp hay sử dụng**: Hệ thống yêu cầu tốc độ ghi cao, chấp nhận rủi ro mất dữ liệu, ví dụ: đếm lượt view, đếm lượt like, ...

#### Cơ chế Eviction (Dọn dẹp Cache)
- Có 2 cơ chế để dọn dẹp Cache:

1. **TTL (Time to Live)**: Cách đơn giản nhất là gán cho mỗi key một thời gian sống. Hết giờ tự xóa.
```Python
redis.set(key, value, ex=3600) # 1h
```

2. **LRU (Least Recently Used)**: Khi đầy bộ nhớ, xóa dữ liệu **ít được truy cập** gần đây nhất. Đây là thuật toán mặc định của Redis.

## 4. Các kiểu dữ liệu trong Redis

### 4.1 String
- Là mảng bytes an toàn. Nghĩa là có thể chứa bất cứ thứ gì: Từ chuỗi văn bản "Hello", số nguyên "100", số thực "3.14", cho đến nội dung file ảnh JPEG, miễn là dưới 512MB.

- **Cơ chế hoạt động**: Redis cấp phát bộ nhớ động. Nếu lưu số nguyên, nó có thể thực hiện các phép toán `INCR`, `DECR`, `INCRBY`, `DECRBY`, ... một cách Atomic.

- **Use Case**:
    - Caching: Cache cả trang HTML hoặc JSON response.
    - Distributed Locks: Dùng lệnh `SET resource_name my_random_value NX PX 30000` để đảm bảo chỉ 1 Worker được chạy job quan trọng trong 1 thời điểm.
    - Session Store: Lưu trữ thông tin đăng nhập của user.

### 4.2 List
- Bản chất là Linked List chứ không phải ArrayList. Nó là một chuỗi các node liên kết với nhau.

- **Hiệu năng**: 
    - `LPUSH/LPOP`: Thêm/xóa ở đầu cực nhanh với độ phức tạp O(1) ngoài ra muốn thêm hoặc xóa ở cuối thì đùng `RPUSH/RPOP`.
    - Truy cập phần tử ở giữa (`LINDEX key 5000`) cực chậm với độ phức tạp O(n).

- **Use Case**:
    - Message Queue: Dùng lệnh `LPUSH message_queue message` để thêm message vào queue và `RPOP message_queue` để lấy message từ queue. Dùng `BRPOP message_queue 0` để lấy message từ queue và block nếu không có message -> Giúp Worker không phải spam vào Redis để hỏi có message mới hay không.
    - Capped Lists: Lưu 10 log mới nhất. Sau khi push dùng `LTRIM` để giữ lại 10 log mới nhất.

### 4.3 Set
- Bản chất là sử dụng Hash Table (bảng băm) bên dưới để đảm bảo tính duy nhất.

- **Hiệu năng**:
    - `SADD/SREM`: Thêm/xóa phần tử cực nhanh với độ phức tạp O(1).
    - `SISMEMBER`: Kiểm tra phần tử có trong set không cực nhanh với độ phức tạp O(1).
    - `SCARD`: Đếm số phần tử trong set cực nhanh với độ phức tạp O(1).
    - `SINTER/SUNION/SDIFF`: Tìm intersection, union, difference với độ phức tạp O(n).

- **Use Case**:
    - Tagging: Lưu trữ danh sách tag của sản phẩm.
    - Unique User ID: Lưu trữ danh sách user ID đã đăng ký.
    - Cached User IDs: Lưu trữ danh sách user ID đã đăng nhập.
    - Blacklist: Lưu trữ danh sách user ID đã bị ban.

### 4.4 Hashes
- Map giữa các trường string và các giá trị string. Tối ưu hóa cực tốt giữa các object nhỏ. Khi một hashes có ít field, Redis dùng cấu trúc Ziplist để tiết kiệm RAM.

- **Hiệu năng**:
    - `HSET/HGET`: Thêm/lấy giá trị cực nhanh với độ phức tạp O(1).
    - `HDEL/HLEN`: Xóa/Đếm số lượng field trong hash cực nhanh với độ phức tạp O(1).
    - `HKEYS/HVALS`: Lấy danh sách các field/giá trị cực nhanh với độ phức tạp O(n).

- **Use Case**:
    - Caching: Cache cả trang HTML hoặc JSON response.
    - Session Store: Lưu trữ thông tin đăng nhập của user.
    - Blacklist: Lưu trữ danh sách user ID đã bị ban.

### 4.5 Sorted Sets
- Set có thứ tự. Mỗi phần tử có một score (số nguyên hoặc thực) để xếp hạng. Nó sử dụng Skip List kết hợp với Hash Table để đảm bảo tính nhanh chóng.

- **Hiệu năng**:
    - `ZADD/ZREM`: Thêm/xóa phần tử cực nhanh với độ phức tạp O(log(n)).
    - `ZRANGE/ZREVRANGE`: Lấy danh sách phần tử cực nhanh với độ phức tạp O(log(n)).
    - `ZCARD`: Đếm số phần tử trong set cực nhanh với độ phức tạp O(1).
    - `ZINTER/ZUNION`: Tìm intersection, union với độ phức tạp O(n).

- **Use Case**:
    - Leaderboard: Bảng xếp hạng real-time.
    - Rate Limiter: Dùng ZSet lưu timestamp của các request. Score = Timestamp. Đếm xem trong cửa sổ 60s qua (Range từ now-60 đến now) có bao nhiêu item -> Nếu lớn hơn giới hạn thì Block.
    - Task Scheduling: Hàng đợi ưu tiên (Job nào quan trọng Score cao được làm trước).

### 4.6 Bitmaps
- Bản chất là các thao tác trên bit của kiểu String.

- **Nguyên lý**: Một String 512MB có thể chứa 2^32 bits = 4GB bits. Có thể bật tắt từng bit.
    - Giả sử muốn theo dõi User online trong ngày. Thay vì lưu Set chứa 1 triệu ID (tốn hàng chục MB), ta gán mỗi User ID là 1 bit offset.

    - User ID 100 online -> Set bit thứ 100 lên 1.

    - Để kiểm tra 1 triệu user, chỉ tốn... 128KB bộ nhớ.

- **Hiệu năng**:
    - `SETBIT/GETBIT`: Thêm/lấy giá trị bit cực nhanh với độ phức tạp O(1).
    - `BITCOUNT`: Đếm số bit 1 trong String cực nhanh với độ phức tạp O(n).
    - `BITPOS`: Tìm vị trí của bit 1 đầu tiên trong String cực nhanh với độ phức tạp O(n).

- **Use Case**:
    - Tracking User Online: Lưu trữ trạng thái online của user.
    - Tracking User Activity: Lưu trữ trạng thái hoạt động của user.
    - Tracking User Status: Lưu trữ trạng thái trạng thái của user.

### 4.7 HyperLogLog
- Cấu trúc dữ liệu xác suất. Ta sẽ chỉ biết được xác suất của nó chứ không thể biết số liệu chính xác.

- **Nguyên lý**: Dùng để đếm số lượng phần tử unique trong một tập hợp lớn mà không cần lưu trữ toàn bộ tập hợp đó.

- **Hiệu năng**:
    - `PFADD/PFCOUNT`: Thêm/Đếm số lượng phần tử unique cực nhanh với độ phức tạp O(1).
    - `PFMERGE`: Merge các HyperLogLog cực nhanh với độ phức tạp O(n).

- **Use Case**:
    - Đếm số lượng user unique trong một tập hợp lớn.

### 4.8 Geospatial
- Lưu trữ tọa độ Longitude/Latitude. Bản chất của nó là Sorted Sets. Nó mã hóa Kinh/Vĩ độ thành một số 52-bit và dùng nó làm Score.

### 4.9 Streams
Biến Redis thành một Lightweight Kafka. Nó là một cấu trúc Log. Dữ liệu chỉ được thêm vào đuôi, mỗi entry có một ID duy nhất.

- **Tính năng**:
    - Consumer Group: Cho phép nhiều Worker có thể cùng đọc 1 streams. Redis đảm bảo mỗi message chỉ được gửi cho một Consumer trong nhóm.
    - Acknowledgment: Worker xử lý xong phải báo ACK, Nếu worker chết khi đang xử lý, message không mất đi mà chuyển sang trạng thái "Pending", Worker khác có thể claim dữ liệu lại -> Đảm bảo không mất dữ liệu.

- **Use Case**:
    - Event Sourcing: Lưu lại các sự kiện xảy ra trong hệ thống.
    - Log collection: Lưu lại các log của hệ thống.
    - Chat history: Lưu lại các tin nhắn trong chat.

## 5. Implement Caching with Redis
Link youtube: https://youtu.be/ZYKflVRSIAQ