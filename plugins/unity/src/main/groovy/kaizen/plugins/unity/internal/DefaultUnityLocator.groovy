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
		operatingSystem.windows ?
			'C:\\Program Files (x86)\\Unity\\Editor' :
			'/Applications/Unity/Unity.app'
	}
}
