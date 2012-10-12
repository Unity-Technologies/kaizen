package kaizen.plugins

import org.gradle.api.Project

class ProjectClassifier {

	static boolean isTest(Project project) {
		project.configurations.findByName(Configurations.TEST)
	}

	static boolean isBundle(Project project) {
		project.plugins.findPlugin(BundlePlugin)
	}

	static boolean isEditorExtension(Project project) {
		project.configurations.findByName(Configurations.EDITOR)
	}
}
