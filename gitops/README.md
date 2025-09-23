# ArgoCD + GIT : helm chart로 관리하는 배포용 apps.yaml

## gitea에 repostiory 등록
git remote add origin https://gitea.k8s.dev/admin/manifest-repo.git

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

