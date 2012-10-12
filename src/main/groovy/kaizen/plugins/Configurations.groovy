package kaizen.plugins

import org.gradle.api.artifacts.Configuration
import org.gradle.api.Project


class Configurations {

	static final String EDITOR = 'editor'
	static final String TEST = 'tests'

	static Configuration defaultConfigurationFor(Project project) {
		project.configurations[defaultConfigurationNameFor(project)]
	}

	static String defaultConfigurationNameFor(Project project) {
		ProjectClassifier.isTest(project) ? TEST : EDITOR
	}

	static void addDefaultAssemblyConfigurationTo(Project project) {
		if (AssemblyConventions.isTest(project)) {
			addTestsConfigurationTo(project)
			return
		}

		if (ProjectClassifier.isTest(project))
			return

		addEditorConfigurationTo(project)
	}

	static void addBundleConfigurationsTo(Project project) {
		addEditorConfigurationTo(project)
		addTestsConfigurationTo(project)
	}

	private static void addTestsConfigurationTo(Project project) {
		addConfigurationTo(project, TEST, 'Configuration for tests.')
	}

	private static void addEditorConfigurationTo(Project project) {
		addConfigurationTo(project, EDITOR, 'Configuration for editor extension artifacts.')
	}

	private static void addConfigurationTo(Project project, String name, String description) {
		def config = project.configurations.add(name)
		config.description = description
	}
}
