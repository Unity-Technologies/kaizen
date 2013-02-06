package kaizen.plugins.unity

import kaizen.commons.Paths
import kaizen.testing.DirectoryBuilder
import org.gradle.internal.os.OperatingSystem
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class UnityPluginSpec extends Specification {

	def 'mono path is resolved against unityDir property'() {

		given:
		def projectDir = DirectoryBuilder.createTempDir()
		def expectedCliExecutable = (
			OperatingSystem.current().windows ? 'Data\\Mono\\bin\\cli.bat'
			: OperatingSystem.current().macOsX ? 'Contents/Frameworks/Mono/bin/cli'
			: /* linux */ 'Data/Mono/bin/cli')

		def bundle = new ProjectBuilder().withProjectDir(projectDir).build()
		bundle.apply plugin: 'unity'

		when:
		bundle.unity.unityDir = '../unity'

		then:
		def expectedPath = Paths.combine(projectDir.absolutePath, '../unity', expectedCliExecutable)
		expectedPath == bundle.unity.mono.cli

	}

	def 'tool executable can be changed'() {
		given:
		def bundle = new ProjectBuilder().build()
		bundle.apply plugin: 'unity'

		def mono = bundle.unity.mono

		when:
		mono.booc.executable = 'src/booc.exe'

		then:
		mono.booc.executable == 'src/booc.exe'
	}
}
