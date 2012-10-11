package kaizen.plugins

import org.gradle.api.Project

abstract class BundleSpecification extends PluginSpecification {

	def bundle = projectWithName('bundle')

	Project subProjectWithName(String name) {
		projectBuilderWithName(name).withParent(bundle).build()
	}
}
