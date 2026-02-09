// Блок параметров для настройки запуска
properties([
    parameters([
        // Параметр для указания тегов тестов
        string(name: 'TAGS',
               defaultValue: 'smoke',
               description: 'Test tags to run: smoke, regress, api, etc.')
    ])
])

pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Получаем код из репозитория
                checkout scm
                // Делаем gradlew исполняемым для Unix-систем
                sh 'chmod +x gradlew'
            }
        }

        stage('Run Tests') {
            steps {
                // Запускаем тесты с указанным тегом из параметра
                sh "./gradlew clean test -Ptags='${params.TAGS}'"
            }
        }
    }

    post {
        always {
            // Всегда генерируем Allure отчёт, даже если тесты упали
            allure includeProperties: false,
                   results: [[path: 'build/allure-results']]
        }
    }
}