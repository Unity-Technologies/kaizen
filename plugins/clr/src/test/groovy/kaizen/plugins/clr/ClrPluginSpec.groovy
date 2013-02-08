package kaizen.plugins.clr

import kaizen.testing.PluginSpecification

class ClrPluginSpec extends PluginSpecification {

	def project = projectWithName('p')

	@Override
	def setup() {
		project.plugins.apply(ClrPlugin)
	}

	def 'no providers by default'() {
		expect:
		clr.providers.empty
	}

	def 'no compilers by default'() {
		expect:
		clr.compilers.empty
	}

	ClrExtension getClr() {
		project.extensions.clr
	}

}
