@Library('jenkins-shared-libs') _
def config = [ appName: 'tomcat-cache-realm',
               podName: 'java-8-maven-3.5.2.yaml',
               containerName: 'jdk-8-maven',
               runUnitTests: true,
               runIntegrationTests: false
]
javaPipeline(config)
