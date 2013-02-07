package kaizen.plugins.unity

import kaizen.testing.PluginSpecification

class InstallationPluginSpec extends PluginSpecification {

	def 'applies all gradle files from kaizen.d directory'() {
		given:
		def project = projectWithDirectoryStructure {
			dir('kaizen.d') {
				file('version.gradle', 'version = 42')
				file('other.gradle', 'ext { other = 42 }')
			}
		}

		when:
		project.plugins.apply(InstallationPlugin)

		then:
		project.version == 42
		project.property('other') == 42
	}
}
