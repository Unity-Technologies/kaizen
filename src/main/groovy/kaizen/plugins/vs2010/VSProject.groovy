package kaizen.plugins.vs2010

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import kaizen.plugins.assembly.AssemblyExtension

class VSProject {
	final Project project
	@Lazy String guid = GuidString.from(project.name)
	String targetFrameworkVersion = 'v3.5'

	VSProject(Project project) {
		this.project = project
	}

	File getFile() {
		new File(project.projectDir, "${project.name}.csproj")
	}

	String getOutputType() {
		def target = assembly.target
		target == 'winexe' ? 'WinExe' : target.capitalize()
	}

	String getAssemblyName() {
		project.extensions.assembly.name
	}

	Iterable<String> getSourceFiles() {
		project.fileTree(dir: project.projectDir, include: '**/*.cs').files.collect {
			project.relativePath(it).replace('/', '\\') // always use \ on vs project files
		}
	}

	Iterable<VSProjectReference> getProjectReferences() {
		return projectDependencies.collect { projectReferenceFor(it) }
	}

	Iterable<Dependency> getExternalDependencies() {
		return dependencies().findAll { !isProjectDependency(it) }
	}

	boolean getIsSupportedLanguage() {
		assembly?.language == 'cs'
	}

	AssemblyExtension getAssembly() {
		project.extensions.findByName('assembly')
	}

	private getProjectDependencies() {
		dependencies().findAll { isProjectDependency(it) }
	}

	private isProjectDependency(Dependency d) {
		if (d instanceof ProjectDependency) {
			def vs = d.dependencyProject.extensions.findByName('vs')
			if (vs)
				return vs.project.isSupportedLanguage
		}
		return false
	}

	private dependencies() {
		project.configurations.getByName('default').allDependencies
	}

	VSProjectReference projectReferenceFor(ProjectDependency projectDependency) {
		new VSProjectReference(project, projectDependency)
	}
}
