# Lab 23 — Argo CD GitOps

GitOps delivery with Argo CD: the desired state of the application lives in Git, and Argo CD keeps the cluster in sync with it automatically. The Jenkins pipeline no longer applies manifests with kubectl — it commits the updated manifest to GitHub and Argo CD deploys it.

## How it works

```
Jenkins pipeline ──▶ commits new image tag to GitHub (k8s/deployment.yaml)
Argo CD ──▶ detects the change ──▶ syncs it to the cluster
```

## Files

| File | Purpose |
| --- | --- |
| `argocd-application.yaml` | Argo CD Application `ivovle`: source repo `demoapp`, path `k8s`, destination namespace `ivolve` |
| `deployOnK8s.groovy` | Shared-library step: updates the image tag and pushes the manifest to GitHub |
| `Jenkinsfile-lab22` | Pipeline with a GitOps deploy stage (no kubectl) |

## Setup

1. Install Argo CD:
   ```bash
   kubectl create namespace argocd
   kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
   ```
2. Open the UI (port-forward, then https://localhost:8443, user `admin`):
   ```bash
   kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
   kubectl port-forward svc/argocd-server -n argocd 8443:443
   ```
3. Apply the Application:
   ```bash
   kubectl apply -f argocd-application.yaml
   ```
4. Create a Jenkins credential `github-credentials` (username + GitHub personal access token, scope `repo`) so the pipeline can push manifests.

## Verify

- The Application status is **Synced / Healthy**.
- After a pipeline run, the manifest in GitHub carries the new image tag and the cluster runs it.

```bash
kubectl get pods -n ivolve
curl http://192.168.228.131:30080
# Hello from Dockerized Spring Boot!
```
