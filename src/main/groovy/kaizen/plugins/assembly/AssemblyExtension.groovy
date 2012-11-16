package kaizen.plugins.assembly

import org.gradle.api.Project

class AssemblyExtension {

	final Project project
	String name
	String target = 'library'
	def keyFile
	String language

	AssemblyExtension(Project project) {
		this.project = project
		this.name = project.name
		this.language = detectLanguageOf(project)
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
