package kaizen.plugins

import kaizen.plugins.core.Configurations
import kaizen.plugins.assembly.AssemblyPlugin

class AssemblyPluginSpec extends PluginSpecification {

	def project = projectWithName('C')

	@Override
	def setup() {
		project.apply plugin: AssemblyPlugin
	}

	def 'assembly name is derived from project name'() {
		expect:
		project.assembly.fileName == "C.dll"
	}
}