# K8S Type

## K8s has 4 types

![alt text](../../images/type.png)

### ClusterIP

- **ClusterIP** is the default Service type in Kubernetes. A ClusterIP Service can only be accessed by applications that are within the same cluster. Applications outside the cluster cannot access this Service.

### NodePort

- **NodePort** is the simplest way to **expose** a Kubernetes Service to the **outside of the cluster**. It’s often used for debugging purposes, allowing you to easily test your service externally.

- The port range for NodePort is 30000–32767. You can either specify a port manually in your YAML file or let Kubernetes assign one automatically.

### Load Balancer

- **A LoadBalancer** service is used when you want a single external IP address to route all incoming traffic to your service — with built-in load balancing.

### **ExternalName**

- The **ExternalName** service is different from the other service types in Kubernetes. Instead of using a selector to route traffic to pods, it **maps the service name to an external DNS name** (e.g., app.test.com) using the externalName field.

## Practice

![alt text](../../images/lab3-demo.png)

### Build Image

```
docker build -t <username>/frontend .

docker build -t <username>/service-a .

docker build -t <username>/service-b .
```

_Note_: If using k3s, try to build image by other way.

<details>
<summary>Click to see the answer</summary>

```bash
docker build -t <image> .

## Save image
docker save image -o image.tar

## Import into K3s (containerd)
sudo k3s ctr images import frontend.tar

## Verify
sudo k3s ctr images list | grep frontend

```

</details>

### Apply k8s configure yaml

```
cd k8s-yaml

kubectl apply -f .
```

### Verify that the run was successful.

```
curl http://<NODE_IP>:<NODE_PORT>/api/service-a

=>

{
  "service": "Service A",
  "message": "Hello from Service A!",
  "timestamp": "2025-12-20T05:41:16.195Z",
  "hostname": "service-a-7fff87d74d-5bb5g"
}

curl http://<NODE_IP>:<NODE_PORT>/api/service-b

=>

{
  "service": "Service B",
  "message": "Hello from Service B!",
  "timestamp": "2025-12-20T05:41:16.195Z",
  "hostname": "service-b-7fff87d74d-5bb5g"
}
```
