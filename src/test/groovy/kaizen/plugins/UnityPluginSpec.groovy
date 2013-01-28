package kaizen.plugins

import spock.lang.*
import org.gradle.testfixtures.ProjectBuilder
import kaizen.foundation.SystemInformation
import kaizen.foundation.Paths

class UnityPluginSpec extends Specification {

	def 'mono path is resolved against unityDir property'() {

		given:
		def projectDir = DirectoryBuilder.createTempDir()
		def expectedCliExecutable = (
			SystemInformation.isWindows() ? 'Data\\Mono\\bin\\cli.bat'
		: SystemInformation.isMac() ? 'Contents/Frameworks/Mono/bin/cli'
		: /* linux */ 'Data/Mono/bin/cli')

		def bundle = new ProjectBuilder().withProjectDir(projectDir).build()
		bundle.apply plugin: UnityPlugin

		when:
		bundle.unity.unityDir = '../unity'

		then:
		def expectedPath = Paths.combine(projectDir.absolutePath, '../unity', expectedCliExecutable)
		expectedPath == bundle.unity.mono.cli

	}

	def 'tool executable can be changed'() {
		given:
		def bundle = new ProjectBuilder().build()
		bundle.apply plugin: UnityPlugin

		def mono = bundle.unity.mono

		when:
		mono.booc.executable = 'src/booc.exe'

		then:
		mono.booc.executable == 'src/booc.exe'
	}
}
