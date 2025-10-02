# 『쿠버네티스 클러스터 : Gitea + Jenkins + ArgoCD + Istio Gateway 로 구성하는 실전 CICD 서비스 구성』


## 전체 구성
1. Gitea → k8s cluster내의 jenkins에서 빌드하는 docker image registry, argoncd의 manifest-repo 
2. Jenkins → CI (빌드, 테스트, 이미지 생성, manifest-repo 업데이트)
3. ArgoCD → CD (manifest-repo 변경 감지 → 배포 자동화)
4. Istion Gateway → 서비스 분기

## Gitea
* 코드 Repo : Github 사용
* manifest Repo : 신규 빌드가 구성되면 빌드 넘버를 업데이트하여 ArgoCD가 배포를 실행하도록 함

## docker desktop의 kubernetes cluster에 생성한 PV를 확인하는 방법
다음 명령으로 kubernetes의 워커노드에 들어가면 확인 가능
docker run -it --privileged --pid=host justincormack/nsenter1

## 워커노드의 timezone을 일률적으로 유지하는 방안
# 상용 서비스(AWS, Azure)에서 제공하는 managed kubernetes의 경우 cloud init script를 지원하면 해당 script에 timezone을 설정하도록 한다
# 베어메탈/VM 기반 자체 클러스터의 경우 워커노드 생성에 사용되는 이미지에 default 세팅을 하고 골든 이미지로 사용한다
# 위의 두가지 외에는 daemon-set을 활용하여, 1회성으로 수정하거나 각 노드에 직접 수정을 하는 방안이 있다
