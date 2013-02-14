package kaizen.plugins.assembly

import kaizen.plugins.assembly.model.Assembly
import kaizen.plugins.assembly.tasks.AssemblyCompile
import kaizen.plugins.assembly.tasks.AssemblyRunTask
import kaizen.plugins.conventions.Configurations
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
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

			def copyDependenciesTask = task(copyDependenciesTaskName, dependsOn: outputDirTaskName) {
				description "Copies all incoming dependencies into the build directory."
			}

			afterEvaluate {
				configure(copyDependenciesTask) {
					dependsOn config
					doFirst {
						config.incoming.files.each { file ->
							logger.info "Unpacking ${file.name} to ${outputDir()}"
							project.copy {
								from project.zipTree(file)
								into outputDir()
								include '*.*'
							}
						}
					}
				}
			}

			afterEvaluate {
				tasks."$compileTaskName" {
					description "Compiles all sources in the project directory."
					dependsOn copyDependenciesTask
					language assembly.language
					inputs.source assembly.sourceFiles
					outputAssembly new File(outputDir(), assembly.fileName)
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

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}
}

