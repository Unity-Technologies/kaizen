package kaizen.plugins

import org.gradle.api.Project

class ProjectClassifier {

	static boolean isTest(Project project) {
		project.name.endsWith('.Tests')
	}

	static boolean isBundle(Project project) {
		project.plugins.findPlugin(BundlePlugin)
	}
}
