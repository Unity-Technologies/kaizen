package kaizen.plugins.vs

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.NamedDomainObjectSet
import org.gradle.util.ConfigureUtil

class VSSolutionItem {

	final Project gradleProject
	final String name
	final NamedDomainObjectSet<Project> projects
	final NamedDomainObjectContainer<VSSolutionFolder> folders
	final VSSolutionItem parent

	protected VSSolutionItem(VSSolutionItem parent, String name, Project gradleProject) {
		this.parent = parent
		this.name = name
		this.gradleProject = gradleProject
		this.projects = gradleProject.container(Project)
		this.folders = gradleProject.container(VSSolutionFolder) {
			newSolutionFolder(it)
		}
	}

	def eachFolderRecurse(Closure closure) {
		folders.each {
			it.eachFolderRecurse(closure)
			closure(it)
		}
	}

	void eachChildGuid(Closure closure) {
		projects.each {
			closure(VSExtension.forProject(it).project.guid)
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
		new VSSolutionFolder(this, name, gradleProject)
	}
}
