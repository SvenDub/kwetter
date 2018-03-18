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
            }
        }
        stage('Embedded Test') {
            steps {
                sh 'mvn clean test -P arquillian-glassfish-embedded -B'
            }
            post {
                always {
                    cucumber 'target/cucumber-report/*.json'
                }
            }
        }
        stage('SonarQube') {
            steps {
                configFileProvider([configFile(fileId: 'maven_settings', variable: 'SETTINGS')]) {
                    sh 'mvn -s $SETTINGS clean compile sonar:sonar -B'
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
                    sh 'mvn -s $SETTINGS clean package deploy -DskipTests -B'
                }
            }
        }
    }
}