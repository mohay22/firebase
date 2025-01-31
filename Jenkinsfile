pipeline {
    agent any

    environment {
        ANDROID_HOME = '/path/to/your/android/sdk' // Set path to Android SDK
        GRADLE_OPTS = "-Dorg.gradle.daemon=false" // Optional for disabling Gradle daemon in CI
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-username/your-repo.git' // Use your repo URL
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build' // Runs Gradle build command
            }
        }

        stage('Run Tests') {
            steps {
                sh './gradlew test' // Runs unit tests
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...' // Optional deployment steps can be added here
            }
        }
    }

    post {
        always {
            cleanWs() // Clean workspace after the build
        }
        success {
            echo 'Build and tests completed successfully!'
        }
        failure {
            echo 'Build or tests failed!'
        }
    }
}
