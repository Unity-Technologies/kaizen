package kaizen.plugins

import spock.lang.*
import org.gradle.testfixtures.ProjectBuilder
import kaizen.foundation.SystemInformation

class UnityPluginSpec extends Specification {

	def 'mono path is resolved against unityDir property'() {

		setup:
		def (projectDir, expectedCliExecutable) = (
			SystemInformation.isWindows() ? ['C:\\root\\project', 'C:\\root\\unity\\Data\\Mono\\bin\\cli.bat']
			: SystemInformation.isMac() ? ['/root/project', '/root/unity/Contents/Frameworks/Mono/bin/cli']
			: /* linux */ ['/root/project', '/root/unity/Data/Mono/bin/cli'])

		def bundle = new ProjectBuilder().withProjectDir(new File(projectDir)).build()
		bundle.apply plugin: UnityPlugin

		when:
		bundle.unity.unityDir = '../unity'

		then:
		expectedCliExecutable == bundle.unity.mono.cli

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
