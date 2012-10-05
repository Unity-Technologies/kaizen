package kaizen.plugins

import org.gradle.api.*;
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip;

class AssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.configure(project) {

			apply plugin: 'base'

			configurations {
				editor
			}

			ext {
				assemblyName = project.name
				assemblyFileName = "${assemblyName}.dll"
				assemblyPath = file("$buildDir/$assemblyFileName")
			}

			task('outputDir') {
				doFirst {
					assemblyPath.parentFile.mkdirs()
				}
			}

			task('compile', type: Exec, dependsOn: 'outputDir') {
				description "Compiles all sources in the project directory."

				outputs.file assemblyPath
				inputs.source fileTree(dir: project.projectDir, include: '**/*.cs')
			}

			afterEvaluate {
				adjustCompilationDependenciesOf project
			}

			task('zip', type: Zip, dependsOn: 'compile') {
				description "Packs the assembly for distribution."

				baseName = assemblyName
				from project.buildDir
				include assemblyFileName
				include "${assemblyName}.xml"
			}

			artifacts {
				editor zip
			}
		}
	}
	
	void adjustCompilationDependenciesOf(Project p) {
		def config = p.configurations['editor']
		def configName = config.name.capitalize()

		def allDeps = config.allDependencies
		def projectDeps = allDeps.findAll { it instanceof ProjectDependency }.collect { it.dependencyProject }
		def assemblyDeps = allDeps.findAll { it instanceof AssemblyDependency }
		def externalDeps = allDeps.findAll { it instanceof ExternalModuleDependency }
		
		def projectAssemblies = projectDeps.collect { it.assemblyPath }
		def externalAssemblies = externalDeps.collect { p.rootProject.file("libs/$configName/${it.name}.dll")}
		def localAssemblies = assemblyDeps.collect { it.name }

		def assemblyReferences = (projectAssemblies + externalAssemblies + localAssemblies).collect { "-r:$it" }
		
		def compileTask = p.tasks.compile
		def defaultCompilerArgs = [
			"-out:$p.assemblyPath",
			"-target:library",
			"-recurse:*.cs",
			"-doc:${xmlDocFileFor(p.assemblyPath)}",
			"-nowarn:1591"
		]
		compileTask.executable = rootProject.unity.tools.gmcs.executable
		compileTask.args = defaultCompilerArgs + assemblyReferences
		compileTask.inputs.files(projectAssemblies)
	}

	String xmlDocFileFor(File assemblyPath) {
		return new File(assemblyPath.parentFile, FileName.withoutExtension(assemblyPath) + ".xml")
	}
}

