package kaizen.plugins.unity

import kaizen.plugins.clr.ClrExtension
import kaizen.plugins.clr.ClrPlugin
import kaizen.plugins.unity.internal.Booc
import kaizen.plugins.unity.internal.DefaultUnityLocator
import kaizen.plugins.unity.internal.ProjectExecHandler
import kaizen.plugins.unity.internal.ProjectPropertyUnityLocator
import kaizen.plugins.unity.internal.Mcs
import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.internal.os.OperatingSystem

class UnityPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.plugins.apply ClrPlugin

		def os = OperatingSystem.current()
		def locator = new ProjectPropertyUnityLocator(project, new DefaultUnityLocator(os))
		def execHandler = new ProjectExecHandler(project)
		def unity = project.extensions.create('unity', Unity, locator, os, execHandler)

		def clr = ClrExtension.forProject(project)
		clr.providers.add(unity)
		clr.compilers.add(new Mcs(unity))
		clr.compilers.add(new Booc(unity))
	}
}
