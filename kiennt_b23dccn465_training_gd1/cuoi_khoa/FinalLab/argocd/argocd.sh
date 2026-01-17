#!/bin/bash
export KUBECONFIG=./k8s-config.yml
kubectl create namespace k8s-argocd
kubectl apply -n k8s-argocd -f argocd/install.yml
sleep 15
kubectl get pods -n k8s-argocd
kubectl patch svc argocd-server -n k8s-argocd -p '{"spec":{"type":"NodePort"}}'
kubectl get svc argocd-server -n k8s-argocd