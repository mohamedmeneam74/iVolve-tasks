# iVolve Cloud DevOps Accelerator — Hands-On Labs

Hands-on implementation of the iVolve Cloud DevOps Accelerator: 28 labs covering Docker, Kubernetes, Jenkins, Argo CD, Ansible, and AWS, built on a two-node Kubernetes cluster.

## What this project does

A complete CI/CD and GitOps workflow for a Spring Boot demo application:

1. **Code** is pushed to GitHub.
2. **Jenkins** builds the app, scans the image with Trivy, and pushes it to Docker Hub.
3. **Argo CD** syncs the Kubernetes manifests from GitHub to the cluster automatically.
4. **Ansible** provisions and configures the nodes (Docker, kubectl, Jenkins, MySQL).

```
GitHub ──▶ Jenkins ──▶ Docker Hub ──▶ Argo CD ──▶ Kubernetes
              │                          ▲
              └──── shared library ──────┘
Ansible ──▶ control plane + worker node (Docker, kubectl, Jenkins, MySQL)
```

## Environment

| Node | IP | Role |
| --- | --- | --- |
| control-plane | 192.168.228.129 | Kubernetes control plane, Jenkins, Ansible control node |
| worker1 | 192.168.228.131 | Kubernetes worker, Jenkins agent, application runtime |

- Kubernetes cluster on VMware (network `192.168.228.0/24`)
- Namespace `ivolve` for application workloads
- Demo application: Spring Boot, served at `http://192.168.228.131:30080`

## Labs

| Lab | Topic | Folder |
| --- | --- | --- |
| 1–2 | Build tools (Maven, Gradle) | `build_tools/` |
| 3–8 | Docker fundamentals | `docker/` |
| 10–18 | Kubernetes | `k8s/` |
| 19 | Kubernetes RBAC for Jenkins | `k8s/lab19/` |
| 21 | Jenkins CI/CD pipeline | `jenk/lab21-jenkins-pipeline/` |
| 22 | Jenkins agent + shared library | `jenk/lab22-jenkins-agent-shared-library/` |
| 23 | Argo CD GitOps | `jenk/lab23-argocd-gitops/` |
| 24 | Ansible basics | `ansible/lab24-ansible-basics/` |
| 25 | Ansible playbook | `ansible/lab25-ansible-playbook/` |
| 26 | Ansible roles | `ansible/lab26-ansible-roles/` |
| 27 | Ansible Vault + MySQL | `ansible/lab27-ansible-vault-mysql/` |
| 28 | AWS EC2 dynamic inventory | `ansible/lab28-aws-dynamic-inventory/` |

Each lab folder contains its own README with purpose, files, setup, and verification steps. The Spring Boot demo application deployed in Labs 21–23 lives in `demoapp/`.

## Tech stack

Docker · Kubernetes · Jenkins · Argo CD · Ansible · Trivy · GitHub Actions (concepts) · AWS EC2 · MySQL / MariaDB · Spring Boot · Maven

## Security

No secrets are stored in this repository: no tokens, passwords, kubeconfigs, or private keys. Jenkins credentials live in Jenkins, Ansible Vault passwords are never committed, and AWS keys are configured locally with `aws configure`.
