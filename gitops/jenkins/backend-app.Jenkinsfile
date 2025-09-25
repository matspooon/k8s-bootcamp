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
      workspaceVolume persistentVolumeClaimWorkspaceVolume(claimName: 'jenkins-workspace-pvc', readOnly: false)
      yaml """
apiVersion: v1
kind: Pod
env:
  - name: JENKINS_HOME
    value: /home/jenkins
spec:
  securityContext:
    runAsUser: 1000
    runAsGroup: 1000
    fsGroup: 1000
  containers:
    - name: gradle
      image: gradle:7.6-jdk17
      imagePullPolicy: IfNotPresent
      command: ['sleep']
      args: ['infinity']
      volumeMounts:
        - name: jenkins-gradle
          mountPath: /home/jenkins/.gradle/
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      imagePullPolicy: IfNotPresent
      securityContext:
        runAsUser: 0
      command: ['sleep']
      args: ['infinity']
      volumeMounts:
        - name: docker-config
          mountPath: /kaniko/.docker
  volumes:
    - name: docker-config
      secret:
        secretName: docker-registry-credential
        items:
          - key: .dockerconfigjson
            path: config.json
    - name: jenkins-gradle
      persistentVolumeClaim:
        claimName: jenkins-gradle-pvc
"""
      defaultContainer 'gradle'
      // customWorkspace '/home/jenkins/agent/workspace'
    }
  }

  environment {
    REGISTRY = 'gitea-http.dev-tools.svc.cluster.local:3000'
    NAMESPACE = 'admin'
    IMAGE = 'backend-app'
    TAG = "${new Date().format('yyyyMMdd')}.${env.BUILD_NUMBER}"
    //TAG = 'latest'
    BRANCH = 'main'
    // GITHUB_CRED_ID = 'github-credential'
    GITEA_USERNAME = 'admin'
    GITEA_PASSWORD = 'admin'
  }

  stages{
    stage('gradle build'){
      steps{
        container('gradle'){
          sh 'git config --global --add safe.directory ${WORKSPACE}'

          git url: 'https://github.com/matspooon/k8s-bootcamp.git',
          branch: env.BRANCH
          // credentialsId: env.GITHUB_CRED_ID
          
          script {
            env.GIT_COMMIT_SHA = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            def props = readJSON file: 'backend-app/app/src/main/resources/version.json'
            props.image = env.IMAGE+':'+env.TAG
            props.appVersion = env.TAG
            props.commit = env.GIT_COMMIT_SHA
            writeJSON file: 'backend-app/app/src/main/resources/version.json', json: props, pretty: 2, overwrite: true
            sh 'cat backend-app/app/src/main/resources/version.json'
          }          

          dir('backend-app') {
            sh 'pwd && ls -la'
            sh 'sh ./gradlew -Dgradle.user.home=/home/jenkins/.gradle -Dorg.gradle.daemon=false bootJar -x test'
            sh 'rm -f ./apps.jar'
            sh 'mv ./app/build/libs/*.jar ./'
            sh 'git reset'
          }
        }
      }
    }
    stage('docker build & push'){
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

    stage('update deployment'){
      steps{
        dir('..') {
          sh '''#!/bin/bash
            if [ -d "manifest-repo/.git" ]; then
              cd manifest-repo
              git fetch --all
              git reset --hard origin/main
            else 
              echo "clone manifest-repo"
              git clone "http://${GITEA_USERNAME}:${GITEA_PASSWORD}@gitea-http.dev-tools.svc.cluster.local:3000/admin/manifest-repo.git"
            fi

          '''

          // update deploy version 
          script {
            def charts = readYaml file: "manifest-repo/charts/${env.IMAGE}/Chart.yaml"
            echo "Chart: ${charts}"
            charts.appVersion = env.TAG
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

    stage('Deploy to ArgoCD via API') {
      steps {
        sh '''
            curl -k -u admin:admin \
              -X POST http://argocd-server.dev-tools.svc.cluster.local/api/v1/applications/backend-app-dev/sync
        '''
      }
    }
  }
}
