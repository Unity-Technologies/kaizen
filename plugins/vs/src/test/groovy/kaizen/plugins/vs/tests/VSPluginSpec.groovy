package kaizen.plugins.vs.tests

import kaizen.plugins.assembly.AssemblyPlugin
import kaizen.plugins.vs.VS2010Plugin
import kaizen.testing.PluginSpecification

class VSPluginSpec extends PluginSpecification {

	def 'vs plugin doesn`t imply assembly'() {
		given:
		def project = projectWithName('p')

		when:
		project.plugins.apply VS2010Plugin

		then:
		project.plugins.findPlugin(AssemblyPlugin) == null
	}
}
