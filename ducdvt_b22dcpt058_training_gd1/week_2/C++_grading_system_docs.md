# Hệ thống Chấm bài C++ Tự động

## 1. Tổng quan Giải pháp (Solution Overview)

Hệ thống được thiết kế để tự động biên dịch, thực thi và chấm điểm các bài nộp (submission) code C++. Kiến trúc sẽ tách biệt rõ ràng giữa giao diện người dùng (FE), hệ thống quản lý (BE - CMS) và các máy chấm bài (Grading Workers) để đảm bảo tính ổn định, an toàn và khả năng mở rộng.

- **Frontend (FE):** Cung cấp giao diện cho người dùng nộp bài, xem kết quả, và cho quản trị viên quản lý bài tập, người dùng.

- **Backend (BE - CMS):** API chính, quản lý logic nghiệp vụ, xác thực, lưu trữ bài nộp và điều phối các tác vụ chấm bài.

- **Message Queue:** Trung gian giao tiếp, giúp BE và Workers hoạt động bất đồng bộ, giảm tải và tăng khả năng chịu lỗi.

- **Grading Workers (Máy chấm):** Các tiến trình độc lập, nhận nhiệm vụ từ Queue, thực thi code trong môi trường an toàn và báo cáo kết quả.

## 2. Lựa chọn Công nghệ (Technology Stack)

| Hạng mục | Công nghệ | Lý do lựa chọn |
|----------|-----------|----------------|
| **Frontend** | **React.js + Ant Design (Antd)** | React cung cấp UI linh hoạt, Antd cung cấp bộ component chuyên nghiệp, đầy đủ, giúp đẩy nhanh tiến độ. |
| **Backend (CMS)** | **Node.js + Express.js** | Express.js là framework tối giản, nhanh và linh hoạt, rất phù hợp để xây dựng các API nhận bài nộp và quản lý nghiệp vụ. |
| **Cơ sở dữ liệu** | **PostgreSQL** | Mạnh mẽ, hỗ trợ tốt các giao dịch (transaction) và dữ liệu có cấu trúc (user, problem, submission). |
| **Message Queue** | **RabbitMQ** | Hệ thống queue message tin cậy, hỗ trợ nhiều kiểu định tuyến (routing), phù hợp cho mô hình Worker-Queue. |
| **Caching / Lock** | **Redis** | Tốc độ truy xuất nhanh, hỗ trợ nhiều cấu trúc dữ liệu, lý tưởng cho Caching, Rate Limit và Distributed Lock. |
| **Môi trường Chấm** | **Docker trên Ubuntu Server** | Docker cung cấp khả năng cô lập (isolation) cấp hệ điều hành, dễ dàng giới hạn tài nguyên và dọn dẹp. |
| **Ngôn ngữ Máy chấm** | **Python** | Python cực kỳ mạnh mẽ cho việc viết script điều khiển hệ thống, quản lý file, và gọi các tiến trình con (subprocess). Có thư viện docker-py và pika (RabbitMQ) hỗ trợ rất tốt. |

## 3. Kiến trúc Hệ thống & Luồng xử lý

Kiến trúc được xây dựng dựa trên mô hình **Worker-Queue** để tách biệt logic nghiệp vụ và tác vụ nặng (chấm bài).

### Luồng chấm bài (Grading Flow)

#### Submit (FE)
Người dùng nộp code (C++) qua giao diện React.

- **Vấn đề Idempotent (Point 1):** Khi nhấn "Submit", FE sẽ disable nút và hiển thị loading. Có thể gửi kèm một request_id (UUID) duy nhất. BE sẽ dùng request_id này (lưu tạm trong Redis) để từ chối các yêu cầu trùng lặp trong một khoảng thời gian ngắn.

#### Receive (BE - CMS)

- BE nhận code, xác thực người dùng, bài tập.
- Lưu bài nộp vào DB PostgreSQL với trạng thái Pending (Đang chờ).
- Đẩy một "Job Message" (chứa submission_id, problem_id, source_code) vào **RabbitMQ**.

#### Consume (Grading Worker)

Đây là một (hoặc nhiều) script Python chạy liên tục như một dịch vụ (service).

