# Week 2  
## Vương Đức Trọng - B22DCCN866

## [Online Judge]

## MÔ TẢ HỆ THỐNG

> Hệ thống **Microservice** bao gồm **server** xử lý yêu cầu và **client** gửi yêu cầu đến server qua giao thức HTTP REST API + RabbitMQ (message queue).**Server** cung cấp API cho phép login logout, xem đề và nộp bài, xem bảng xếp hạng. **Client** là frontend - web để hiển thị giao diện tương tác với **Server**

![System Diagram](structure/system-structure.png)

- Click https://www.icewolfsoft.com/fossflow/
<!-- - Then upload file [Judge-Online Structure](structure/judge-online-2025-11-01.json) -->

## CÔNG NGHỆ SỬ DỤNG

### 1. Kiến trúc tổng thể
| Thành phần | Công nghệ | Ghi chú |
|------------|-----------|---------|
| Structure | Microservices | Architecture|
| Server | Node.js 18 + Axios,  | REST API |
| Client | ReactJS + TailwindCSS/antd | Giao tiếp HTTP |
| Deploy | Docker & Docker Compose | Build Service |
| Store Cache & Queue|Redis, RabbitMQ|Cache và hàng đợi|
| Database | MongoDB (3 instance tách biệt users, problems, submissions) | Lưu trữ dữ liệu |
| proxy / load balancer|nginx|N/A|


### 3. Các Microservices chính

| Service | Công nghệ chính |Framework / Thư viện chính|Chức năng|
|-------- |-----------------|--------------------------|---------|
|API Gateway|Node.js (v18+)|express, axios, ioredis, express-rate-limit, cors|Định tuyến request, rate limiting, kết nối frontend <=> backend|
|User Service|Node.js (v18+)|express, bcryptjs, jsonwebtoken, mongodb, ioredis, dotenv|Quản lý người dùng, xác thực JWT|
|Problem Service|Node.js (v18+)|express, mongodb, ioredis|Quản lý bài tập, dữ liệu bài toán|
|Submission Service|Node.js (v18+)|express, amqplib, mongodb, axios, ioredis|Quản lý bài nộp, tương tác với Judge Worker qua RabbitMQ|
|Judge Worker|Java 21+| springboot, amqplib, ioredis|N/A|

### 5. Bài toán giải quyết
#### 5.1. Load Shedding (Giảm tải động)
> Là kỹ thuật giảm tải có kiểm soát khi hệ thống bị quá tải tài nguyên (CPU, memory, queue đầy, I/O chậm)

> Thay vì để toàn hệ thống sập, một phần yêu cầu sẽ bị từ chối (reject) sớm để bảo vệ hệ thống chính

**Hệ thống theo dõi CPU usage, queue length, request latency. Khi vượt ngưỡng (ví dụ CPU > 85% hoặc queue backlog quá lớn):Ngừng chấp nhận một số request.Trả về mã lỗi 429 (Too Many Requests) hoặc 503 (Service Unavailable). Ưu tiên giữ lại các request quan trọng (nộp bài), bỏ qua request không quan trọng (xem bảng xếp hạng)**

**Trong hệ thống Judge Online**
```
- Áp dụng tại API Gateway hoặc Submission Service:
Khi hàng đợi RabbitMQ đầy => gateway tạm ngưng nhận thêm submission mới
Khi Redis cache quá tải => bỏ qua caching tạm thời và truy vấn DB trực tiếp
Công nghệ hỗ trợ:
    1. express-rate-limit (Node.js)
    2. redis để giám sát tải
```


#### 5.2. Distributed Lock (Khóa phân tán)
> Khi nhiều instance service chạy song song, cùng thao tác lên một tài nguyên chung (database, cache, queue), cần một cơ chế khóa (lock) để tránh xung đột (race condition)

>Distributed lock cho phép quản lý khóa trên nhiều node khác nhau (không chỉ trong 1 process)

Dùng Redis để tạo khóa chung.
- Khi service muốn thao tác, nó sẽ:

    - Tạo khóa trên Redis (SETNX key value)
    - Nếu thành công => được quyền thao tác
    - Nếu khóa tồn tại => chờ hoặc bỏ qua
    - Sau khi xong => xóa khóa (release)

**Trong hệ thống Judge Online**
```
- Áp dụng tại API Gateway hoặc Submission Service:
    Khi nhiều worker cùng xử lý bài nộp của một người dùng, cần khóa để:
        - Tránh chấm trùng cùng 1 submission
        - Đảm bảo chỉ 1 worker xử lý một bài tại một thời điểm
Công nghệ hỗ trợ:
    1. Redlock (Node.js) — cài qua ioredis
    2. Redis với lệnh SET key value NX PX timeout
```

