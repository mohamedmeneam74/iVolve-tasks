# Lab 21 — Jenkins CI/CD Pipeline

A Jenkins pipeline that builds a Spring Boot demo application, scans its container image, pushes it to Docker Hub, and deploys it to Kubernetes.

## Pipeline stages

1. **Build App** — Maven test and package.
2. **Build Image** — Build the Docker image and scan it with Trivy (HIGH and CRITICAL vulnerabilities reported, non-blocking).
3. **Push** — Push the image to Docker Hub, tagged with the Jenkins build number.
4. **Deploy** — Apply the Kubernetes manifests and wait for the rollout.

## Files

| File | Purpose |
| --- | --- |
| `Jenkinsfile` | Pipeline definition (place at the demo app repository root) |
| `Dockerfile` | Multi-stage build on `eclipse-temurin:17-jre` |
| `k8s/deployment.yaml` | Deployment (1 replica, NodePort service) |
| `k8s/demoapp-svc.yaml` | NodePort service on port 30080 |

## Prerequisites

- Jenkins running on the control plane (`http://192.168.228.129:8080`)
- Jenkins credentials: `dockerhub-credentials` (Docker Hub access token), `k8s-token` (ServiceAccount token), `k8s-ca` (cluster CA certificate)
- A Jenkins job (`demoapp-pipeline`) pointing at the demo app repository with Script Path `Jenkinsfile`

## Run

Trigger **Build Now** on the `demoapp-pipeline` job.

## Verify

```bash
kubectl get pods -n ivolve
curl http://192.168.228.131:30080
# Hello from Dockerized Spring Boot!
```
