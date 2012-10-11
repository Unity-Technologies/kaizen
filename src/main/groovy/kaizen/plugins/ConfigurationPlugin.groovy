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

		if (ProjectClassifier.isTest(project)) {
			addTestsConfigurationTo(project)
			return
		}

		addEditorConfigurationTo(project)
	}

	static void addBundleConfigurationsTo(Project project) {
		addEditorConfigurationTo(project)
		addTestsConfigurationTo(project)
	}

	private static void addTestsConfigurationTo(Project project) {
		addConfigurationTo(project, TESTS, 'Configuration for tests.')
	}

	private static void addEditorConfigurationTo(Project project) {
		addConfigurationTo(project, EDITOR, 'Configuration for editor extension artifacts.')
	}

	private static void addConfigurationTo(Project project, String name, String description) {
		def config = project.configurations.add(name)
		config.description = description
	}

	private static String defaultConfigurationNameFor(Project project) {
		ProjectClassifier.isTest(project) ? TESTS : EDITOR
	}
}
