#!/bin/bash
#see : https://artifacthub.io/packages/helm/k8s-dashboard/kubernetes-dashboard

helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard \
  --create-namespace --namespace kubernetes-dashboard \
  -f k8s-dashboard-values.yaml
#helm delete kubernetes-dashboard --namespace kubernetes-dashboard

# create istio gateway VirtualService
kubectl apply -f istio-k8s-dashboard.yaml

# create service account & token
#kubectl -n kubernetes-dashboard create token admin-user
kubectl apply -f k8s-dashboard-account.yaml
kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath="{.data.token}" | base64 -d