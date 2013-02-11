package kaizen.plugins.nunit

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil
import kaizen.plugins.conventions.Configurations
import kaizen.plugins.assembly.tasks.AssemblyCompile
import org.gradle.api.Task

class NUnitAssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		def masterTestTask = project.task('test') {
			description 'Runs all nunit tests.'
		}
		def bundle = project.rootProject
		bundle.afterEvaluate {
			configureNUnitTasksOn project, masterTestTask
		}
	}

	void configureNUnitTasksOn(Project project, Task masterTestTask) {

		def rootProject = project.rootProject
		NUnitExtension nunit = rootProject.extensions.nunit
		def nunitVersion = nunit.version

		project.afterEvaluate {
			project.configurations.each { config ->
				def configLabel = Configurations.labelFor(config)
				AssemblyCompile compileTask = project.tasks.findByName("compile${configLabel}")
				if (compileTask) {
					project.dependencies.add(config.name, "nunit:nunit.framework:${nunitVersion}")
					configure(project) {
						def outputAssembly = project.file(compileTask.outputAssembly)
						def testConfigTask = task("test$configLabel", type: NUnitTask, dependsOn: [compileTask, rootProject.tasks.updateNUnit]) {
							inputs.file outputAssembly
							outputs.file new File(outputAssembly.parentFile, 'TestResult.xml')
						}
						masterTestTask.dependsOn(testConfigTask)
					}
				}
			}
		}
	}

	def configure(Project project, Closure closure) {
		ConfigureUtil.configure(closure, project)
	}
}


