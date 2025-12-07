-- Kế hoạch làm project Web chấm code C++ --
1. Xác định yêu cầu
Xác định yêu cầu:
User chỉ được:
- chọn bài
- xem đề
- gửi file code
- ấn Submit
- xem kết quả chấm


Thiết kế database:
- Problems
- TestCases
- Submissions (để lưu lịch sử và debug)


Vẽ flow Submit → Chấm → Trả kết quả 
                   ┌─────────────────────────────┐
                   │          FRONTEND            │
                   │           (React)            │
                   ├─────────────────────────────┤
                   │ - Giao diện xem đề bài       │
                   │                              │
                   │ - Nút  Submit                │
                   │ - Hiển thị kết quả chấm      │
                   └───────────────┬─────────────┘
                                   │  POST /submit
                                   ▼
             ┌──────────────────────────────────────────┐
             │                BACKEND API                │
             │           (Node.js + Express)            │
             ├──────────────────────────────────────────┤
             │ 1. Nhận code + ProblemId                 │
             │ 2. Lấy test case từ Database             │
             │ 3. Gửi code vào Sandbox để chạy          │
             │ 4. Nhận output từ Sandbox                │
             │ 5. So sánh với expected output           │
             │ 6. Tính điểm + lưu submission            │
             └───────────────┬──────────────────────────┘
                             │
                             ▼
           ┌────────────────────────────────────────┐
           │                SANDBOX                  │
           │         (Docker or Safe Runner)         │
           ├────────────────────────────────────────┤
           │ - Tạo file Main.cpp                     │
           │ - Biên dịch bằng g++                    │
           │ - Chạy với giới hạn:                    │
           │     • CPU time limit                    │
           │     • Memory limit                      │
           │     • No file access                    │
           │ - Gửi output về Backend                 │
           └────────────────────────────────────────┘
                             │
                             ▼
              ┌────────────────────────────────┐
              │            DATABASE             │
              │       (MySQL / MongoDB)         │
              ├────────────────────────────────┤
              │  Bảng Problems                  │
              │    - id, title, description     │
              │                                  │
              │  Bảng TestCases                 │
              │    - id, problem_id             │
              │    - input, expected_output     │
              │                                  │
              │  Bảng Submissions               │
              │    - id, user_id, code          │
              │    - problem_id, score, result  │
              └────────────────────────────────┘


🔥 TIMELINE RÚT GỌN THEO NGÀY (Dự án nhỏ)
🗓 Tuần 1
Ngày
Công việc
1 Thiết kế database + cấu trúc backend
2 Tạo backend + API problems/testcases
3 Làm phần compile C++
4 Làm phần run code (với timeout)
5 Tạo API /submit chấm toàn bộ test case
6 Test backend, hoàn thiện sandbox
7 Nghỉ / Fix lỗi backend

🗓 Tuần 2
Ngày
Công việc
1 Tạo project React
2 UI danh sách bài + UI xem đề
3 UI code editor + Submit button
4 Gọi API submit và hiển thị kết quả
5 Làm đẹp UI + xử lý lỗi
6 Test toàn hệ thống
7 Hoàn thiện + làm báo cáo

