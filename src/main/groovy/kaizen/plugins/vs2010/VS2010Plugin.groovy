package kaizen.plugins.vs2010

import org.gradle.api.Project
import org.gradle.api.Plugin
import groovy.text.SimpleTemplateEngine
import java.security.MessageDigest
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.Dependency

class VS2010Plugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		def vs2010Project = new VS2010Project(project)
		project.extensions.add('vs2010', vs2010Project)
		project.task('vs2010Project') {
			doFirst {
				def resource = VS2010Plugin.class.getResource('/templates/vs2010/csproj.template')
				def template = new SimpleTemplateEngine().createTemplate(resource)
				def templateResult = template.make(project: vs2010Project)
				vs2010Project.projectFile.withWriter { templateResult.writeTo(it) }
			}
		}
	}
}

class VS2010Project {
	final Project project

	VS2010Project(Project project) {
		this.project = project
	}

	File getProjectFile() {
		new File(project.projectDir, "${project.name}.csproj")
	}

	String getGuid() {
		"{${uuidFrom(project.name).toString().toUpperCase()}}"
	}


	private uuidFrom(String s) {
		new UUID(MessageDigest.getInstance("MD5").digest(s.bytes))
	}

	String getOutputType() {
		'Library'
	}

	String getAssemblyName() {
		project.extensions.assembly.name
	}

	Iterable<String> getSourceFiles() {
		project.fileTree(dir: project.projectDir, include: '**/*.cs').files.collect {
			project.relativePath(it)
		}
	}

	Iterable<VSProjectReference> getProjectReferences() {
		return projectDependencies.collect { projectReferenceFor(it) }
	}

	Iterable<Dependency> getExternalDependencies() {
		return dependencies().findAll { !(it instanceof ProjectDependency) }
	}

	private getProjectDependencies() {
		dependencies().findAll { it instanceof ProjectDependency }
	}

	private dependencies() {
		project.configurations.getByName('default').allDependencies
	}

	VSProjectReference projectReferenceFor(ProjectDependency projectDependency) {
		new VSProjectReference(project, projectDependency)
	}
}

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
		return project.relativePath(referencedProject.extensions.vs2010.projectFile)
	}

	private getReferencedProject() {
		projectDependency.dependencyProject
	}
}
