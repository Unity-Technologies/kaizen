package kaizen.plugins.unity

import org.gradle.api.Project
import org.gradle.api.Plugin

class UnityPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.extensions.add('unity', new Unity(project))
	}
}
