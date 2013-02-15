package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExecSpec
import kaizen.plugins.clr.ClrLanguageNames
import kaizen.plugins.unity.Mono
import kaizen.plugins.unity.MonoProvider
import org.gradle.process.ExecResult
import org.gradle.util.ConfigureUtil

class Mcs implements ClrCompiler {

	final MonoProvider monoProvider

	@Lazy Mono mono = monoProvider.runtimeForFrameworkVersion('v3.5')

	Mcs(MonoProvider monoProvider) {
		this.monoProvider = monoProvider
	}

	@Override
	String getLanguage() {
		ClrLanguageNames.CSHARP
	}

	@Override
	ExecResult exec(Closure compileSpec) {
		mono.exec { ClrExecSpec execSpec ->
			execSpec.executable mcs
			ConfigureUtil.configure(compileSpec, new McsCommandLineBuilder(execSpec))
		}
	}

	def getMcs() {
		mono.lib('2.0', 'mcs.exe')
	}
}
