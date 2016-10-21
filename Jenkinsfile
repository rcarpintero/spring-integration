def gitUrl = 'https://github.com/rcarpintero/spring-integration.git'

def release = env.BRANCH_NAME.startsWith('zooplus/')

if (release) {
    node('java8-dind-maven3') {
        stage 'Checkout'
        git url: gitUrl, branch: env.BRANCH_NAME

        stage 'Compile'
        sh './gradlew clean'

        stage 'Unit tests'
        sh './gradlew test'

        stage 'Package'
        sh './gradlew bundle'

        stage 'Publish to Artifactory'
        sh './gradlew publish'
    
    }
}
