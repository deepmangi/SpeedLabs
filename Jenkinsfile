pipeline {
    agent any   // Run on any available Jenkins agent
    tools {
        maven 'M3'      
        jdk 'JDK17'     
    }
    stages {
        stage('Checkout') {
            steps {
                
                checkout scm
            }
        }
        stage('Build & Test') {
            steps {
             
                sh 'mvn clean test'
            }
        }
    }
    post {
        always {
            // Collect test reports (JUnit XML format from Maven Surefire plugin)
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: '**/target/surefire-reports/**', allowEmptyArchive: true
        }
    }
}