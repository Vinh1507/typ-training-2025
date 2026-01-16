#!/bin/bash
kubectl apply -f mysql-namespace.yml
kubectl apply -f mysql-config.yml
kubectl apply -f mysql-secret.yml
kubectl apply -f mysql-pv.yml
kubectl apply -f mysql-pvc.yml
kubectl apply -f mysql-deployment.yml
kubectl apply -f mysql-service.yml