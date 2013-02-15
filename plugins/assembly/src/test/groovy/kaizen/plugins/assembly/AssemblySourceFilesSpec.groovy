package kaizen.plugins.assembly

import kaizen.plugins.assembly.model.Assembly
import kaizen.plugins.assembly.tasks.AssemblyCompile
import kaizen.testing.PluginSpecification
import spock.lang.Unroll

class AssemblySourceFilesSpec extends PluginSpecification {

	@Unroll
	def 'project language is set to #language when source files end in #fileExtension'() {
		given:
		def sourceFileName = "code.$fileExtension"
		def project = projectWithDirectoryStructure {
			file(sourceFileName)
		}

		when:
		project.plugins.apply AssemblyPlugin
		evaluateProject project

		then:
		AssemblyCompile compileTask = project.tasks.getByName('compileDefault')
		compileTask.language == language
		compileTask.inputs.sourceFiles.files*.name == [sourceFileName]
		Assembly.forProject(project).language == language

		where:
		language | fileExtension
		'c#'     | 'cs'
		'boo'    | 'boo'
	}
}
