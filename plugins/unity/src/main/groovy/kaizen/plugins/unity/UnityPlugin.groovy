package kaizen.plugins.unity

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.internal.os.OperatingSystem

class UnityPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.extensions.create('unity', Unity, project, OperatingSystem.current())
	}
}
