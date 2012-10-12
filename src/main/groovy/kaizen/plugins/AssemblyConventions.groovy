package kaizen.plugins

import org.gradle.api.Project

class AssemblyConventions {
	static boolean isTest(Project p) {
		return p.name.endsWith('.Tests')
	}
}
