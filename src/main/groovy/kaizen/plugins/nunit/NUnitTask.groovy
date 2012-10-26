package kaizen.plugins.nunit

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class NUnitTask extends DefaultTask {

	@TaskAction
	def runTests() {
		def nunitConsole = project.rootProject.file('libs/Test/nunit-console.exe')
		def result = project.exec {
			commandLine project.unity.mono.cli
			args nunitConsole
			args '-nologo', '-nodots'
			args "-work=${project.buildDir}"
			args project.assembly.file
		}
	}

}
