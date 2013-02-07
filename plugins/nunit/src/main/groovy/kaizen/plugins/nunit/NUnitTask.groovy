package kaizen.plugins.nunit

import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.ClrProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class NUnitTask extends DefaultTask {

	final Set<String> includedCategories = new HashSet<String>()
	final Set<String> excludedCategories = new HashSet<String>()

	def executable = 'lib/NUnit/nunit-console.exe'

	def include(String... categories) {
		includedCategories.addAll(categories)
	}

	def exclude(String... categories) {
		excludedCategories.addAll(categories)
	}

	@TaskAction
	def runTests() {
		def assembly = inputs.files.getSingleFile()
		//def cli = project.unity.mono.cli
		def result = clr.exec {
			//environment 'MONO_EXECUTABLE': cli
			//commandLine cli
			//args '--debug'
			args executableFile
			args '-nologo', '-nodots', '-domain:none'
			args "-work=${assembly.parentFile}"
			args includedCategories.collect { "-include=$it" }
			args excludedCategories.collect { "-exclude=$it" }
			args assembly
		}
	}

	Clr getClr() {
		ClrProvider provider = project.extensions.clr
		provider.runtimeForFrameworkVersion('v3.5')
	}

	File getExecutableFile() {
		project.rootProject.file(executable)
	}
}
