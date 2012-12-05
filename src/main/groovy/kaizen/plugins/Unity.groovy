package kaizen.plugins

import org.gradle.api.Project

import kaizen.foundation.SystemInformation
import kaizen.foundation.Paths

class Unity implements FrameworkLocator {

	def unityDir

	final MonoFramework mono

	final MonoFramework monoBleedingEdge

	final Project project

	Unity(Project project) {
		this.project = project
		this.unityDir = defaultUnityLocation()
		this.mono = new MonoFramework(this, 'Mono')
		this.monoBleedingEdge = new MonoFramework(this, 'MonoBleedingEdge')
	}

	def getExecutable() {
		Paths.combine absoluteUnityDir(), relativeExecutablePath()
	}

	String relativeExecutablePath() {
		switch (SystemInformation.systemFamily) {
			case SystemInformation.WINDOWS:
				return 'Unity.exe'
			case SystemInformation.MAC:
				return 'Contents/MacOS/Unity'
			default:
				return 'Unity'
		}
	}

	@Override
	String getFrameworkPath(String frameworkName) {
		def frameworksPath = SystemInformation.isMac() ? 'Contents/Frameworks' : 'Data'
		Paths.combine absoluteUnityDir(), frameworksPath, frameworkName
	}

	def defaultUnityLocation() {
		SystemInformation.isWindows() ?
			'C:\\Program Files (x86)\\Unity\\Editor' :
			'/Applications/Unity/Unity.app'
	}

	private absoluteUnityDir() {
		project.file(configuredUnityDir).absolutePath
	}

	def getConfiguredUnityDir() {
		project.hasProperty('unityDir') ? project.property('unityDir') : unityDir
	}
}