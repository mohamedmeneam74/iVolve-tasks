def call(Map cfg) {
    def image = "${cfg.imageName}:${cfg.imageTag}"
    def name  = cfg.deploymentName ?: 'demoapp'

    sh "sed -i 's|image: .*|image: ${image}|' ${cfg.deploymentFile}"
    sh "grep 'image:' ${cfg.deploymentFile}"

    withCredentials([string(credentialsId: cfg.tokenCredId, variable: 'K8S_TOKEN'),
                     file(credentialsId: cfg.caCredId, variable: 'K8S_CA')]) {
        sh """
            kubectl --kubeconfig=/dev/null --server=${cfg.server} --token=\$K8S_TOKEN --certificate-authority=\$K8S_CA \
              apply -f ${cfg.deploymentFile} -n ${cfg.namespace}
            kubectl --kubeconfig=/dev/null --server=${cfg.server} --token=\$K8S_TOKEN --certificate-authority=\$K8S_CA \
              rollout status deployment/${name} -n ${cfg.namespace} --timeout=180s
        """
    }
}

