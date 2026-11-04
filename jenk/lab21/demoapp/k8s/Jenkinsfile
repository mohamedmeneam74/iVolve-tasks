pipeline {
    agent any

    environment {
        IMAGE_NAME = '<dockerhub-username>/demoapp'      // CHECK: your Docker Hub username
        IMAGE_TAG  = "${env.BUILD_NUMBER}"
        K8S_SERVER = 'https://192.168.228.129:6443'      // CHECK: control plane IP
        NAMESPACE  = 'ivolve'
    }

    stages {
        stage('Build App') {
            steps {
                sh 'mvn -B clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Scan Docker Image') {
            steps {
                // Reports HIGH/CRITICAL findings. Change --exit-code to 1 to fail the build on findings.
                sh 'trivy image --no-progress --severity HIGH,CRITICAL --exit-code 0 $IMAGE_NAME:$IMAGE_TAG'
            }
        }

        stage('Push Image to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials',
                                                  usernameVariable: 'DH_USER',
                                                  passwordVariable: 'DH_PASS')]) {
                    sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin'
                    sh 'docker push $IMAGE_NAME:$IMAGE_TAG'
                }
            }
        }

        stage('Delete Image Locally') {
            steps {
                sh 'docker rmi $IMAGE_NAME:$IMAGE_TAG || true'
            }
        }

        stage('Update Image in Deployment File') {
            steps {
                sh 'sed -i "s|image: .*|image: ${IMAGE_NAME}:${IMAGE_TAG}|" k8s/deployment.yaml'
                sh 'grep "image:" k8s/deployment.yaml'
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([string(credentialsId: 'k8s-token', variable: 'K8S_TOKEN'),
                                 file(credentialsId: 'k8s-ca', variable: 'K8S_CA')]) {
                    sh '''
                        kubectl --kubeconfig=/dev/null --server=$K8S_SERVER --token=$K8S_TOKEN --certificate-authority=$K8S_CA \
                          apply -f k8s/deployment.yaml -n $NAMESPACE
                        kubectl --kubeconfig=/dev/null --server=$K8S_SERVER --token=$K8S_TOKEN --certificate-authority=$K8S_CA \
                          rollout status deployment/demoapp -n $NAMESPACE --timeout=180s
                    '''
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline #${env.BUILD_NUMBER} finished with status: ${currentBuild.currentResult}"
            sh 'docker logout || true'
            deleteDir()
        }
        success {
            echo "Success: ${IMAGE_NAME}:${IMAGE_TAG} is deployed to ${NAMESPACE}."
        }
        failure {
            echo 'Pipeline failed: check the stage logs above.'
            sh 'docker rmi $IMAGE_NAME:$IMAGE_TAG || true'
        }
    }
}
