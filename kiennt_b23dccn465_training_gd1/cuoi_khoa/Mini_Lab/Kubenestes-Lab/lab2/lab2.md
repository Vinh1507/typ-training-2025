# Kubernetes Objects Creation Lab

## Objective

Learn to create and manage basic Kubernetes objects using both imperative commands (kubectl) and declarative YAML files. Practice creating Namespaces, Pods, Deployments, and Services.

## Prerequisites

- Access to a Kubernetes cluster
- kubectl configured and connected to the cluster
- Text editor (vim, nano, or VS Code)
- Basic understanding of Kubernetes architecture from Lab 1

## Lab Overview

This lab covers creating and managing these Kubernetes objects:

1. **Namespace** - Logical cluster partitioning
2. **Pod** - Basic execution unit
3. **Deployment** - Managed pod replicas
4. **Service** - Network access to pods

Each exercise provides a task description first, then the solution in a collapsible section.

---

## Part 1: Namespace Creation

### Exercise 1.1: Create Namespace using Commands

**Task:** Create a namespace called `lab2` using an imperative kubectl command.

<details>
<summary>Click to see the answer</summary>

```bash
# Create a namespace for our lab
kubectl create namespace lab2
```

</details>

**Verification:**

```bash
# Verify the namespace was created
kubectl get namespaces
kubectl describe namespace lab2
```

### Exercise 1.2: Create Namespace using YAML

**Task:** Create a namespace called `lab2-yaml` using a YAML file. Add labels: `purpose: lab-exercise` and `created-by: student`.

<details>
<summary>Click to see the answer</summary>

Create a file called `namespace.yaml`:

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: lab2-yaml
  labels:
    purpose: lab-exercise
    created-by: student
```

Apply the YAML file:

```bash
# Create namespace from YAML file
kubectl apply -f namespace.yaml
```

</details>

**Verification:**

```bash
# Verify both namespaces exist
kubectl get namespaces
kubectl get namespace lab2-yaml --show-labels
```

---

## Part 2: Pod Creation

### Exercise 2.1: Create Pod using Commands

**Task 1:** Create a pod named `nginx-pod` with nginx image in the `lab2` namespace.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl run nginx-pod --image=nginx --namespace=lab2
```

</details>

**Task 2:** Create a pod named `busy-pod` with busybox image in the `lab2` namespace. The pod should run the command `sleep 3600`.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl run busy-pod --image=busybox --namespace=lab2 --command -- sleep 3600
```

</details>

**Verification:**

```bash
kubectl get pods -n lab2
kubectl describe pod nginx-pod -n lab2
```

### Exercise 2.2: Create Pod using YAML

**Task:** Create a pod named `web-pod` in the `lab2-yaml` namespace with the following specifications:

- Image: `nginx:1.20`
- Labels: `app: web`, `tier: frontend`
- Container name: `web-container`
- Container port: `80`
- Resource requests: memory `64Mi`, cpu `250m`
- Resource limits: memory `128Mi`, cpu `500m`

<details>
<summary>Click to see the answer</summary>

Create a file called `pod.yaml`:

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: web-pod
  namespace: lab2-yaml
  labels:
    app: web
    tier: frontend
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

Apply the YAML file:

```bash
kubectl apply -f pod.yaml
```

</details>

**Verification:**

```bash
kubectl get pods -n lab2-yaml
kubectl describe pod web-pod -n lab2-yaml
kubectl get pod web-pod -n lab2-yaml -o yaml
kubectl logs web-pod -n lab2-yaml
```

---

## Part 3: Deployment Creation

### Exercise 3.1: Create Deployment using Commands

**Task 1:** Create a deployment named `nginx-deployment` with nginx image and 3 replicas in the `lab2` namespace.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl create deployment nginx-deployment --image=nginx --replicas=3 --namespace=lab2
```

</details>

