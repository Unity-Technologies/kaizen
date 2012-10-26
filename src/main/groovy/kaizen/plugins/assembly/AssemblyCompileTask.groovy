package kaizen.plugins.assembly

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import kaizen.plugins.core.Configurations

class AssemblyCompileTask extends DefaultTask {

	Collection<String> defines = []
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
		def assemblyReferences = configuration.allDependencies.collect { new File(resolvedOutputDir, "${it.name}.dll") }

		def unity = project.rootProject.unity
		def mono = unity.mono
		def assembly = project.assembly
		def keyFile = assembly.keyFile
		def isBoo = isBooProject()
		configure {
			outputs.file assemblyFile
			inputs.files assemblyReferences

			if (isBoo) {
				inputs.source project.fileTree(dir: project.projectDir, include: "**/*.boo")
			} else {
				inputs.source project.fileTree(dir: project.projectDir, include: "**/*.cs")
			}

			doFirst {
				def args = []
				if (isBoo) {
					inputs.source project.fileTree(dir: project.projectDir, include: "**/*.boo")
					args << unity.monoBleedingEdge.cli
					args << mono.booc.executable
					args << "-srcdir:${project.projectDir}"
					args << '-r:Boo.Lang.PatternMatching.dll'
					args << '-r:Boo.Lang.Useful.dll'
				} else {
					inputs.source project.fileTree(dir: project.projectDir, include: "**/*.cs")
					args << unity.mono.cli
					args << mono.gmcs.executable
					args << "-recurse:*.cs"
					args << "-doc:${xmlDocFile}"
					args << "-nowarn:1591"
				}
				args.addAll(assemblyReferences.collect { "-r:$it" })
				args << "-out:$assemblyFile"
				args << "-target:$assembly.target"
				if (keyFile) {
					args << "-keyfile:${project.file(keyFile)}"
				}
				args.addAll(defines.collect { "-define:$it" })
				project.exec {
					commandLine(args)
				}
			}
		}
	}

	boolean isBooProject() {
		project.projectDir.listFiles({ dir, name -> name.endsWith('.boo') } as FilenameFilter).any()
	}
}
