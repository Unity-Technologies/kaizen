package kaizen.plugins.unity

import kaizen.commons.Paths
import kaizen.plugins.unity.internal.MonoFramework
import kaizen.testing.OperatingSystemSensitiveSpecification
import spock.lang.Unroll

class UnitySpec extends OperatingSystemSensitiveSpecification {

	@Unroll
	def 'default executable for #operatingSystem is #executable'() {

		given:
		def unity = new Unity(null, operatingSystem, null)

		when:
		unity.location = '../unity'

		then:
		unity.executable == Paths.combine('../unity', executable)

		where:
		operatingSystem | executable
		windows()       | 'Unity.exe'
		osx()           | 'Contents/MacOS/Unity'
		linux()         | 'Unity'
	}

	def 'executable resolved against locator when location is not set'() {
		given:
		def unityLocation = 'Unity.app'
		def unityLocator = Mock(UnityLocator)
		def unity = new Unity(unityLocator, operatingSystem, null)

		when:
		def unityExecutable = unity.executable

		then:
		1 * unityLocator.getUnityLocation() >> unityLocation
		unityExecutable == Paths.combine(unityLocation, executable)

		where:
		operatingSystem | executable
		windows()       | 'Unity.exe'
		osx()           | 'Contents/MacOS/Unity'
		linux()         | 'Unity'
	}

	@Unroll
	def 'clr location for target framework #targetFramework on #operatingSystem is #cli'() {
		given:
		def unityLocation = 'Unity.app'
		def unity = new Unity({ unityLocation} as UnityLocator, operatingSystem, null)

		when:
		def clr = unity.runtimeForFrameworkVersion(targetFramework)

		then:
		(clr as MonoFramework).cli == Paths.combine(unityLocation, cli)

		where:
		operatingSystem | targetFramework | cli
		windows()       |  'v3.5'         | 'Data/MonoBleedingEdge/bin/cli.bat'
		osx()           |  'v3.5'         | 'Contents/Frameworks/MonoBleedingEdge/bin/cli'
		linux()         |  'v3.5'         | 'Data/MonoBleedingEdge/bin/cli'
		windows()       |  'unity'        | 'Data/Mono/bin/cli_unity.bat'
		osx()           |  'unity'        | 'Contents/Frameworks/Mono/bin/cli_unity'
		linux()         |  'unity'        | 'Data/Mono/bin/cli_unity'
	}
}

