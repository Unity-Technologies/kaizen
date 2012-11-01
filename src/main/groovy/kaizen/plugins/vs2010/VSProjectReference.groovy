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
		referencedProject.extensions.vs2010.guid
	}

	String getRelativePath() {
		return project.relativePath(referencedProject.extensions.vs2010.projectFile).replace('/', '\\')
	}

	private getReferencedProject() {
		projectDependency.dependencyProject
	}
}