- Sử dụng thư viện pika để lắng nghe (consume) message từ RabbitMQ.
- Khi nhận được submission_id, Worker sẽ:
  - Giao tiếp với BE để lấy chi tiết bài nộp (hoặc thông tin đã có sẵn trong message).
  - Sử dụng thư viện docker-py (Docker SDK cho Python) để khởi chạy một container Docker (Ubuntu + g++).
  - **Thực thi logic chấm bài:**
    1. Mount code vào container.
    2. Chạy lệnh biên dịch g++ bên trong container (dùng container.exec_run()).
    3. Lặp qua các test case.
    4. Chạy file thực thi với timeout và giới hạn tài nguyên đã thiết lập khi tạo container (--memory, --cpus, --ulimit).
    5. Thu thập kết quả (AC, WA, TLE, MLE...).
  - **Dọn dẹp:** Dùng docker-py để đảm bảo container được xoá (container.remove(force=True)).
  - Báo cáo kết quả về cho BE (qua API hoặc một Queue kết quả).

#### Execute (The Hard Part - Point 3)

Máy chấm tạo một thư mục tạm.

Nó khởi chạy một **Docker container** mới từ một image Ubuntu đã cài sẵn g++.

**Thiết lập môi trường cô lập:**

- `--network=none`: Vô hiệu hóa mạng hoàn toàn.
- `--memory=256m`: Giới hạn bộ nhớ (chống MLE).
- `--cpus=1`: Giới hạn CPU.
- `--read-only`: Mount thư mục code ở chế độ read-only (chỉ cho phép ghi vào thư mục output).
- `-u <username>`: Chạy container với user không phải root.

**Biên dịch:** Chạy g++ bên trong container. Nếu lỗi, ghi nhận Compile Error (CE) và kết thúc.

**Chạy Test Cases:**

- Lặp qua từng test case (input/output) của bài tập.
- Chạy file thực thi với input.
- Sử dụng lệnh timeout (của Linux) để giới hạn thời gian (chống TLE).
- So sánh output của chương trình với output chuẩn.
- Ghi nhận kết quả: Accepted (AC), Wrong Answer (WA), Time Limit Exceeded (TLE), Memory Limit Exceeded (MLE), Runtime Error (RE).

#### Cleanup & Report

- Sau khi chạy xong (hoặc lỗi), máy chấm **phải** huỷ container (docker rm -f) để dọn dẹp.
- Máy chấm gửi kết quả chi tiết về cho BE (qua một API nội bộ hoặc một Queue kết quả).

#### Update (BE - CMS)
BE nhận kết quả, cập nhật vào DB (PostgreSQL).

#### Display (FE)
FE (thông qua polling hoặc WebSocket) nhận được kết quả mới và hiển thị cho người dùng.

### Luồng Dữ liệu

- Admin tạo problems và test_cases (Bảng users, problems, test_cases).
- User nộp bài: Một hàng mới được tạo trong submissions (trạng thái pending).
- BE (Express.js) đẩy submission_id vào RabbitMQ.
- Worker (Python) nhận submission_id:
  - Cập nhật submissions.status = grading.
  - Truy vấn problems (để lấy time/memory limit) và test_cases (để lấy input/output).
  - Chạy code.
  - Ghi kết quả của từng test case vào submission_results.
  - Sau khi xong, Worker tính toán tổng điểm (từ submission_results) và cập nhật lại submissions.status (thành accepted, wrong_answer...) và submissions.score.
- FE (React) liên tục hỏi (poll) hoặc lắng nghe (WebSocket) submissions.submission_id để lấy kết quả cuối cùng.

## 4. Các Vấn đề Nghiên cứu

Đây là các giải pháp cho những vấn đề khó để đảm bảo hệ thống ổn định và hiệu suất cao.

### 4.1. An toàn Môi trường (Sandbox & Security)

Việc dùng Docker giải quyết phần lớn vấn đề cô lập, nhưng cần nghiên cứu kỹ:

#### Tấn công Fork Bomb
Người dùng submit code `while(true) fork();`.

**Giải pháp:** Giới hạn số lượng process (PID) mà user bên trong container có thể tạo ra bằng cờ `--ulimit nproc=64` (hoặc tương tự).

#### Tấn công System Call
Code C++ có thể gọi các system call nguy hiểm (ví dụ: reboot).

**Giải pháp:** Docker sử dụng seccomp (Secure Computing Mode) của Linux để lọc các system call. Cần định nghĩa một profile seccomp chặt chẽ, chỉ cho phép các hàm cần thiết (như read, write, execve, brk) và cấm toàn bộ các hàm khác (như socket, fork, mount).

#### Vấn đề TLE chính xác
Lệnh timeout chỉ đo "wall time" (thời gian thực tế), không phải "CPU time". Một process có thể sleep() mà không tốn CPU.

**Giải pháp:** Cần kết hợp timeout (wall time) và ulimit -t (CPU time) để đảm bảo giới hạn thời gian thực thi của CPU.

### 4.2. Caching (Tăng tốc độ)

