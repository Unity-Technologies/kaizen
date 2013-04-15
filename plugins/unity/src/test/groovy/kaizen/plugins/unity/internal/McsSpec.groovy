package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompileSpec
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
		def embeddedResources = ['resourceA': new File('/tmp/resources/a.txt'), 'resourceB': new File('/tmp/resources/b.png')]
		def output = new File('/tmp/file.dll')
		def xmldoc = new File('/tmp/file.xml')
		def mcsExe = 'lib/mono/2.0/mcs.exe'

		def expectedArgs = []
		expectedArgs.add "-sdk:$expectedSdk"
		expectedArgs.addAll sources*.canonicalPath
		expectedArgs.add '-target:library'
		expectedArgs.add "-out:$output.canonicalPath"
		expectedArgs.add "-doc:$xmldoc.canonicalPath"
		//args << "-nowarn:1591"
		expectedArgs.addAll assemblyReferences.collect { "-r:$it" }
		expectedArgs.addAll embeddedResources.collect { "-resource:$it.value,$it.key" }
		expectedArgs.addAll defines.collect { "-define:$it" }

		when:
		def result = compiler.exec { ClrCompileSpec spec ->
			spec.targetFrameworkVersion targetFramework
			spec.sourceFiles sources
			spec.outputAssembly output
			spec.outputXmlDoc xmldoc
			spec.references assemblyReferences
			spec.embeddedResources embeddedResources
			spec.defines defines
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
		targetFramework | expectedSdk | assemblyReferences | defines
		'v3.5'					| 2           | ['System.Xml']     | ['TRACE', 'DEBUG']
		'v4.0'					| 4           | []                 | []
	}
}
