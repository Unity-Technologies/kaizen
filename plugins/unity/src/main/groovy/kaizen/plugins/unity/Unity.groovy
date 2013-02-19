package kaizen.plugins.unity

import kaizen.commons.Paths
import kaizen.plugins.unity.internal.LegacyMonoFramework
import kaizen.plugins.unity.internal.BleedingEdgeMonoFramework
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

class Unity implements MonoProvider {

	static Unity forProject(Project project) {
		project.extensions.findByType(Unity)
	}

	def location

	final UnityLocator locator

	final OperatingSystem operatingSystem

	final ExecHandler execHandler

	Unity(UnityLocator locator, OperatingSystem operatingSystem, ExecHandler execHandler) {
		this.locator = locator
		this.operatingSystem = operatingSystem
		this.execHandler = execHandler
	}

	def getExecutable() {
		Paths.combine getLocation(), relativeExecutablePath()
	}

	String relativeExecutablePath() {
		if (operatingSystem.windows)
			return 'Unity.exe'
		if (operatingSystem.macOsX)
			return 'Contents/MacOS/Unity'
		return 'Unity'
	}

	String getLocation() {
		location ?: locator.unityLocation
	}

	/**
	 *
	 * @param frameworkVersion one of 'v3.5', 'v4.0', 'unity'
	 * @return
	 */
	@Override
	Mono runtimeForFrameworkVersion(String frameworkVersion) {
		if (frameworkVersion == 'v3.5')
			return mono35
		if (frameworkVersion == 'v4.0')
			return mono4
		if (frameworkVersion == 'unity')
			return monoUnity
		throw new IllegalArgumentException("$frameworkVersion not supported")
	}

	Mono getMono35() {
		new BleedingEdgeMonoFramework(operatingSystem, getFrameworkPath('MonoBleedingEdge'), execHandler, 'v2.0.50727')
	}

	Mono getMono4() {
		new BleedingEdgeMonoFramework(operatingSystem, getFrameworkPath('MonoBleedingEdge'), execHandler, 'v4.0')
	}

	Mono getMonoUnity() {
		new LegacyMonoFramework(operatingSystem, getFrameworkPath('Mono'), execHandler)
	}

	String getFrameworkPath(String frameworkName) {
		def frameworksPath = operatingSystem.macOsX ? 'Contents/Frameworks' : 'Data'
		Paths.combine getLocation(), frameworksPath, frameworkName
	}
}