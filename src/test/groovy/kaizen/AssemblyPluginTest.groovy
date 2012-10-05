import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class AssemblyPluginTest {
	@Test
	void assemblyFileName() {
		def project = ProjectBuilder.builder().withName('C').build()
		project.apply plugin: 'assembly'
		assertEquals("C.dll", project.assemblyFileName.toString())
	}

	@Test
	void defaultConfiguration() {
		def project = ProjectBuilder.builder().build()
		project.apply plugin: 'assembly'
		assertNotNull(project.configurations.getByName('editor'))
	}
}