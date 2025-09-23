#!/bin/bash

docker pull gradle:7.6-jdk17
docker pull gcr.io/kaniko-project/executor:debug
docker pull eclipse-temurin:17-jdk-jammy
docker pull jenkins/inbound-agent:3327.v868139a_d00e0-6
docker pull postgres:17
docker pull docker.gitea.com/gitea:1.24.5-rootless
docker pull jenkins/jenkins:2.516.2-jdk21
docker pull quay.io/argoproj/argocd:v3.1.4

# docker image load/save
#docker save -o gradle-jdk.tar gradle:7.6-jdk17
#docker save -o kaniko-executor-debug.tar gcr.io/kaniko-project/executor:debug