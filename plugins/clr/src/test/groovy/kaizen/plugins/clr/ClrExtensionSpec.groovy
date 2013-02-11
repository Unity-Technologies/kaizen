package kaizen.plugins.clr

import kaizen.testing.PluginSpecification

class ClrExtensionSpec extends PluginSpecification {

	def 'forProject returns null for no clr'() {
		expect:
		null == ClrExtension.forProject(projectWithName('p'))
	}
}
