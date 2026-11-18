# Lab 24 — Ansible Basics

Ansible fundamentals: install Ansible on the control node, connect to a managed node over SSH, and run ad-hoc commands.

## Setup

The control plane is the Ansible control node; `worker1` is the managed node.

**1. Install Ansible (control plane):**

```bash
sudo dnf install -y ansible-core
```

**2. Inventory** (`inventory.ini`, included in this folder):

```ini
[ivolve]
worker1 ansible_host=192.168.228.131 ansible_user=mohamed
```

**3. SSH access:**

```bash
ssh-keygen -t ed25519 -f ~/.ssh/ansible_key -N ""
ssh-copy-id -i ~/.ssh/ansible_key.pub mohamed@192.168.228.131
```

## Verify

Run the commands below from this folder:

```bash
ansible -i inventory.ini all -m ping
# worker1 | SUCCESS => {"ping": "pong"}

ansible -i inventory.ini all -m dnf -a "name=docker-ce state=present" -b
ansible -i inventory.ini all -m shell -a "docker --version"
```
