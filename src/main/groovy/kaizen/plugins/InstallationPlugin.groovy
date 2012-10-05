package kaizen.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin

class InstallationPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		// ensures temporary files are generated
		// in unity project Temp dir
		project.buildDir = project.file("../../Temp/kaizen")

		project.configure(project) {
			configurations {
				editor
			}
			repositories {
				ivy { url '../../../repository' }
				ivy { url 'http://bamboo.github.com/kaizen/repository/' }
			}
		}

		// load dependencies from bundles file
		def bundlesFile = project.file('bundles.gradle')
		if (bundlesFile.exists()) project.apply(from: bundlesFile)

		project.apply(plugin: LibsPlugin)
	}
}
