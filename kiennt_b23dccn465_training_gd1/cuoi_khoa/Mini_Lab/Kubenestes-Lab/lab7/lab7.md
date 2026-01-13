# ArgoCD Lab

## Deploy ArgoCD

```
# Create the argocd namespace first
kubectl create namespace argocd

# Deploy ArgoCD using the official manifest
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

## Check ArgoCD pods

```
kubectl get pods -n argocd
```

## Expose ArgoCD to external browser

```
kubectl patch svc argocd-server -n argocd -p '{"spec":{"type":"NodePort"}}'
```
