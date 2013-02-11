package kaizen.plugins.clr

import kaizen.testing.PluginSpecification
import org.gradle.testfixtures.ProjectBuilder

class ClrPluginSpec extends PluginSpecification {

	def project = projectWithName('p')

	@Override
	def setup() {
		project.plugins.apply ClrPlugin
	}

	def 'no providers by default'() {
		expect:
		clr.providers.empty
	}

	def 'no compilers by default'() {
		expect:
		clr.compilers.empty
	}

	def 'child project uses Clr from parent'() {
		given:
		def child = ProjectBuilder.builder().withParent(project).build()

		expect:
		ClrExtension.forProject(child) == clr
	}

	ClrExtension getClr() {
		ClrExtension.forProject(project)
	}
}
