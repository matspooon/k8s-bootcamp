#!/bin/bash

# Kubernetes apps namespace
kubectl create namespace apps-dev
kubectl create namespace apps-stg
kubectl create namespace apps

PROFILE=dev
if [ -n "$1" ]; then
    PROFILE="$1"
fi
NAMESPACE=apps-dev

# install timezone daemonset
kubectl apply -f tz-daemon.yaml
kubectl delete -f tz-daemon.yaml --cascade=true

# install db
kubectl apply -n $NAMESPACE -f $PROFILE/postgres-pvc.yaml
kubectl apply -n $NAMESPACE -f $PROFILE/postgres.yaml

# initialize db
kubectl create configmap db-schema -n $NAMESPACE --from-file=db-schema.sql=./dev/db-schema.sql
kubectl create configmap db-init-data -n $NAMESPACE --from-file=db-init-data.sql=./dev/db-init-data.sql
kubectl apply -n $NAMESPACE -f $PROFILE/postgres-job.yaml
kubectl delete -n $NAMESPACE -f $PROFILE/postgres-job.yaml

# install redis

# install mq
