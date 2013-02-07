package kaizen.plugins.vs.tests

import kaizen.testing.PluginSpecification
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.TaskInternal

abstract class VSProjectSpecification extends PluginSpecification {
	def loadProjectFileOf(Project project) {
		executeVsProjectTaskOf(project)
		parseProjectFileOf(project)
	}

	def executeVsProjectTaskOf(Project project) {
		executeTask(project.tasks.vsProject)
	}

	def parseProjectFileOf(Project project) {
		new XmlParser().parse(project.file(project.name + '.csproj'))
	}
}
