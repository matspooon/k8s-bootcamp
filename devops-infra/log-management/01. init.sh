#!/bin/bash
PROFILE=dev
NAMESPACE=logging

kubectl create namespace $NAMESPACE
# install opensearch
helm repo add opensearch https://opensearch-project.github.io/helm-charts/
helm repo update
helm upgrade -install opensearch opensearch/opensearch -n $NAMESPACE \
  --set securityConfig.disableTransportSSL=true \
  --set securityConfig.disableRestSSL=true \
  --set securityConfig.enabled=false \
  -f $PROFILE/opensearch-values.yaml
helm upgrade -install opensearch-dashboards opensearch/opensearch-dashboards -n $NAMESPACE -f $PROFILE/opensearch-dashboard-values.yaml
kubectl apply -n $NAMESPACE -f $PROFILE/opensearch.yaml
# pod이 crash나 error로 인해 강제 삭제하고자 할 경우
# kubectl delete statefulset opensearch-cluster-master -n apps-dev

# install 
kubectl apply -n $NAMESPACE -f $PROFILE/fluent-bit-config.yaml
helm repo add fluent https://fluent.github.io/helm-charts
helm repo update
helm upgrade -install fluent-bit fluent/fluent-bit -n logging \
  -f $PROFILE/fluent-bit-values.yaml