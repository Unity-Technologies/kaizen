package kaizen.plugins

import spock.lang.Specification
import org.gradle.testfixtures.ProjectBuilder

class ProjectClassifierSpec extends Specification {

	def project = ProjectBuilder.builder().build()

	def 'isBundle returns false for non bundles'() {
		expect:
		!ProjectClassifier.isBundle(project)
	}

	def 'isBundle returns true for bundles'() {
		when:
		project.apply plugin: BundlePlugin

		then:
		ProjectClassifier.isBundle(project)
	}
}
