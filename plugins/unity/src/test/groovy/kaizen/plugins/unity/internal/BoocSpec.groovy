package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrExecSpec
import kaizen.plugins.unity.Mono
import kaizen.plugins.unity.MonoProvider
import org.gradle.process.ExecResult
import org.gradle.util.ConfigureUtil
import spock.lang.Specification
import spock.lang.Unroll

class BoocSpec extends Specification {

	@Unroll
	def 'executes booc with the correct command line for #targetFramework'() {
		given:
		def monoProvider = Mock(MonoProvider)
		def mono = Mock(Mono)
		def clrExecSpec = Mock(ClrExecSpec)
		def clrExecResult = Mock(ExecResult)

		def compiler = new Booc(monoProvider)
		def sources = [new File('/tmp/a.boo'), new File('/tmp/b.boo')]
		def output = new File('/tmp/file.dll')
		def boocExe = "booc.exe"

		def expectedArgs = []
		expectedArgs.add '-target:library'
		expectedArgs.add "-out:$output.canonicalPath"
		expectedArgs.addAll assemblyReferences.collect { "-r:$it" }
		expectedArgs.addAll sources*.canonicalPath

		when:
		def result = compiler.exec {
			targetFrameworkVersion targetFramework
			outputAssembly output
			references assemblyReferences
			sourceFiles sources
		}

		then:
		_ * monoProvider.runtimeForFrameworkVersion(targetFramework) >> mono
		1 * mono.lib(expectedProfile, 'booc.exe') >> boocExe
		1 * mono.exec { ConfigureUtil.configure(it, clrExecSpec) } >> clrExecResult
		1 * clrExecSpec.executable(boocExe)
		1 * clrExecSpec.args(expectedArgs)
		result == clrExecResult

		where:
		targetFramework | expectedProfile | assemblyReferences
		'v3.5'          | 'unity'         | ['System.Xml']
		'v4.0'          | '4.0'           | []
	}
}
