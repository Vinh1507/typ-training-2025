# Deployment Approach

This deployment follows the official Grafana documentation for monitoring Kubernetes using the Prometheus Operator.

Instead of deploying Prometheus manually, we leverage the Prometheus Operator–based approach to simplify configuration and lifecycle management of monitoring components. This method enables declarative management of Prometheus, Alertmanager, and related resources through Kubernetes Custom Resource Definitions (CRDs).

The configuration is implemented according to the guidance provided in Grafana’s documentation:

[Monitor Kubernetes infrastructure using Prometheus Operator
(Grafana Cloud – Kubernetes Monitoring)](https://grafana.com/docs/grafana-cloud/monitor-infrastructure/kubernetes-monitoring/configuration/config-other-methods/prometheus/prometheus-operator/).
