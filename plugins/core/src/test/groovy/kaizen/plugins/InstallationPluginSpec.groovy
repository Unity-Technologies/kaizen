package kaizen.plugins

import kaizen.testing.DirectoryBuilder
import kaizen.testing.PluginSpecification


class InstallationPluginSpec extends PluginSpecification {

	def 'applies all gradle files from kaizen.d directory'() {
		given:
		def projectDir = DirectoryBuilder.tempDirWith {
			dir('kaizen.d') {
				file('version.gradle', 'version = 42')
				file('other.gradle', 'ext { other = 42 }')
			}
		}
		def project = projectBuilderWithName('kaizen').withProjectDir(projectDir).build()

		when:
		project.apply plugin: InstallationPlugin

		then:
		project.version == 42
		project.property('other') == 42
	}
}
