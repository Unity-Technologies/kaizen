package kaizen.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

class LibsPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		def configurations = project.configurations
		def editorConfig = configurations['editor']
		def testsConfig = configurations.findByName('tests')
		def availableConfigs = testsConfig ? [editorConfig, testsConfig] : [editorConfig]

		availableConfigs.each { addUpdateTaskForConfig(it, project) }

		project.tasks.add(
			name: 'update',
			dependsOn: availableConfigs.collect { updateTaskNameFor(it) },
			description: 'Updates all libs')
	}

	private void addUpdateTaskForConfig(Configuration config, Project project) {
		def configName = capitalizedNameOf(config)

		def outputDir = project.file("libs/$configName")
		def update = project.tasks.add(updateTaskNameFor(config)) {
			description "Updates 'libs/$configName'."
			doLast {
				def localArtifacts = project.subprojects.collectMany {
					it.configurations.collectMany { it.artifacts.files.collect() }
				}
				def remoteArtifacts = config.incoming.files.findAll { !localArtifacts.contains(it) }
				remoteArtifacts.each { file ->
					println file.name
					project.copy {
						from project.zipTree(file)
						into outputDir
						include "*.exe"
						include "*.dll"
						include "*.xml"
					}
				}
			}
		}
	}

	private String updateTaskNameFor(Configuration config) {
		"update${capitalizedNameOf(config)}"
	}

	private String capitalizedNameOf(Configuration config) {
		config.name.capitalize()
	}
}
