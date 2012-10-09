package kaizen.plugins

import org.gradle.api.artifacts.Configuration
import org.gradle.api.Project
import org.gradle.api.Plugin

class ConfigurationPlugin implements Plugin<Project> {

	static final String EDITOR = 'editor'
	static final String TESTS = 'tests'

	static Configuration defaultConfigurationFor(Project project) {
		project.configurations[defaultConfigurationNameFor(project)]
	}

	@Override
	void apply(Project project) {
		project.configurations.add(EDITOR) {
			description 'Configuration for editor extension artifacts.'
		}

		project.configurations.add(TESTS) {
			description 'Configuration for test assemblies.'
		}
	}

	private static String defaultConfigurationNameFor(Project project) {
		ProjectClassifier.isTestProject(project) ? TESTS : EDITOR
	}
}
