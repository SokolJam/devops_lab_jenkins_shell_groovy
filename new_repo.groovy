import org.sonatype.nexus.repository.config.Configuration

def configuration = new Configuration()
configuration.setAttributes(
        'maven': [
                'versionPolicy': 'RELEASE',
                'layoutPolicy': 'STRICT'
        ],
        'proxy': [
                'remoteUrl': 'https://repo.maven.apache.org/maven2/',
                'contentMaxAge': 1440,
                'metadataMaxAge': 1440
        ],
        'httpclient': [
                'blocked': false,
                'autoBlock': true,
                'authentication': [
                        'type': 'username',
                        'username': 'Jenkins',
                        'password': 'Yauheni1601',
                        'ntlmHost': '',
                        'ntlmDomain': ''
                ]
        ],
        'storage': [
                'blobStoreName': 'default',
                'strictContentTypeValidation': true
        ]
)
configuration.setRepositoryName('My-repo2')
configuration.setRecipeName('maven2-proxy')
configuration.setOnline(true)

def repositoryManager = container.lookup(org.sonatype.nexus.repository.manager.RepositoryManager.class.name)
repositoryManager.create(configuration)
