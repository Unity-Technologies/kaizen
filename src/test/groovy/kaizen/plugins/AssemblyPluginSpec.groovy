package kaizen.plugins

class AssemblyPluginSpec extends PluginSpecification {

	def project = projectWithName('C')

	@Override
	def setup() {
		project.apply plugin: AssemblyPlugin
	}

	def 'assembly file goes to build dir'() {
		expect:
		project.buildDir == project.assembly.file.parentFile
	}

	def 'assembly name is derived from project name'() {
		expect:
		project.assembly.file.name == "C.dll"
	}

	def 'editor configuration is present'() {
		expect:
		project.configurations.editor != null
	}
}