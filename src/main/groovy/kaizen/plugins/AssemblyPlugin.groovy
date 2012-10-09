package kaizen.plugins

import org.gradle.api.*;
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil
import kaizen.foundation.FileName
import org.apache.tools.ant.taskdefs.ExecTask;

class AssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.extensions.add('assembly', new AssemblyExtension(project))

		configure(project) {

			apply plugin: ConfigurationPlugin
			apply plugin: 'base'

			task('compile', type: Exec, dependsOn: 'outputDir') {
				description "Compiles all sources in the project directory."

				outputs.file assembly.file
				inputs.source fileTree(dir: project.projectDir, include: '**/*.cs')
			}

			afterEvaluate {
				adjustCompilationDependenciesOf project
			}

			task('outputDir') {
				doFirst {
					assembly.file.parentFile.mkdirs()
				}
			}
		}

		// test projects are never published
		if (ProjectClassifier.isTest(project))
			return

		configure(project) {
			task('publish', dependsOn: ['uploadEditor'])

			task('zip', type: Zip, dependsOn: 'compile') {
				description "Packs the assembly for distribution."

				baseName = assembly.name
				from project.buildDir
				include assembly.file.name
				include "${assembly.name}.xml"
			}

			artifacts {
				editor zip
			}
		}
	}

	def configure(Project project, Closure closure) {
		ConfigureUtil.configure(closure, project)
	}

	void adjustCompilationDependenciesOf(Project p) {
		def config = ConfigurationPlugin.defaultConfigurationFor(p)
		def configName = config.name.capitalize()

		def allDeps = config.allDependencies
		def projectDeps = allDeps.findAll { it instanceof ProjectDependency }.collect { it.dependencyProject }
		def assemblyDeps = allDeps.findAll { it instanceof AssemblyDependency }
		def externalDeps = allDeps.findAll { it instanceof ExternalModuleDependency }
		
		def projectAssemblies = projectDeps.collect { it.assembly.file }
		def externalAssemblies = externalDeps.collect { p.rootProject.file("libs/$configName/${it.name}.dll")}
		def localAssemblies = assemblyDeps.collect { it.name }

		def assemblyFiles = projectAssemblies + externalAssemblies
		def assemblyReferences = (assemblyFiles + localAssemblies).collect { "-r:$it" }
		
		DefaultTask compileTask = p.tasks.compile
		def assemblyFile = p.assembly.file
		def defaultCompilerArgs = [
			"-out:$assemblyFile",
			"-target:library",
			"-recurse:*.cs",
			"-doc:${xmlDocFileFor(assemblyFile)}",
			"-nowarn:1591"
		]
		compileTask.executable = p.rootProject.unity.tools.gmcs.executable
		compileTask.args = defaultCompilerArgs + assemblyReferences
		compileTask.inputs.files(assemblyFiles)
		compileTask.dependsOn(*projectDeps*.tasks.compile)
		compileTask.doLast {
			assemblyFiles.each { file ->
				p.copy {
					from file.parentFile
					include file.name
					into p.buildDir
				}
			}
		}
	}

	String xmlDocFileFor(File assemblyFile) {
		return new File(assemblyFile.parentFile, FileName.withoutExtension(assemblyFile) + ".xml")
	}
}

class AssemblyExtension {

	final Project project
	String name

	AssemblyExtension(Project project) {
		this.project = project
		this.name = project.name
	}

	File getFile() {
		new File(project.buildDir, "${name}.dll")
	}
}