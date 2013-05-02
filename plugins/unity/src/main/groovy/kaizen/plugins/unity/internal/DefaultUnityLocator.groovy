package kaizen.plugins.unity.internal

import kaizen.plugins.unity.UnityLocator
import org.gradle.internal.os.OperatingSystem

class DefaultUnityLocator implements UnityLocator {

	final OperatingSystem operatingSystem

	DefaultUnityLocator(OperatingSystem operatingSystem = OperatingSystem.current()) {
		this.operatingSystem = operatingSystem
	}

	@Override
	String getUnityLocation() {
		if (operatingSystem.windows)
			return 'C:\\Program Files (x86)\\Unity\\Editor'
		if (operatingSystem.macOsX)
			return '/Applications/Unity/Unity.app'
		return '/usr/lib/unity3d/Editor'
	}
}
