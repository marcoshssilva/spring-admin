@Library(['java@main']) _
pipelineUsingJava17AndMavenWithPublicDockerImage('marcoshssilva/spring-admin',
    [
        'APP_NAME': 'spring-admin',
        'CERT_DOMAIN': 'starlord443.dev',
        'DEPLOY': 'DOKKU',
        'DOKKU_SELECTED_BUILDPACK': 'herokuish', // Options can be 'dockerfile', 'null' and DEFAULT 'herokuish'
        'HOST': 'spring-admin.starlord443.dev',
    ],
)
