package kaizen.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil

class BundlePlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.ext.repositoryForPublishing = project.file('../repository').absoluteFile
		project.group = project.name
		project.apply(plugin: UnityPlugin)
		ConfigurationPlugin.addBundleConfigurationsTo(project)

		// all sub projects are assemblies by convention
		project.subprojects.each { subProject ->
			subProject.apply(plugin: AssemblyPlugin)
		}

		// a bundle needs the deployment capabilities given by the base plugin
		project.apply(plugin: 'base')

		// a bundle needs the ability to depend on external libs
		project.apply(plugin: LibsPlugin)

		// a bundle depends on all of its sub projects
		project.subprojects.each { subProject ->
			def config = ConfigurationPlugin.defaultConfigurationFor(subProject).name
			project.dependencies.add(
				config,
				project.dependencies.project(
					path: subProject.path,
					configuration: config))
		}

		configure(project) {

			task('zip', type: Zip) {
				description "Packs the bundle for distribution."

				baseName = project.name
				from project.projectDir
				include "README.md"
				include "readme.txt"
			}

			// just an alias for now
			task('publish', dependsOn: ['uploadEditor'])

			// setup upload tasks after project evaluation
			// so extension properties can be freely modified
			// by custom build scripts
			afterEvaluate {

				// download dependencies from the repository for publishing as well
				repositories {
					ivy { url repositoryForPublishing }
				}

				tasks.uploadEditor {
					repositories {
						ivy { url repositoryForPublishing }
					}
				}

				subprojects {
					repositories {
						ivy { url repositoryForPublishing }
					}
					if (tasks.findByName('uploadEditor')) {
						tasks.uploadEditor {
							repositories {
								ivy { url repositoryForPublishing }
							}
						}
					}
				}
			}
		}
	}

	def configure(Project project, Closure closure) {
		ConfigureUtil.configure(closure, project)
	}
}
