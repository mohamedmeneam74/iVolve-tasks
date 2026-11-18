def call(Map cfg) {
    def image = "${cfg.imageName}:${cfg.imageTag}"

    sh "docker build -t ${image} ."
    sh "trivy image --no-progress --severity HIGH,CRITICAL --exit-code 0 --timeout 15m --skip-version-check --scanners vuln ${image}"

    withCredentials([usernamePassword(credentialsId: cfg.credentialsId,
                                      usernameVariable: 'DH_USER',
                                      passwordVariable: 'DH_PASS')]) {
        sh 'echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin'
        sh "docker push ${image}"
        sh 'docker logout'
    }

    sh "docker rmi ${image} || true"
}

