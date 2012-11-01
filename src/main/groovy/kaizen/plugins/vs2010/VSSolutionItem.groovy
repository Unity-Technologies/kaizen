package kaizen.plugins.vs2010

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.NamedDomainObjectSet
import org.gradle.util.ConfigureUtil

class VSSolutionItem {

	final Project gradleProject
	final String name
	final NamedDomainObjectSet<Project> projects
	final NamedDomainObjectContainer<VSSolutionFolder> folders

	protected VSSolutionItem(String name, Project gradleProject) {
		this.name = name
		this.gradleProject = gradleProject
		this.projects = gradleProject.container(Project)
		this.folders = gradleProject.container(VSSolutionFolder) {
			newSolutionFolder(it)
		}
	}

	def getAllProjects() {
		projects + childProjects
	}

	def getChildProjects() {
		folders.collectMany { it.allProjects }
	}

	def project(String path) {
		projects.add(gradleProject.project(path))
	}

	def folder(String name, Closure configuration) {
		folders.add(ConfigureUtil.configure(configuration, newSolutionFolder(name)))
	}

	private newSolutionFolder(String name) {
		new VSSolutionFolder(name, gradleProject)
	}
}
