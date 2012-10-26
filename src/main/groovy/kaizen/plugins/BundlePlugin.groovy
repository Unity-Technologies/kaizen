package kaizen.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.util.ConfigureUtil

class BundlePlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.group = project.name
		project.apply(plugin: UnityPlugin)

		project.subprojects.each { subProject ->
			subProject.apply(plugin: AssemblyPlugin)
		}

		// a bundle needs the deployment capabilities given by the base plugin
		project.apply(plugin: 'base')

		// a bundle needs the ability to depend on external libs
		project.apply(plugin: LibsPlugin)
			// just an alias for now

		// a bundle needs a local repository to be published to
		project.apply(plugin: LocalRepositoryPlugin)
	}
}