**Vấn đề:** Người dùng nộp lại y hệt code cũ. Hệ thống chấm các bài giống hệt nhau nhiều lần.

**Giải pháp:**

1. Khi nhận bài nộp, BE tạo một hash (ví dụ: SHA-256) của source code.
2. Kiểm tra trong **Redis**: `GET cache:hash:<sha256_hash>`.
3. Nếu tìm thấy kết quả (result_id cũ), BE có thể *ngay lập tức* trả về kết quả đó mà không cần đẩy vào queue.
4. Nếu không thấy, BE tiến hành chấm bình thường. Sau khi có kết quả, lưu vào Redis: `SET cache:hash:<sha256_hash> <result_id> EX 3600`.
5. Điều này giảm tải cực lớn cho hệ thống chấm.

### 4.3. Rate Limit (Chống spam)

**Vấn đề:** Một người dùng submit 100 lần trong 1 phút, làm tắc nghẽn queue.

**Giải pháp:** Sử dụng **Redis** để giới hạn số lần nộp bài.

- **Cách làm:** Dùng thuật toán Token Bucket hoặc đơn giản là INCR và EXPIRE.
- Khi user (ví dụ user_id: 123) submit, BE chạy: `INCR rate:limit:submit:user:123`.
- Nếu kết quả > 10 (ví dụ), trả về lỗi HTTP 429 Too Many Requests.
- Nếu là lần đầu (INCR trả về 1), set Hết hạn: `EXPIRE rate:limit:submit:user:123 60` (giới hạn 10 lần/phút).

### 4.4. Distributed Lock (Đảm bảo tính toàn vẹn)

**Vấn đề:** Cần đảm bảo chỉ có *một* máy chấm được xử lý một submission_id. Mặc dù RabbitMQ thường đảm bảo "at-most-once" hoặc "at-least-once", nhưng có thể có trường hợp lỗi phức tạp.

**Giải pháp:** Khi một máy chấm nhận submission_id: 555, nó phải chiếm quyền "khoá" (lock) bài đó.

- **Cách làm:** Máy chấm gọi `SET lock:submission:555 "worker_id_A" NX EX 120` trên **Redis**.
- NX (Not Exists): Chỉ thành công nếu key chưa tồn tại.
- Nếu thành công: Máy chấm bắt đầu làm việc. Sau khi xong, nó `DEL lock:submission:555`.
- Nếu thất bại (máy B khác đã khoá): Máy chấm này bỏ qua, chờ việc mới.
- EX 120 (Hết hạn 2 phút): Đề phòng máy chấm bị "chết" khi đang giữ khoá.

### 4.5. Load Shedding (Tự bảo vệ khi quá tải)

**Vấn đề:** Có 1000 người dùng submit cùng lúc (cuộc thi), số lượng job trong **RabbitMQ** tăng vọt (ví dụ > 10.000 jobs). Các máy chấm không xử lý kịp, DB bắt đầu chậm.

**Giải pháp (Nâng cao):** BE (CMS) phải *chủ động từ chối* yêu cầu mới để bảo vệ hệ thống.

- BE định kỳ kiểm tra kích thước của queue (ví dụ: rabbitmqctl list_queues).
- Nếu queue_length > 10000 (ngưỡng báo động): BE kích hoạt "chế độ quá tải".
- Mọi yêu cầu submit mới sẽ bị từ chối ngay lập tức với lỗi HTTP 503 Service Unavailable ("Hệ thống đang quá tải, vui lòng thử lại sau 1 phút").
- Điều này "sheds" (vứt bỏ) bớt load, cho phép các máy chấm hiện có "thở" và xử lý hết các job tồn đọng.
- Khi queue_length giảm xuống dưới ngưỡng an toàn, BE tự động tắt chế độ này và nhận bài nộp trở lại.

## 5. Thiết kế Database

### 5.1 Các kiểu dữ liệu Enum

```sql
-- Vai trò của người dùng
CREATE TYPE user_role AS ENUM ('student', 'admin');

-- Độ khó của bài tập
CREATE TYPE problem_difficulty AS ENUM ('easy', 'medium', 'hard');

-- Trạng thái tổng quan của bài nộp
CREATE TYPE submission_status AS ENUM (
  'pending',                -- Đang chờ trong queue
  'grading',                -- Đang được máy chấm xử lý
  'accepted',               -- Đúng hoàn toàn
  'wrong_answer',           -- Sai kết quả
  'time_limit_exceeded',    -- Chạy quá thời gian
  'memory_limit_exceeded',  -- Dùng quá bộ nhớ
  'compile_error',          -- Lỗi biên dịch
  'runtime_error'           -- Lỗi khi chạy (vd: crash)
);
```

