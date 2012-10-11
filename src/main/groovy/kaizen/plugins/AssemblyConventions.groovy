package kaizen.plugins

import org.gradle.api.Project

class AssemblyConventions {
	static boolean shouldBeTreatedAsTestProject(Project p) {
		return p.name.endsWith('.Tests')
	}
}
