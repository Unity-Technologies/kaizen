package kaizen.plugins.vs

import kaizen.plugins.assembly.model.AssemblyReference
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency

import kaizen.plugins.assembly.model.Assembly

class VSProject {
	final Project project
	@Lazy String guid = GuidString.from(project.name)

	VSProject(Project project) {
		this.project = project
	}

	String getTargetFrameworkVersion() {
		assembly.targetFrameworkVersion
	}

	File getFile() {
		project.file("${project.name}.csproj")
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
		projectDependencies.collect { projectReferenceFor(it) }
	}

	Iterable<Dependency> getExternalDependencies() {
		dependencies().findAll { !isProjectDependency(it) }
	}

	Iterable<AssemblyReference> getAssemblyReferences() {
		assembly.references
	}

	boolean getIsSupportedLanguage() {
		assembly?.language == 'cs'
	}

	Assembly getAssembly() {
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
