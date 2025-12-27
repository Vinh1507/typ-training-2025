# [Tuần 4: CI/CD Với GitLab](https://gitlab.com/NguyenTuKien/Tuan_4_Gitlab.git)

## 1. Build và Test ứng dụng sử dụng Docker Compose trong GitLab CI/CD
- [x] Tạo GitLab repository và đẩy source code lên.
- [x] Viết file `.gitlab-ci.yml`.
- [x] Định nghĩa các stage cơ bản:
  - `build`
  - `test`
- [x] Chạy pipeline tự động khi push code (Luồng CI/CD chạy khi push code lên theo tag).
- [x] Xuất artifact sau build (nếu có).

## 2. Quản lý Container Registry và Deploy Staging
- [x] Cài và đăng ký GitLab Runner tự quản lý.
- [x] Thực hiện tự động build Docker image.
- [x] Push image lên GitLab Container Registry.
- [x] Sử dụng Variable để quản lý thông tin:
  - Docker registry
  - Username/password
  - Application config
- [x] Thiết lập deploy tự động lên môi trường **STAGING**.
- [x] Triển khai deploy qua:
  - SSH script
  - Docker Compose

## 3. Deploy Production và Nâng cao
- [x] Thiết lập deploy thủ công (manual) lên **PRODUCTION**.
- [x] Sử dụng Branch Rule:
  - `develop` → deploy STAGING
  - `main` → deploy PRODUCTION
- [x] Thêm job scan bảo mật:
  - Dependency scan
  - Container image scan
- [x] Thêm Code Quality hoặc Coverage Report.
- Thêm thông báo pipeline:
  - [x] Slack
  - [ ] Email
  - [ ] Teams
- [ ] Triển khai Blue/Green hoặc Canary (nếu có).
- [x] Thiết lập Auto Rollback nếu deploy thất bại.