### 5.2 Các bảng chính

#### Bảng users
Lưu trữ thông tin người dùng và quản trị viên (CMS):

```sql
CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(100),
  role user_role NOT NULL DEFAULT 'student',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

#### Bảng problems
Lưu trữ thông tin chi tiết của từng bài tập:

```sql
CREATE TABLE problems (
  problem_id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,  -- Có thể chứa Markdown
  difficulty problem_difficulty NOT NULL DEFAULT 'medium',
  
  -- Giới hạn tài nguyên cho máy chấm
  time_limit_ms INT NOT NULL DEFAULT 1000,    -- Giới hạn thời gian (miliseconds)
  memory_limit_mb INT NOT NULL DEFAULT 256,   -- Giới hạn bộ nhớ (megabytes)
  
  author_id INT,  -- Quản trị viên tạo bài này
  is_public BOOLEAN NOT NULL DEFAULT TRUE,    -- Có hiển thị cho mọi người hay không
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE SET NULL
);
```

#### Bảng test_cases
Lưu trữ các bộ dữ liệu vào/ra (test case) cho mỗi bài tập:

```sql
CREATE TABLE test_cases (
  test_case_id SERIAL PRIMARY KEY,
  problem_id INT NOT NULL,
  input TEXT NOT NULL,              -- Dữ liệu đầu vào (stdin)
  expected_output TEXT NOT NULL,    -- Kết quả mong đợi (stdout)
  is_sample BOOLEAN NOT NULL DEFAULT FALSE,  -- Test case này có hiển thị cho người dùng không?
  points INT NOT NULL DEFAULT 1,    -- Điểm cho test case này (nếu chấm từng phần)
  
  FOREIGN KEY (problem_id) REFERENCES problems(problem_id) ON DELETE CASCADE
);
```

#### Bảng submissions
Lưu trữ mọi bài nộp của người dùng:

```sql
CREATE TABLE submissions (
  submission_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL,
  problem_id INT NOT NULL,
  source_code TEXT NOT NULL,
  language VARCHAR(20) NOT NULL DEFAULT 'cpp',  -- (Có thể mở rộng thành ENUM sau)
  
  -- Trạng thái tổng quan của bài nộp
  status submission_status NOT NULL DEFAULT 'pending',
  
  -- Kết quả tổng hợp (lấy từ submission_results)
  score INT DEFAULT 0,                  -- Tổng điểm đạt được
  max_execution_time_ms INT,            -- Thời gian chạy lâu nhất
  max_execution_memory_mb INT,          -- Bộ nhớ dùng nhiều nhất
  
  submitted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (problem_id) REFERENCES problems(problem_id) ON DELETE CASCADE
);

