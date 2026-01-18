#!/bin/bash
export KUBECONFIG=./k8s-config.yml
kubectl create namespace argocd
kubectl apply -n argocd -f argocd/setup/install.yml
sleep 15
kubectl get pods -n argocd
kubectl patch svc argocd-server -n argocd -p '{"spec":{"type":"NodePort"}}'
kubectl get svc argocd-server -n argocd
# kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo