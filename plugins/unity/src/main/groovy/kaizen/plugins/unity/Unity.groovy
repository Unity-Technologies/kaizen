package kaizen.plugins.unity

import kaizen.commons.Paths
import org.gradle.api.Project
import kaizen.plugins.unity.internal.FrameworkLocator
import kaizen.plugins.unity.internal.MonoFramework
import org.gradle.internal.os.OperatingSystem

class Unity implements FrameworkLocator {

	def unityDir

	final MonoFramework mono

	final MonoFramework monoBleedingEdge

	final Project project

	final OperatingSystem operatingSystem

	Unity(Project project, OperatingSystem operatingSystem) {
		this.project = project
		this.operatingSystem = operatingSystem
		this.unityDir = defaultUnityLocation()
		this.mono = new MonoFramework(operatingSystem, this, 'Mono')
		this.monoBleedingEdge = new MonoFramework(operatingSystem, this, 'MonoBleedingEdge')
	}

	def getExecutable() {
		Paths.combine absoluteUnityDir(), relativeExecutablePath()
	}

	String relativeExecutablePath() {
		if (operatingSystem.windows)
			return 'Unity.exe'
		if (operatingSystem.macOsX)
			return 'Contents/MacOS/Unity'
		return 'Unity'
	}

	@Override
	String getFrameworkPath(String frameworkName) {
		def frameworksPath = operatingSystem.macOsX ? 'Contents/Frameworks' : 'Data'
		Paths.combine absoluteUnityDir(), frameworksPath, frameworkName
	}

	def defaultUnityLocation() {
		operatingSystem.windows ?
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