# Lab 28 — AWS EC2 Dynamic Inventory

A dynamic Ansible inventory that discovers EC2 instances from AWS instead of a static inventory file: any instance tagged `server=ivolve` appears in the inventory automatically.

## Files

| File | Purpose |
| --- | --- |
| `aws_ec2.yml` | Inventory plugin config (`amazon.aws.aws_ec2`, region `eu-central-1`, filter `tag:server=ivolve`) |
| `README-lab28.md` | Full setup guide |

## Prerequisites

- An AWS account and an IAM user with EC2 read access (`AmazonEC2ReadOnlyAccess` is enough for this lab)
- EC2 instances tagged `Key = server`, `Value = ivolve` (change the region in `aws_ec2.yml` if your instances live elsewhere)
- AWS credentials configured locally (`aws configure` or `~/.aws/credentials`). Never commit keys to Git or paste them in chat.

## Setup

```bash
ansible-galaxy collection install amazon.aws
pip3 install boto3 botocore
```

## Run

```bash
ansible-inventory -i aws_ec2.yml --graph
ansible-inventory -i aws_ec2.yml --list
ansible -i aws_ec2.yml all -m ping
```

With no matching instances the inventory is empty — that itself confirms the plugin is reading from AWS correctly.

## Cost note

The dynamic inventory itself is free; the EC2 instances it discovers are billed by AWS. Stop or terminate instances when you are done.
