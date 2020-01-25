@Library(['my-shared-library','jenkins-pipeline-shared-lib-sample'])_

stage('printBuildinfo') {
    printBuildinfo {
        name = "Sample Name"
    }
}

def config = [:]
config["branch"] = "master"
config["url"] = "https://github.com/gwsu2008/jenkins-pipeline-shared-lib-sample.git"


node {
    timestamps {
        try {
            ansiColor('xterm') {
                // Just some echoes to show the ANSI color.
                stage "\u001B[31mI'm Red\u001B[0m Now not"
            }

            parameters {
                string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
                text(name: 'BIOGRAPHY', defaultValue: '', description: 'Enter some information about the person')
                booleanParam(name: 'TOGGLE', defaultValue: true, description: 'Toggle this value')
                choice(name: 'CHOICE', choices: ['One', 'Two', 'Three'], description: 'Pick something')
                password(name: 'PASSWORD', defaultValue: 'SECRET', description: 'Enter a password')
            }

            properties ([ 
            buildDiscarder(logRotator(daysToKeepStr: '1', numToKeepStr: '5')),
            disableConcurrentBuilds()
            ])

            stage ('Script') {
                script { 
                    logs.info 'Starting'
                    logs.warning 'Nothing to do!'
                }
            }

            stage('Build') {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
            }

            withEnv(['DISABLE_AUTH=true',
                'DB_ENGINE=sqlite']) {
                stage('Check Env') {
                    echo "Database engine is ${DB_ENGINE}"
                    echo "DISABLE_AUTH is ${DISABLE_AUTH}"
                    sh 'printenv'
                }
            }

            stage('Test') {
                sh 'echo "Success!"; exit 0'
            }

            stage('Example') {
                echo "Hello ${params.PERSON}"

                echo "Biography: ${params.BIOGRAPHY}"

                echo "Toggle: ${params.TOGGLE}"

                echo "Choice: ${params.CHOICE}"

                echo "Password: ${params.PASSWORD}"
            
            }

            stage('CheckStatus') {
                checkStatus()
            }
        
            stage('Git-Checkout') {
                gitCheckout(config)
            }
        } catch (Exception e) {
            currentBuild.result = 'FAILURE'
            throw e
        }
        if (currentBuild.result == 'UNSTABLE') {
            echo 'I am UNSTABLE :/'
        } else if (currentBuild.result == 'FAILURE') {
            echo 'I am FAILURE :/'
        else {
            echo 'One way or another, I have finished'
        }
    }
}