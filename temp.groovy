@Grab(group='org.codehaus.groovy', module='groovy-all', version='2.5.1', type='pom' )

import groovy.json.JsonOutput
import org.sonatype.nexus.repository.storage.Component
import org.sonatype.nexus.repository.storage.Query
import org.sonatype.nexus.repository.storage.StorageFacet

//def repositoryId = args.split(',')[0]
//def groupId = args.split(',')[1]
//def artifactId = args.split(',')[2];
//def baseVersion = args.split(',')[3];
//def latestOnly = args.split(',')[4];

def repositoryId = "My-release"
def groupId = "Jenkins"
def artifactId = "task-8-first-step"
def baseVersion = "build-20"
//def latestOnly = args.split(',')[4];

def repo = repository.repositoryManager.get(repositoryId)
println("repo - ${repo}")
StorageFacet storageFacet = repo.facet(StorageFacet)
def tx = storageFacet.txSupplier().get()

tx.begin()
def components = tx.findComponents(Query.builder().where('group = ').param(groupId).and('name = ').param(artifactId).build(), [repo])
print(components)
//def found = components.findAll{it.attributes().child('maven2').get('baseVersion')==baseVersion}.collect()
//def version = it.attributes().child('maven2').get('version');\"${version}\"}
//
//// found = found.unique().sort();
//def latest = found.isEmpty() ? found : found.last()
//
//tx.commit()
//def result = latestOnly == 'latest' ? JsonOutput.toJson(latest) : JsonOutput.toJson(found)
//    return result
