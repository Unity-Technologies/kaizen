package kaizen.plugins.assembly.tasks

import kaizen.plugins.assembly.model.Assembly
import kaizen.plugins.clr.ClrExtension
import kaizen.plugins.clr.ClrLanguageNames
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import kaizen.plugins.conventions.Configurations
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.TaskAction

class AssemblyCompile extends DefaultTask {

	String language = ClrLanguageNames.CSHARP
	String targetFrameworkVersion = 'v3.5'
	def outputAssembly
	def keyFile
	Collection<String> defines = []
	Collection<String> assemblyReferences = []
	Collection<String> compilerOptions = []

	def references(Object... assemblies) {
		assemblyReferences.addAll(assemblies.collect { it.toString() })
	}

	@TaskAction
	def compile() {
		def clr = ClrExtension.forProject(project)
		assert clr, "clr plugin missing!"

		def compiler = clr.compilerForLanguage(language)
		assert compiler, "No compiler for language $language was found!"

		compiler.exec { spec ->
			spec.sourceFiles inputs.sourceFiles
			spec.targetFrameworkVersion targetFrameworkVersion
			spec.outputAssembly file(outputAssembly)
			if (assemblyReferences) spec.references assemblyReferences
			if (defines) spec.defines defines
			if (keyFile) spec.keyFile file(keyFile)
			if (compilerOptions) spec.compilerOptions compilerOptions
		}
	}

	File file(file) {
		project.file(file)
	}

	void setUp() {
		def assemblyDependencies = configuration.allDependencies.collect {
			new File(resolvedOutputDir, assemblyFileNameFor(it))
		}

		def assembly = Assembly.forProject(project)
		configure {
			dependsOn configuration
			outputs.file assemblyFile
			inputs.files assemblyDependencies
			inputs.source assembly.sourceFiles
			doFirst {
				def args = []
					args << unity.mono.cli
					args << mono.gmcs.executable
					args << "-recurse:*.cs"
					args << "-doc:${xmlDocFile}"
					args << "-nowarn:1591"
				args.addAll(customArgs)
				project.exec {
					commandLine(args)
				}
			}
		}
	}

	def assemblyFileNameFor(Dependency dependency) {
		if (dependency instanceof ProjectDependency) {
			def assembly = dependency.dependencyProject.extensions.findByName('assembly')
			if (assembly)
				return assembly.fileName
		}
		return "${dependency.name}.dll"
	}
}