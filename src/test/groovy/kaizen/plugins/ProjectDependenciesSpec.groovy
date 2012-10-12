package kaizen.plugins

import org.gradle.api.Project

class ProjectDependenciesSpec extends BundleSpecification {

	def 'projectsDependedUponBy does NOT include transitive dependencies'() {
		given:
		def p1 = subProjectWithDefaultConfigAndName('p1')
		def p2 = subProjectWithDefaultConfigAndName('p2')
		def p3 = subProjectWithDefaultConfigAndName('p3')

		when:
		dependsOn(p2, p1)
		dependsOn(p3, p2)

		then:
		projectsDependedUponByDefaultConfigOf(p3) == [p2]
	}

	def projectsDependedUponByDefaultConfigOf(Project project) {
		ProjectDependencies.projectsDependedUponBy(project.configurations[DEFAULT_CONFIG])
	}

	def subProjectWithDefaultConfigAndName(String name) {
		def project = subProjectWithName(name)
		project.configurations.add(DEFAULT_CONFIG)
		project
	}

	def dependsOn(Project p2, Project p1) {
		p2.dependencies.add(DEFAULT_CONFIG, p2.dependencies.project(path: p1.path))
	}

	private static final String DEFAULT_CONFIG = 'default'

}
