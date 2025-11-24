# Phần 1 : Viết file .gitlab-ci.yml để build và test ứng dụng sử dụng Docker Compose
## 1. Cấu trúc và quy tắc cơ bản của file .gitlab-ci.yml
### a. Cấu trúc toàn cục (Global)
- `image`: Xác định môi trường (Docker Image) để chạy các câu lệnh.
- `stages` : Định nghĩa trình tự chạy. Các job cùng stage chạy song song, stage trước xong mới đến stage sau.
- `variables` : Định nghĩa các biến môi trường dùng chung cho toàn bộ pipeline.
> **Lưu ý:** Vì các biến môi trường có thể chứa thông tin nhạy cảm (như mật khẩu, token), nên tốt nhất bạn nên khai báo chúng trong phần **CI/CD Settings** của dự án trên GitLab thay vì viết trực tiếp trong file `.gitlab-ci.yml`.
___
### b. Định nghĩa Job (Job Keywords)
- `script` (Bắt buộc) : Chứa danh sách các lệnh Terminal sẽ được thực thi.
>  **Lưu ý:** *Các lệnh chạy tuần tự từ trên xuống dưới. Nếu một lệnh sai (exit code khác 0), job dừng ngay lập tức.*
- `stage` : Xác định job này thuộc giai đoạn nào (phải khớp với tên trong stages ở trên).
- `before_script` / `after_script` : Chạy trước hoặc sau phần script chính. Dùng để cài đặt môi trường hoặc dọn dẹp.
- `needs` : Báo cho job này biết cần chờ job nào xong mới được chạy, thường dùng để tự động tải Artifacts từ job đó về. 
___
### c. Điều kiện chạy (Logic Flow)
- `rules` (Thay thế cho only/except cũ) : Quyết định khi nào job được phép chạy. Các biến thường dùng:
    - `$CI_COMMIT_TAG` : Chạy khi có Tag.
    - `$CI_COMMIT_BRANCH == "main"` : Chạy khi push vào nhánh main.
- `when` : Chỉnh thời điểm chạy. Giá trị phổ biến:
    - `on_success` (Mặc định) : Chạy khi job trước thành công.
    - `manual` : Phải bấm nút bằng tay trên web mới chạy (thường dùng cho Deploy).
    - `always` : Luôn chạy dù job trước có lỗi (thường dùng để gửi thông báo lỗi).
___
### d. Dữ liệu & Lưu trữ (Artifacts & Cache)
- `artifacts` : Lưu lại file/thư mục sau khi job kết thúc để dùng cho job sau hoặc tải về. Thuộc tính con:
    - `paths` : Đường dẫn thư mục muốn lưu.
    - `expire_in` : Thời gian tồn tại (vd: 1 week, 2 days).
- `cache` (Khác với Artifacts) : Lưu file tạm (như node_modules, .m2) để tăng tốc độ cho lần chạy sau. Không dùng để truyền file giữa các stage.
___
### e. Dịch vụ (Services)
- `services` : Khởi tạo các container phụ trợ song song với job.
    - ***Trường hợp dùng nhiều nhất***: `docker:dind` (Docker-in-Docker) để cho phép chạy lệnh docker build/run bên trong GitLab Runner.
___
### f. QUY TẮC VÀNG VỀ CÚ PHÁP YAML ⚠️
YAML rất nhạy cảm, sai một dấu cách cũng lỗi pipeline.

Thụt lề (Indentation): Dùng 2 dấu cách (Space), KHÔNG dùng phím Tab. Các cấp con phải thụt vào đều nhau.

Dấu hai chấm (:): Sau dấu hai chấm phải luôn có 1 dấu cách.

✅ Đúng: image: node

❌ Sai: image:node

Danh sách (-): Các gạch đầu dòng phải thẳng hàng nhau.

Chuỗi ký tự: Nếu câu lệnh chứa ký tự đặc biệt (:, {, }, *), hãy bao quanh bằng dấu ngoặc kép "".

Ví dụ: script: - echo "Build for tag: $CI_COMMIT_TAG"
___
## 2. Ví dụ thực tế: CI/CD cho ứng dụng sử dụng Docker Compose
Dưới đây là ví dụ về file `.gitlab-ci.yml` để tự động build và test một ứng dụng sử dụng Docker Compose mỗi khi có Tag mới được tạo ra trong GitLab

```yaml
# --- CẤU HÌNH DOCKER-IN-DOCKER ---
# Sử dụng image Docker chính thức để có thể chạy lệnh docker/docker compose
image: docker:latest

# Kích hoạt dịch vụ dind (Docker-in-Docker) bắt buộc
services:
  - docker:dind

variables:
  # Cấu hình kết nối tới service dind
  DOCKER_HOST: tcp://docker:2375
  DOCKER_TLS_CERTDIR: ""
  # Nếu gặp lỗi driver, có thể bỏ comment dòng dưới
  # DOCKER_DRIVER: overlay2

stages:
  - build
  - test

# --- JOB 1: BUILD (Dùng Docker Compose) ---
build_job:
  stage: build
  script:
    - echo "Bắt đầu build với Docker Compose cho tag $CI_COMMIT_TAG"
    
    # 1. Kiểm tra xem có file docker-compose.yml không
    - ls -la
    
    # 2. Build các services (image) defined trong file compose
    - docker compose build
    
    # 3. (Tùy chọn) Xuất Artifact
    # Vì Docker build xong image nằm trong Docker Engine, muốn lấy file ra (vd: file .jar, folder dist)
    # ta cần chạy container và copy ra ngoài.
    # Ví dụ: Giả sử service tên là 'app' và build ra file tại /app/dist
    # - docker compose run --rm -v $PWD/../build_output:/app/dist app echo "Exporting artifacts..."
    
    # Demo tạo artifact giả lập để test luồng
    - mkdir -p ../build_output
    - echo "Kết quả build từ Docker Compose Minilab" > ../build_output/app.bin

  artifacts:
    paths:
      - build_output/
    expire_in: 1 week

  rules:
    - if: $CI_COMMIT_TAG

# --- JOB 2: TEST (Chạy Integration Test) ---
test_job:
  stage: test
  script:
    - echo "Chuẩn bị môi trường test với Docker Compose..."
    # Sao chép file .env từ biến môi trường CI/CD vào thư mục hiện tại
    - cp "$ENV_FILE" .env
    # 1. Khởi động các container ở chế độ nền (detached)
    - docker compose up -d
    
    # 2. Kiểm tra trạng thái container (đảm bảo nó đang chạy)
    - docker compose ps
    
    # 3. Chạy lệnh test thực tế BÊN TRONG container
    # Thay 'tên-service' bằng tên service trong file yml của bạn (vd: backend, app...)
    # - docker compose exec -T tên-service npm test
    # - docker compose exec -T tên-service python manage.py test
    - echo "Giả lập: Chạy test thành công bên trong container..."
    
    # 4. Dọn dẹp môi trường sau khi test
    - docker compose down

  needs: ["build_job"]
  rules:
    - if: $CI_COMMIT_TAG
```