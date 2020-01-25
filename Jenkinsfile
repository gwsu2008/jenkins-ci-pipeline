@Library(['my-shared-library','jenkins-pipeline-shared-lib-sample'])_

stage('Print Build Info') {
    printBuildinfo {
        name = "Sample Name"
    }
} 


// stage('Disable balancer') {
//     disableBalancerUtils()
// } stage('Deploy') {
//     deploy()
// } stage('Enable balancer') {
//     enableBalancerUtils()
// } stage('Check Status') {
//     checkStatus()
// }

pipeline {
    agent any

    parameters {
        string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')

        text(name: 'BIOGRAPHY', defaultValue: '', description: 'Enter some information about the person')

        booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value')

        choice(name: 'CHOICE', choices: ['One', 'Two', 'Three'], description: 'Pick something')

        password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'Enter a password')
    }

    options { 
        buildDiscarder(logRotator(daysToKeepStr: '1', numToKeepStr: '2'))
        disableConcurrentBuilds()
    }
    
    environment {
        DISABLE_AUTH = 'true'
        DB_ENGINE    = 'sqlite'
    }


    stages {
        stage ('Script') {
            steps {
                script { 
                    logs.info 'Starting'
                    logs.warning 'Nothing to do!'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
            }
        }
        stage('Check Env') {
            steps {
                echo "Database engine is ${DB_ENGINE}"
                echo "DISABLE_AUTH is ${DISABLE_AUTH}"
                sh 'printenv'
            }
        }
        stage('Test') {
            steps {
                sh 'echo "Success!"; exit 0'
            }
        }
        stage('Example') {
            steps {
                echo "Hello ${params.PERSON}"

                echo "Biography: ${params.BIOGRAPHY}"

                echo "Toggle: ${params.TOGGLE}"

                echo "Choice: ${params.CHOICE}"

                echo "Password: ${params.PASSWORD}"
            }
        }
        stage('CheckStatus') {
            steps {
                checkStatus()
            }
        }
        
        stage('Git-Checkout') {
            def config
            config["branch"] = "master"
            config["url"] = "https://github.com/gwsu2008/jenkins-pipeline-shared-lib-sample.git"
            steps {
                gitCheckout(config)
            }
        }
    }

    post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}