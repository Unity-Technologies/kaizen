package kaizen.plugins.unity.internal

import kaizen.commons.Paths
import kaizen.plugins.unity.ExecHandler
import kaizen.testing.OperatingSystemSensitiveSpecification
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.gradle.util.ConfigureUtil

class MonoFrameworkSpec extends OperatingSystemSensitiveSpecification {

	def 'exec runs in debug mode by default'() {
		given:
		def execHandler = Mock(ExecHandler)
		def mono = new MonoFramework(operatingSystem, 'Mono', execHandler)

		def execSpec = Mock(ExecSpec)
		def execResult = Mock(ExecResult)

		def expectedMonoExePath = Paths.combine 'Mono', 'bin', monoExecutable

		when:
		def result = mono.exec {
			executable 'foo.exe'
			args 'a', 'b', 'c'
		}

		then:
		1 * execHandler.exec({ ConfigureUtil.configure(it, execSpec) }) >> execResult
		1 * execSpec.executable(expectedMonoExePath)
		1 * execSpec.args('--debug')
		1 * execSpec.args('foo.exe')
		1 * execSpec.args(['a', 'b', 'c'])
		result == execResult
		0 * _

		where:
		operatingSystem | monoExecutable
		windows()       | 'mono.exe'
		osx()           | 'mono'
	}
}
