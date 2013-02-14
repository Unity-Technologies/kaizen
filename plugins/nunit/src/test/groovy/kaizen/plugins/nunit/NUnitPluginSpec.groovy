package kaizen.plugins.nunit

import kaizen.testing.PluginSpecification
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

class NUnitPluginSpec extends PluginSpecification {

	def project = projectWithName('p')

	@Override
	def setup() {
		project.plugins.apply(NUnitPlugin)
	}

	def 'NUnit configuration depends upon nunit-console'() {
		when:
		evaluateProject(project)
		then:
		project.configurations['NUnit'].dependencies.collect { "$it.group:$it.name:$it.version" } == ['nunit:nunit-console:2.6+']
	}
}
