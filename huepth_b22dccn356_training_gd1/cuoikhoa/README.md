# 1. Triển khai Kubernetes
- Triển khai một Kubernetes Cluster gồm 1 Master node và 1 Worker node trên nền tảng Ubuntu 22.04.
- Lựa chọn triển khai **K3s – Lightweight Kubernetes**
- Mô hình hệ thống chuẩn bị:

| Node | Vai trò | Hệ điều hành | IP |
| :--- | :--- | :--- | :--- |
| master1 | K3s Server  | Ubuntu 22.04 | 192.168.126.102 |
| worker | K3S Worker | Ubuntu 22.04 | 192.168.126.100 |

- Cài đặt cơ bản:  

```
sudo apt update -y
sudo apt install -y curl 
```
- Cài đặt K3s (Master node):
  - SSH vào master node: ` ssh devops@192.168.126.102`
  - Cài đặt K3s: `curl -sfL https://get.k3s.io | sh`
  - Kiểm tra trạng thái: `systemctl status k3s`
  - Kiểm tra node: `sudo kubectl get nodes`
  
  ![alt txt](./images/bai1/statusk3s.png)

  - Lấy token để join Worker `sudo cat /var/lib/rancher/k3s/server/node-token`

- Cài đặt K3s Agent (Worker node):
  - SSH vào worker node `ssh worker@192.168.126.100`
  - Join worker vào cluster
  
  ![alt txt](./images/bai1/joinworker.png)

- Kiểm tra trạng thái: `sudo kubectl get nodes -o wide`

  ![alt txt](./images/bai1/result.png)

# 2. Triển khai web application sử dụng các DevOps tools & practices
## K8S Helm Chart (1.5đ)
**2.1. Yêu cầu 1**
- **Cài đặt ArgoCD** lên Kubernetes Cluster, expose được qua ArgoCD qua NodePort
  - Tạo namespace và triển khai bằng cách áp dụng các file từ chính kho lưu trưc của dự án Argi Project:
  ```bash
  kubectl create namespace argocd

  kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

  ```
 ![alt txt](./images/argo/setup.png)

  ![alt txt](./images/argo/pods.png)

  - Expose qua NodePort
  ```bash
  kubectl patch svc argocd-server -n argocd -p '{"spec":{"type":"NodePort"}}'
  ```

   ![alt txt](./images/argo/nodeport.png)

 - Lấy mật khẩu:
 ```bash
 kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
 ```

 - Kết quả:
 ![alt txt](./images/argo/login.png)

  ![alt txt](./images/argo/result.png)

- **Cài đặt Jenkins** lên Kubernetes Cluster, expose được Jenkins qua NodePort
  - Cài đặt và triển khai:
  
  ```bash
     kubectl apply -f jenkins/jenkins_namespace.yml
  ```

  ![alt](./images/jenkins/result.png)

  - Lấy mật khẩu:
  ```bash
  kubectl exec -it jenkins-5dd88f7bf9-7vtvp -n jenkins -- cat /var/jenkins_home/secrets/initialAdminPassword
  ```
  ![alt](./images/jenkins/pwd.png)
  
  ![alt](./images/jenkins/unlock.png)

  ![alt](./images/jenkins/install.png)


**2.2. Yêu cầu 2**

- **Source code & Helm Chart:** [fhcoffee_v2](https://github.com/fhpthh/fhcoffee_v2)
- **Config Repo:** [fh-coffee-config](https://github.com/fhpthh/fh-coffee-config.git)

- Cài đặt Helm Chart:

  ```bash
  curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3

  chmod 700 get_helm.sh

  ./get_helm.sh

  helm version 
  ```

- Khởi tạo cấu trúc

  ```bash
    mkdir -p ~/k8s-practice/cuoikhoa/fhcoffee_v2/charts

    cd ~/k8s-practice/cuoikhoa/fhcoffee_v2/charts

    helm create fh-coffee-chart
  ```
  
- Kiểm tra các cú pháp trong heml:

  ```bash
  helm lint fh-coffee-chart/
  helm template fh-coffee ./fh-coffee-chart
  ```


- File Manifest ArgoCD Application:
  ```bash
      apiVersion: argoproj.io/v1alpha1
      kind: Application
      metadata:
        name: fh-coffee-app
        namespace: argocd
      spec:
        project: default
        sources:
          - repoURL: 'https://github.com/fhpthh/fhcoffee_v2.git'
            targetRevision: HEAD
            path: charts/fh-coffee-chart
            helm:
              valueFiles:
                - $values/values-prod.yaml
          - repoURL: 'https://github.com/fhpthh/fh-coffee-config.git'
            targetRevision: HEAD
            ref: values
        destination:
          server: 'https://kubernetes.default.svc'
          namespace: cloud-final
        syncPolicy:
          automated:
            prune: true
            selfHeal: true
          syncOptions:
            - CreateNamespace=true
  ```

  - Để chạy Helm Chart: 
  ```c
    kubectl apply -f application.yaml
  ```
  **Kết quả**:
  ![alt](./images/helmchart/heathy.png)

  ![alt](./images/helmchart/detail.png)

  ![alt](./images/helmchart/admin.png)

## CI/CD

  - Trigger:
    - Đổi http sang https để add vào webhook trên github
    ```c
    ngrok http 192.168.126.100:32080
    ```

    ![alt](./images/jenkins/ngrok.png)

    ![alt](./images/jenkins/webhook.png)

    ![alt](./images/jenkins/dockerhubbe.png)

    ![alt](./images/jenkins/dockerhubfe.png)




## Monitoring

Link github: [Monitoring](https://github.com/fhpthh/monitoring_typ_cloud.git)

- ![alt](./images/monitoring/pic3.png)

- ![alt](./images/monitoring/pic1.png)

- ![alt](./images/monitoring/pic2.png)

Lệnh chạt dự án: `ansible-playbook -i inventory/hosts playbooks/site.yml `

## Logging
Link github: [Ansible logging](https://github.com/fhpthh/logging_f[alt](./images/logging/1.png)inal_typ.git)

- ![alt](./images/logging/1.png)

- ![alt](./images/logging/2.png)

- ![alt](./images/logging/3.png)

- ![alt](./images/logging/4.png)

- ![alt](./images/logging/5.png)

- ![alt](./images/logging/6.png)