package kaizen.plugins.vs2010

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class VSProjectReference {
	final Project project
	final ProjectDependency projectDependency

	VSProjectReference(Project project, ProjectDependency projectDependency) {
		this.project = project
		this.projectDependency = projectDependency
	}

	String getName() {
		projectDependency.name
	}

	String getGuid() {
		referencedVsProject.guid
	}

	private getReferencedVsProject() {
		referencedProject.extensions.vs.project
	}

	String getRelativePath() {
		return project.relativePath(referencedVsProject.file).replace('/', '\\')
	}

	private getReferencedProject() {
		projectDependency.dependencyProject
	}
}
