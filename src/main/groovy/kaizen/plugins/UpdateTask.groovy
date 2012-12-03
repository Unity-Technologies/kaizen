package kaizen.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction
import kaizen.plugins.core.Configurations

class UpdateTask extends DefaultTask {

	def outputDir
	private Configuration configuration

	def setConfiguration(Configuration c) {
		configuration = c
		outputDir = "lib/${Configurations.labelFor(configuration)}"

		inputs.source(configuration)
		outputs.dir(outputDir)
	}

	def getConfiguration() {
		configuration
	}

	@TaskAction
	void update() {
		def localArtifacts = project.subprojects.collectMany { it.configurations }.collectMany { it.artifacts.files.collect() }
		def remoteArtifacts = configuration.incoming.files.findAll { !localArtifacts.contains(it) }
		remoteArtifacts.each { file ->
			logger.info "Unpacking ${file.name} into $outputDir"
			project.copy {
				from project.zipTree(file)
				into project.file(outputDir)
				include '**/*'
			}
		}
	}
}
