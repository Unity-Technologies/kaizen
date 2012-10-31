package kaizen.plugins

import org.gradle.api.Project

abstract class VSProjectSpecification extends PluginSpecification {
	def parseProjectFileOf(Project project) {
		new XmlParser().parse(new File(project.projectDir, project.name + '.csproj'))
	}
}
