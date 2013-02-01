package kaizen.plugins

import kaizen.testing.PluginSpecification
import org.gradle.api.Project
import kaizen.plugins.nunit.NUnitPlugin
import org.gradle.api.internal.project.ProjectInternal

class NUnitPluginSpec extends PluginSpecification {

	def bundle = projectWithName('bundle')
	def component = subProjectWithName('C')
	def tests = subProjectWithName('C.Tests')

	@Override
	def setup() {
		bundle.apply plugin: NUnitPlugin
	}

	def 'default nunit version is 2.6+'() {
		expect:
		bundle.nunit.version == '2.6+'
	}

	def 'NUnit configuration depends upon nunit-console'() {
		when:
		(bundle as ProjectInternal).evaluate()
		then:
		bundle.configurations['NUnit'].dependencies.collect { "$it.group:$it.name:$it.version" } == ['nunit:nunit-console:2.6+']
	}

	Project subProjectWithName(String name) {
		projectBuilderWithName(name).withParent(bundle).build()
	}
}
