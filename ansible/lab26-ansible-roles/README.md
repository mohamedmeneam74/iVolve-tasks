# Lab 26 — Ansible Roles

The Lab 25 playbook refactored into reusable roles: one role per tool, and a thin playbook that only lists the roles in order.

## Structure

```
lab26-playbook.yml
roles/
├── docker/tasks/main.yml    # Docker CE repo, docker-ce, service
├── kubectl/tasks/main.yml   # kubectl binary
└── jenkins/tasks/main.yml   # Java 17, Jenkins repo, jenkins, service
```

Roles are resolved relative to the playbook, so keep the `roles/` folder next to `lab26-playbook.yml`.

## Run

```bash
cd lab26-ansible-roles
ansible-playbook -i ~/ansible/inventory.ini lab26-playbook.yml --ask-become-pass
```

## Verify

```bash
ansible -i ~/ansible/inventory.ini all -m shell -a "docker --version; kubectl version --client; systemctl is-active jenkins"
```
