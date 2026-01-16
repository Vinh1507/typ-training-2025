#!/bin/bash
kubectl apply -f be-namespace.yml
kubectl apply -f be-config.yml
kubectl apply -f be-secret.yml
kubectl apply -f be-deployment.yml
kubectl apply -f be-service.yml