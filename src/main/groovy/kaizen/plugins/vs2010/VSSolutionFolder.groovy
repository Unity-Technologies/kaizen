package kaizen.plugins.vs2010

import org.gradle.api.Project

class VSSolutionFolder extends VSSolutionItem {

	@Lazy String guid = GuidString.from(name)

	VSSolutionFolder(String name, Project gradleProject) {
		super(name, gradleProject)
	}
}
