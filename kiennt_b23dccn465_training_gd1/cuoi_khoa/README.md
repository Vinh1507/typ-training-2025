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


  
  