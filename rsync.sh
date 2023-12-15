#/bin/bash

rsync -zari . ubuntu@oracle-cloud:/home/ubuntu/NEU/semester-4/CSYE_6220-Enterprise_Software_Design/vehicle-route-planning --include='**.gitignore' --exclude='.git' --exclude=".github" --filter=':- .gitignore' --delete-after
