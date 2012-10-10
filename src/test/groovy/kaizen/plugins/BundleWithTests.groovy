package kaizen.plugins

import org.gradle.api.Project

class BundleWithTests extends PluginSpecification {

	def bundle = projectWithName('bundle')
	def p1 = subProjectWithName('p1')
	def p1Tests = subProjectWithName('p1.Tests')
	def p2 = subProjectWithName('p2')
	def p2Tests = subProjectWithName('p2.Tests')

	@Override
	void setup() {
		bundle.apply plugin: BundlePlugin
	}

	def 'editor configuration depends on every sub project except tests'() {
		expect:
		projectsDependedUponBy('editor') == [p1, p2]
	}

	def 'test configuration depends on every test sub project'() {
		expect:
		projectsDependedUponBy('tests') == [p1Tests, p2Tests]
	}

	def 'test projects automatically depend on tested projects'() {
		expect:
		projectsDependedUponBy(p1Tests, 'tests') == [p1]
		projectsDependedUponBy(p2Tests, 'tests') == [p2]
	}

	def projectsDependedUponBy(String config) {
		projectsDependedUponBy(bundle, config)
	}

	def projectsDependedUponBy(Project project, String config) {
		project.configurations[config].dependencies.collect { it.dependencyProject }
	}

	Project subProjectWithName(String name) {
		projectBuilderWithName(name).withParent(bundle).build()
	}
}
