package kaizen.plugins.clr

import org.gradle.api.Plugin
import org.gradle.api.Project

class ClrPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.extensions.create('clr', ClrExtension)
	}
}
