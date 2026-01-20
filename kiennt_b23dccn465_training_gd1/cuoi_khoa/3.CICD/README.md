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
* Tài liệu hướng dẫn: [Cloudflared](https://developers.cloudflare.com/cloudflare-one/networks/connectors/cloudflare-tunnel/)
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

### Cấu hình Jenkins Agent
Tạo 1 Jenkins Agent và cài đặt các công cụ cần thiết (`docker`, `java`, `git`) và tạo user Jenkins trên agent để Jenkins master có thể kết nối và sử dụng agent này cho các job CI/CD.
* Cấu hình phần cứng cho Jenkins Agent: 
  * RAM: 2GB

    ![alt text](image/3.RAM.png)
  * CPU: 2 vCPU

    ![alt text](image/3.CPU.png)
  * OS: Ubuntu 22.04 LTS

    ![alt text](image/3.OS.png)
  * Bộ nhớ: 60GB

    ![alt text](image/3.MEM.png)
* Cấu hình mạng: 
  * IP tĩnh: 192.168.123.111
  * Hostname: agent1
  * SSH port: 22 (Mặc định)

    ![alt text](image/3.IP.png) 
* Cài đặt các công cụ cần thiết (`docker`, `java`) trên Jenkins Agent:
  ```bash
  # git của em được cài sẵn khi tạo VM
  sudo apt update && sudo apt upgrade -y 
  sudo apt install docker.io openjdk-17-jre git -y
  sudo useradd -m -s /bin/bash jenkins
  sudo usermod -aG docker jenkins
  ```
* Tạo user Jenkins, thêm vào nhóm docker:
  ```bash
  sudo useradd -m -d /home/jenkins -s /bin/bash jenkins
  sudo passwd jenkins
  sudo usermod -aG docker jenkins
  ```
* Cấu hình SSH key để Jenkins master có thể kết nối đến Jenkins Agent.
* Thêm Jenkins Agent vào thành một nodes trong Jenkins master:
  * **Node name**: agent1
  * **Remote root directory**: /home/jenkins/agent
  * **Labels**: agent1
  * **Launch method**: Launch agents by connecting it to the controller

  ![alt text](image/3.Agent_cfg.png)
* Kiểm tra kết nối từ Jenkins master đến Jenkins Agent:
  
  ![alt text](image/3.Agent_verify.png)

### Cấu hình pipeline CI/CD trên Jenkins

Tài liệu hướng dẫn cài đặt Jenkins Pipeline: [DEVOPSEDU VN](https://youtu.be/8ujz58xmMFI?si=qJ3vtS4UZlvyuTu3)

**Các bước cần chuẩn bị:**
* Tạo **Gitlab Webhook** gửi request đến Jenkins khi có tag mới được tạo. 
  * URL: lấy từ bước Trigger khi tạo pipeline trên Jenkins (ví dụ: `https://jenkins.ngtukien.id.vn/project/TYP_2026/Backend`
  )
  * Chọn event: Tag push events & nhánh main.

    ![alt text](image/3.Webhook_1.png)
    ![alt text](image/3.Webhook_2.png)
* Tạo **Personal Access Token (PAT)** trên **Docker Hub** để Jenkins có thể push image lên Docker Hub.
* Tạo **Personal Access Token (PAT)** trên **GitLab** để Jenkins có thể push code lên repo config.
* Cấu hình các credential trên Jenkins:
  * **Gitlab API Token**: Cấu hình cho Gitlab plugin.
  * **dockerhub_credential**: Chứa Docker Hub PAT.
  * **git_credential**: Chứa GitLab PAT.
    
    ![alt text](image/3.Credential.png)
  
**Cấu hình pipeline CI/CD:**
* Giữ lại không quá 10 bản và xóa bất kỳ bản build nào cũ hơn 30 ngày.

  ![alt text](image/3.History.png)
* Chọn build khi có 1 thay đổi được push lên Gitlab (cấu hình webhook ở bước trên).

  ![alt text](image/3.Trigger.png)

* Pipeline script từ SCM, chọn Git và nhập URL repo source code.

  ![alt text](image/3.Pipeline_1.png)
  ![alt text](image/3.Pipeline_2.png)

### Tạo Jenkinsfile
* [Backend/Jenkinsfile](../0.Source_code/typ_2026_backend/Jenkinsfile)
* [Frontend/Jenkinsfile](../0.Source_code/typ_2026_frontend/Jenkinsfile)
---
## Kết quả chạy CI/CD pipeline
* Chi tiết log quá trình chạy pipeline CI/CD trên Jenkins:
  * [Backend CI/CD log](kiennt_b23dccn465_training_gd1/cuoi_khoa/3.CICD/logs/#30_backend_ci_cd.txt)
  * [Frontend CI/CD log](kiennt_b23dccn465_training_gd1/cuoi_khoa/3.CICD/logs/#2_frontend_ci_cd.txt)
### Thực hiện chạy pipeline CI/CD cho Backend
* Tạo tag mới và push lên repo:

  ![alt text](image/3.Backend_1.png)
* Tag được push lên repo:

  ![alt text](image/3.Backend_2.png)
* Jenkins nhận webhook và thực hiện chạy pipeline:

  ![alt text](image/3.Backend_3.png)
* Quá trình pipeline CI/CD diễn ra:

  ![alt text](image/3.Backend_4.png)
* Build và push image lên Docker Hub:

  ![alt text](image/3.Backend_5.png)
* Sửa file values-prod.yaml và push code lên repo config:
  
  ![alt text](image/3.Backend_6.png)
* ArgoCD phát hiện sự thay đổi trên repo config và thực hiện deploy lại:

  ![alt text](image/3.Backend_7.png)
* ArgoCD triển khai thành công:

  ![alt text](image/3.Backend_8.png)
### Thực hiện chạy pipeline CI/CD cho Frontend
* Tạo tag mới và push lên repo:

  ![alt text](image/3.Frontend_1.png)
* Tag được push lên repo: 

  ![alt text](image/3.Frontend_2.png)
* Jenkins nhận webhook và thực hiện chạy pipeline:
  
  ![alt text](image/3.Frontend_3.png)
* Quá trình pipeline CI/CD diễn ra.

  ![alt text](image/3.Frontend_4.png)
* Build và push image lên Docker Hub:

  ![alt text](image/3.Frontend_5.png)
* Sửa file values-prod.yaml và push code lên repo config:

  ![alt text](image/3.Frontend_6.png)
* ArgoCD phát hiện sự thay đổi trên repo config và thực hiện deploy lại:

  ![alt text](image/3.Frontend_7.png)
* ArgoCD triển khai thành công:

  ![alt text](image/3.Frontend_8.png)