**Task 2:** Scale the `nginx-deployment` to 5 replicas.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl scale deployment nginx-deployment --replicas=5 --namespace=lab2
```

</details>

**Verification:**

```bash
kubectl get deployments -n lab2
kubectl get pods -n lab2
kubectl describe deployment nginx-deployment -n lab2
```

### Exercise 3.2: Create Deployment using YAML

**Task:** Create a deployment named `web-deployment` in the `lab2-yaml` namespace with the following specifications:

- 3 replicas
- Labels: `app: web-app`
- Pod template with label: `app: web-app`
- Container name: `web-container`
- Image: `nginx:1.20`
- Container port: `80`
- Resource requests: memory `64Mi`, cpu `250m`
- Resource limits: memory `128Mi`, cpu `500m`

<details>
<summary>Click to see the answer</summary>

Create a file called `deployment.yaml`:

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

Apply the YAML file:

```bash
kubectl apply -f deployment.yaml
```

</details>

**Verification:**

```bash
kubectl get deployments -n lab2-yaml
kubectl get pods -n lab2-yaml -l app=web-app
kubectl rollout status deployment/web-deployment -n lab2-yaml
kubectl get replicasets -n lab2-yaml
```

---

## Part 4: Service Creation

### Exercise 4.1: Create Service using Commands

**Task 1:** Expose the `nginx-deployment` as a ClusterIP service on port 80 in the `lab2` namespace.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl expose deployment nginx-deployment --port=80 --target-port=80 --type=ClusterIP --namespace=lab2
```

</details>

**Task 2:** Create a NodePort service named `nginx-nodeport` for the `nginx-deployment` on port 80 in the `lab2` namespace.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl expose deployment nginx-deployment --port=80 --target-port=80 --type=NodePort --name=nginx-nodeport --namespace=lab2
```

</details>

**Verification:**

```bash
kubectl get services -n lab2
kubectl describe service nginx-deployment -n lab2
kubectl get endpoints -n lab2
```

### Exercise 4.2: Create Service using YAML

**Task:** Create two services in the `lab2-yaml` namespace for the `web-deployment`:

1. A ClusterIP service named `web-service` on port 80
2. A NodePort service named `web-nodeport` on port 80 with nodePort 30080
   Both services should select pods with label `app: web-app`

<details>
<summary>Click to see the answer</summary>

Create a file called `service.yaml`:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: web-service
  namespace: lab2-yaml
  labels:
    app: web-service
spec:
  selector:
    app: web-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  type: ClusterIP
---
apiVersion: v1
kind: Service
metadata:
  name: web-nodeport
  namespace: lab2-yaml
  labels:
    app: web-service
spec:
  selector:
    app: web-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
      nodePort: 30080
  type: NodePort
```

Apply the YAML file:

```bash
kubectl apply -f service.yaml
```

</details>

**Verification:**

```bash
kubectl get services -n lab2-yaml
kubectl describe service web-service -n lab2-yaml
kubectl get endpoints -n lab2-yaml
kubectl get service web-nodeport -n lab2-yaml -o yaml
```

---

## Part 5: Service Connectivity Testing

### Exercise 5.1: Test Service Connectivity

**Task:** Create a temporary test pod to verify that the `nginx-deployment` service is working in the `lab2` namespace. Use busybox image with wget command to test connectivity.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl run test-pod --image=busybox --rm -it --restart=Never --namespace=lab2 -- wget -qO- nginx-deployment:80
```

</details>

**Task:** Create a temporary curl pod to test the `web-service` in the `lab2-yaml` namespace.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl run curl-test --image=curlimages/curl --rm -it --restart=Never --namespace=lab2-yaml -- curl web-service:80
```

</details>

---

## Part 6: Complete Application Stack Exercise

### Exercise 6.1: Create Full Application Stack with Commands

**Task:** Using imperative commands only, create the following in order:

1. Namespace: `student-app`
2. Deployment: `apache-deploy` with httpd:2.4 image, 2 replicas
3. Service: ClusterIP service to expose the deployment on port 80
4. Test pod: Use curl to verify service connectivity

<details>
<summary>Click to see the answer</summary>

```bash
# 1. Create namespace
kubectl create namespace student-app

# 2. Create deployment
kubectl create deployment apache-deploy --image=httpd:2.4 --replicas=2 -n student-app

# 3. Create service
kubectl expose deployment apache-deploy --port=80 --target-port=80 -n student-app

```

</details>

**Verification:**

```bash
kubectl get all -n student-app
kubectl describe deployment apache-deploy -n student-app
kubectl get endpoints -n student-app
```

### Exercise 6.2: Create Full Application Stack with YAML

**Task:** Create the same application stack using YAML files. Create separate files for each object type.

<details>
<summary>Click to see the answer</summary>

**app-namespace.yaml:**

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: student-yaml-app
  labels:
    purpose: student-exercise
```

**app-deployment.yaml:**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: apache-deploy
  namespace: student-yaml-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: apache
  template:
    metadata:
      labels:
        app: apache
    spec:
      containers:
        - name: apache-container
          image: httpd:2.4
          ports:
            - containerPort: 80
```

