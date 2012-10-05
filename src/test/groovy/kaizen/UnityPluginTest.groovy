package kaizen

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import kaizen.foundation.SystemInformation
import static org.junit.Assert.assertEquals

class UnityPluginTest {

	@Test
	void monoPathIsResolvedAgainstUnityDirProperty() {

		def (projectDir, expectedGmcsExecutable) = SystemInformation.isWindows() ?
			['C:\\root\\project', 'C:\\root\\unity\\Data\\Mono\\bin\\gmcs.bat'] :
			['/root/project', '/root/unity/Contents/Frameworks/Mono/bin/gmcs']

		def bundle = new ProjectBuilder().withProjectDir(new File(projectDir)).build()
		bundle.apply plugin: 'unity'

		bundle.unity.unityDir = '../unity'
		assertEquals expectedGmcsExecutable, bundle.unity.tools.gmcs.executable

	}
}
