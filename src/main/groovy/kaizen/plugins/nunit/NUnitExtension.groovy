package kaizen.plugins.nunit

import org.gradle.api.Project

class NUnitExtension {
	final Project project
	def version = '2.6+'
	NUnitExtension(Project project) {
		this.project = project
	}
}
