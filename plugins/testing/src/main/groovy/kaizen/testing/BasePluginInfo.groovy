package kaizen.testing

import org.gradle.testfixtures.ProjectBuilder

class BasePluginInfo {
	static def configurationsContributedByBasePlugin() {
		return ProjectBuilder.builder().build().with {
			it.apply plugin: 'base'
			it.configurations*.name
		}
	}
}