**app-service.yaml:**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: apache-service
  namespace: student-yaml-app
spec:
  selector:
    app: apache
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  type: ClusterIP
```

**test-pod.yaml:**

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: curl-test
  namespace: student-yaml-app
spec:
  containers:
    - name: curl-container
      image: curlimages/curl
      command: ["sleep", "3600"]
  restartPolicy: Never
```

Apply the files:

```bash
kubectl apply -f app-namespace.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml
kubectl apply -f test-pod.yaml

# Test connectivity
kubectl exec curl-test -n student-yaml-app -- curl apache-service:80
```

</details>

**Verification:**

```bash
kubectl get all -n student-yaml-app
kubectl logs curl-test -n student-yaml-app
kubectl describe endpoints apache-service -n student-yaml-app
```

---

## Part 7: Object Modification

### Exercise 7.1: Update Objects Imperatively

**Task 1:** Scale the `nginx-deployment` in `lab2` namespace to 2 replicas.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl scale deployment nginx-deployment --replicas=2 -n lab2
```

</details>

**Task 2:** Update the image of `nginx-deployment` to `nginx:1.21`.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl set image deployment/nginx-deployment nginx=nginx:1.21 -n lab2
```

</details>

**Task 3:** Add a label `environment=test` to the `nginx-pod`.

<details>
<summary>Click to see the answer</summary>

```bash
kubectl label pod nginx-pod environment=test -n lab2
```

</details>

**Verification:**

```bash
kubectl get deployment nginx-deployment -n lab2
kubectl rollout status deployment/nginx-deployment -n lab2
kubectl get pod nginx-pod -n lab2 --show-labels
```

### Exercise 7.2: Update Objects Declaratively

**Task:** Modify the `web-deployment` to have 2 replicas instead of 3 using the YAML file approach.

<details>
<summary>Click to see the answer</summary>

Edit the `deployment.yaml` file and change replicas from 3 to 2:

```yaml
spec:
  replicas: 2 # Changed from 3
```

Apply the updated file:

```bash
kubectl apply -f deployment.yaml
```

</details>

**Verification:**

```bash
kubectl get deployment web-deployment -n lab2-yaml
kubectl rollout status deployment/web-deployment -n lab2-yaml
```

---

## Part 8: Resource Inspection

### Exercise 8.1: Object Relationships

**Task:** Find and document the relationships between deployments, replica sets, and pods in both namespaces.

<details>
<summary>Click to see the answer</summary>

```bash
# Show all resources and their relationships
kubectl get all -n lab2
kubectl get all -n lab2-yaml

# Show pod labels to see how they're selected
kubectl get pods -n lab2 --show-labels
kubectl get pods -n lab2-yaml --show-labels

# Show replica sets and their owners
kubectl get replicasets -n lab2 -o wide
kubectl get replicasets -n lab2-yaml -o wide

# Show service endpoints
kubectl get endpoints -n lab2
kubectl get endpoints -n lab2-yaml
```

</details>

### Exercise 8.2: Resource Monitoring

**Task:** Check the resource usage and logs for the pods in your deployments.

<details>
<summary>Click to see the answer</summary>

```bash
# Check resource usage (if metrics server is available)
kubectl top pods -n lab2
kubectl top pods -n lab2-yaml

# Check pod logs
kubectl logs deployment/nginx-deployment -n lab2
kubectl logs deployment/web-deployment -n lab2-yaml

# Get specific pod logs
kubectl get pods -n lab2 -o name | head -1 | xargs kubectl logs -n lab2
```

</details>

## Lab Summary

### What You Learned

**Object Creation Methods:**

- **Imperative**: Direct commands (`kubectl create`, `kubectl run`, `kubectl expose`)
- **Declarative**: YAML files with `kubectl apply`

**Objects Mastered:**

- **Namespaces**: Logical resource separation
- **Pods**: Basic execution units with resource limits
- **Deployments**: Managed replica sets with rolling updates
- **Services**: Network access and load balancing

**Key Skills Developed:**

- Creating objects with both approaches
- Verifying object creation and status
- Understanding object relationships
- Testing service connectivity
- Updating and scaling applications
- Proper resource cleanup

### Best Practices Learned

- Always use namespaces for organization
- Prefer Deployments over standalone Pods for production workloads
- Use meaningful labels for object selection
- Verify objects after creation with appropriate commands
- Use YAML files for reproducible and version-controlled deployments
- Test service connectivity after creation