-- Tạo index để tăng tốc truy vấn "Bài nộp của tôi" hoặc "Các bài nộp của bài A"
CREATE INDEX idx_submissions_user_problem ON submissions (user_id, problem_id);
```

#### Bảng submission_results
Lưu trữ kết quả chi tiết của **từng test case** cho mỗi bài nộp:

```sql
CREATE TABLE submission_results (
  result_id SERIAL PRIMARY KEY,
  submission_id INT NOT NULL,
  test_case_id INT NOT NULL,
  
  -- Trạng thái của riêng test case này
  status submission_status NOT NULL,
  execution_time_ms INT,
  execution_memory_mb INT,
  
  -- Lưu lại output thực tế và lỗi (nếu có) để debug
  actual_output TEXT,
  error_message TEXT,  -- Dùng để lưu Compile Error hoặc Runtime Error
  
  FOREIGN KEY (submission_id) REFERENCES submissions(submission_id) ON DELETE CASCADE,
  FOREIGN KEY (test_case_id) REFERENCES test_cases(test_case_id) ON DELETE CASCADE,
  
  -- Đảm bảo một bài nộp chỉ có 1 kết quả cho 1 test case
  UNIQUE(submission_id, test_case_id)
);
```

## 6. Các API dự kiến

### a. Module xác thực

#### Đăng nhập
**[POST] /auth/login**

Request:
```json
{
  "email": "user@example.com",
  "password": "your_password"
}
```

Response:
```json
{
  "token": "ey...[jwt_token]...",
  "user": {
    "user_id": 1,
    "full_name": "Nguyen Van A",
    "email": "user@example.com",
    "role": "student"
  }
}
```

#### Đăng ký
**[POST] /auth/register**

Request:
```json
{
  "full_name": "Tran Thi B",
  "email": "new_user@example.com",
  "password": "strong_password_123"
}
```

Response:
```json
{
  "user_id": 2,
  "full_name": "Tran Thi B",
  "email": "new_user@example.com",
  "role": "student"
}
```

### b. Module Bài tập

#### Lấy danh sách tất cả các bài tập công khai (public)
**[GET] /problems**

Query params:
```
?page=1
?limit=20
?difficulty=medium
```

Response:
```json
{
  "pagination": { "total": 50, "page": 1, "limit": 20 },
  "data": [
    {
      "problem_id": 1,
      "title": "Bài toán A+B",
      "difficulty": "easy",
      "status": "solved"  // (Trạng thái giải của user hiện tại, nếu đã login)
    },
    {
      "problem_id": 2,
      "title": "Dãy con tăng dần dài nhất",
      "difficulty": "medium",
      "status": "unsolved"
    }
  ]
}
```

#### Lấy thông tin chi tiết của một bài tập
**[GET] /problems/:problem_id**

Response:
```json
{
  "problem_id": 1,
  "title": "Bài toán A+B",
  "description": "Cho hai số nguyên A và B...",
  "difficulty": "easy",
  "time_limit_ms": 1000,
  "memory_limit_mb": 256,
  "sample_test_cases": [
    {
      "input": "2 3",
      "expected_output": "5"
    },
    {
      "input": "10 5",
      "expected_output": "15"
    }
  ]
}
```

### c. Module Nộp bài

#### Nộp code để chấm điểm
**[POST] /submissions**

Access: Student (Phải có tài khoản)

Request:
```json
{
  "problem_id": 1,
  "language": "cpp",  // (Mặc định 'cpp', có thể mở rộng sau)
  "source_code": "#include <iostream> int main() { ... }"
}
```

Response:
```json
{
  "submission_id": 123,
  "status": "pending",
  "submitted_at": "2025-11-02T15:30:00Z"
}
```

#### Lấy kết quả của một bài nộp cụ thể
**[GET] /submissions/:submission_id**

Access: Student (chỉ được xem bài của mình), Admin

Lấy kết quả của một bài nộp cụ thể. **FE sẽ gọi API này lặp lại (poll)** 2-3 giây/lần cho đến khi status không còn là pending hay grading.

Response khi đang chờ hoặc đang chấm:
```json
{
  "submission_id": 123,
  "status": "grading",  // hoặc 'pending'
  "submitted_at": "...",
  "problem": { "problem_id": 1, "title": "Bài toán A+B" }
}
```

Response khi đã chấm xong:
```json
{
  "submission_id": 123,
  "status": "accepted",  // 'wrong_answer', 'time_limit_exceeded', ...
  "submitted_at": "...",
  "source_code": "#include ...",
  "score": 100,
  "max_execution_time_ms": 120,
  "max_execution_memory_mb": 15,
  "problem": { "problem_id": 1, "title": "Bài toán A+B" },
  "results": [  // Chi tiết từng test case
    { "test_case_id": 1, "status": "accepted", "execution_time_ms": 110 },
    { "test_case_id": 2, "status": "accepted", "execution_time_ms": 120 }
  ]
}
```

#### Lấy lịch sử nộp bài
**[GET] /submissions**

Access: Student (chỉ thấy bài của mình), Admin (thấy mọi bài)

Query Params:
```
?page=1
?limit=10
?problem_id=1
?user_id=5  (Chỉ Admin)
```

Response:
```json
{
  "pagination": { "total": 5, "page": 1, "limit": 10 },
  "data": [
    {
      "submission_id": 123,
      "status": "accepted",
      "score": 100,
      "problem_title": "Bài toán A+B",
      "submitted_at": "..."
    },
    {
      "submission_id": 122,
      "status": "wrong_answer",
      "score": 0,
      "problem_title": "Bài toán A+B",
      "submitted_at": "..."
    }
  ]
}
```

### d. Module Quản lý
**(Tất cả API ở module này đều access: admin)**

#### Tạo một bài tập mới
**[POST] /admin/problems**

Request:
```json
{
  "title": "Bài toán C",
  "description": "Nội dung bài toán...",
  "difficulty": "hard",
  "time_limit_ms": 2000,
  "memory_limit_mb": 512,
  "test_cases": [  // Thêm test case ngay lúc tạo
    { 
      "input": "1 1", 
      "expected_output": "2", 
      "is_sample": true, 
      "points": 10 
    },
    { 
      "input": "100 200", 
      "expected_output": "300", 
      "is_sample": false, 
      "points": 90 
    }
  ]
}
```

#### Cập nhật thông tin một bài tập
**[PUT] /admin/problems/:problem_id**

Request (Gửi các trường cần cập nhật):
```json
{
  "title": "Bài toán C (Đã sửa)",
  "is_public": false
}
```
