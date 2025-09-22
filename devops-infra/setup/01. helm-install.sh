#!/bin/bash
# helm package 설치 (Ubuntu 기준)
# k8s cluster에 설치하는 devops 서비스 설치와 변경을 helm을 통해 관리함.

# 1. 필수 패키지 설치
echo ">>> 필수 패키지 설치..."
sudo apt-get update
sudo apt-get install -y curl gnupg lsb-release apt-transport-https software-properties-common

# kubectl 설치
if false; then
echo ">>> kubectl 설치..."
curl -fsSL https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo gpg --dearmor -o /usr/share/keyrings/kubernetes-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | \
    sudo tee /etc/apt/sources.list.d/kubernetes.list > /dev/null
sudo apt-get update
sudo apt-get install -y kubectl

kubectl version --client
else
  echo ">>> kubectl 설치 스킵..."
fi

#2. Helm 설치 (공식 스크립트 사용)
echo ">>> Helm 설치..."
curl -fsSL https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
helm version

# 설치 확인
echo ">>> 설치 및 초기화 완료!"

