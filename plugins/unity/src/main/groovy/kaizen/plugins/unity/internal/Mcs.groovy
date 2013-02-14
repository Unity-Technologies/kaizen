package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompileSpec
import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExecSpec
import kaizen.plugins.clr.ClrLanguageNames
import kaizen.plugins.unity.MonoProvider
import org.gradle.process.ExecResult
import org.gradle.util.ConfigureUtil

class Mcs implements ClrCompiler<ClrCompileSpec> {

	final MonoProvider monoProvider

	Mcs(MonoProvider monoProvider) {
		this.monoProvider = monoProvider
	}

	@Override
	String getLanguage() {
		ClrLanguageNames.CSHARP
	}

	@Override
	ExecResult exec(Closure<ClrCompileSpec> compileSpec) {
		mono.exec { ClrExecSpec execSpec ->
			execSpec.executable mcs
			ConfigureUtil.configure(compileSpec, new McsCommandLineBuilder(execSpec))
		}
	}

	def getMono() {
		monoProvider.getMono()
	}

	def getMcs() {
		mono.lib('2.0', 'mcs.exe')
	}
}
