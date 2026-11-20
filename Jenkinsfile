pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Этап Build: компиляция'
                bat 'mvn clean compile -Dcheckstyle.skip=true'
            }
        }
        stage('Static Analysis') {
            steps {
                echo 'Этап Static Analysis: проверка кода Checkstyle'
                bat 'mvn checkstyle:check'
            }
        }
        stage('Test') {
            steps {
                echo 'Этап Test: запуск тестов'
                bat 'mvn test'
            }
        }
        stage('Package') {
            steps {
                echo 'Этап Package: упаковка в JAR'
                bat 'mvn package -DskipTests'
            }
        }
        stage('Verify JAR') {
            when {
                expression { fileExists('target/bicycle-rental.jar') }
            }
            steps {
                echo 'Проверяем наличие JAR...'
                bat 'dir target\\bicycle-rental.jar'
            }
        }
    }

    post {
        success {
            echo 'Pipeline завершён успешно: SUCCESS'
        }
        failure {
            echo 'Pipeline упал: FAILURE'
        }
    }
}