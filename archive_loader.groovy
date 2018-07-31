@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.2' )
import groovyx.net.http.RESTClient
import org.apache.http.entity.*
import hudson.model.*


CliBuilder cli = new CliBuilder(usage: 'groovy task_8.groovy [-h] -n {name_of_artifact} -c {command}')
cli.h(args: 1, argName: 'help', 'Show usage information',required: false)
cli.n(args: 1, argName: 'name_of_artifact', 'Artifact\'s name what created after building', required: true)
cli.c(args: 1, argName: 'command', 'push/pull a file', required: true)

argument = cli.parse(args)
if (!argument) or (argument.h) {
    cli.usage()
    return
}

server = "http://nexus/"
repo = "My-release"
groupId = "Jenkins"
username = 'Jenkins'
password = 'Yauheni1601'

name = argument.n.split("/")[-1].split(".tar")[-2]
println(name)
artifactId = name.split('-build')[-2]
version = 'build-' + name.tokenize('-build')[-1]
println("artId - ${artifactId}, ver - ${version}")
command = [push: {->upload()}, pull: {->download()}]

request = new RESTClient(server)
request.auth.basic("${username}", "${password}")


def encodeZipFile( Object data ) throws UnsupportedEncodingException {
    def entity = new FileEntity( (File) data, "application/zip" )
    entity.setContentType( "application/zip" )
    return entity
}

def upload() {
    try {
        request.encoder.'application/zip' = this.&encodeZipFile
        respons_up = request.put(
                uri: "${server}repository/${repo}/${groupId}/${artifactId}/${version}/${name}.tar.gz",
                body: new File(argument.n),
                requestContentType: 'application/zip'
        )
        assert respons_up.status == 201


    }
    catch (Exception ex) {
        println ex.getMessage()
    }
}

def download() {
    try {
        respons_down = request.get(
                uri: "${server}repository/${repo}/${groupId}/${artifactId}/${version}/${name}.tar.gz")
        new File("/home/student/Downloads/${artifactId}-${version}.tar.gz") << respons_down.data
        assert respons_down.status == 200
    }
    catch (Exception ex) {
        println ex.getMessage()
    }
}

if (argument.c) {
    command[argument.c]()
}
