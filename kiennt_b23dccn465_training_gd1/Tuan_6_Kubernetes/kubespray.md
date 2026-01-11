# Tài nguyên của cụm Kubernetes  

| Hostanme |  IP      | OS       | CPU (tối thiểu) | RAM (tối thiểu) |  Role        |  
|----------|----------------|------------|--------------|----------|------------------|  
| k8s-master-1 | 192.168.213.111 | Ubuntu 24.04   | 2            | 3      | Master/Node     |  
| k8s-master-2 | 192.168.213.112 | Ubuntu 24.04   | 2            | 3      | Master/Node     |  
| k8s-master-3 | 192.168.213.113 | Ubuntu 24.04   | 2            | 3      | Master/Node     |  

# Cài đặt
## Thực hiện trên tất cả servers

```bash  
$ vi /etc/hosts
```

```sh
192.168.1.111 k8s-master-1
192.168.1.112 k8s-master-2
192.168.1.113 k8s-master-3
```

```sh
$ swapoff -a
$ sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab
```

## Thực hiện trên server k8s-master-1
```sh
$ apt install git python3 python3-pip -y
$ apt install ansible-core -y
```

```sh
$ ssh-keygen -t rsa
$ ssh-copy-id 192.168.1.111
$ ssh-copy-id 192.168.1.112
$ ssh-copy-id 192.168.1.113
```

```sh
$ git clone https://github.com/kubernetes-incubator/kubespray.git --branch release-2.24
$ cd kubespray/
$ cp -rfp inventory/sample inventory/devopseduvn-cluster
```

```sh
$ vi inventory/beeshoes-cluster/hosts.ini
```

```sh
[all]
k8s-master-1  ansible_host=192.168.1.111      ip=192.168.1.111
k8s-master-2  ansible_host=192.168.1.112      ip=192.168.1.112
k8s-master-3  ansible_host=192.168.1.113      ip=192.168.1.113

[kube-master]
k8s-master-1
k8s-master-2
k8s-master-3

[kube-node]
k8s-master-1
k8s-master-2
k8s-master-3

[etcd]
k8s-master-1

[k8s-cluster:children]
kube-node
kube-master

[calico-rr]

[vault]
k8s-master-1
k8s-master-2
k8s-master-3
```

```sh
$ ansible-playbook -i inventory/beeshoes-cluster/hosts.ini  --become --become-user=root cluster.yml
```

## Cài đặt tiếp tục nginx-ingress giống hệt bài giảng (Bài 5). 

> Chú ý: Kubernetes có thể rất nhanh bị outdate và chắc chắn không có docs nào là mãi mãi vậy nên mình mới phải thêm nhiều phần hướng dẫn mọi người làm sao ra được kết quả đó nên nếu như cài theo tài liệu chưa thành công bạn có thể góp ý vào page [DEVOPSEDU.VN](https://www.facebook.com/devopsedu.vn) để mình cập nhật nhé.