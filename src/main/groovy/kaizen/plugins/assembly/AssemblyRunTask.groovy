package kaizen.plugins.assembly

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AssemblyRunTask extends DefaultTask {

	def monoExecutable

	def workingDir

	def assembly

	def runAssemblyBuiltBy(AssemblyCompileTask compileTask) {
		this.monoExecutable = { project.rootProject.extensions.unity.mono.cli }
		this.workingDir = { compileTask.resolvedOutputDir }
		this.assembly = { compileTask.assemblyFile }
		this.dependsOn compileTask
		this.onlyIf { compileTask.assembly.isExecutable() }
	}

	@TaskAction
	def run() {
		def task = this
		project.exec {
			workingDir project.file(task.workingDir)
			commandLine project.file(task.monoExecutable)
			args "--debug"
			args project.file(task.assembly)
		}
	}
}
