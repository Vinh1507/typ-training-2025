#!/bin/bash
# Chạy từ vị trí của thư mục '2.K8s_Helm_Chart'
export KUBECONFIG=./k8s-config.yml
kubectl apply -f argocd/deploy/database.yml
kubectl apply -f argocd/deploy/backend.yml
kubectl apply -f argocd/deploy/frontend.yml