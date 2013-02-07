package kaizen.plugins.clr

import kaizen.testing.PluginSpecification

class ClrPluginSpec extends PluginSpecification {

	def 'no providers by default'() {
		given:
		def project = projectWithName('p')

		when:
		project.plugins.apply(ClrPlugin)

		then:
		project.extensions.clr.providers == []
	}
}
