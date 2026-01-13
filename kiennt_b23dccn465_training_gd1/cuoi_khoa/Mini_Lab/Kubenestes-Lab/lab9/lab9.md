# Lab 9 — Learn Helm: package, template, and deploy applications

This lab teaches students the fundamentals of Helm (the Kubernetes package manager) through a set of short explanations and hands-on exercises. By the end of the lab students will be able to:

- Create a Helm chart scaffold
- Understand chart structure (Chart.yaml, templates/, values.yml)
- Use templating, values, and conditionals
- Install, upgrade, and rollback releases
- Lint and package charts, add a local repo
- Use hooks, tests, and dependencies (intro)

Estimated time: 45–75 minutes.

## Prerequisites

- A Kubernetes cluster reachable by `kubectl`.
- Helm 3 installed locally (helm client). See https://helm.sh/docs/intro/install/.

Overview and examples in this lab use a tiny example webapp: a basic nginx Deployment + Service.

## Exercise 1 — Install Helm and confirm setup

1. Install Helm 3 (skip if already installed).

2. Verify Helm can talk to the cluster:

```shell
helm version
kubectl version --short
```

## Exercise 2 — Scaffold a new chart

1. Create a working directory and scaffold a new chart called `myweb`:

```shell
mkdir -p ~/k8s-labs/lab9 && cd ~/k8s-labs/lab9
helm create myweb
ls -R myweb
```

2. Inspect the chart structure briefly:

- `Chart.yaml` — metadata (name, version, description)
- `values.yaml` — default values for templates
- `templates/` — Kubernetes manifest templates (deployment, service, ingress, tests)

## Exercise 3 — Make the chart minimal and app-specific

1. Replace the sample application with a minimal nginx deployment. Edit `myweb/values.yaml` and set values that will control the image, replica count and service port. Example minimal `values.yaml` changes:

```yaml
replicaCount: 1
image:
  repository: nginx
  tag: "1.23"
  pullPolicy: IfNotPresent
service:
  type: ClusterIP
  port: 80
```

2. Edit `myweb/templates/deployment.yaml` (it already exists from `helm create`) and keep the template, using the chart values. Example snippet inside `spec.template.spec.containers`:

```yaml
image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
imagePullPolicy: { { .Values.image.pullPolicy } }
ports:
  - containerPort: { { .Values.service.port } }
```

3. Make sure `templates/service.yaml` uses `{{ .Values.service.type }}` and `{{ .Values.service.port }}`.

## Exercise 4 — Render templates locally

Before installing, render the templates to see what will be applied:

```shell
helm template myweb ./myweb --values ./myweb/values.yaml
```

Read the rendered YAML and note where templating injects values.

## Exercise 5 — Install the chart

Install your chart as a release called `lab-web` into namespace `lab9`:

```shell
kubectl create ns lab9 || true
helm install lab-web ./myweb -n lab9
kubectl get all -n lab9
```

Verify the pod comes up and the service exposes the app.

## Exercise 6 — Upgrade using custom values

Make a copy of `values.yaml` named `values-blue.yaml` and change the image tag to a different value (or change replicaCount to 2). Then upgrade the release:

```shell
helm upgrade lab-web ./myweb -n lab9 -f values-blue.yaml
kubectl get deployment lab-web -n lab9 -o yaml | yq '.spec.replicas'
```

## Exercise 7 — Rollback

If the upgrade caused a problem, rollback to the previous revision:

```shell
helm history lab-web -n lab9
helm rollback lab-web 1 -n lab9   # rollback to revision 1 (adjust as necessary)
```

## Exercise 8 — Lint, package and install from a local repo

1. Lint the chart:

```shell
helm lint ./myweb
```

2. Package the chart:

```shell
helm package ./myweb
ls -1 myweb-*.tgz
```

3. Create a local chart repository directory and index it, then add it to Helm:

```shell
mkdir -p repo && mv myweb-*.tgz repo/
helm repo index repo --url http://example.com/charts   # url is illustrative
helm repo add local-repo http://example.com/charts --force-update || true
# Alternatively, use 'helm serve' plugin or a simple file server to host the index and tgz
```

## Exercise 9 — Chart testing and hooks (intro)

- Helm charts can include `templates/tests` that run inside the cluster. The `helm create` scaffold already includes an example test pod.
- Hooks are special annotations (e.g., `helm.sh/hook: pre-install`) to run jobs at lifecycle events. Use them sparingly for bootstrapping tasks.

## Exercise 10 — Using values files and environment-specific overrides

1. Create `values-dev.yaml` and `values-prod.yaml` with different replica counts, resource requests, or service types (ClusterIP vs NodePort).
2. Install or upgrade with the correct file per environment:

```shell
helm install lab-web-dev ./myweb -n lab9 -f values-dev.yaml
helm upgrade lab-web-prod ./myweb -n lab9 -f values-prod.yaml
```

## Exercise 11 — Chart dependencies (brief)

- Charts can declare dependencies in `Chart.yaml` (or `requirements.yaml` in older versions). Use `helm dependency update` to download them.
- Example: chart A depends on chart B (redis) declared as a dependency. This is useful when packaging a complete app with backing services.

## Cleanup

```shell
helm uninstall lab-web -n lab9 || true
kubectl delete ns lab9 --wait || true
rm -rf ~/k8s-labs/lab9/myweb repo myweb-*.tgz
```

## Assessment tasks (for students)

1. Create a small feature change (for example, change the server message by mounting a ConfigMap) and deliver it via Helm upgrade.
2. Add a `cronjob` post-install hook that performs a one-time migration job (explain the risks of hooks).
3. Create a `values-prod.yaml` that uses a more robust `Service` type (LoadBalancer) and resource requests and then show how to deploy it.

## Further reading and next steps

- Helm docs: https://helm.sh/docs/
- Best practices: chart structure, semantic versioning, and security considerations.
- Use ChartMuseum or an OCI registry to host real chart repos.
