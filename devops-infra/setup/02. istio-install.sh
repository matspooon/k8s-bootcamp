#! /bin/bash
# istio gateway 설치
# 참조 : https://istio.io/latest/docs/setup/install/helm

helm repo add istio https://istio-release.storage.googleapis.com/charts
helm repo update
helm install istio-base istio/base -n istio-system --set defaultRevision=default --create-namespace
helm install istiod istio/istiod -n istio-system --wait
helm upgrade --install istio-ingressgateway istio/gateway -n istio-system --set service.type=LoadBalancer

# 2. *.k8s.dev를 서비스할 istio gateway의 443 HTTPS용 TLS secret 생성
kubectl create -n istio-system secret tls selfsigned-gateway-cert \
  --key k8s.dev.key \
  --cert k8s.dev.crt

kubectl apply -f istio-gateway.yaml
kubectl apply -f istio-ingress.yaml
kubectl apply -f istio-cicd.yaml

######################################################################################
# k8s cluster의 coredns에 gitea-https.dev-tools.svc.cluster.local를 alias로 추가해줘야한다
# istio gateway의 clusterIP 확인
if false; then
kubectl get svc istio-ingressgateway -n istio-system
# cordens 편집
kubectl -n kube-system edit configmap coredns
# 다음의 내용을 .:53 { 에 추가
#        hosts {
#            10.108.127.16 gitea-https.dev-tools.svc.cluster.local
#            fallthrough
#        }
# corddns가 바로 반영안될경우 reload
kubectl rollout restart deployment coredns -n kube-system
fi