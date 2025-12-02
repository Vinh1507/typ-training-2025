# [Tuần 4: CI/CD Với GitLab](https://gitlab.com/NguyenTuKien/Tuan_4_Gitlab.git)

## 1. Build và Test ứng dụng sử dụng Docker Compose trong GitLab CI/CD
- [ ] Tạo GitLab repository và đẩy source code lên.
- [ ] Viết file `.gitlab-ci.yml`.
- [ ] Định nghĩa các stage cơ bản:
  - `build`
  - `test`
- [ ] Chạy pipeline tự động khi push code (Luồng CI/CD chạy khi push code lên theo tag).
- [ ] Xuất artifact sau build (nếu có).

## 2. Quản lý Container Registry và Deploy Staging
- [ ] Cài và đăng ký GitLab Runner tự quản lý.
- [ ] Thực hiện tự động build Docker image.
- [ ] Push image lên GitLab Container Registry.
- [ ] Sử dụng Variable để quản lý thông tin:
  - Docker registry
  - Username/password
  - Application config
- [ ] Thiết lập deploy tự động lên môi trường **STAGING**.
- [ ] Triển khai deploy qua:
  - SSH script
  - Docker Compose

## 3. Deploy Production và Nâng cao
- [ ] Thiết lập deploy thủ công (manual) lên **PRODUCTION**.
- [ ] Sử dụng Branch Rule:
  - `develop` → deploy STAGING
  - `main` → deploy PRODUCTION
- [ ] Thêm job scan bảo mật:
  - Dependency scan
  - Container image scan
- [ ] Thêm Code Quality hoặc Coverage Report.
- [ ] Thêm thông báo pipeline:
  - Slack
  - Email
  - Teams
- [ ] Triển khai Blue/Green hoặc Canary (nếu có).
- [ ] Thiết lập Auto Rollback nếu deploy thất bại.

> **Note:** Tất cả luồng CI/CD cần được thực hiện xây dựng mô phỏng hệ thống thật, đảm bảo bảo mật và tính sẵn sàng cao (HA).