package kaizen.plugins.vs2010

import org.gradle.api.Project

class VSSolution extends VSSolutionItem {
	def destinationFile

	VSSolution(String name, Project gradleProject) {
		super(name, gradleProject)
	}

	String relativePathOf(Project p) {
		gradleProject.relativePath(p.extensions.vs.project.file)
	}
}
