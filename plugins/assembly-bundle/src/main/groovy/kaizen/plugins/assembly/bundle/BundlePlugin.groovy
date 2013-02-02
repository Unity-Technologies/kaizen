package kaizen.plugins.assembly.bundle

import kaizen.plugins.LibClientPlugin
import kaizen.plugins.LocalRepositoryPlugin
import kaizen.plugins.unity.UnityPlugin
import org.gradle.api.Project
import org.gradle.api.Plugin

import kaizen.plugins.assembly.AssemblyPlugin

class BundlePlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {

		project.group = project.name
		project.apply(plugin: UnityPlugin)

		// all projects in a bundle are considered assemblies by default
		// TODO: remove from here (or create a ManagedBundlePlugin)
		project.subprojects.each { subProject ->
			subProject.apply(plugin: AssemblyPlugin)
		}

		// a bundle needs the deployment capabilities given by the base plugin
		project.apply(plugin: 'base')

		// a bundle needs the ability to depend on external libs
		project.apply(plugin: LibClientPlugin)

		// a bundle needs a local repository to be published to
		project.apply(plugin: LocalRepositoryPlugin)
	}
}