#### 5.3. Caching (Bộ nhớ đệm)

>Lưu tạm dữ liệu truy cập thường xuyên vào bộ nhớ nhanh (RAM, Redis, Memcached) để giảm tải DB và tăng tốc phản hồi

>Khi client gửi request:Service kiểm tra cache (Redis). Nếu có (cache hit) => trả ngay.Nếu không (cache miss) => truy vấn DB => lưu vào cache

- Các chiến lược cache:
    - Write-through : Ghi vào cache và DB đồng thời
    - Write-back : Ghi vào cache trước, DB sau (background sync)
    - Cache-aside : App kiểm tra cache trước khi gọi DB

**Trong hệ thống Judge Online**
```
Cache thông tin:
    - User profile, bảng xếp hạng, danh sách bài tập phổ biến
    - Cache kết quả submissions gần nhất để hiển thị nhanh
    - Dùng ioredis hoặc node-cache tại các service
```

#### 5.4. Rate Limiting (Giới hạn tốc độ yêu cầu)
>Giới hạn số lượng request mà một người dùng (hoặc IP) có thể gửi đến server trong một khoảng thời gian. Ngăn spam, DDoS, và bảo vệ tài nguyên backend

- Dựa trên token bucket hoặc leaky bucket algorithm:
    - Mỗi người dùng có “xô” chứa token.
    - Mỗi request tiêu tốn 1 token.
    - Token được nạp lại theo thời gian.

**Trong hệ thống Judge Online**
```
Tại API Gateway:
    - Giới hạn request /submit để tránh spam nộp bài
    - Giới hạn request /login để tránh brute-force password
Tại User Service:
    - Rate limit tạo tài khoản hoặc reset mật khẩu.
Công nghệ hỗ trợ:
    - express-rate-limit (Node.js)
    - rate-limiter-flexible (Redis backend, hỗ trợ cluster)
```

| Giải pháp | Mục tiêu chính | Cấp độ | Dùng ở đâu | Công nghệ |
|-----------|----------------|--------|------------|-----------|
|Load Shedding|Giảm tải khi hệ thống quá tải|Toàn hệ thống|API Gateway, Submission Service|express, circuit breaker|
|Distributed Lock|Ngăn race condition trong môi trường nhiều instance|Service level|Judge Worker, Submission Service|Redis (SETNX), Redlock|
|Caching| Giảm truy vấn DB, tăng tốc phản hồi|Service level|User, Problem|Redis, ioredis|
|Rate Limiting|Ngăn spam, DDoS, abuse API|Gateway/API|API Gateway, User Service|express-rate-limit, Redis|

## 6. Doccument
## Problem
```
Mô tả qua serice này:
Serice này để tạo danh sách đề bài và lấy test cases nội bộ để cho hệ thống chấm tự động
Sử dụng Redis để cache dữ liệu giúp giảm tải MongoDB

```
```
/internal/problems/:id/testcases: API nội bộ, chỉ để Judge Worker truy cập lấy testcases để chấm
API Gateway / Frontend: để hiển thị danh sách đề bài chi tiết đề
Dùng projection để ẩn test cases khi hiện đề bài cho user
```

## User

```
1. Kết nối với mongodb
2. Thêm dữ liệu vào users db
```

> Bài toán đặt ra: Khi đẩy hàng nghìn / triệu User vào db 1 lúc xử lý như nào với 1 db khi hệ thống cấu hình 1 container user-service

> Giải quyết: 
> 1. Tăng số lượng container thông qua replica. Sử dụng **distribute lock** của redis để khóa không để bị dulicate key khi nhiều container cùng thêm 1 user giống nhau.

> 2. Vậy thì khi chia như vậy thì khi bị lock thì chỉ có 1 container làm việc còn mấy container khác ngồi chơi => tốn bộ nhớ => cần có cơ chế song song cho các container

> 3. Khi chạy song song thì redis có cơ chế **atomic counter** <nghĩa là tăng giá trị key lên 1 đơn vị 1 cách toàn vẹn> ta có thể chia ra thành các batch cho các container mà không cần lock thủ công để đảm bảo không bị trùng lặp và thỏa mãn chạy song song.

> 4. Ta kết hợp scale các container phù hợp và sử dụng các cơ chế của redis **distribute lock** init và **atomic counter** và **redis TTL** phân tán các task để tối ưu tốc độ lưu trữ dữ liệu vào db và cải thiện CPU.


