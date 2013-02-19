package kaizen.plugins.assembly.tasks

import kaizen.plugins.clr.ClrCompileSpec
import kaizen.plugins.clr.ClrExtension
import kaizen.plugins.clr.ClrLanguageNames
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskAction

class AssemblyCompile extends DefaultTask {

	String language = ClrLanguageNames.CSHARP
	String targetFrameworkVersion = 'v3.5'
	File outputAssemblyFile
	File outputXmlDocFile
	def keyFile
	Collection<String> defines = []
	Collection<String> assemblyReferences = []
	Collection<String> compilerOptions = []

	AssemblyCompile() {
		group = BasePlugin.BUILD_GROUP
	}

	def references(Iterable<Object> assemblies) {
		assemblyReferences.addAll(assemblies*.toString())
	}

	def references(Object... assemblies) {
		assemblyReferences.addAll(assemblies*.toString())
	}

	def outputAssembly(output) {
		outputAssemblyFile = file(output)
		outputs.file(outputAssemblyFile)
	}

	def outputXmlDoc(output) {
		outputXmlDocFile = outputs.file(output)
	}

	File getOutputAssembly() {
		outputAssemblyFile
	}

	@TaskAction
	def compile() {
		def clr = ClrExtension.forProject(project)
		assert clr, "clr plugin missing"

		def compiler = clr.compilerForLanguage(language)
		assert compiler, "No compiler for language $language was found"

		compiler.exec { ClrCompileSpec spec ->
			spec.sourceFiles inputs.sourceFiles
			spec.targetFrameworkVersion targetFrameworkVersion
			spec.outputAssembly outputAssemblyFile
			if (outputXmlDocFile) spec.outputXmlDoc outputXmlDocFile
			if (assemblyReferences) spec.references assemblyReferences
			if (defines) spec.defines defines
			if (keyFile) spec.keyFile file(keyFile)
			if (compilerOptions) spec.compilerOptions compilerOptions
		}
	}

	File file(file) {
		project.file(file)
	}
}
