package kaizen.plugins.vs

import kaizen.plugins.assembly.model.AssemblyReference
import kaizen.plugins.clr.ClrLanguageNames
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
		assembly.name
	}

	Iterable<String> getSourceFiles() {
		assembly.sourceFiles.collect {
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
		assembly?.language == ClrLanguageNames.CSHARP
	}

	Assembly getAssembly() {
		project.extensions.findByName('assembly')
	}

	private getProjectDependencies() {
		dependencies().findAll { isProjectDependency(it) }
	}

	private boolean isProjectDependency(Dependency d) {
		if (d instanceof ProjectDependency)
			VSExtension.forProject(d.dependencyProject)?.project.isSupportedLanguage
		else
			false
	}

	private dependencies() {
		project.configurations.getByName('default').allDependencies
	}

	VSProjectReference projectReferenceFor(ProjectDependency projectDependency) {
		new VSProjectReference(project, projectDependency)
	}
}
