package kaizen.plugins.vs.tests

import kaizen.testing.PluginSpecification
import org.gradle.api.Project
import org.gradle.api.internal.TaskInternal

abstract class VSProjectSpecification extends PluginSpecification {
	def loadProjectFileOf(Project project) {
		executeVsProjectTaskOf(project)
		parseProjectFileOf(project)
	}

	def executeVsProjectTaskOf(Project project) {
		(project.tasks.vsProject as TaskInternal).execute()
	}

	def parseProjectFileOf(Project project) {
		new XmlParser().parse(project.file(project.name + '.csproj'))
	}
}
