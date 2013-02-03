package kaizen.plugins.unity

import kaizen.commons.Paths
import kaizen.plugins.clr.FrameworkLocator
import kaizen.plugins.clr.MonoFramework
import org.apache.commons.lang3.SystemUtils
import org.gradle.api.Project

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
		if (SystemUtils.IS_OS_WINDOWS)
			return 'Unity.exe'
		if (SystemUtils.IS_OS_MAC_OSX)
			return 'Contents/MacOS/Unity'
		return 'Unity'
	}

	@Override
	String getFrameworkPath(String frameworkName) {
		def frameworksPath = SystemUtils.IS_OS_MAC_OSX ? 'Contents/Frameworks' : 'Data'
		Paths.combine absoluteUnityDir(), frameworksPath, frameworkName
	}

	def defaultUnityLocation() {
		SystemUtils.IS_OS_WINDOWS ?
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