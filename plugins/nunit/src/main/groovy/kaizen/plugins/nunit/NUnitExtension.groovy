package kaizen.plugins.nunit

import org.gradle.api.Project
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.util.ConfigureUtil

class NUnitExtension {
	private final Project project
	public final NamedDomainObjectContainer<NUnitConfiguration> testConfigurations
	public def version = '2.6.1.12217'

	NUnitExtension(Project project) {
		this.project = project
		this.testConfigurations = project.container(NUnitConfiguration)
	}

	def testConfigurations(Closure closure) {
		ConfigureUtil.configure(closure, testConfigurations)
	}
}

