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

	def 'isTest returns true for projects with a test configuration'() {
		when:
		project.configurations.add(Configurations.TEST)

		then:
		ProjectClassifier.isTest(project)
	}

	def 'isTest returns false for projects without a test configuration'() {
		expect:
		!ProjectClassifier.isTest(project)
	}

	def 'isEditorExtension returns true for projects with an editor configuration'() {
		when:
		project.configurations.add(Configurations.EDITOR)

		then:
		ProjectClassifier.isEditorExtension(project)
	}

	def 'isEditorExtension returns false for projects without an editor configuration'() {
		expect:
		!ProjectClassifier.isEditorExtension(project)
	}
}
