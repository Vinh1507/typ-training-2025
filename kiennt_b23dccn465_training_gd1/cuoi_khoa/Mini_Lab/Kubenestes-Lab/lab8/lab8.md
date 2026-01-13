# Lab 8 — Cluster Logging (EFK)

This lab deploys a simple EFK (Elasticsearch, Fluent Bit, Kibana) stack into the `kube-logging` namespace and demonstrates how to collect, inspect, and remove cluster logs.

Files used (under `lab8/logging`):

- `logging_namespace.yml` — creates the `kube-logging` namespace
- `elastic_search/elastic_statefulset.yml` — Elasticsearch StatefulSet
- `elastic_search/elastic_service.yml` — Elasticsearch Service (NodePort: 32000)
- `fluent-bit/fluent_configMap.yml` — Fluent Bit config (inputs/parsers/outputs)
- `fluent-bit/fluent_daemonset.yml` — Fluent Bit DaemonSet to tail node/container logs
- `kibana/kibana_deployment.yml` — Kibana Deployment
- `kibana/kibana_service.yml` — Kibana Service (NodePort)

## Goals

- Deploy the EFK components into a dedicated namespace.
- Confirm Fluent Bit is shipping logs to Elasticsearch.
- Access Kibana to view logs and dashboards.

## Prerequisites

- A working Kubernetes cluster and `kubectl` configured to point at it.
- Nodes must allow hostPath mounts (DaemonSet uses `/var/log`).

## Logging

### Deployment steps

1. Create the namespace and core resources.

```shell
kubectl apply -f lab8/logging/logging_namespace.yml
kubectl apply -f lab8/logging/elastic_search/elastic_statefulset.yml
kubectl apply -f lab8/logging/elastic_search/elastic_service.yml
kubectl apply -f lab8/logging/fluent-bit/fluent_configMap.yml
kubectl apply -f lab8/logging/fluent-bit/fluent_daemonset.yml
kubectl apply -f lab8/logging/kibana/kibana_deployment.yml
kubectl apply -f lab8/logging/kibana/kibana_service.yml
```

Notes:

- The Elasticsearch StatefulSet uses image `docker.elastic.co/elasticsearch/elasticsearch:7.13.4` and runs in single-node mode.
- The Fluent Bit ConfigMap configures a `tail` input for `/var/log/containers/*.log` and an Elasticsearch output that points to the `elasticsearch` service. Environment variables in the DaemonSet set the Elasticsearch host/port.

### Verify the stack

1. Check the namespace and pods:

```shell
kubectl get ns
kubectl get pods -n kube-logging -o wide
kubectl get sts,deploy,ds -n kube-logging
kubectl get svc -n kube-logging
```

2. Confirm Elasticsearch is ready (one pod for single-node):

```shell
kubectl get pods -n kube-logging -l app=elasticsearch
kubectl logs -n kube-logging -l app=elasticsearch --tail=200
```

You can query Elasticsearch from a node using the Service NodePort (manifest sets nodePort 32000 for the `elasticsearch` service):

```shell
# replace <NODE_IP> with one of your node addresses
curl http://<NODE_IP>:32000/_cluster/health?pretty
```

3. Confirm Fluent Bit daemonset is running on nodes and forwarding logs:

```shell
kubectl get daemonset -n kube-logging
kubectl get pods -n kube-logging -l k8s-app=fluent-bit-logging
kubectl logs -n kube-logging <fluent-bit-pod-name>
```

Look in the Fluent Bit logs for successful connections to `elasticsearch:9200` and whether records are being indexed.

4. Access Kibana

Get the Kibana service NodePort (Kubernetes may have allocated a NodePort). Use `kubectl get svc -n kube-logging` and then open it in your browser:

```shell
kubectl get svc -n kube-logging
# If a NodePort is assigned (e.g. 3xxxx), access http://<NODE_IP>:<NODEPORT>
```

If you cannot or prefer not to use the NodePort, you can port-forward Kibana locally:

```shell
kubectl port-forward -n kube-logging svc/kibana 5601:5601
# then open http://localhost:5601
```

Once Kibana is open, configure an index pattern matching the `fluent-bit` prefix (the ConfigMap uses `Logstash_Prefix fluent-bit`) or check Discover to see incoming logs.

### Generate test logs (optional)

Deploy a simple test pod that writes to stdout so Fluent Bit will pick it up and ship to Elasticsearch:

```yaml
apiVersion: v1
kind: Pod
metadata:
	name: log-generator
	namespace: default
spec:
	containers:
		- name: generator
			image: busybox
			command: ["/bin/sh", "-c", "while true; do echo \"hello-from-log-generator $(date)\"; sleep 5; done"]
	restartPolicy: Always
```

Apply it and then look for its logs in Kibana (or query Elasticsearch) after a short wait.

```shell
kubectl apply -f - <<'EOF'
# (paste the YAML block above)
EOF
```

## Monitoring

Following ./monitoring/README.md

## Troubleshooting tips

- If Fluent Bit cannot reach Elasticsearch, check service DNS and ports: `kubectl exec -n kube-logging -it <fluent-bit-pod> -- curl http://elasticsearch:9200/`.
- If logs are missing in Kibana, check Fluent Bit logs for parsing errors and check Elasticsearch indices with `curl` against `_cat/indices?v`.
- Ensure hostPath access to `/var/log` is permitted on your cluster nodes (some managed clusters restrict hostPath mounts).

## References / Next steps

1. Trần Đức, "Triển khai EFK Stack trên Kubernetes", https://viblo.asia/p/trien-khai-efk-stack-tren-kubernetes-WAyK8AJpZxX
2. Trịnh Quốc Việt, "Monitoring trên Kubernetes Cluster dùng Prometheus và Grafana", https://viblo.asia/p/k8s-phan-8-monitoring-tren-kubernetes-cluster-dung-prometheus-va-grafana-Qbq5QRkEKD8
