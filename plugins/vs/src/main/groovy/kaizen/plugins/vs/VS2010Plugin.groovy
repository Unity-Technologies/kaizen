package kaizen.plugins.vs

import kaizen.plugins.assembly.AssemblyPlugin
import org.gradle.api.Project
import org.gradle.api.Plugin
import groovy.text.SimpleTemplateEngine

class VS2010Plugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.plugins.apply AssemblyPlugin

		def vs = new VSExtension(project)
		project.extensions.add('vs', vs)

		def vsProject = vs.project
		def vsProjectTask = project.task('vsProject') {
			doFirst {
				if (!vsProject.isSupportedLanguage) {
					logger.info("vs project file for $project.name won't be generated.")
					return
				}
				applyTemplate('csproj', vsProject.file, project: vsProject)
			}
		}
		def vsSolutionTask = project.task('vsSolution') {
			doFirst {
				vs.solutions.each {
					applyTemplate('sln', project.file(it.destinationFile), solution: it)
				}
			}
		}
		project.task('vs', dependsOn: [vsProjectTask, vsSolutionTask], group: 'IDE') {
			description 'Generates Visual Studio files (csproj, sln)'
		}
	}

	def applyTemplate(Map properties, String templateName, File destinationFile) {
		def resource = VS2010Plugin.class.getResource("/templates/vs2010/${templateName}.template")
		def template = new SimpleTemplateEngine().createTemplate(resource)
		def templateResult = template.make(properties)
		destinationFile.withWriter { templateResult.writeTo(it) }
	}
}


