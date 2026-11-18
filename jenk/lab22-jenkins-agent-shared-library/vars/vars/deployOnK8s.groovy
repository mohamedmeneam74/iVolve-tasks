def call(Map cfg) {
    def image = "${cfg.imageName}:${cfg.imageTag}"

    sh "sed -i 's|image: .*|image: ${image}|' ${cfg.deploymentFile}"
    sh "grep 'image:' ${cfg.deploymentFile}"

    // GitOps (Lab 23): do NOT kubectl apply. Push the updated manifest to
    // GitHub and let Argo CD sync it to the cluster.
    withCredentials([usernamePassword(credentialsId: cfg.gitCredId ?: 'github-credentials',
                                       usernameVariable: 'GIT_USER',
                                       passwordVariable: 'GIT_PASS')]) {
        sh """
            git config user.email "jenkins@ivolve.local"
            git config user.name "jenkins"
            git add ${cfg.deploymentFile}
            git commit -m "Update demoapp image to ${image} [build ${env.BUILD_NUMBER}]" || echo "nothing to commit"
            git push https://\${GIT_USER}:\${GIT_PASS}@github.com/mohamedmeneam74/demoapp.git HEAD:main
        """
    }
    echo "Manifest pushed to GitHub. Argo CD will sync image ${image} to the cluster."
}

