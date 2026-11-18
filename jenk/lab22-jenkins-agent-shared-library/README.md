# Lab 22 — Jenkins Agent + Shared Library

Runs the Lab 21 pipeline on a Jenkins agent and moves the pipeline steps into a shared library, so every job reuses the same build, scan, and deploy logic.

## Components

- **Agent `agent1`** — a permanent Jenkins agent on the worker node (`192.168.228.131`), launched by connecting to the controller, labeled `docker-agent`.
- **Shared library `ivolve-lib`** — a GitHub repository (`jenkins-shared-library`) registered in Jenkins as a Global Trusted Pipeline Library, default version `main`.

## Shared library steps (`vars/`)

| Step | Purpose |
| --- | --- |
| `buildApp` | Maven test and package |
| `buildImage` | Build the Docker image, scan with Trivy, push to Docker Hub, clean up locally |
| `deployOnK8s` | Update the image tag in the deployment manifest and apply it with kubectl |

## Files

| File | Purpose |
| --- | --- |
| `vars/buildApp.groovy` | Build step |
| `vars/buildImage.groovy` | Image build, scan, and push step |
| `vars/deployOnK8s.groovy` | Kubernetes deploy step |
| `Jenkinsfile-lab22` | Pipeline using `@Library('ivolve-lib') _` on agent label `docker-agent` |
| `README-lab22.md` | Setup notes |

## Prerequisites

- Lab 21 working (Docker Hub and Kubernetes credentials configured in Jenkins)
- The shared library repository registered in Jenkins (Manage Jenkins → System → Global Trusted Pipeline Library)

## Run

Trigger **Build Now** on the `demoapp-shared-lib` job.

## Verify

- The build log shows `Loading library ivolve-lib@main` and `Running on agent1`.
- The deployed image tag matches the build number: `medomeneam/demoapp:<BUILD_NUMBER>`.

```bash
kubectl get pods -n ivolve
curl http://192.168.228.131:30080
# Hello from Dockerized Spring Boot!
```
