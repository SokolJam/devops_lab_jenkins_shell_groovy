import hudson.model.FreeStyleProject
import hudson.tasks.Shell
import jenkins.model.Jenkins
import hudson.plugins.git.GitSCM
import hudson.plugins.ws_cleanup.*

repo = "https://github.com/SokolJam/lab.git"

job = Jenkins.instance.createProject(FreeStyleProject, "MNT-CD-module9-extcreated-job")
job.buildersList.add(new Shell('echo "hello world"'))

Scm = new GitSCM(repo)
Scm.branches = [new hudson.plugins.git.BranchSpec("*/jenkins_09")]
job.scm = Scm
job.setDescription("Unit 9 Task 2: groovy script which should create Freestyle job")
job.buildWrappersList.add(new PreBuildCleanup(null, true, "", ""))
job.save()

build = job.scheduleBuild2(5, new hudson.model.Cause.UserIdCause())
build.get()