package kaizen.testing

import org.gradle.internal.os.OperatingSystem
import spock.lang.Specification

class OperatingSystemSensitiveSpecification extends Specification {

	def windows() {
		Mock(OperatingSystem) {
			1 * isWindows() >> true
			_ * getScriptName(_) >> { args -> "${args[0]}.bat" }
			_ * getExecutableName(_) >> { args -> "${args[0]}.exe" }
			_ * toString() >> 'windows'
		}
	}

	def osx() {
		Mock(OperatingSystem) {
			1 * isMacOsX() >> true
			_ * getScriptName(_) >> { args -> args[0] }
			_ * getExecutableName(_) >> { args -> args[0] }
			_ * toString() >> 'osx'
		}
	}

	def linux() {
		Mock(OperatingSystem) {
			1 * isLinux() >> true
			_ * getScriptName(_) >> { args -> args[0] }
			_ * getExecutableName(_) >> { args -> args[0] }
			_ * toString() >> 'linux'
		}
	}
}
