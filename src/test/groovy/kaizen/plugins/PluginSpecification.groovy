package kaizen.plugins

import spock.lang.Specification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.util.ConfigureUtil

abstract class PluginSpecification extends Specification {

	Project projectWithName(String name) {
		projectBuilderWithName(name).build()
	}

	ProjectBuilder projectBuilderWithName(String name) {
		ProjectBuilder.builder().withName(name)
	}

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}

}
