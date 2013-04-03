package kaizen.plugins.refactorings

import org.gradle.api.Plugin
import org.gradle.api.Project

class ProjectRefactoringsPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.task(type: RenameProjectTask, 'renameProject')
		project.task(type: CreateProjectTask, 'createProject')
	}
}
