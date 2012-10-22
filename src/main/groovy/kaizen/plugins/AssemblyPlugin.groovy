package kaizen.plugins

import org.gradle.api.*;
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil
import kaizen.foundation.FileName

class AssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.extensions.add('assembly', new AssemblyExtension(project))

		if (project.configurations.isEmpty()) {
			addDefaultAssemblyConfigurationTo(project)
		}

		configureCompilationOf(project)

		if (ProjectClassifier.isTest(project)) {
			configureTest(project)
			return
		}
	}

	private void addDefaultAssemblyConfigurationTo(Project project) {
		Configurations.addDefaultAssemblyConfigurationTo(project)
	}

	private void configureCompilationOf(Project project) {
		configure(project) {

			apply plugin: 'base'

			task('compile', type: Exec, dependsOn: 'copyDependencies') {
				description "Compiles all sources in the project directory."
			}

			task('copyDependencies', dependsOn: 'outputDir') {
				description "Copies all incoming dependencies into the build directory."
			}

			task('zip', type: Zip, dependsOn: 'compile') {
				description "Packs the assembly for distribution."

				baseName = assembly.name
				from project.buildDir
				include assembly.file.name
				include "${assembly.name}.xml"
			}

			afterEvaluate {
				adjustCompilationDependenciesOf project
			}

			task('outputDir') << {
				assembly.file.parentFile.mkdirs()
			}
		}
	}

	private void configureTest(Project project) {
		if (AssemblyConventions.isTest(project))
			makeTestProjectDependOnTestee(project)
	}

	private void makeTestProjectDependOnTestee(Project testProject) {
		def testeeName = testProject.name[0..(testProject.name.lastIndexOf('.') - 1)]
		def testee = testProject.rootProject.findProject(testeeName)
		if (testee) {
			testProject.dependencies.add(
				compileConfigurationNameFor(testProject),
				testProject.dependencies.project(
					path: testee.path,
					configuration: compileConfigurationNameFor(testee)))
		}
	}

	def compileConfigurationNameFor(Project project) {
		compileConfigurationFor(project).name
	}

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}

	void adjustCompilationDependenciesOf(Project project) {

		def buildDir = project.buildDir
		def assemblyInBuildDir = { name -> new File(buildDir, "${name}.dll") }
		def config = compileConfigurationFor(project)
		def assemblyReferences = config.allDependencies.collect { assemblyInBuildDir(it.name) }

		configure(project.tasks.copyDependencies) {
			inputs.source config.incoming.files
			//for some reason, declaring the outputs doesn't work
			//outputs.files config.incoming.collect { assemblyInBuildDir(it.name) }
			doFirst {
				config.incoming.files.each { file ->
					project.copy {
						from project.zipTree(file)
						into buildDir
						include '*.dll'
					}
				}
			}
		}

		def unity = project.rootProject.unity
		def mono = unity.mono
		def assembly = project.assembly
		def assemblyFile = assembly.file
		def keyFile = assembly.keyFile
		configure(project.tasks.compile) {
			outputs.file assemblyFile
			inputs.files assemblyReferences

			if (isBooProject(project)) {
				inputs.source project.fileTree(dir: project.projectDir, include: "**/*.boo")
				executable unity.monoBleedingEdge.cli
				args mono.booc.executable
				args "-srcdir:${project.projectDir}"
				args '-r:Boo.Lang.PatternMatching.dll'
				args '-r:Boo.Lang.Useful.dll'
			} else {
				inputs.source project.fileTree(dir: project.projectDir, include: "**/*.cs")
				executable unity.mono.cli
				args mono.gmcs.executable
				args "-recurse:*.cs"
				args "-doc:${xmlDocFileFor(assemblyFile)}"
				args "-nowarn:1591"
			}

			args assemblyReferences.collect { "-r:$it" }
			args "-out:$assemblyFile"
			args "-target:library"
			if (keyFile) {
				args "-keyfile:${project.file(keyFile)}"
			}
		}

		project.artifacts.add(config.name, project.tasks.zip)
	}

	boolean isBooProject(Project project) {
		project.projectDir.listFiles({ dir, name -> name.endsWith('.boo') } as FilenameFilter).any()
	}

	private Configuration compileConfigurationFor(Project p) {
		Configurations.defaultConfigurationFor(p)
	}

	String xmlDocFileFor(File assemblyFile) {
		return new File(assemblyFile.parentFile, FileName.withoutExtension(assemblyFile) + ".xml")
	}
}

class AssemblyExtension {

	final Project project
	String name
	def keyFile

	AssemblyExtension(Project project) {
		this.project = project
		this.name = project.name
	}

	File getFile() {
		new File(project.buildDir, "${name}.dll")
	}
}