![alt text](structure/user-redis.png)

```
CMD: 
    - k : runing parallel
    1. docker-compose up -d --build --scale user-service=k
    2. $ for i in {1..k}; do
        docker exec source-user-service-$i node seed-data.js &
    done
    wait
    echo "All seed processes completed"
    [run upload data into mongodb with k service div <all users> / <user_batch> batchs]
```


## Submission

### 1. Khi hàng nghìn user submit cùng 1 lúc thì làm sao để xác định được ai là người nộp đầu tiên và accepted bài đấy ?

```
Dùng distribute lock trong redis (3s) để khóa người nộp tránh spam.
Dùng Atomic counter để tăng key lên 1 để tránh race condition (khi nhiều tiến trình dùng chung tài nguyên trong 1 lúc và không xác định được thứ tự)
kiểu như
``` 
| time | process1     | process2     | cnt | Vấn đề                 |
|------|--------------|--------------|-----|------------------------|
|1     |read 0        |              |0    |                        |
|2     |              | read 0       |0    | cả 2 cùng đọc giá trị 0|
|3     |cal 0 + 1 = 1 |              |0    |                        |
|4     |              |cal 0 + 1 = 1 |0    |                        |
|5     |write cnt = 1 |              |1    |                        |
|6     |              |write cnt = 1 |1    | sai chỉ tăng 1 lần     |

```
do 2 luồng thực hiện các bước xen kẽ => kết quả sai
kết quả là 1 do race condition => kết quả đúng là 0 + 1 + 1 = 2
thì khi sử dụng atomic counter đảm bảo được thứ tự các tiến trình tuyệt đối 
để biết chính xác ai là người đến trước
```
| time | process1     | process2     | cnt | Vấn đề                 |
|------|--------------|--------------|-----|------------------------|
|1     |INCR(read, incr, write)|     |0    |                        |
|2     |processing(0->1)|wait        |0    | process2 truy cập nhưng phải chờ do process1 đang khóa|
|3     |end INCR        |            |1    |                        |
```
Tương tự 4 5 6.
=> sử dụng atomic counter đảm bảo tuần tự khi thao tác dùng chung tài nguyên
```

```
Đề xuất có thể:
Khi nhiều người cùng nộp cùng một bài, có thể thêm lock riêng theo problemId. 
Người nào được accepted đầu tiên thì Redis sẽ ghi nhận key này. 
Những người sau khi chấm xong mà thấy key problem:X:accepted đã tồn tại thì không thể ghi accepted nữa => đây chỉ thỏa mãn với trường hợp đăng ký tín nhưng đây là trình chấm nên ai cũng có thể accepted => không cần thiết
```

### 2. Làm sao khi hàng nghìn user submit thì hệ thống không bị crash hay user phải đợi chờ đợi vài phút mới nộp được bài vì số lượng người nộp rất đông ?

```
Hàng nghìn request /submit phải xử lý ổn định, không nghẽn.
Không để user phải chờ quá lâu (giảm queue backlog và response latency)
```

```
RabbitMQ Queue bản chất là queue nó sử dụng giao thức AMQP(Advanced message queuing protocol) đảm bảo độ tin cậy khi gửi gói tin và chức năng quan trọng của nó là định tuyến.

Khi sử dụng RabbitMQ Queue giúp phân tán công việc
Khi user nhấn submit hệ thống không xử lý code ngay mà nó gửi message vào RabbitMQ => xếp hàng đợi để các con judge worker xử lý song song.
```
```
Tên queue đó: là judge_submissions
có 3 con worker container mỗi con sử lý đồng thời 5 message
``` 
```
Redis lock giúp tránh spam submit (người dùng chỉ được submit mỗi 3 giây)
Redis counter và cache giúp truy xuất nhanh, không cần query MongoDB liên tục → giảm tải I/O.
=> Thực chất nó là 1 dạng của redis aside nếu đã có trong bộ nhớ đệm thì lấy ra luôn không thì vào db để lấy cho vào bộ nhớ đệm
```

```
MongoDB connection pool:
cho phép tối đa kết nối đồng thời để giảm độ trễ khi insert nhiều submission
```

```
Horizontal scaling:
chạy 2 instance submission-service thông qua replica ở docker-compose
```

```
Estimated wait time:
ước lượng thời gian cứ 10 submissions xử lý 1s user thấy thời gian chờ thực tế trên UI.
```

