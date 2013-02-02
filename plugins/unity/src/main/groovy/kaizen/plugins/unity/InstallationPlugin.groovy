package kaizen.plugins.unity

import org.gradle.api.Project
import org.gradle.api.Plugin

class InstallationPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		// ensures temporary files are generated
		// in unity project Temp dir
		project.buildDir = project.file("../../Temp/kaizen")
		project.configurations.add('editor')

		applyStartupFiles(project)

		project.apply(plugin: kaizen.plugins.LibClientPlugin)
	}

	// loads all files from kaizen.d
	private void applyStartupFiles(Project project) {
		def startupDir = project.file('kaizen.d')
		if (!startupDir.isDirectory()) {
			project.logger.warn('kaizen.d is not a directory. No startup scripts will be loaded.')
			return
		}
		gradleFilesIn(startupDir).each {
			project.apply from: it
		}
	}

	private File[] gradleFilesIn(File startupDir) {
		startupDir.listFiles({ dir, name -> name.endsWith('.gradle') } as FilenameFilter)
	}
}
