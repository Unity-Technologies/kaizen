package kaizen.plugins.core

import org.gradle.api.artifacts.Configuration

class Configurations {
	static String labelFor(Configuration config) {
		config.name.capitalize()
	}
}
