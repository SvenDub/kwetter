pipeline {
    agent {
        docker {
            image 'maven:3.5.2-jdk-8'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile -B'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn clean test  -B'
                stash name: 'test-reports', includes: 'target/surefire-reports/**,target/jacoco.exec'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco(execPattern: 'target/jacoco.exec')
                }
            }
        }
        stage('Embedded Test') {
            steps {
                sh 'mvn clean test -P arquillian-glassfish-embedded -B'
                stash name: 'embedded-test-reports', includes: 'target/surefire-reports/**'
            }
            post {
                always {
                    cucumber 'target/cucumber-report/*.json'
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('SonarQube') {
            steps {
                configFileProvider([configFile(fileId: 'maven_settings', variable: 'SETTINGS')]) {
                    sh 'mvn -s $SETTINGS clean compile -B'
                    unstash 'test-reports'
                    unstash 'embedded-test-reports'
                    sh 'mvn -s $SETTINGS sonar:sonar -B'
                }
            }
        }
        stage('Deploy') {
            when {
                anyOf {
                    branch 'master'
                    branch 'develop'
                    branch 'feature/jenkins'
                }
            }
            steps {
                configFileProvider([configFile(fileId: 'maven_settings', variable: 'SETTINGS')]) {
                    sh 'mvn -s $SETTINGS clean package -DskipTests -B'
                }
            }
        }
    }
}