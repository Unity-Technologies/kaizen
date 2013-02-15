package kaizen.plugins.unity

import kaizen.plugins.clr.ClrExtension
import kaizen.testing.PluginSpecification
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Unroll

class UnityPluginSpec extends PluginSpecification {

	def project = ProjectBuilder.newInstance().build()

	@Override
	def setup() {
		project.plugins.apply UnityPlugin
	}

	def 'installs unity as clr provider'() {
		expect:
		clr.providers.contains(unity)
	}

	@Unroll
	def 'installs #language compiler'() {
		expect:
		clr.compilerForLanguage(language) != null

		where:
		language << ['c#', 'boo']
	}

	def getClr() {
		ClrExtension.forProject(project)
	}

	def getUnity() {
		Unity.forProject(project)
	}
}
