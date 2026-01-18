# 3. CI/CD
---
## Yêu cầu:

Viết 1 luồng CI/CD cho app, khi có thay đổi từ source code, 1 tag mới được tạo ra trên repo này thì luồng CI/CD tương ứng của repo đó thực hiện các công việc sau:

- Sửa code trong source code
- Thực hiện build source code trên jenkin bằng docker với image tag là tag name đã được tạo ra trên gitlab/github và push docker image sau khi build xong lên Docker Hub
- Sửa giá trị Image version trong file values.yaml trong config repo và push thay đổi lên config repo.
- Cấu hình ArgoCD tự động triển khai lại web Deployment và api Deployment khi có sự thay đổi trên config repo.

## Output:

- Các file setup công cụ của luồng CI/CD
- Output log của luồng CI/CD khi tạo tag mới trên repo app
- Show log chứng minh jenkin đã chạy đúng
- Jenkin file cấu hình các luồng
- Ảnh luồng CI/CD chạy qua các stage trên giao diện Jenkins (sử dụng Plugin Pipeline Stage View)
- Hình ảnh app triển khai argoCD, hình ảnh diff khi argoCD phát hiện thay đổi ở config repo tương tự hình ảnh sau
- Hình ảnh app trước khi sửa code và sau khi sửa code.
---
## Kịch bản triển khai CI/CD
1. Cấu hình webhook trên github repo chứa source code, khi có event tạo tag mới, webhook sẽ gửi một request về cho jenkins
2. Jenkins nhận request và thực hiện chạy pipeline CI
3. Jenkins sẽ tạo image từ source code trên repo github, build image và push images này lên docker hub
4. Jenkins thực hiện sửa tag của images trong file values-prod.yaml và push code lên repo config
5. ArgoCD mặc định cấu hình 3 phút / lần polling thay đổi trên repo config, nếu có sự khác biệt sẽ thực hiện tự động deploy lại trên cụm k8s
---
## Cấu hình luồng CI/CD
### Thiết lập truy cập Jenkins
Do Jenkins được triển khai trên cụm Kubernetes và chỉ có thể truy cập qua NodePort, cần sử dụng một giải pháp tunneling để có thể truy cập từ bên ngoài:

**Sử dụng cloudflared để tạo tunnel:**
* Public URL: https://jenkins.ngtukien.id.vn
* Forward to: http://192.168.123.11:30000
* Mục đích: Cho phép GitHub webhook có thể gọi đến Jenkins để trigger CI/CD pipeline
* File cấu hình cloudflared: 
```yaml
tunnel: 85daa144-d09d-4d29-b1e6-b370acf84ec5
credentials-file: /home/typ/.cloudflared/85daa144-d09d-4d29-b1e6-b370acf84ec5.json # Đường dẫn đến file credentials

ingress:
  - hostname: jenkins.ngtukien.id.vn
    service: http://192.168.123.11:30000
  - service: http_status:404
```
* Lệnh khởi chạy tunnel:
```bash
cloudflared tunnel --config /home/typ/.cloudflared/config.yml run
```

### Cấu hình Jenkins Pipeline
* Tạo một pipeline mới trong Jenkins với tên "ci-cd-pipeline"
* Cấu hình pipeline để sử dụng Jenkinsfile từ repo GitHub chứa source code của ứng


