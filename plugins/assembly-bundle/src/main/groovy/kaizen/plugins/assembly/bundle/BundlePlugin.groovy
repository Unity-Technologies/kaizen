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

		project.plugins.apply UnityPlugin

		// all projects in a bundle are considered assemblies by default
		project.subprojects.each { subProject ->
			subProject.plugins.apply AssemblyPlugin
		}

		// a bundle needs the deployment capabilities given by the base plugin
		project.plugins.apply 'base'

		// a bundle needs the ability to depend on external libs
		project.plugins.apply LibClientPlugin

		// a bundle needs a local repository to be published to
		project.plugins.apply LocalRepositoryPlugin
	}
}
