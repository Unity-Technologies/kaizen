package kaizen.plugins.unity.internal

import kaizen.plugins.unity.ExecHandler
import org.gradle.api.Project
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

class ProjectExecHandler implements ExecHandler {

	final Project project

	ProjectExecHandler(Project project) {
		this.project = project
	}

	@Override
	ExecResult exec(Closure<ExecSpec> execSpecConfig) {
		project.exec execSpecConfig
	}
}
