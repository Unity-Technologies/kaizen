package kaizen.plugins.nunit

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil
import kaizen.plugins.core.Configurations
import kaizen.plugins.assembly.AssemblyCompileTask
import org.gradle.api.Task

class NUnitAssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		def masterTestTask = project.task('test')
		def bundle = project.rootProject
		bundle.afterEvaluate {
			configureNUnitTasksOn project, masterTestTask
		}
	}

	void configureNUnitTasksOn(Project project, Task masterTestTask) {

		NUnitExtension nunit = project.rootProject.extensions.nunit
		def nunitVersion = nunit.version

		project.configurations.each { config ->
			def configLabel = Configurations.labelFor(config)
			AssemblyCompileTask compileTask = project.tasks.findByName("compile${configLabel}")
			if (compileTask) {
				project.dependencies.add(config.name, "nunit:nunit.framework:${nunitVersion}")
				configure(project) {
					def testConfigTask = task("test$configLabel", type: NUnitTask, dependsOn: compileTask) {
						description 'Runs the nunit tests.'
						inputs.file compileTask.assemblyFile
						outputs.file new File(compileTask.resolvedOutputDir, 'TestResult.xml')
					}
					masterTestTask.dependsOn(testConfigTask)
				}
			}
		}
	}

	def configure(Project project, Closure closure) {
		ConfigureUtil.configure(closure, project)
	}
}


