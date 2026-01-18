#!/bin/bash
# Thực hiện từ vị trí của FinalLab
export KUBECONFIG=./k8s-config.yml
kubectl apply -f argocd/deploy/database.yml
kubectl apply -f argocd/deploy/backend.yml
kubectl apply -f argocd/deploy/frontend.yml