package kaizen.plugins.unity

import kaizen.plugins.clr.ClrExtension
import kaizen.testing.PluginSpecification
import org.gradle.testfixtures.ProjectBuilder

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

	def 'installs c# compiler'() {
		expect:
		clr.compilerForLanguage('c#') != null
	}

	def getClr() {
		ClrExtension.forProject(project)
	}

	def getUnity() {
		Unity.forProject(project)
	}
}
