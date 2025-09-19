pipeline{
  agent {
    /*
    kubernetes {
      label 'kaniko-gradle'
    }
    kubernetes {
      yamlFile 'cicd/jenkins/jenkins-pod-template.yaml'
      defaultContainer 'gradle'
    }
    */
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
env:
  - name: JENKINS_HOME
    value: /home/jenkins
  - name: HOME
    value: /home/jenkins
spec:
  securityContext:
    runAsUser: 1000
    fsGroup: 1000
  containers:
    - name: gradle
      image: gradle:7.6-jdk17
      securityContext:
        runAsUser: 1000
      command: ['sleep']
      args: ['infinity']
      volumeMounts:
        - name: workspace
          mountPath: /home/jenkins/workspace
        - name: gradle-cache
          mountPath: /home/jenkins/.gradle
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      securityContext:
        runAsUser: 0
      command: ['sleep']
      args: ['infinity']
      volumeMounts:
        - name: docker-config
          mountPath: /kaniko/.docker
        - name: workspace
          mountPath: /home/jenkins/workspace
  volumes:
    - name: docker-config
      secret:
        secretName: docker-registry-credential
        items:
          - key: .dockerconfigjson
            path: config.json
    - name: workspace
      emptyDir:
        memory: false
    - name: gradle-cache
      persistentVolumeClaim:
        claimName: jenkins-gradle-cache
"""
      defaultContainer 'gradle'
      //customWorkspace '/home/jenkins/agent/workspace'
    }
  }

  environment {
    REGISTRY = 'gitea-http.dev-tools.svc.cluster.local:3000'
    NAMESPACE = 'admin'
    IMAGE = 'backend-app'
    // TAG = "${env.BUILD_NUMBER}"
    TAG = 'latest'
    BRANCH = 'main'
    GITHUB_CRED_ID = 'github-matspooon-credential'
    GITEA_USERNAME = 'admin'
    GITEA_PASSWORD = 'admin'
  }

  stages{
    stage('gradle'){
      steps{
        container('gradle'){
          sh 'git config --global --add safe.directory ${WORKSPACE}'

          git url: 'https://github.com/matspooon/ks8-bootcamp.git',
          branch: env.BRANCH,
          credentialsId: env.GITHUB_CRED_ID
          
          script {
            env.GIT_COMMIT_SHA = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          }          

          dir('backend-app') {
            sh 'pwd && ls -la'
            sh 'sh ./gradlew bootJar -x test'
            sh 'mv ./app/build/libs/*.jar ./'
          }
        }
      }
    }
    stage('docker'){
      steps{
        container('kaniko'){
          sh """
            /kaniko/executor \
              --context=dir://${env.WORKSPACE} \
              --dockerfile backend-app/Dockerfile \
              --destination ${REGISTRY}/${NAMESPACE}/${IMAGE}:${TAG} \
              --skip-tls-verify
          """
        }
      }
      post{
        success{
            echo 'success Build & Push'
        }
        failure{
            echo 'failure Build & Push'
        }
      }
    }

    stage('update k8s deployment'){
      steps{
        dir('..') {
          sh '''#!/bin/bash
            git clone "http://${GITEA_USERNAME}:${GITEA_PASSWORD}@gitea-http.dev-tools.svc.cluster.local:3000/admin/manifest-repo.git"
            pwd
          '''

          // update deploy version 
          script {
            def today = new Date().format("yyyyMMdd")
            def charts = readYaml file: "manifest-repo/charts/${env.IMAGE}/Chart.yaml"
            echo "Chart: ${charts}"
            charts.appVersion = today+'.'+BUILD_NUMBER
            echo "Updated chart: ${charts}"
            writeYaml file: "manifest-repo/charts/${env.IMAGE}/Chart.yaml", data: charts, overwrite: true
          }
        }
        dir('../manifest-repo') {
          sh '''#!/bin/bash
            # Git 설정
            git config user.email "jenkins@k8s.dev"
            git config user.name "Jenkins CI"

            # 파일 수정 (예: build.txt 업데이트)
            git add charts/${IMAGE}/Chart.yaml

            # 커밋 (변경 사항 없을 경우 에러 방지)
            git diff --cached --quiet || git commit -m "Update Charts.yaml (build ${BUILD_NUMBER})"

            # 푸시 (username/password 사용)
            git push "http://${GITEA_USERNAME}:${GITEA_PASSWORD}@gitea-http.dev-tools.svc.cluster.local:3000/admin/manifest-repo.git" ${BRANCH}
          '''
        }
      }
    }
  }
}
