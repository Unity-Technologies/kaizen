package kaizen.plugins.refactorings

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CreateProjectTask extends DefaultTask {

	def projectName

	def projectsFolder = 'src'

	def buildScriptName = { newProjectName -> "${newProjectName}.gradle" }

	def CreateProjectTask() {
		description = "Creates a new project. Required argument: -PprojectName=projectName"
		group = "Refactoring"
	}

	@TaskAction
	def create() {
		def newProjectName = projectName ?: project.properties.projectName
		def projectDir = project.file("$projectsFolder/$newProjectName")
		assert !projectDir.exists()
		projectDir.mkdirs()
		def buildFile = new File(projectDir, buildScriptName(newProjectName))
		buildFile.text = "version = '0.1.0'\n"
	}
}
