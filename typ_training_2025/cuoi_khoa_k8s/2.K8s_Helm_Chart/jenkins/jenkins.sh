#!/bin/bash
# Chạy từ vị trí của thư mục '2.K8s_Helm_Chart'
export KUBECONFIG=./k8s-config.yml
kubectl apply -f jenkins/namespace.yml
kubectl apply -f jenkins/pv-volume.yml
kubectl apply -f jenkins/pv-claim.yml
kubectl apply -f jenkins/deployment.yml
kubectl apply -f jenkins/service.yml
sleep 15
kubectl get pv,pvc -n jenkins
kubectl get pods -n jenkins
kubectl get svc -n jenkins