package kaizen.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.bundling.Zip

class BundlePlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.ext.repositoryForPublishing = project.file('../repository').absoluteFile
		project.group = project.name
		project.apply(plugin: UnityPlugin)

		// all sub projects are assemblies by convention
		project.subprojects.each { subProject ->
			subProject.apply(plugin: AssemblyPlugin)
		}

		project.configurations.add('editor') {
			description 'Configuration for editor extension artifacts.'
		}

		project.configurations.add('tests') {
			description 'Configuration for test assemblies.'
		}

		// a bundle needs the deployment capabilities given by the base plugin
		project.apply(plugin: 'base')

		// a bundle needs the ability to depend on external libs
		project.apply(plugin: LibsPlugin)

		// a bundle depends on all of its sub projects
		project.subprojects.each { subProject ->
			def isTest = subProject.name.endsWith('.Tests')
			def config = isTest ? 'tests' : 'editor'
			project.dependencies.add(
				config,
				project.dependencies.project(
					path: subProject.path,
					configuration: config))
		}

		project.configure(project) {

			task('zip', type: Zip) {
				description "Packs the bundle for distribution."

				baseName = project.name
				from project.projectDir
				include "README.md"
				include "readme.txt"
			}

			// just an alias for now
			task('publish', depends: uploadEditor)

			artifacts {
				editor zip
			}

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
