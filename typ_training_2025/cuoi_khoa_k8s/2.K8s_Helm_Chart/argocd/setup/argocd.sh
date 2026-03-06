#!/bin/bash
# Chạy từ vị trí của thư mục '2.K8s_Helm_Chart'
export KUBECONFIG=../k8s-config.yml
kubectl create namespace argocd
kubectl apply -n argocd -f argocd/setup/install.yml
kubectl apply -n argocd -f argocd/setup/service.yml
sleep 15
kubectl get pods -n argocd
kubectl get svc argocd-server -n argocd
# kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo