# Bài tập cuối khóa lĩnh vực Cloud
## 1. Triển khai Kubernetes
Triển khai Kubernetes thông qua công cụ kubespray lên 1 master node VM + 1 worker node VM
* Clone và run container:

  ![img.png](image/1.1.clone-run.png)
* Viết lại inventory cho kubespray:

  ![img.png](image/1.2.inventory.png)
* Tạo ssh-key cho container để tạo kết nối tới 2 VM:

  ![img.png](image/1.3.ssh-keygen.png)
  * Trên master node:
  
    ![img.png](image/1.3.1.master.png)
  * Trên worker node:
  
    ![img.png](image/1.3.2.worker.png)
* Chạy ansible playbook (Hệ thống sẽ hỏi password của user và root password cho 2 VM):

  ![img.png](image/1.4.ansible.png)
* Kết quả:

  ![img.png](image/1.5.result.png)
* Cài đặt kubectl:

  ![img.png](image/1.6.kubectl.png)
* Config kubectl:
  * Copy kubectl config từ master node về local:
  
    ![img.png](image/1.7.1.copy-config.png)
  * Paste và sửa ip chỏ tới master node:
  
    ![img.png](image/1.7.2.paste-config.png)
  * Export kubectl config: `export KUBECONFIG=./k8s-config.yml`
* Verify:

![img.png](image/1.8.verify.png)
## 2. Triển khai web application sử dụng các DevOps tools & practices.
### 2.1. Jenkins & ArgoCD
#### 2.1.1. Jenkins
* **Các file manifest**: nằm trong thư mục `jenkins/`
  
  ![img.png](image/2.1.1.1.jenkins-setup.png)
* Kết quả: 
  
  ![img.png](image/2.1.1.2.jenkins-web.png)
* Lấy passwork Jenkins bằng câu lệnh: `kubectl exec -n k8s-jenkins jenkins-deployment-6f9c57d55c-nbg6t -- cat /var/jenkins_home/secrets/initialAdminPassword`

* Login as admin Jenkins:
  ![img.png](image/2.1.1.3.jenkins-unlock.png)
  
* Sau khi hoàn thành việc setting cho Jenkins:
  ![img.png](image/2.1.1.4.jenkins-finish.png)

#### 2.1.2. ArgoCD
* **Các file manifest**: nằm trong thư mục `argocd/`
  
  ![img.png](image/2.1.2.1.argocd-setup.png)
* Kết quả:
  
  ![img.png](image/2.1.2.2.argocd-web.png)
* Login ArgoCD:
  * Username: admin
  * Password: Lấy bằng câu lệnh: `kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo`
* Đăng nhập ArgoCD:
    
  ![img.png](image/2.1.2.3.argocd-signin.png)
### 2.2. Helm Charts
* From script:
  
    ![img.png](image/2.2.1.helm-setup.png)
* Tạo thư mục helm-charts:
    
    ![img.png](image/2.2.2.helm-create.png)
* Điều chỉnh các file manifest trong thư mục helm-charts (đã có trong src code).
* Kiểm tra kết quả (log dài quá nên em xin phép không chụp ảnh):
    ```shell
    helm template helm-chart ./helm-chart --values ./helm-chart/values.yaml 
    ```  


    

    
  
    
    
  


  
  