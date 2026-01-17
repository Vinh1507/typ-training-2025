#!/bin/bash
export KUBECONFIG=./k8s-config.yml
kubectl apply -f jenkins/jenkins-namespace.yml
kubectl apply -f jenkins/jenkins-pv-volume.yml
kubectl apply -f jenkins/jenkins-pv-claim.yml
kubectl apply -f jenkins/jenkins-deployment.yml
kubectl apply -f jenkins/jenkins-service.yml
sleep 15
kubectl get ns
kubectl get pv,pvc -n k8s-jenkins
kubectl get pods -n k8s-jenkins
kubectl get svc -n k8s-jenkins