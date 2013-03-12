package kaizen.plugins.vs

import org.gradle.api.Project

class VSSolutionFolder extends VSSolutionItem {

	@Lazy String guid = GuidString.from(name)

	VSSolutionFolder(VSSolutionItem parent, String name, Project gradleProject) {
		super(parent, name, gradleProject)
	}

	@Override
	void eachChildGuid(Closure closure) {
		super.eachChildGuid(closure)
		folders.each {
			closure(it.guid)
		}
	}
}
