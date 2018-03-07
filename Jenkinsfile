pipeline {
    agent {
            docker { image 'maven:3.5.2-jdk-8' }
        }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile -B'
                archiveArtifacts artifacts: 'target/', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test -P arquillian-glassfish-embedded -B'
                archiveArtifacts artifacts: 'target/surefire-reports/', fingerprint: true
            }
        }
        stage('Deploy') {
            steps {
                sh 'mvn package -B'
                archiveArtifacts artifacts: 'target/kwetter.war', fingerprint: true
            }
        }
    }
}
