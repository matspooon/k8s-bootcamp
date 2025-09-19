# Kubernetes apps namespace
kubectl create namespace apps-dev
kubectl create namespace apps-stg
kubectl create namespace apps

profile=dev
# install db
kubectl apply -f $profile/postgres.yaml
# db secret
kubectl upgrade --install -n apps-$profile -y db-secret-$profile.yaml

# install redis

# install mq

# install opensearch
