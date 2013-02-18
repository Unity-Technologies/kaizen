package kaizen.plugins.assembly

import kaizen.plugins.assembly.tasks.AssemblyCompile
import kaizen.plugins.conventions.Configurations
import kaizen.testing.PluginSpecification
import org.gradle.api.Project
import spock.lang.Unroll

class AssemblyPluginCompileTasksSpec extends PluginSpecification {

	static final def CompilableConfigurationNames = ['default', 'net4']

	def project = newProject()

	@Override
	def setup() {
		project.plugins.apply AssemblyPlugin
		project.configurations.add 'net4'
	}

	@Unroll
	def 'output file for #configName goes to build/#configName'() {
		when:
		evaluateProject project

		then:
		compileTaskForConfig(configName).outputAssembly == file("build/${labelFor(configName)}/${project.name}.dll")

		where:
		configName << CompilableConfigurationNames
	}

	@Unroll
	def 'compile task for #configName configuration depends on copy #configName dependencies'() {
		when:
		evaluateProject project

		then:
		compileTaskForConfig(configName).dependsOn.contains(copyDependenciesTaskForConfig(configName))

		where:
		configName << CompilableConfigurationNames
	}

	@Unroll
	def 'compileTask.targetFrameworkVersion is set to #targetFramework to match assembly.targetFrameworkVersion'() {
		when:
		configure(project) {
			assembly {
				targetFrameworkVersion = targetFramework
			}
		}
		evaluateProject project

		then:
		compileTaskForConfig(configName).targetFrameworkVersion == targetFramework

		where:
		configName << CompilableConfigurationNames
		targetFramework << ['v3.5', 'v4.0']
	}

	def 'configuration dependencies become assembly references on the corresponding tasks'() {
		given:
		def a = newAssemblySubProject('a')
		def b = newAssemblySubProject('b')
		def c = newAssemblySubProject('c')

		when:
		configure(c) {
			configurations {
				net2
				net4
			}
			dependencies {
				net2 project(':a')
				net4 project(':b')
			}
		}
		[project, a, b, c].each {
			evaluateProject it
		}

		then:
		def compileNet2 = compileTaskForProjectAndConfig(c, 'net2')
		compileNet2.assemblyReferences == [c.file("build/${labelFor('net2')}/${a.name}.dll").canonicalPath]

		def compileNet4 = compileTaskForProjectAndConfig(c, 'net4')
		compileNet4.assemblyReferences == [c.file("build/${labelFor('net4')}/${b.name}.dll").canonicalPath]
	}

	def newAssemblySubProject(String name) {
		def subProject = projectBuilderWithName(name).withParent(project).build()
		subProject.plugins.apply AssemblyPlugin
		subProject
	}

	def compileTaskForConfig(String name) {
		compileTaskForProjectAndConfig(project, name)
	}

	def copyDependenciesTaskForConfig(String configName) {
		project.tasks.getByName("copy${labelFor(configName)}Dependencies")
	}

	def compileTaskForProjectAndConfig(Project project, String name) {
		compileTasksOf(project).getByName(compileTaskNameForConfigName(name))
	}

	def compileTaskNameForConfigName(String name) {
		"compile${labelFor(name)}"
	}

	def labelFor(String name) {
		Configurations.labelFor(name)
	}

	File file(GString fileName) {
		project.file(fileName)
	}

	def compileTasksOf(Project project) {
		project.tasks.withType(AssemblyCompile)
	}
}
