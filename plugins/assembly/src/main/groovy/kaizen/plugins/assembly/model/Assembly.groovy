package kaizen.plugins.assembly.model

import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.api.internal.DefaultDomainObjectSet
import org.gradle.util.ConfigureUtil

class Assembly {

	static def forProject(Project project) {
		project.extensions.findByType(Assembly)
	}

	final Project project
	String name
	String target = 'library'
	def keyFile
	String language
	final DomainObjectSet<AssemblyReference> references
	String targetFrameworkVersion = 'v3.5'

	Assembly(Project project) {
		this.project = project
		this.name = project.name
		this.language = detectLanguageOf(project)
		this.references = new DefaultDomainObjectSet<AssemblyReference>(AssemblyReference.class)
	}

	def references(Closure<AssemblyReferencesHandler> configuration) {
		ConfigureUtil.configure(configuration, new AssemblyReferencesHandler(references))
	}

	private detectLanguageOf(Project project) {
		project.projectDir.listFiles({ dir, name -> name.endsWith('.boo') } as FilenameFilter).any() ? 'boo' : 'cs'
	}

	Iterable<File> getSourceFiles() {
		project.fileTree(dir: project.projectDir, include: "**/*.$language")
	}

	String getFileName() {
		"${name}.${extension}"
	}

	String getXmlDocFileName() {
		"${name}.xml"
	}

	String getExtension() {
		assert target in ['library', 'exe', 'winexe']
		isExecutable() ? 'exe' : 'dll'
	}

	boolean isExecutable() {
		target.endsWith('exe')
	}
}