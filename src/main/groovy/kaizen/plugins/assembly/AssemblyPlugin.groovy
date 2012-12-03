package kaizen.plugins.assembly

import kaizen.plugins.core.Configurations
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil

class AssemblyPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.apply plugin: 'base'
		project.extensions.add('assembly', new AssemblyExtension(project))

		def configurations = project.configurations
		configurations.each { configureCompilationOf(it, project) }
		configurations.whenObjectAdded { configureCompilationOf(it, project) }
	}

	private void configureCompilationOf(Configuration config, Project project) {

		if (config.name == 'archives')
			return

		def configLabel = Configurations.labelFor(config)

		def copyDependenciesTaskName = "copy${configLabel}Dependencies"

		def compileTaskName = "compile${configLabel}"
		def compileTask = project.tasks.add(compileTaskName, AssemblyCompileTask)
		compileTask.description = "Compiles all sources in the project directory."
		compileTask.dependsOn copyDependenciesTaskName
		compileTask.configuration = config

		def runTask = project.tasks.add("run$configLabel", AssemblyRunTask)
		runTask.runAssemblyBuiltBy(compileTask)

		def assembleTaskName = "assemble${configLabel}"
		def outputDirTaskName = "create${configLabel}OutputDir"

		configure(project) {

			def copyDependenciesTask = task(copyDependenciesTaskName, dependsOn: outputDirTaskName) {
				description "Copies all incoming dependencies into the build directory."
			}

			afterEvaluate {
				configure(copyDependenciesTask) {
					dependsOn compileTask.configuration
					doFirst {
						config.incoming.files.each { file ->
							logger.info "Unpacking ${file.name} to ${compileTask.resolvedOutputDir}"
							project.copy {
								from project.zipTree(file)
								into compileTask.resolvedOutputDir
								include '*.*'
							}
						}
					}
				}
			}

			def assembleTask = task(assembleTaskName, type: Zip, dependsOn: compileTaskName) {
				description "Packs the assembly for distribution."
			}

			afterEvaluate {
				def assembly = compileTask.assembly
				configure(assembleTask) {
					baseName = assembly.name
					appendix = config.name
					from compileTask.resolvedOutputDir
					include assembly.fileName
					include assembly.xmlDocFileName
					destinationDir new File(compileTask.resolvedOutputDir, 'distributions')
				}

				project.artifacts.add(config.name, assembleTask)
			}

			def outputDirTask = task(outputDirTaskName) {
				doFirst {
					compileTask.resolvedOutputDir.mkdirs()
				}
			}
			outputDirTask.outputs.upToDateWhen {
				compileTask.resolvedOutputDir.exists()
			}
		}
	}

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}
}

