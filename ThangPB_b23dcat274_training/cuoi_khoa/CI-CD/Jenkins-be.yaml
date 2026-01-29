pipeline {
  agent { label 'agent-lab' }

  environment {
    IMAGE_NAME = 'thang05/typ-backend'
    DOCKER_HUB_CREDENTIALS = 'dockerhub-creds'
    GITHUB_CREDENTIALS = 'git-hub'
    CONFIG_REPO_URL = 'https://github.com/Phamthang-25/typ-backend-config.git'
    VALUES_FILE = 'helm-values/values-prod.yaml'
    BRANCH = 'main'
  }

  triggers {
    GenericTrigger(
      token: 'typ-be',
      causeString: 'Triggered by GitHub tag: $ref',
      genericVariables: [
        [key: 'GH_REF', value: '$.ref'],
        [key: 'GH_REF_TYPE', value: '$.ref_type']
      ],
      printContributedVariables: true,
      printPostContent: true,
      regexpFilterText: '$GH_REF_TYPE',
      regexpFilterExpression: '^tag$'
    )
  }

  stages {

    stage('Agent Information') {
      steps {
        echo "Running on agent: ${env.NODE_NAME}"
        echo "Workspace: ${env.WORKSPACE}"
        sh 'whoami'
        sh 'pwd'
        sh 'uname -a'
        sh 'docker --version'
        sh 'git --version'
      }
    }

    stage('Checkout Source Code') {
      steps {
        echo "Cloning source code..."
        checkout scm
        echo "Clone completed!"
        sh 'ls -la'
      }
    }

    stage('Get Tag From Webhook') {
      steps {
        script {
          if (env.GH_REF?.trim() && env.GH_REF_TYPE == 'tag') {
            env.TAG_NAME = env.GH_REF.trim()
            echo "Using tag from webhook: ${env.TAG_NAME}"
          } else {
            echo "No tag payload from webhook (manual build or wrong event). Fallback to git describe..."
            sh 'git fetch --tags --force || true'

            def tagVersion = sh(
              script: 'git describe --tags --exact-match 2>/dev/null || git describe --tags --abbrev=0 2>/dev/null || git rev-parse --short HEAD',
              returnStdout: true
            ).trim()

            env.TAG_NAME = tagVersion
            echo "Using fallback version: ${env.TAG_NAME}"
          }

          echo "Docker image will be: ${env.IMAGE_NAME}:${env.TAG_NAME}"
        }
      }
    }

    stage('Checkout Tag (when tag build)') {
      steps {
        script {
          if (env.GH_REF?.trim() && env.GH_REF_TYPE == 'tag') {
            echo "Checking out tag: ${env.TAG_NAME}"
            sh """
              git fetch --tags --force
              git checkout -f refs/tags/${env.TAG_NAME}
              git rev-parse HEAD
              git describe --tags --exact-match
            """
          } else {
            echo "Not a tag build -> keep current checkout"
          }
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          echo "Building Docker image: ${env.IMAGE_NAME}:${env.TAG_NAME}"
          sh """
            docker build -t ${env.IMAGE_NAME}:${env.TAG_NAME} .
            docker tag ${env.IMAGE_NAME}:${env.TAG_NAME} ${env.IMAGE_NAME}:latest
          """
          echo "Docker image built successfully!"
          sh "docker images | grep ${env.IMAGE_NAME} || true"
        }
      }
    }

    stage('Push to Docker Hub') {
      steps {
        script {
          echo "Pushing image to Docker Hub..."
          withCredentials([usernamePassword(
            credentialsId: env.DOCKER_HUB_CREDENTIALS,
            passwordVariable: 'DOCKER_PASSWORD',
            usernameVariable: 'DOCKER_USERNAME'
          )]) {
            sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
          }

          sh """
            docker push ${env.IMAGE_NAME}:${env.TAG_NAME}
            docker push ${env.IMAGE_NAME}:latest
          """
          echo "Successfully pushed to Docker Hub!"
        }
      }
    }

    stage('Clone Config Repo') {
      steps {
        script {
          echo "Cloning config repository..."
          sh 'rm -rf config-repo && mkdir -p config-repo'

          dir('config-repo') {
            withCredentials([gitUsernamePassword(credentialsId: env.GITHUB_CREDENTIALS, gitToolName: 'Default')]) {
              sh """
                git clone ${env.CONFIG_REPO_URL} .
                git checkout ${env.BRANCH}
                git config user.email "jenkins@local"
                git config user.name "Jenkins CI/CD Backend"
              """
            }
            sh 'ls -la'
          }
        }
      }
    }

    stage('Update Helm Values') {
      steps {
        script {
          dir('config-repo') {
            echo "Updating ${env.VALUES_FILE} with tag: ${env.TAG_NAME}"
            sh """
              sed -i 's/^  tag:.*/  tag: "${env.TAG_NAME}"/' ${env.VALUES_FILE}
              echo "After update:"
              grep -n 'tag:' ${env.VALUES_FILE} || true
            """
          }
        }
      }
    }

    stage('Push Config Changes') {
      steps {
        script {
          dir('config-repo') {
            def gitStatus = sh(script: 'git status --porcelain', returnStdout: true).trim()

            if (gitStatus) {
              echo "Changes detected, committing and pushing..."
              sh """
                git add ${env.VALUES_FILE}
                git commit -m "Update backend image tag to ${env.TAG_NAME} (build ${env.BUILD_NUMBER})"
              """
              withCredentials([gitUsernamePassword(credentialsId: env.GITHUB_CREDENTIALS, gitToolName: 'Default')]) {
                sh "git push origin ${env.BRANCH}"
              }
              echo "Config changes pushed successfully!"
            } else {
              echo "No changes detected in config repo"
            }
          }
        }
      }
    }
  }

  post {
    always {
      echo "Cleaning up..."
      sh """
        docker logout || true
        docker rmi ${env.IMAGE_NAME}:${env.TAG_NAME} || true
        docker rmi ${env.IMAGE_NAME}:latest || true
        docker system prune -f || true
      """
      cleanWs()
    }
    success {
      echo "BUILD SUCCESS!"
      echo "Image pushed: ${env.IMAGE_NAME}:${env.TAG_NAME}"
      echo "Config repo updated: ${env.CONFIG_REPO_URL}"
    }
    failure {
      echo "BUILD FAILED! Please check logs."
    }
  }
}