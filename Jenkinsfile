properties([
        [$class: 'GitLabConnectionProperty', gitLabConnection: 'GitlabMakumba'],
        disableConcurrentBuilds()
])
String cron_string = BRANCH_NAME == "master" ? "0 22 * * *" : ""

pipeline {
    agent any
    triggers {
        cron(cron_string)
    }
    options {
        skipDefaultCheckout(true)
    }
    stages {
        stage('Checkout Git') {
            steps {
                script {
                    currentBuild.result = 'SUCCESS'
                    deleteDir()
                    checkout scm
                }
            }
        }
        stage('Compile sources') {
            steps {
                script {
                if(BRANCH_NAME == "master"){
                        configFileProvider([configFile(fileId: '4bcc5ae5-ad71-4c46-a4d7-7f48e45e341c', variable: 'MAVEN_SETTINGS')]) {
                            sh 'mvn -T 6 clean deploy -U -f $WORKSPACE/pom.xml -s $MAVEN_SETTINGS -P legacy'
                        }
                    }
                else {
                    configFileProvider([configFile(fileId: '4bcc5ae5-ad71-4c46-a4d7-7f48e45e341c', variable: 'MAVEN_SETTINGS')]) {
                                                sh 'mvn -T 6 clean compile -U -f $WORKSPACE/pom.xml -s $MAVEN_SETTINGS -P legacy'
                    }
                }
            }
        }
    }
}
}
