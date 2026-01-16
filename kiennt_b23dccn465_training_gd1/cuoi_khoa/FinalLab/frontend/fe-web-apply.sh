#!/bin/bash
kubectl apply -f fe-namespace.yml
kubectl apply -f fe-config.yml
kubectl apply -f fe-deployment.yml
kubectl apply -f fe-service.yml