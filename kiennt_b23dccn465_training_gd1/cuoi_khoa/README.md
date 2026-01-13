# Kubernetes
___
## 1. Overview — Kubernetes Fundamentals
**Kubernetes (K8S)** là một nền tảng mã nguồn mở, khả năng chuyển, có thể mở rộng để quản lý các ứng dụng được đóng gói và các service, giúp thuận tiện trong việc cấu hình và tự động hóa việc triển khai ứng dụng.
### Mô hình triển khai
![img.png](Image/overview.png)

---
### 1.1. Bài toán và giải pháp của K8s
Công nghệ ảo hóa và container được tạo ra để giải quyết được vấn đề về tài nguyên phần cứng và mở rộng quy mô, bằng cách cấp phát tài nguyên rành mạch và tính riêng biệt giữa chúng. Tuy nhiên, việc backup, sửa lỗi, quản lý giá trị quan trọng hay tăng giảm tài nguyên đều phải thực hiện thủ công gây mất nhiều thời gian và có rủi ro.

#### **K8s** ra đời để khắc phục bằng cách:
* **Automated deployment:**
    * Trước đây, ta cần ssh vào từng server để triển khai ứng dụng, rất mất thời gian và dễ sai sót.
    * K8s cho phép ta mô tả trạng thái mong muốn của ứng dụng (số lượng bản sao, cấu hình mạng, tài nguyên...) trong file YAML, sau đó K8s sẽ tự động thực hiện việc triển khai và duy trì trạng thái đó.
* **Self-healing:** 
    * Trong môi trường container, các tiến trình có thể bị lỗi hoặc dừng đột ngột.
    * K8s giải quyết bằng cách:
        * Auto-restart: Tự động khởi động lại các node bị chết ngay lập tức trên cùng node đó.
        * Replication: Nếu cả node bị chết, K8s sẽ tạo các bản sao mới trên các node còn hoạt động để đảm bảo số lượng bản sao luôn đúng như mong muốn.
        * Health-check: K8s liên tục check xem container có làm việc hay không.
            * Liveness probe: Nếu container không phản hồi, K8s sẽ khởi động lại nó.
            * Readiness probe: Nếu container không sẵn sàng nhận traffic, K8s sẽ tạm thời ngừng gửi traffic đến nó cho đến khi nó sẵn sàng trở lại.
* **Horizontal scaling:** 
    * Trước đây, khi traffic tăng, ta thường "Scale Up" (Mở rộng chiều dọc) bằng cách mua CPU/RAM mạnh hơn cho server. Việc này tốn kém và có giới hạn vật lý.
    * K8s hỗ trợ "Scale Out" (Mở rộng chiều ngang) bằng cách:
        * Thay đổi số lượng: Thay vì nâng cấp phần cứng, ta chỉ cần tăng số lượng bản sao (replica) của ứng dụng để phân phối tải.
        * HPA (Horizontal Pod Autoscaler): K8s có thể tự động điều chỉnh số lượng bản sao dựa trên các chỉ số như CPU, memory hoặc các chỉ số tùy chỉnh khác.

#### Khi nào nên dùng K8s?
- Dự án lớn, dự án lâu dài.
- Dự án có nhu cầu scaling cao.
- Dự án cần triển khai đa môi trường.
- Dự án theo mô hình microservice.
- Dự án cần khả năng tự phục hồi.
---
### 1.2. High-level K8s Architecture
![img.png](Image/architecture.png)
* **Control Plane (Bộ não)**
    * **`kube-api-server`:** Cửa ngõ giao tiếp API. Tiếp nhận, xác thực và xử lý *mọi* yêu cầu (lệnh) từ người dùng và các thành phần khác.
    * **`etcd`:** Cơ sở dữ liệu (kho lưu trữ) lưu *toàn bộ* trạng thái và cấu hình của cluster.
    * **`kube-scheduler`:** Quyết định (xếp lịch) xem Pod mới nên được chạy ở Node (máy trạm) nào.
    * **`kube-controller-manager`:** Giám sát trạng thái cluster. Khi thực tế khác mong muốn (ví dụ: Pod chết), nó sẽ tự động "sửa lỗi" (tạo Pod mới).
    * **`cloud-controller-manager`**: (Tùy chọn) "Phiên dịch" các yêu cầu của K8s thành hành động của nhà cung cấp mây (AWS, GCP, Azure). Ví dụ: tạo Load Balancer, yêu cầu ổ đĩa lưu trữ (storage) từ mây.
