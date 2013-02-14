package kaizen.plugins.unity

import kaizen.commons.Paths
import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.ClrProvider
import kaizen.plugins.unity.internal.MonoFramework
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

class Unity implements ClrProvider, MonoProvider {

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

	@Override
	Clr runtimeForFrameworkVersion(String frameworkVersion) {
		if (frameworkVersion == 'v3.5')
			return mono
		throw new IllegalArgumentException("$frameworkVersion not supported")
	}

	@Override
	Mono getMono() {
		new MonoFramework(operatingSystem, getFrameworkPath('MonoBleedingEdge'), execHandler)
	}

	String getFrameworkPath(String frameworkName) {
		def frameworksPath = operatingSystem.macOsX ? 'Contents/Frameworks' : 'Data'
		Paths.combine getLocation(), frameworksPath, frameworkName
	}
}