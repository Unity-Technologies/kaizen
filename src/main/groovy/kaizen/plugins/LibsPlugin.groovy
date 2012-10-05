package kaizen.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class LibsPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		def config = project.configurations['editor']
		def configName = config.name.capitalize()
		def outputDir = project.file("libs/$configName")

		def update = project.tasks.add('update') {
			description "Updates 'libs/$configName'."
			doLast {
				def localArtifacts = project.subprojects.collect {
					it.configurations[config.name].artifacts.files
				}
				config.incoming.files.each { file ->
					if (localArtifacts.any { it.contains(file) }) {
						return
					}
					println file.name
					project.copy {
						from project.zipTree(file)
						into outputDir
						include "*.dll"
						include "*.xml"
					}
				}
			}
		}
	}
}
