# Lab 27 — Ansible Vault + MySQL

A MySQL role that installs the database server and creates a database and user, with the database password stored encrypted in Ansible Vault.

## Structure

```
lab27-playbook.yml
roles/mysql/
├── tasks/main.yml   # Install server, start service, create DB and user
├── vars/main.yml    # db_name: ivolve, db_user: ivolveuser
└── vars/vault.yml   # db_password (ENCRYPTED with ansible-vault)
```

## Notes

- RHEL 10 ships MariaDB as its MySQL-compatible database server, so the role installs `mariadb-server` and manages the `mariadb` service. The client and SQL are the same as MySQL.
- `vault.yml` is encrypted: it is safe to commit, but the Vault password itself must never be committed or shared.

## Setup

1. Set the database password in `roles/mysql/vars/vault.yml` (letters and numbers only).
2. Encrypt it (choose a Vault password and keep it safe):
   ```bash
   ansible-vault encrypt roles/mysql/vars/vault.yml
   ```

## Run

```bash
ansible-playbook -i ~/ansible/inventory.ini lab27-playbook.yml --ask-become-pass --ask-vault-pass
```

## Verify

```bash
mysql -u ivolveuser -p -e "SHOW DATABASES;"
# ivolve appears in the list
```
