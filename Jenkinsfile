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
                sh 'mvn clean test  -B'
                archiveArtifacts artifacts: 'target/surefire-reports/', fingerprint: true
            }
        }
        stage('Embedded Test') {
            steps {
                sh 'mvn clean test -P arquillian-glassfish-embedded -B'
                archiveArtifacts artifacts: 'target/surefire-reports/', fingerprint: true
            }
            post {
                always {
                    cucumber target/cucumber-report/*.json'
                }
            }
        }
        stage('Deploy') {
            when {
                branch 'master || develop'
            }
            steps {
                sh 'mvn clean package -B'
                archiveArtifacts artifacts: 'target/kwetter.war', fingerprint: true
            }
        }
    }
}
