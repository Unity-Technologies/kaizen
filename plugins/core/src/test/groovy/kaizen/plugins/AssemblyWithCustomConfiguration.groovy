package kaizen.plugins

import kaizen.plugins.assembly.AssemblyPlugin
import kaizen.testing.PluginSpecification

class AssemblyWithCustomConfiguration extends PluginSpecification {

	def 'has no additional configurations contributed'() {
		given:
		def project = projectWithName('p')
		project.configurations.add('c')

		when:
		project.apply plugin: AssemblyPlugin

		then:
		setOf(project.configurations*.name) == setOf(['c'] + configurationsContributedByBasePlugin())
	}

	def configurationsContributedByBasePlugin() {
		BasePluginInfo.configurationsContributedByBasePlugin()
	}

	def setOf(List<String> strings) {
		strings.toSet()
	}
}