### 3. Kịch bản test 10000 user cùng submit 1 bài cùng lúc. Nếu có 10.000 user cùng nộp code 1 lúc, hệ thống có crash, nghẽn hàng đợi, hoặc xử lý chậm không?

```
Sử dụng worker threads để để giả lập 10000 người cùng submit code cùng lúc

Tạo tối đa 8 worker //
mỗi worker gửi requests cho các nhóm user riêng => sau đấy tổng hợp kết quả
```

```
Mục đích: 
Kiểm tra redis lock: xem khi hàng nghìn user cùng submit redis có bottleneck không
Kiểm tra RabbitMQ: đánh giá xem queue có đầy hoặc delay không
Kiểm tra MongoDB Pool: đánh giá tốc độ ghi submission record
Kiểm tra API Layer: đo độ trễ trung bình của /submit endpoint
Phát hiện race condition: Nếu có lỗi trùng submissionId, duplicate record, hay lock hỏng
```

## Worker
```
Chưa tối ưu
```


```
1. Submission-service
Nhận request submit từ submission-service
Ghi lại trạng thái pending vào DB
sau đấy push job vào RabbitMQ Queue name = judge_submissions
```

```
2. Judge-worker
Kết nối tới Redis để lưu trạng thái nhanh (judging, result, stats)
Kết nối RabbitMQ để nhận các submission job
Dùng prefetch(MAX_CONCURRENT_JOBS) để giới hạn số job song song (mặc định 5)

Nhận job từ RabbitMQ
Chấm bài dựa trên ngôn ngữ Js Py Java, hiện tại C++ đang bị sai format nên chưa chấm được
So sánh output với các test case chuẩn và cập nhật lại submission-service bằng rest API
Từ judging thành completed và ghi kết quả là:
accepted | wrong_answer | failed

Sau đấy ghi cache vào redis result:subbmissionId
Ghi thống kê vào redis (stats:*)

Mỗi lần chạy, worker:
    Truyền testCases vào
    So sánh output và expected
    Lưu kết quả vào results[]
    Đánh dấu allPassed = true/false

```

```
3. user-service
Khi bài được accepted => worker gọi POST /internal/user/{userId}/solved để tăng điểm và cập nhật danh sách problem đã giải

```


## API-Gateway
```
Mới cấu hình để kết nối các service với nhau
chưa tối ưu
```
 

## Sandbox

```
Sandbox là 1 môi trường chạy mã nguồn bị cô lập 
Trong sandbox chương trình chỉ được phép:
1. Chạy trong giới hạn tài nguyên CPU, RAM, Time,...
2. Không được truy cập FileSystem, Internet
3. Không được truy cập bộ nhớ máy thật
4. Không thể gây crash hay chiếm quyền hệ thống
```

```
Tại sao hệ thống chấm bài Online judge phải dùng Sandbox ?
1. Người dùng có thể gửi mã độc
2. Người dùng có thể gửi code vô hạn, gây infinite loop
3. Người dùng có thể cố tình đọc file system, tấn công server, đánh cắp dữ liệu

Giải pháp của sanbox:
1. Security:
    - chặn truy cập: filesystem, network, process system
    => ngăn hacker gửi code phá server
2. Resource limiting
    - Sandbox áp CPU, RAM, Time limiter
    => Nếu vượt quá => program bị kill ngay (TLE, MLE, RE)
3. Tạo môi trường giống nhau cho mọi bài nộp
    - Không ai được ưu tiên hay gian lận tài nguyên
4. Ngăn crash System
    - Nếu crash trong sandbox thì system vẫn an toàn
```

```
Cách hoạt động của Sandbox:
1. Khi nhấn submit: => system complier code
2. Create 1 sandbox: bằng container
3. Copy input test vào sandbox
4. Run code trong giới hạn tài nguyên
5. Thu output => so sánh với đáp án
6. Xóa sandbox => không còn dấu vết gì
```

```
Sandbox được xây dựng như thế nào ?
1. Docker
2. Chạy code trong container - seccomp
3. Chặn các system call nguy hiểm (open, exec, socket,...) - cgroups
4. Giới hạn CPU, RAM, IO - chroot
Biến thư mục chạy thành root directory mới => Không truy cập được ra ngoài
```

```
Ví dụ codeforces dùng:
1. isolate (sandbox của olympic IOI)
2. seccomp
3. cgroups
LeetCode / HackerRank sử dụng container công nghệ tương tự Docker
```

### Nguồn tham khảo
1. https://github.com/ioi/isolate
2. https://docs.docker.com/engine/security/
3. https://codeforces.com/blog/entry/79