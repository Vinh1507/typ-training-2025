# Lựa chọn dự án: Hệ thống chấm bài C++


## Tài liệu giải pháp
- Xây dựng trang web chấm bài C++ theo kiến trúc MicroService, áp dụng DDD (domain driven design) để quản lý nghiệp vụ tách biệt, rõ ràng.
- Hệ thống được chia thành CMS (Java/Spring Boot) và Judge (C++/Docker), giao tiếp bất đồng bộ qua Queue (RabbitMQ)
- Chấm bài an toàn: Sử dụng Docker làm Sandbox trên nền Ubuntu để cô lập hoàn toàn mã nguồn C++ của thí sinh, ngăn chặn rủi ro bảo mật.
- Tập trung vào vấn đề khó: Backend API được giữ ở mức tối giản, chủ yếu xoay quanh việc quản lý Submission Aggregate và gửi Job chấm bài vào Queue.

## Công nghệ lựa chọn


| Thành phần      | Công nghệ             | Vai trò chính                                                                                   |
|-----------------|----------------------|-------------------------------------------------------------------------------------------------|
| Backend API     | Java (Spring Boot)   | Xây dựng nghiệp vụ chính (CMS), quản lý Aggregate Roots, cung cấp API cho hệ thống.             |
| Frontend        | React & Ant Design   | Xây dựng giao diện người dùng hiện đại, chuyên nghiệp, đảm bảo trải nghiệm tốt và idempotency.   |
| Judge Core      | C++ & Docker         | Chấm bài hiệu suất cao, tạo môi trường thực thi cô lập (sandbox) trên nền Ubuntu.               |
| Hạ tầng         | Redis, PostgreSQL    | Redis dùng cho Caching, Distributed Lock, Idempotency Key; PostgreSQL lưu trữ dữ liệu quan trọng.|


## Vấn đề dự định nghiên cứu 

### A. Judge Security & Resource Control (Sandboxing)
**Nghiên cứu:** Chi tiết cách cấu hình Docker (trên Ubuntu) để tạo Sandbox.

**Trọng tâm:**  
- Sử dụng các tham số Docker để giới hạn CPU, RAM (Cgroups).
- Vô hiệu hóa Network 
- Giới hạn quyền truy cập Filesystem (Volume mapping chỉ cho thư mục làm việc).

**Mục tiêu:**  
Đảm bảo cô lập hoàn toàn mã nguồn C++ của thí sinh, ngăn chặn các tấn công hệ thống và kiểm soát TLE/MLE chính xác.

---

### B. Distributed Lock (Khóa Phân tán)
**Nghiên cứu:** Kỹ thuật triển khai Khóa Phân tán trong Java bằng Redis RedLock hoặc các thư viện tương tự.

**Trọng tâm:**  
- Áp dụng khóa để bảo vệ tính nhất quán của các thao tác:
  - **Idempotency Check:** Khóa nhanh khi tạo Submission.
  - **Trạng thái Submission:** Đảm bảo chỉ một Judge Worker được phép cập nhật trạng thái của một Submission cụ thể.

---

### C. Load Shedding & Rate Limit (Quản lý Tải)
**Nghiên cứu:** Chiến lược xử lý khi hệ thống bị quá tải đột ngột.

**Trọng tâm:**  
- **Rate Limit:** Triển khai giới hạn số lần nộp bài/phút (ví dụ: dùng Redis Counter) để bảo vệ tài nguyên chấm bài.
- **Load Shedding:** Nghiên cứu cơ chế để Judge Worker chủ động từ chối nhận Job từ Queue khi tài nguyên hệ thống (Load Average/Memory) đạt ngưỡng nguy hiểm, nhằm duy trì hiệu suất cho các Job đang chạy.
