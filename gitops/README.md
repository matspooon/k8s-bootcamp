# ArgoCD + GIT : helm chart로 관리하는 배포용 apps.yaml

## Direcotry Structure

## 서비스(운영) 환경
namespace : apps
labels :
  profile : prd

## 스테이징 환경
namespace : apps-stg
labels :
  profile : stg

## 개발 환경
namespace : apps-dev
labels :
  profile : dev

## helm template 검증 방법
<pre>
helm template backend-app charts/backend-app --namespace apps-dev --values envs/dev/backend-values.yaml > backend-app.yaml
kubectl apply -f backend-app.yaml
</pre>