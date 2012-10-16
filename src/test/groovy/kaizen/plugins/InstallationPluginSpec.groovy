package kaizen.plugins

class InstallationPluginSpec extends PluginSpecification {

	def 'applies all gradle files from kaizen.d directory'() {
		given:
		def projectDir = createTempDir()
		def kaizenD = new File(projectDir, 'kaizen.d').with { it.mkdir(); it }
		new File(kaizenD, 'version.gradle').text = 'version = 42'
		new File(kaizenD, 'other.gradle').text = 'ext { other = 42 }'

		def project = projectBuilderWithName('kaizen').withProjectDir(projectDir).build()

		when:
		project.apply plugin: InstallationPlugin

		then:
		project.version == 42
		project.property('other') == 42
	}

	private File createTempDir() {
		File.createTempFile("kaizen", ".tmp").with { file ->
			file.delete()
			file.mkdir()
			file
		}
	}
}
