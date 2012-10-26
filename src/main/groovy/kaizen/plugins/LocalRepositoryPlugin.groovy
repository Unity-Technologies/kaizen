package kaizen.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.util.ConfigureUtil
import kaizen.plugins.core.Configurations

class LocalRepositoryPlugin implements Plugin<Project> {

	@Override
	void apply(Project bundle) {
		def localRepository = new LocalRepositoryExtension()
		bundle.extensions.add('localRepository', localRepository)
		bundle.afterEvaluate {
			def location = bundle.file(localRepository.location)
			bundle.logger.info("local repository set to '$location'")
			configureRepositoryOn(bundle, location)
			bundle.subprojects*.afterEvaluate {
				configureRepositoryOn(it, location)
			}
		}
	}

	private void configureRepositoryOn(Project project, File location) {
		configure(project) {
			repositories {
				ivy { url location }
			}
		}
		project.configurations.each { config ->
			def uploadConfigTask = project.tasks.findByName("upload${Configurations.labelFor(config)}")
			if (uploadConfigTask) {
				configure(uploadConfigTask) {
					repositories {
						ivy { url location }
					}
				}
			}
		}
	}

	def configure(Object o, Closure closure) {
		ConfigureUtil.configure(closure, o)
	}
}

class LocalRepositoryExtension {
	def location = '../repository'
}
