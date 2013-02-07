package kaizen.plugins.nunit

import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.ClrPlugin
import kaizen.plugins.clr.ClrProvider
import kaizen.plugins.clr.internal.DefaultClrExecSpec
import kaizen.testing.PluginSpecification
import org.gradle.util.ConfigureUtil

class NUnitTaskSpec extends PluginSpecification {

	def 'executes nunit-console with the correct command line'() {
		given:
		def project = projectWithName('p')
		def clrProvider = Mock(ClrProvider)
		def clr = Mock(Clr)
		def clrExecSpec = new DefaultClrExecSpec()

		when:
		project.plugins.apply(ClrPlugin)
		project.extensions.clr.providers.add(clrProvider)

		configure(project) {
			task('test', type: NUnitTask) {
				inputs.file 'build/p.tests.dll'
			}
		}
		executeTask(project.tasks.test)

		then:
		1 * clrProvider.runtimeForFrameworkVersion('v3.5') >> clr
		1 * clr.exec({ ConfigureUtil.configure(it, clrExecSpec) }) >> null

		def projectPath = { project.file(it).toString() }
		def expectedExecutable = projectPath('lib/NUnit/nunit-console.exe')
		def expectedWorkDir = projectPath('build')
		def expectedAssembly = projectPath('build/p.tests.dll')

		clrExecSpec.args == [expectedExecutable, '-nologo', '-nodots', '-domain:none', "-work=$expectedWorkDir", expectedAssembly]
	}
}
