pipeline {
    agent {
        docker {
            image 'maven:3.5.2-jdk-8'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    stages {
        stage('Test') {
            steps {
                configFileProvider([configFile(fileId: 'maven_settings', variable: 'SETTINGS')]) {
                    sh 'mvn -s $SETTINGS clean test  -B'
                }
                stash name: 'test-reports', includes: '**/target/surefire-reports/**,**/target/jacoco.exec'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(execPattern: '**/target/jacoco.exec')
                }
            }
        }
        stage('Embedded Test') {
            steps {
                configFileProvider([configFile(fileId: 'maven_settings', variable: 'SETTINGS')]) {
                    sh 'mvn -s $SETTINGS clean test -P arquillian-glassfish-embedded,!default -B'
                }
                stash name: 'embedded-test-reports', includes: '**/target/surefire-reports/**', '**/target/jacoco-it.exec'
            }
            post {
                always {
                    cucumber '**/target/cucumber-report/*.json'
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(execPattern: '**/target/jacoco-it.exec')
                }
            }
        }
        stage('SonarQube') {
            when {
                branch 'develop'
            }
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
                    sh 'mvn -s $SETTINGS clean deploy -DskipTests -B'
                }
                sshPublisher(publishers: [sshPublisherDesc(configName: '192.168.25.98', transfers: [sshTransfer(excludes: '**', execCommand: 'docker stack deploy kwetter_sven -c /home/dockeruser/sven/stack.yml')])])
            }
        }
    }
}
