package kaizen.plugins

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ProjectDependency

class ProjectDependencies {
	static def projectsDependedUponBy(Configuration configuration) {
		configuration.allDependencies.findAll { it instanceof ProjectDependency }.collect { it.dependencyProject }
	}
}
