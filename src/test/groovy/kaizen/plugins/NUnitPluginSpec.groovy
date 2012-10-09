package kaizen.plugins

import org.gradle.api.Project

class NUnitPluginSpec extends PluginSpecification {

	def bundle = projectWithName('bundle')
	def component = subProjectWithName('C')
	def tests = subProjectWithName('C.Tests')

	@Override
	def setup() {
		bundle.configurations.add('tests')
		bundle.apply plugin: NUnitPlugin
	}

	def 'default nunit version is 2.6+'() {
		expect:
		bundle.nunit.version == '2.6+'
	}

	def 'bundle depends upon nunit-console'() {
		expect:
		bundle.configurations['tests']
	}

	Project subProjectWithName(String name) {
		projectBuilderWithName(name).withParent(bundle).build()
	}
}
