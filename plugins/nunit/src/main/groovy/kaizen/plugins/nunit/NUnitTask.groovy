package kaizen.plugins.nunit

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class NUnitTask extends DefaultTask {

	final Set<String> includedCategories = new HashSet<String>()
	final Set<String> excludedCategories = new HashSet<String>()

	def include(String... categories) {
		includedCategories.addAll(categories)
	}

	def exclude(String... categories) {
		excludedCategories.addAll(categories)
	}

	@TaskAction
	def runTests() {
		def nunitConsole = project.rootProject.file('lib/NUnit/nunit-console.exe')
		def assembly = inputs.files.getSingleFile()
		def cli = project.unity.mono.cli
		def result = project.exec {
			environment 'MONO_EXECUTABLE': cli
			commandLine cli
			args '--debug'
			args nunitConsole
			args '-nologo', '-nodots'
			args "-work=${assembly.parentFile}"
			args includedCategories.collect { "-include=$it" }
			args excludedCategories.collect { "-exclude=$it" }
			args assembly
		}
	}
}
