package kaizen.plugins.nuget

import org.gradle.api.Plugin
import org.gradle.api.Project

class NuGetPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.repositories.add(NuGetDependencyResolverFactory.newNuGetDependencyResolver())
	}
}
