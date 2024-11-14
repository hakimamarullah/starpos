pipeline {
    agent { docker { image 'maven:3.9.9-eclipse-temurin-21-alpine' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn clean package -Pproduction -Dvaadin.force.production.build=true -Dmaven.repo.local=$HOME/.m2/repository'
            }
        }
    }

    post {
           always {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
           }
    }
}
