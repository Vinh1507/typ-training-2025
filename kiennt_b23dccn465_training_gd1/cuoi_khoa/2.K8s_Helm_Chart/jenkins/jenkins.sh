#!/bin/bash
export KUBECONFIG=./k8s-config.yml
kubectl apply -f jenkins/namespace.yml
kubectl apply -f jenkins/pv-volume.yml
kubectl apply -f jenkins/pv-claim.yml
kubectl apply -f jenkins/deployment.yml
kubectl apply -f jenkins/service.yml
sleep 15
kubectl get ns
kubectl get pv,pvc -n jenkins
kubectl get pods -n jenkins
kubectl get svc -n jenkins