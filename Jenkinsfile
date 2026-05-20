@Library('java') _
pipelineSimpleMavenJavaProject('marcoshssilva/spring-admin', 'jdk-17',
    [
        'APP_NAME': 'spring-admin',
        'DEPLOY': 'DOKKU',
        'DOKKU_SELECTED_BUILDPACK': 'dockerfile',
        'ENABLE_SONARQUBE_CHECK': 'true',
        'AGENT_EXTRA_LABELS': 'node-builder'
    ])
