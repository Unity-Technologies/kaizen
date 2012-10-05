package kaizen.plugins

import org.gradle.api.artifacts.Dependency

class AssemblyDependency implements Dependency {

	final String name

	AssemblyDependency(String name) {
		this.name = name
	}

	String getGroup() { "BCL" }
	String getName() { name }
	String getVersion() { "latest.integration" }
	Dependency copy() { new AssemblyDependency(name) }
	boolean contentEquals(Dependency other) {
		other instanceof AssemblyDependency && name == ((AssemblyDependency)other).name
	}
}
