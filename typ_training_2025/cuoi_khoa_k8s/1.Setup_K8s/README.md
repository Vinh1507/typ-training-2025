# 1. Triển khai Kubernetes
---
##  Yêu cầu:
### Y/c 1:
- Triển khai được Kubernetes thông qua công cụ **Minikube trên 1 node**: **0.5 điểm**  
  **Hoặc**
- Triển khai được Kubernetes thông qua công cụ **kubeadm** hoặc **kubespray** lên:
  - **1 master node VM**
  - **1 worker node VM**  
  👉 **1 điểm**
---
##  Output:
- **Tài liệu cài đặt**:
  - Đã sử dụng công cụ gì?
  - Các file config liên quan

- **Ảnh chụp log** của các lệnh kiểm tra hệ thống như:
  - `kubectl get nodes -o wide`
  - `kubectl get pods -A -o wide`
---
## Tài liệu triển khai:
**Chuẩn bị:** 2 máy ảo có cấu hình như sau: 
* CPU: 2 vCPU

  ![alt text](image/1.1.CPU.png)

* RAM: 3 GB

  ![alt text](image/1.1.RAM.png)

* Memory: 20 GB

  ![alt text](image/1.1.MEM.png)

* OS: Ubuntu 22.04 LTS

  ![alt text](image/1.1.OS.png)

**Tài liệu hướng dẫn cài đặt Kubespray:** [Lab1](https://github.com/nguyenvuong310/Kubenestes-Lab/blob/master/lab1/lab1.md)

**Cấu hình mạng**: 
* Master node: 
  * IP tĩnh: 192.168.123.10
  * Hostname: master1
  * SSH port: 22 (Mặc định)

  ![alt text](image/1.1.MasterIP.png)
* Worker node:
  * IP tĩnh: 192.168.123.11
  * Hostname: worker1
  * SSH port: 22 (Mặc định)

  ![alt text](image/1.1.WorkerIP.png)

**Bổ xung ssh-key cho 2 VM:** mục đích để tiện kết nối và triển khai tự động.
* Trên master node (Đã bao gồm ssh-key của docker container cùa kubespray): 
  
  ![alt text](image/1.1.MasterSSH.png)

* Trên worker node (Đã bao gồm ssh-key của docker container cùa kubespray):

  ![alt text](image/1.1.WorkerSSH.png)

---
## Triển khai Kubernetes thông qua công cụ kubespray lên 1 master node VM + 1 worker node VM
* Clone và run container:
  ```shell
  git clone https://github.com/kubernetes-sigs/kubespray

  cd kubespray

  docker run --rm -it \
  --mount type=bind,source="$(pwd)"/inventory/sample,dst=/inventory \
  --mount type=bind,source="${HOME}"/.ssh/id_rsa,dst=/root/.ssh/id_rsa \
  quay.io/kubespray/kubespray:v2.28.0 bash
  ```
* Viết lại inventory cho kubespray:

  ![img.png](image/1.2.inventory.png)

* Thực hiện cài đặt Kubernetes bằng ansible playbook (cần nhập mật khẩu user và root của 2 VM):
  ```shell
  ansible-playbook -i /inventory/inventory.ini cluster.yml --become --ask-pass --ask-become-pass
  ```
---
## Cài đặt kubectl:
* Cài đặt kubectl trên local machine: 
  ```shell
  curl -LO "https://dl.k8s.io/release/$(curl -sL https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"

  sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
  ```
* Config kubectl:
  * Copy kubectl config từ master node về local:
  
    ![img.png](image/1.3.copy-config.png)
  * Paste và sửa ip chỏ tới master node:
  
    ![img.png](image/1.3.paste-config.png)
  * Export kubectl config: `export KUBECONFIG=./k8s-config.yml`
---
## Kiểm tra kết quả:
* **Kiểm tra các node trong cluster:**
  ```shell
  kubectl get nodes -o wide
  ```
  ![alt text](image/1.4.get-nodes.png)
* **Kiểm tra các pod trong cluster:**
  ```shell
  kubectl get pods -A -o wide
  ```
  ![alt text](image/1.4.get-pods.png)




    

    
  
    
    
  


  
  