* **Worker Nodes (Nơi làm việc)**
    * **`kubelet`:** Agent (đặc vụ) chạy trên mỗi Node. Nhận lệnh từ Control Plane và đảm bảo các container trong Pod đang chạy đúng như yêu cầu.
    * **`kube-proxy`:** Quản lý quy tắc mạng (networking) trên mỗi Node, xử lý cân bằng tải (load balancing) cho các Service.
    * **`Pod`:** Đơn vị nhỏ nhất, là "ngôi nhà" chứa một hoặc nhiều container ứng dụng của bạn.
* **kubectl**: Công cụ dòng lệnh (CLI) để giao tiếp với K8s cluster, gửi lệnh và truy vấn trạng thái.
    * Các cài đặt: [Install kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) (Chỉ cần cài trên máy quản lý, không cần trên tất cả Node).
    * Một sộ lệnh cơ bản:
        ```sh
        kubectl get nodes               # Liệt kê các Node trong cluster
        kubectl get pods                # Liệt kê các Pod trong namespace hiện tại
        kubectl get pods -A             # Liệt kê tất cả các Pod trong tất cả các namespace
        kubectl describe pod <pod-name> # Hiển thị chi tiết về một Pod cụ thể
        kubectl logs <pod-name>         # Xem log của một Pod
        kubectl apply -f <file.yaml>    # Tạo hoặc cập nhật tài nguyên từ file YAML
        kubectl delete -f <file.yaml>   # Xóa tài nguyên từ file YAML
        ```
