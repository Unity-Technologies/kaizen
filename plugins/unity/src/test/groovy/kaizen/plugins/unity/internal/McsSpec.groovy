package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrExecSpec
import kaizen.plugins.unity.Mono
import kaizen.plugins.unity.MonoProvider
import org.gradle.process.ExecResult
import org.gradle.util.ConfigureUtil
import spock.lang.Specification
import spock.lang.Unroll

class McsSpec extends Specification {

	@Unroll
	def 'executes mcs with the correct command line for #targetFramework'() {
		given:
		def monoProvider = Mock(MonoProvider)
		def mono = Mock(Mono)
		def clrExecSpec = Mock(ClrExecSpec)
		def clrExecResult = Mock(ExecResult)

		def compiler = new Mcs(monoProvider)
		def sources = [new File('/tmp/a.cs'), new File('/tmp/b.cs')]
		def output = new File('/tmp/file.dll')
		def mcsExe = 'lib/mono/2.0/mcs.exe'

		def expectedArgs = []
		expectedArgs.add "-sdk:$expectedSdk"
		expectedArgs.addAll sources*.canonicalPath
		expectedArgs.add '-target:library'
		expectedArgs.add "-out:$output.canonicalPath"
		expectedArgs.addAll assemblyReferences.collect { "-r:$it" }

		when:
		def result = compiler.exec {
			targetFrameworkVersion targetFramework
			sourceFiles sources
			outputAssembly output
			references assemblyReferences
		}

		then:
		_ * monoProvider.runtimeForFrameworkVersion('v3.5') >> mono
		1 * mono.lib('2.0', 'mcs.exe') >> mcsExe
		_ * monoProvider.runtimeForFrameworkVersion(targetFramework) >> mono
		1 * mono.exec { ConfigureUtil.configure(it, clrExecSpec) } >> clrExecResult
		1 * clrExecSpec.executable(mcsExe)
		1 * clrExecSpec.args(expectedArgs)
		0 * _
		result == clrExecResult

		where:
		targetFramework | expectedSdk | assemblyReferences
		'v3.5'					| 2           | ['System.Xml']
		'v4.0'					| 4           | []
	}
}
