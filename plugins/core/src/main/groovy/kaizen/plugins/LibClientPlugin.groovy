package kaizen.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class LibClientPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		def updateAllTask = project.tasks.add('update') {
			description 'Downloads and unpacks all dependencies.'
		}

		def createUpdateTaskForConfig = { config ->
			def configName = config.name.capitalize()
			UpdateTask updateTask = project.tasks.add(name: "update$configName", type: UpdateTask) {
				description "Downloads and unpacks all $configName dependencies."
			}
			updateTask.configuration = config
			updateAllTask.dependsOn(updateTask)
		}

		def configurations = project.configurations
		configurations.each createUpdateTaskForConfig
		configurations.whenObjectAdded createUpdateTaskForConfig
	}
}
