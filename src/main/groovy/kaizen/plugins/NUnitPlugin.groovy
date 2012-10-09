package kaizen.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.util.ConfigureUtil

class NUnitPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.extensions.add('nunit', new NUnitExtension(project))
		project.afterEvaluate { configureNUnitDependencies it }
	}

	void configureNUnitDependencies(Project bundle) {
		def nunitVersion = bundle.extensions.nunit.version
		configure(bundle) {
			dependencies {
				tests "NUnit:NUnit:${nunitVersion}"
			}
		}
		bundle.subprojects.findAll { ProjectClassifier.isTest(it) }.each { testProject ->
			configure(testProject) {
				dependencies {
					tests "NUnit:nunit.framework:${nunitVersion}"
				}

				task('test', type: NUnitTask, dependsOn: 'compile') {
					description 'Runs the nunit tests.'
					inputs.file testProject.assemblyPath
					outputs.file file("${testProject.buildDir}/TestResult.xml")
				}
			}
		}
	}

	def configure(Project project, Closure closure) {
		ConfigureUtil.configure(closure, project)
	}
}

class NUnitExtension {
	final Project project
	def version = '2.6+'
	NUnitExtension(Project project) {
		this.project = project
	}
}

class NUnitTask extends DefaultTask {

	@TaskAction
	def runTests() {
		def nunitConsole = project.rootProject.file('libs/Tests/nunit-console.exe')
		def result = project.exec {
			commandLine project.unity.tools.cli.executable
			args nunitConsole
			args '-nologo', '-nodots'
			args "-work=${project.buildDir}"
			args project.assemblyPath
		}
	}

}