---
### 1.3. Setup a Kubernetes Cluster
#### 1.3.1. Các cách triển khai K8s phổ biến
**Kubespray / Kubeadm**
* **Kubeadm:** Công cụ chính thống để khởi tạo cluster (bootstrap) chuẩn mực.
* **Kubespray:** Sử dụng **Ansible** để tự động hóa việc gọi Kubeadm, giúp triển khai cluster nhiều node trên nhiều môi trường (On-premise, Cloud).
* **Tài liệu:** [Kubespray.io](https://kubespray.io/)

**K3s**
* Phiên bản rút gọn siêu nhẹ của K8s (chỉ 1 binary < 100MB).
* Tối ưu hóa cho các môi trường tài nguyên thấp như thiết bị IoT, Edge Computing và máy ảo cấu hình yếu.
* **Tài liệu:** [K3s.io](https://k3s.io/)

Ngoài ra còn một số cách khác như Minikube, Kind, Kops,... và một số nền tảng K8s managed service trên cloud như EKS (AWS), GKE (GCP), AKS (Azure)...
#### 1.3.2. K8s config
* Là file cấu hình kết nối đến K8s cluster.
* Mặc định nằm ở: `/etc/kubernetes/admin.conf` hoặc `~/.kube/config` trên Master Node.
* Có thể copy file này về máy cá nhân để sử dụng với `kubectl`.
* Cấu trúc file kubeconfig:
```yaml
apiVersion: v1
clusters:
- cluster:  # Thông tin về cluster K8s
    certificate-authority-data: DATA+OMITTED  # Chứng chỉ CA để xác thực
    server: https://<MASTER_IP>:6443 # Địa chỉ API server của K8s cluster
  name: kubernetes # Tên cluster
contexts: # Ngữ cảnh kết nối
- context: 
    cluster: kubernetes # Tên cluster
    user: kubernetes-admin # Tên user
  name: kubernetes-admin@kubernetes # Tên ngữ cảnh
current-context: kubernetes-admin@kubernetes # Ngữ cảnh hiện tại
kind: Config # Loại file
preferences: {} # Thiết lập tùy chọn (nếu có)
users: 
- name: kubernetes-admin # Tên
  user: # Thông tin user
    client-certificate-data: REDACTED # Chứng chỉ client
    client-key-data: REDACTED # Khóa riêng client
```
* Thiết lập biến môi trường `KUBECONFIG` để `kubectl` sử dụng file cấu hình:
```sh
export KUBECONFIG=/path/to/k8s-config.yaml
```
* Để kiểm tra kết nối cluster:
```sh
kubectl get nodes -o wide
kubectl get pods -A -o wide
```
---
### 1.4. Basic Kubernetes objects
#### 1.4.1. Namespaces
* Cách để phân chia tài nguyên trong K8s cluster.
* Mặc định có namespace `default`, `kube-system`, `kube-public`.
* Tạo namespace mới:
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: lab2-yaml
```
* Tạo namespace bằng lệnh:
```sh
kubectl apply -f namespace.yaml
# Hoặc
kubectl create namespace lab2-yaml
```
* Sử dụng namespace:
```sh
kubectl get namespaces
kubectl describe namespace lab2
kubectl get namespace lab2-yaml --show-labels
```
#### 1.4.2. Pods
* Đơn vị triển khai nhỏ nhất trong K8s, có thể chứa một hoặc nhiều container.
* Ví dụ Pod đơn giản:
```yaml
apiVersion: v1 # Loại API
kind: Pod # Loại tài nguyên
metadata: # Metadata của Pod
  name: web-pod # Tên Pod
  namespace: lab2-yaml # Namespace
  labels: # Nhãn
    app: web 
    tier: frontend
spec: # Đặc tả Pod
  containers: # Danh sách container
    - name: web-container # Tên container
      image: nginx:1.20 # Image container
      ports: # Cổng mở
        - containerPort: 80 
      resources: # Tài nguyên
        requests: # Yêu cầu tối thiểu
          memory: "64Mi"
          cpu: "250m"
        limits: # Giới hạn tối đa
          memory: "128Mi"
          cpu: "500m"
```
* Tạo Pod:
```sh
kubectl apply -f pod.yaml
# Hoặc
kubectl run busy-pod --image=busybox --namespace=lab2 --command -- sleep 3600
```
* Kiểm tra Pod:
```sh
kubectl get pods -n lab2
kubectl describe pod nginx-pod -n lab2

kubectl get pod web-pod -n lab2-yaml -o yaml
kubectl logs web-pod -n lab2-yaml
```
#### 1.4.3. Replica Sets
* Đối tượng K8s dùng để duy trì số lượng bản sao (replica) của Pod.
* Ví dụ Replica Set đơn giản:
```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: web-replicaset
  namespace: lab2-yaml
spec:
  replicas: 3
  selector: # Chọn Pod để quản lý
    matchLabels: # Nhãn để chọn Pod
      app: web
      tier: frontend
  template: # Mẫu Pod
    metadata:
      labels:
        app: web
        tier: frontend
    spec: 
      containers:
        - name: web-container
          image: nginx:1.20
          ports:
            - containerPort: 80
```
* Tạo Replica Set:
```sh
kubectl apply -f replicaset.yaml
# Hoặc      
kubectl create rs web-replicaset --image=nginx:1.20 --replicas=3 --namespace=lab2-yaml
```
* Kiểm tra Replica Set:
```sh
kubectl get replicasets -n lab2-yaml
kubectl describe rs web-replicaset -n lab2-yaml
kubectl get pods -n lab2-yaml -l app=web
```
#### 1.4.3. Deployments
* Đối tượng K8s dùng để quản lý việc triển khai và cập nhật Pod và Replica Set.
* Ví dụ Deployment đơn giản:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web-deployment
  namespace: lab2-yaml
  labels:
    app: web-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: web-app
  template:
    metadata:
      labels:
        app: web-app
    spec:
      containers:
        - name: web-container
          image: nginx:1.20
          ports:
            - containerPort: 80
          resources:
            requests:
              memory: "64Mi"
              cpu: "250m"
            limits:
              memory: "128Mi"
              cpu: "500m"
    
```
* Tạo Deployment:
```sh
kubectl apply -f deployment.yaml
# Hoặc      
kubectl create deployment nginx-deployment --image=nginx:alpine --replicas=3 --namespace=lab2
```
* Kiểm tra Deployment:
```sh
kubectl get deployments -n lab2-yaml
kubectl describe deployment web-deployment -n lab2-yaml
kubectl get pods -n lab2-yaml -l app=web
kubectl rollout status deployment/web-deployment -n lab2-yaml
```
#### 1.4.4. Service
* Đối tượng K8s dùng để tạo điểm truy cập mạng ổn định cho các Pod.
* Ví dụ Service đơn giản:
```yaml
apiVersion: v1
kind: Service 
metadata:
  name: web-service
  namespace: lab2-yaml
spec:
  selector: # Chọn Pod để liên kết
    app: web 
    tier: frontend
  ports: # Cổng dịch vụ
    - protocol: TCP # Giao thức mặc định là TCP
      port: 80
      targetPort: 80 # Mặc định là cùng cổng với port
  type: ClusterIP # Loại Service
```
* Tạo Service:
```sh
kubectl apply -f service.yaml
# Hoặc
kubectl expose deployment nginx-deployment --typ
e=ClusterIP --name=nginx-deployment --port=80 --namespace=lab2
service/nginx-deployment exposed
```
* Kiểm tra Service:
```sh
kubectl get services -n lab2-yaml
kubectl describe service web-service -n lab2-yaml
```



