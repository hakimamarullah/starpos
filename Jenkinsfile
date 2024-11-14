pipeline {
    agent {
         docker {
              image 'maven:3.9.9-eclipse-temurin-21-alpine'
              args '-u root'
    }    }
    stages {
        stage('build') {
            steps {
                sh 'mvn clean package -Pproduction -Dvaadin.force.production.build=true'
            }
        }
    }

    post {
           always {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
           }
    }
}
