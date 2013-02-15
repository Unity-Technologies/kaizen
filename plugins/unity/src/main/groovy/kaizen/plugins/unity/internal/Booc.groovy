package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExecSpec
import kaizen.plugins.clr.ClrLanguageNames
import kaizen.plugins.unity.MonoProvider
import org.gradle.process.ExecResult
import org.gradle.util.ConfigureUtil

class Booc implements ClrCompiler {

	final MonoProvider monoProvider

	Booc(MonoProvider monoProvider) {
		this.monoProvider = monoProvider
	}

	@Override
	String getLanguage() {
		'boo'
	}

	@Override
	ExecResult exec(Closure compileSpec) {

		def builder = new BoocCommandLineBuilder()
		ConfigureUtil.configure(compileSpec, builder)

		def mono = monoProvider.runtimeForFrameworkVersion(builder.targetFramework)
		def boocProfile = builder.targetFramework == 'v3.5' ? 'unity' : '4.0'
		mono.exec { ClrExecSpec execSpec ->
			execSpec.executable mono.lib(boocProfile, 'booc.exe')
			execSpec.args builder.arguments
		}
	}
}
