package kaizen.plugins

class AssemblyPluginSpecification extends PluginSpecification {

	def project = projectWithName('C')

	@Override
	def setup() {
		project.apply plugin: AssemblyPlugin
	}

	def 'assembly name is derived from project name'() {
		expect:
		project.assemblyFileName == "C.dll"
	}

	def 'editor configuration is present'() {
		expect:
		project.configurations.editor != null
	}
}