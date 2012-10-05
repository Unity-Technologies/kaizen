package kaizen.plugins

import org.gradle.api.Project

import kaizen.foundation.SystemInformation
import kaizen.foundation.Paths

class Unity implements MonoFramework {

	def unityDir

	final UnityTools tools

	final Project project

	Unity(Project project) {
		this.project = project
		this.tools = new UnityTools(this)
	}

	String monoExecutable(String name) {
		platformSpecificExecutable monoBinPath(name)
	}

	String platformSpecificExecutable(String executable) {
		SystemInformation.isWindows() ? "${executable}.bat" : executable
	}

	String monoBinPath(String path) {
		Paths.combine monoPath("bin"), path
	}

	String monoPath(String relativePath) {
		Paths.combine absoluteUnityDir(), "Data", "Mono", relativePath
	}

	private absoluteUnityDir() {
		project.file(configuredUnityDir()).absolutePath
	}

	private configuredUnityDir() {
		project.hasProperty('unityDir') ? project.unityDir : unityDir
	}
}


class UnityTools {
	Unity parent

	UnityTools(Unity parent) {
		this.parent = parent
	}

	MonoTool getGmcs() {
		new MonoTool(parent, "gmcs")
	}
}


interface MonoFramework {
	String monoExecutable(String name)
}


class MonoTool {
	final MonoFramework framework
	final String name

	MonoTool(MonoFramework framework, String name) {
		this.framework = framework
		this.name = name
	}

	String getExecutable() {
		framework.monoExecutable(name)
	}
}

