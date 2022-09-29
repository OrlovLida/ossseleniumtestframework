properties([
        [$class: 'GitLabConnectionProperty', gitLabConnection: 'GitlabMakumba'],
        disableConcurrentBuilds()
])

def sendEmail(){
    script {
            if(BRANCH_NAME == "master"){
            def emailReportRecipients = 'Gabriela.Zaranek@comarch.pl, Krzysztof.Pogonowski@comarch.pl'
                step([
                    $class                  : 'Mailer',
                    notifyEveryUnstableBuild: true,
                    recipients              : emailReportRecipients + emailextrecipients([
                            [$class: 'CulpritsRecipientProvider'],
                            [$class: 'RequesterRecipientProvider']
                    ]),
                    sendToIndividuals       : true
                ])
            }
        }
}

pipeline {
    agent any
    triggers {
        gitlab(triggerOnPush: true)
    }
    options {
        skipDefaultCheckout(true)
    }
    stages {
	
        stage('Checkout Git') {
			steps {
				deleteDir()
				checkout scm
			}
        }

        stage('Compile and Unit Tests') {
            steps {
				configFileProvider([configFile(fileId: '4bcc5ae5-ad71-4c46-a4d7-7f48e45e341c', variable: 'MAVEN_SETTINGS')]) {
					sh 'mvn -T 6 clean install -U -f $WORKSPACE/pom.xml -s $MAVEN_SETTINGS ${PROFILE}'
				}
            }
        }
    }
    
    post {
        failure {
            sendEmail()
        }
        unstable {
            sendEmail()
        }
        fixed {
            sendEmail()
        }
    }
}
