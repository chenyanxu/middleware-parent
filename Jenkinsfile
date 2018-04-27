// refer to https://github.com/siamaksade/cart-service/blob/jenkinsfiles/Jenkinsfile
node('maven') {
  stage('Build middleware-parent App') {
    git url: "https://github.com/chenyanxu/middleware-parent.git"
    sh "mvn install -DskipTests=true -s settings.xml"
  }
  stage('Deploy middleware-parent App') {
    sh "mvn deploy -DskipTests=true -s settings.xml"
  }
}
