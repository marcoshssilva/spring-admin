@Library(['java@main']) _
pipelineUsingJava17AndMavenWithPublicDockerImage('marcoshssilva/spring-admin',
    [
        'APP_NAME': 'spring-admin',
        'CERT_DOMAIN': 'starlord443.dev',
        'DEPLOY': 'DOKKU',
        'DOKKU_SELECTED_BUILDPACK': 'herokuish', // Options can be 'dockerfile', 'null' and DEFAULT 'herokuish'
        'HOST': 'sp-cloud-admin.starlord443.dev',
        'USE_SSL': true,
        'USE_LETSENCRYPT': true, // available only for branch 'main'
    ],
)