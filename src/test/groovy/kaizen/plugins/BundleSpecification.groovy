package kaizen.plugins

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

abstract class BundleSpecification extends PluginSpecification {

	def bundle = projectWithName('bundle')

	Project subProjectWithName(String name) {
		projectBuilderWithName(name).withParent(bundle).build()
	}

	def projectsDependedUponBy(String config) {
		projectsDependedUponBy(bundle, config)
	}

	def projectsDependedUponBy(Project project, String config) {
		project.configurations[config].dependencies.collect { it.dependencyProject }
	}

	void evaluateBundle() {
		bundle.allprojects.each {
			(it as ProjectInternal).evaluate()
		}
	}

}
