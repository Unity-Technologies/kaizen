package kaizen.plugins.vs

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class VSExtension {

	static VSExtension forProject(Project project) {
		project.extensions.findByType(VSExtension)
	}

	final Project gradleProject
	final VSProject project

	final NamedDomainObjectContainer<VSSolution> solutionContainer

	VSExtension(Project project) {
		this.gradleProject = project
		this.project = new VSProject(project)
		this.solutionContainer = project.container(VSSolution) {
			def solution = new VSSolution(it, gradleProject)
			solution.destinationFile = gradleProject.file("${it}.sln")
			solution
		}
	}

	def getSolutions() {
		solutionContainer
	}

	def solutions(Closure configuration) {
		this.solutionContainer.configure configuration
	}
}



