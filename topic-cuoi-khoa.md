# Yêu cầu Trình bày Seminar Kỹ thuật

---

## 1. Docker Core: Namespaces, Cgroups, và UnionFS

**Yêu cầu trình bày:**

- Phân tích cơ chế cô lập (isolation) của container thông qua **Linux Namespaces** (bao gồm `PID`, `NET`, `MNT`, `UTS`, `IPC`, `User`).
- Phân tích cơ chế giới hạn (limitation) tài nguyên thông qua **Cgroups** (Control Groups) cho CPU, Memory, và I/O.
- Phân tích cơ chế quản lý file system của image và container thông qua **Union File System** (cụ thể là `OverlayFS`) và cấu trúc copy-on-write (CoW) của các layer.

**Yêu cầu Demo:**

- Tái tạo một môi trường container-like tối giản bằng các lệnh Linux native (ví dụ: `unshare`, `cgroups`, `chroot`, `ip link`).

---

## 2. Docker Security: Hardening Lifecycle

**Yêu cầu trình bày:**

- **Image Security:**
  - Phân tích rủi ro của `latest` tag và các base image lớn (non-minimal).
  - Trình bày kỹ thuật **Multi-stage builds** để tối ưu dung lượng và giảm bề mặt tấn công (attack surface).
  - Trình bày phương pháp chạy **Rootless containers** (sử dụng `USER` directive và quản lý UID/GID).
  - Tích hợp công cụ quét lỗ hổng (ví dụ: `Trivy`, `Snyk`) vào quy trình CI.
- **Daemon Security:**
  - Phân tích rủi ro của việc expose `docker.sock` và các phương pháp giảm thiểu (mitigation).
- **Runtime Security:**
  - Trình bày cơ chế **Linux Capabilities** (sử dụng `drop: ALL` và `add: [...]` có chọn lọc).
  - Trình bày vai trò và cách áp dụng **Seccomp** và **AppArmor** profiles.

---

## 3. Ansible Core: Custom Extension và Tối ưu hóa

**Yêu cầu trình bày:**

- Phân tích kiến trúc **Ansible Plugin** và phân biệt các loại: `lookup`, `filter`, `callback`, `strategy`, `connection`, và `module`.
- Trình bày các trường hợp sử dụng (use cases) thực tế cho việc phát triển plugin tùy chỉnh.
- Phân tích các kỹ thuật tối ưu hóa hiệu năng Playbook: `pipelining`, `fact_caching` (sử dụng Redis hoặc JSONFS), và `strategy: free`.

**Yêu cầu Demo:**

- Phát triển và trình bày một **Custom Filter Plugin** bằng Python.
- Phát triển và trình bày một **Custom Module Plugin** (ví dụ: tương tác với một API) và giải thích vòng đời (lifecycle) của nó.

---

## 4. Kubernetes Security: Zero-Trust Implementation

**Yêu cầu trình bày:**

- **Authentication & Authorization (RBAC):**
  - Phân tích chi tiết các đối tượng: `ServiceAccount`, `Role`, `ClusterRole`, `RoleBinding`, `ClusterRoleBinding`.
  - Phân tích rủi ro của `default` ServiceAccount và việc lạm dụng `cluster-admin` binding.
- **Network Isolation:**
  - Trình bày cơ chế hoạt động của **NetworkPolicy** và sự phụ thuộc vào CNI plugin.
  - Phân tích các loại policy: `ingress` và `egress`.
- **Workload Security:**
  - Phân tích **PodSecurityStandards (PSS)** (Baseline, Restricted) và các thiết lập `securityContext` tương ứng (trong Pod và Container).
  - Trình bày các rủi ro liên quan đến `privileged: true`, `allowPrivilegeEscalation: true`, và `runAsRoot`.

**Yêu cầu Demo:**

- Cấu hình một **NetworkPolicy** (ví dụ: `ingress` isolation cho một `backend` service) và thực hiện xác thực kết nối (ví dụ: dùng `curl` từ một Pod khác).
