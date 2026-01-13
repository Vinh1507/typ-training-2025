# Setup a Kubernetes Cluster on Ubuntu 22.04

You will have two options

1. Install K8s cluster
2. Install k3s cluster - it is k8s lightweight

## Prerequisites

- Ubuntu 22.04 Master with at least 1GB of RAM
- Ubuntu 22.04 Worker with at least 1GB of RAM

## Installing K8s

This setup uses Kubespray for convenient Kubernetes deployment.

For a manual setup, refer to the documentation below.

### Prepare addtional ansible Node

- Ubuntu 22.04 Master with at least 1GB of RAM

### Ansible Node

**Clone and run container Kubespray**

```
git clone https://github.com/kubernetes-sigs/kubespray

cd kubespray

docker run --rm -it \
--mount type=bind,source="$(pwd)"/inventory/sample,dst=/inventory \
--mount type=bind,source="${HOME}"/.ssh/id_rsa,dst=/root/.ssh/id_rsa \
quay.io/kubespray/kubespray:v2.28.0 bash
```

**Edit file inventory**

```
[kube_control_plane]
master1 ansible_host=<ip_master> ansible_port=<port_ssh_sang_master1> ansible_user=<user_có_quyền_lên_root> #same line

[etcd:children]
kube_control_plane

[kube_node]
worker1 ansible_host=<ip_worker> ansible_port=<port_ssh_sang_worker1>
ansible_user=<user_có_quyền_lên_root>

[k8s_cluster:children]
kube_control_plane
kube_node
```

**Setup K8s**
ansible-playbook -i /inventory/inventory.ini cluster.yml --become --ask-pass --ask-become-pass

### Install kubectl (Ansible or Worker)

```
curl -LO "https://dl.k8s.io/release/$(curl -sL
https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"

sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

### Config kubeconfig

_Master Node_

```
sudo cat /etc/kubernetes/admin.conf
```

Copy the kubeconfig file to the Ansible machine, update the API server IP address, and save it to a file (for example, k8s-config.yaml):

```
https://127.0.0.1:6443 → https://<master1_ip>:6443
```

After that, export the KUBECONFIG environment variable:

```
export KUBECONFIG=~/k8s-config.yaml
```

To verify that it works

```
kubectl get nodes -o wide
kubectl get pods -A -o wide
```

## Installing K3s

### Server Node (Master)

You will install the latest version of K3s on your Ubuntu machine

```
ssh sammy@your_server_ip
```

Next, install the K3s using the following command.

```
curl -sfL https://get.k3s.io | sh -
```

Next, you will check the K3s service status using systemctl to verify if it is running or not using the following command.

```
systemctl status k3s
```

To avoid needing sudo while executing kubectl commands, change the permissions of the config file with chmod, as shown below.

```
sudo chmod 644 /etc/rancher/k3s/k3s.yaml
```

### Agent Node (Worker)

To install additional agent nodes and add them to the cluster, run the installation script with the K3S_URL and K3S_TOKEN environment variables. Here is an example showing how to join an agent:

```
curl -sfL https://get.k3s.io | K3S_URL=https://myserver:6443 K3S_TOKEN=mynodetoken sh -
```

Setting the **K3S_URL** parameter causes the installer to configure K3s as an agent, instead of a server. The K3s agent will register with the K3s server listening at the supplied URL. The value to use for **K3S_TOKEN** is stored at **/var/lib/rancher/k3s/server/node-token** on your server node.

# Reference

1. kubespray.io, "Deploy a Production Ready Kubernetes Cluster", [Open the Link](https://kubespray.io/#/).

2. Raghav Aggarwal (December 23, 2023), "How to Setup a K3s Kubernetes Cluster on Ubuntu 22.04", [Open the Link](https://www.digitalocean.com/community/tutorials/how-to-setup-k3s-kubernetes-cluster-on-ubuntu).

3. K3S (Dec 16, 2025), "Quick-Start Guide", [Open the Link](https://docs.k3s.io/quick-start).
