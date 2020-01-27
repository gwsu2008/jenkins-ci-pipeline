@Library(['my-shared-library','jenkins-pipeline-shared-lib-sample'])_

stage('printBuildinfo') {
    printBuildinfo {
        name = "Sample Name"
    }
}

def config = [:]
config["branch"] = "master"
config["url"] = "https://github.com/gwsu2008/jenkins-pipeline-shared-lib-sample.git"


node ('master') {
    timestamps {
        try {
            stage('Cleanup workspace') {
                step([$class: 'WsCleanup'])
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
                test_config = [:]
                test_config["branch"] = "master"
                test_config["url"] = "https://github.com/gwsu2008/jenkins-ci-pipeline.git"
                gitCheckout(test_config)
                def workDir = "${WORKSPACE}"
                commitId = utils.readCommitId(workDir)
                println "Commit ID: " + commitId
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
        } finally {
            def currentResult = currentBuild.result ?: 'SUCCESS'
            def previousResult = currentBuild.getPreviousBuild()?.result
            if (currentResult == 'UNSTABLE') {
                echo 'This will run only if the run was marked as UNSTABLE'
            }
            if (currentResult == 'FAILURE') {
                echo 'This will run only if the run was marked as FAILURE'
            }
            if (previousResult != null && previousResult != currentResult) {
                echo 'This will run only if the state of the Pipeline has changed'
                echo 'For example, if the Pipeline was previously failing but is now successful'
            }
            echo 'This will always run'
        }
    }
}