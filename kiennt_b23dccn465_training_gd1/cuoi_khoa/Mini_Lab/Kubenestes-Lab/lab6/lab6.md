# Continuous Integration

## Practice

1. Review and fix issues in Kubernetes configuration files in jenkins folder.
2. Deploy **Jenkins** on a Kubernetes cluster and expose it externally using **NodePort**.
3. Create a simple **Jenkinsfile** to validate the CI pipeline following the sample folder.
4. Set up a **CI workflow between Jenkins and GitHub** so that pipelines are triggered automatically on source code changes.

# Lab 6 — Continuous Integration with Jenkins

This lab walks through deploying Jenkins into Kubernetes, fixing the provided manifest issues, creating a basic pipeline using the sample project and `Jenkinsfile`, and verifying tests run in CI.

Folder layout (relevant files):

- `lab6/jenkins/jenkins_namespace.yml` — namespace for Jenkins
- `lab6/jenkins/jenkins_pv.yml` — PersistentVolume (hostPath)
- `lab6/jenkins/jenkins_pvc.yml` — PersistentVolumeClaim
- `lab6/jenkins/jenkins_deployment.yml` — Jenkins Deployment
- `lab6/jenkins/jenkins_service.yml` — (broken) Service manifest — fix required
- `lab6/sample/jenkinsfile` — example pipeline for the sample app
- `lab6/sample/package.json`, `sum.js`, `sum.test.js` — sample Node project and tests

## Goals

- Fix and deploy working Jenkins manifests.
- Expose Jenkins via NodePort for browser access.
- Create and run a pipeline using the `lab6/sample/Jenkinsfile` that runs the tests.

## Prerequisites

- `kubectl` configured for a cluster.
- Cluster nodes that allow `hostPath` mounts (the PV uses `/data/jenkins`).
- `node` or local machine able to reach the cluster NodePort or use `kubectl port-forward`.

## Quick notes about the repository state

- The `jenkins_service.yml` in the repo is incomplete/placeholder (empty `type`, empty `selector` and ports). You will need to fix it before applying.
- `lab6/sample/sum.js` is empty. The sample test `sum.test.js` calls `require('./sum')` and expects a function that adds two numbers. Implement `sum.js` (example provided below) before running tests locally or in Jenkins.

Example `sum.js` (create or update `lab6/sample/sum.js`):

```javascript
module.exports = function sum(a, b) {
  return a + b;
};
```

## Fix the Jenkins Service manifest

## Deployment steps

1. Create the namespace, PV and PVC, then deploy Jenkins and its Service:

```shell
kubectl apply -f lab6/jenkins/jenkins_namespace.yml
kubectl apply -f lab6/jenkins/jenkins_pv.yml
kubectl apply -f lab6/jenkins/jenkins_pvc.yml
# After fixing jenkins_service.yml as shown above
kubectl apply -f lab6/jenkins/jenkins_service.yml
kubectl apply -f lab6/jenkins/jenkins_deployment.yml
```

2. Verify pods and resources:

```shell
kubectl get ns
kubectl get pv,pvc -n jenkins
kubectl get pods -n jenkins
kubectl get svc -n jenkins
```

3. Wait for the Jenkins pod to become Ready. Check its logs if startup is slow:

```shell
kubectl describe pod -n jenkins -l app=jenkins
kubectl logs -n jenkins -l app=jenkins --follow
```

## Access Jenkins and obtain initial admin password

- If you used the NodePort example above, open your browser at `http://<NODE_IP>:32080` (replace `<NODE_IP>` with any node address).
- Alternatively, port-forward the Service locally:

```shell
kubectl port-forward -n jenkins svc/jenkins-service 8080:8080
# then open http://localhost:8080
```

To get the initial admin password (inside the Jenkins master pod):

```shell
POD=$(kubectl get pods -n jenkins -l app=jenkins -o jsonpath='{.items[0].metadata.name}')
kubectl exec -n jenkins $POD -- cat /var/jenkins_home/secrets/initialAdminPassword
```

Use that password to unlock Jenkins in the browser, then install suggested plugins (or install specific plugins you need such as Git, GitHub integration, NodeJS, Pipeline, etc.).

## Create the pipeline (using the sample project)

You can create a Pipeline job in Jenkins and either paste the `Jenkinsfile` content directly or configure the job to use your repository as a Multibranch Pipeline.

Basic steps:

1. Create a new item -> Pipeline -> give it a name -> OK.
2. In pipeline section, either:
   - Choose "Pipeline script" and paste the contents of `lab6/sample/jenkinsfile`.
   - Or choose "Pipeline script from SCM" and point it to the repo URL and path to `lab6/sample/Jenkinsfile`.

The sample `Jenkinsfile` (repo: `lab6/sample/jenkinsfile`) simply runs `npm install` and `npm test`.

## Run and verify

1. Make sure the sample `sum.js` is implemented (see example above). You can test locally:

```shell
cd lab6/sample
npm install
npm test
```

2. Trigger the pipeline in Jenkins and watch the console logs. The Test stage should run `jest` and pass if `sum.js` implements the add function.

## Automating with GitHub (optional)

- Install GitHub plugin in Jenkins and configure credentials.
- Create webhook in GitHub to notify Jenkins on push/pull-request events.
- Use Multibranch Pipeline or GitHub Organization Folder to automatically pick branches and run builds.

## Cleanup

To delete the Jenkins resources created by this lab:

```shell
kubectl delete -f lab6/jenkins/jenkins_deployment.yml -n jenkins
kubectl delete -f lab6/jenkins/jenkins_service.yml -n jenkins
kubectl delete -f lab6/jenkins/jenkins_pvc.yml -n jenkins
kubectl delete -f lab6/jenkins/jenkins_pv.yml
kubectl delete -f lab6/jenkins/jenkins_namespace.yml
```

Because the PV uses `persistentVolumeReclaimPolicy: Retain`, the hostPath data at `/data/jenkins` will be preserved unless you remove it manually on the node.

## Troubleshooting tips

- If the Jenkins pod fails to start due to permission issues writing to `/var/jenkins_home`, ensure the hostPath directory `/data/jenkins` exists on the node and has proper permissions: `sudo mkdir -p /data/jenkins && sudo chown 1000:1000 /data/jenkins` (Jenkins may run as `jenkins` user UID 1000).
- If NodePorts conflict, choose an available port in the cluster's allowed NodePort range.
- If the pipeline fails in Jenkins but passes locally, check the Jenkins agent environment (PATH, Node/NPM versions) and consider installing a NodeJS tool in Jenkins or using a Docker agent in the pipeline.

## Next steps / extensions

- Convert the Deployment to a StatefulSet if you need stable network identity (not necessary for typical Jenkins Master).
- Use an Ingress / LoadBalancer instead of NodePort for production-like access.
- Integrate with GitHub Actions or GitHub Apps for tighter GitHub-Jenkins integration.
