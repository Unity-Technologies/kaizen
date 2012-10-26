package kaizen.plugins.assembly

import org.gradle.api.Project

class AssemblyExtension {

	final Project project
	String name
	String target = 'library'
	def keyFile

	AssemblyExtension(Project project) {
		this.project = project
		this.name = project.name
	}

	String getFileName() {
		"${name}.${extension}"
	}

	String getXmlDocFileName() {
		"${name}.xml"
	}

	String getExtension() {
		assert target in ['library', 'exe', 'winexe']
		target.endsWith('exe') ? 'exe' : 'dll'
	}
}
