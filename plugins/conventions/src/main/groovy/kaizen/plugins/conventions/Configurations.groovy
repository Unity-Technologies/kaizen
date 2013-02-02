package kaizen.plugins.conventions

import org.gradle.api.artifacts.Configuration

class Configurations {
	static String labelFor(Configuration config) {
		labelFor(config.name)
	}

	static String labelFor(String configName) {
		configName.capitalize()
	}
}
