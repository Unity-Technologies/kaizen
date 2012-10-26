package kaizen.plugins.assembly

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil
import kaizen.plugins.core.Configurations

class AssemblyPlugin implements Plugin<Project> {

	static final Set<String> nonCompilableConfigurations = ['default', 'archives'].toSet()

	@Override
	void apply(Project project) {

		project.apply plugin: 'base'
		project.extensions.add('assembly', new AssemblyExtension(project))

		compilableConfigurationsOf(project).each {
			configureCompilationOf(it, project)
		}
	}

	private Set<Configuration> compilableConfigurationsOf(Project project) {
		project.configurations.findAll { !(it.name in nonCompilableConfigurations) }
	}

	private void configureCompilationOf(Configuration config, Project project) {
		def configLabel = Configurations.labelFor(config)

		def copyDependenciesTaskName = "copy${configLabel}Dependencies"

		def compileTaskName = "compile${configLabel}"
		def compileTask = project.tasks.add(compileTaskName, AssemblyCompileTask)
		compileTask.description = "Compiles all sources in the project directory."
		compileTask.dependsOn copyDependenciesTaskName
		compileTask.configuration = config

		def assembleTaskName = "assemble${configLabel}"
		def outputDirTaskName = "create${configLabel}OutputDir"

		configure(project) {

			def copyDependenciesTask = task(copyDependenciesTaskName, dependsOn: outputDirTaskName) {
				description "Copies all incoming dependencies into the build directory."
			}

			afterEvaluate {
				configure(copyDependenciesTask) {
					inputs.source config.incoming.files
					//for some reason, declaring the outputs doesn't work
					//outputs.files config.incoming.collect { assemblyInBuildDir(it.name) }
					doFirst {
						config.incoming.files.each { file ->
							project.copy {
								from project.zipTree(file)
								into compileTask.resolvedOutputDir
								include '*.dll'
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
					from compileTask.resolvedOutputDir
					include assembly.fileName
					include assembly.xmlDocFileName
				}

				project.artifacts.add(config.name, assembleTask)
			}

			task(outputDirTaskName) <<{
				compileTask.resolvedOutputDir.mkdirs()
			}
		}
	}

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}
}

