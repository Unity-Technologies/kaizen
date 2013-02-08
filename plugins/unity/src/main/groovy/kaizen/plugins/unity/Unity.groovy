package kaizen.plugins.unity

import kaizen.commons.Paths
import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.ClrProvider
import kaizen.plugins.unity.internal.MonoFramework
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

class Unity implements ClrProvider {

	static Unity forProject(Project project) {
		project.extensions.findByType(Unity)
	}

	def location

	final UnityLocator locator

	final OperatingSystem operatingSystem

	Unity(UnityLocator locator, OperatingSystem operatingSystem) {
		this.locator = locator
		this.operatingSystem = operatingSystem
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
		new MonoFramework(operatingSystem, getFrameworkPath('MonoBleedingEdge'), frameworkVersion)
	}

	@Override
	String getFrameworkPath(String frameworkName) {
		def frameworksPath = operatingSystem.macOsX ? 'Contents/Frameworks' : 'Data'
		Paths.combine getLocation(), frameworksPath, frameworkName
	}
}