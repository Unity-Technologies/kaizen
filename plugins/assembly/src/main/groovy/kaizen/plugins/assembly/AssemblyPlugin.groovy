package kaizen.plugins.assembly

import kaizen.plugins.assembly.model.Assembly
import kaizen.plugins.assembly.tasks.AssemblyCompile
import kaizen.plugins.assembly.tasks.AssemblyRunTask
import kaizen.plugins.conventions.Configurations
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil

class AssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.plugins.apply 'base'
		def assembly = project.extensions.create('assembly', Assembly, project)

		def configurations = project.configurations
		configurations.each { configureCompilationOf(it, assembly) }
		configurations.whenObjectAdded { configureCompilationOf(it, assembly) }
	}

	private void configureCompilationOf(Configuration config, Assembly assembly) {

		if (config.name == 'archives')
			return

		def project = assembly.project
		def configLabel = Configurations.labelFor(config)
		def outputDir = { project.file("${project.buildDir}/$configLabel") }

		def copyDependenciesTaskName = "copy${configLabel}Dependencies"

		def compileTaskName = "compile${configLabel}"
		def compileTask = project.tasks.add(compileTaskName, AssemblyCompile)

		def runTask = project.tasks.add("run$configLabel", AssemblyRunTask)
		runTask.runAssemblyBuiltBy(compileTask)

		def assembleTaskName = "assemble${configLabel}"
		def outputDirTaskName = "create${configLabel}OutputDir"

		configure(project) {

			Task copyDependenciesTask = task(copyDependenciesTaskName, dependsOn: outputDirTaskName) {
				description "Copies all incoming dependencies into the build directory."
			}

			afterEvaluate {
				configure(copyDependenciesTask) {
					inputs.source config
					outputs.file new File(outputDir(), "${config.name}.receipt")
					doFirst {
						inputs.sourceFiles.each { file ->
							logger.info "Unpacking ${file.name} to ${outputDir()}"
							project.copy {
								from project.zipTree(file)
								into outputDir()
								include '*.*'
							}
						}
						copyDependenciesTask.outputs.files.singleFile.withPrintWriter { writer ->
							copyDependenciesTask.inputs.sourceFiles.each { f ->
								writer.println(f.name)
							}
						}
					}
				}
			}

			afterEvaluate {
				tasks."$compileTaskName" {
					description "Compiles all sources in the project directory."
					dependsOn copyDependenciesTask
					inputs.files config
					language assembly.language
					inputs.source assembly.sourceFiles
					inputs.files assembly.embeddedResources
					outputAssembly new File(outputDir(), assembly.fileName)
					references assemblyReferencesFor(config, outputDir())
					references assembly.references*.name
					targetFrameworkVersion assembly.targetFrameworkVersion
					embeddedResources embeddedResourcesFor(assembly)
				}
			}

			def assembleTask = task(assembleTaskName, type: Zip, dependsOn: compileTaskName) {
				description "Packs the assembly for distribution."
			}

			afterEvaluate {
				configure(assembleTask) {
					baseName = assembly.name
					appendix = config.name
					from outputDir
					include assembly.fileName
					include assembly.xmlDocFileName
					destinationDir new File(outputDir(), 'distributions')
				}
				project.artifacts.add(config.name, assembleTask)
			}

			def outputDirTask = task(outputDirTaskName) {
				doFirst {
					outputDir().mkdirs()
				}
			}
			outputDirTask.outputs.upToDateWhen {
				outputDir().exists()
			}
		}
	}

	def embeddedResourcesFor(Assembly assembly) {
		def resources = [:]
		assembly.embeddedResources.each {
			resources.put(resourceIdFor(it, assembly), it)
		}
		resources
	}

	def resourceIdFor(File file, Assembly assembly) {
		def relativeResourceId = assembly.project.relativePath(file).replace('\\', '/').replace('/', '.')
		"${assembly.name}.$relativeResourceId"
	}

	def assemblyReferencesFor(Configuration config, File outputDir) {
		config.allDependencies.collect {
			new File(outputDir, assemblyFileNameFor(it))
		}
	}

	def assemblyFileNameFor(Dependency dependency) {
		assemblyProjectFileNameFor(dependency) ?: "${dependency.name}.dll"
	}

	def assemblyProjectFileNameFor(Dependency dependency) {
		dependency instanceof ProjectDependency ?
			Assembly.forProject(dependency.dependencyProject)?.fileName :
			null
	}

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}
}

