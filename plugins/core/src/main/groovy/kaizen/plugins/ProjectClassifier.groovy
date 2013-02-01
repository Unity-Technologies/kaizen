package kaizen.plugins

import org.gradle.api.Project

class ProjectClassifier {
	static boolean isBundle(Project project) {
		project.plugins.findPlugin(BundlePlugin)
	}
}
