package kaizen.plugins.assembly

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import kaizen.plugins.core.Configurations
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency

class AssemblyCompileTask extends DefaultTask {

	Collection<String> defines = []
	Collection<String> customArgs = []
	def outputDir

	private Configuration configuration

	void setConfiguration(Configuration c) {
		assert configuration == null
		configuration = c
		outputDir = "${project.buildDir}/${Configurations.labelFor(configuration)}"
		project.afterEvaluate {
			setUp()
		}
	}

	Configuration getConfiguration() {
		configuration
	}

	File getAssemblyFile() {
		return new File(resolvedOutputDir, assembly.fileName)
	}

	File getXmlDocFile() {
		return new File(resolvedOutputDir, assembly.xmlDocFileName)
	}

	File getResolvedOutputDir() {
		project.file(outputDir)
	}

	def AssemblyExtension getAssembly() {
		return project.extensions.assembly
	}

	void setUp() {
		def assemblyReferences = configuration.allDependencies.collect {
			new File(resolvedOutputDir, assemblyFileNameFor(it))
		}

		def unity = project.rootProject.unity
		def mono = unity.mono
		def assembly = project.assembly
		def keyFile = assembly.keyFile
		def isBoo = assembly.language == 'boo'
		configure {
			outputs.file assemblyFile
			inputs.files assemblyReferences
			inputs.source assembly.sourceFiles
			doFirst {
				def args = []
				if (isBoo) {
					args << unity.monoBleedingEdge.cli
					args << mono.booc.executable
					args << "-srcdir:${project.projectDir}"
				} else {
					args << unity.mono.cli
					args << mono.gmcs.executable
					args << "-recurse:*.cs"
					args << "-doc:${xmlDocFile}"
					args << "-nowarn:1591"
				}
				args << "-out:$assemblyFile"
				args << "-target:$assembly.target"
				if (keyFile) {
					args << "-keyfile:${project.file(keyFile)}"
				}
				assemblyReferences.each { args << "-r:$it" }
				defines.each { args << "-define:$it" }
				args.addAll(customArgs)
				project.exec {
					commandLine(args)
				}
			}
		}
	}

	def assemblyFileNameFor(Dependency dependency) {
		dependency instanceof ProjectDependency ?
			dependency.dependencyProject.assembly.fileName :
			"${dependency.name}.dll"
	}
}
