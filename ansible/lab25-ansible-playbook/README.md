# Lab 25 — Ansible Playbook

A single playbook that installs Docker, kubectl, and Jenkins on the managed node.

## Files

| File | Purpose |
| --- | --- |
| `lab25-playbook.yml` | Playbook: Docker CE repo + `docker-ce`, kubectl binary, Java 17 + Jenkins repo + `jenkins`, all services started and enabled |

## Notes

- RHEL base repositories do not ship a `docker` package, so the playbook adds the Docker CE repository first and installs `docker-ce`.
- The playbook is idempotent: re-running it only changes what is missing.

## Run

```bash
cd ~/ansible
ansible-playbook -i inventory.ini lab25-playbook.yml --ask-become-pass
```

## Verify

```bash
ansible -i inventory.ini all -m shell -a "docker --version; kubectl version --client; systemctl is-active jenkins"
```
