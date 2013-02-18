package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExecSpec
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

		def mono = monoForFrameworkVersion(builder.targetFramework)
		def booc = boocFor(builder.targetFramework)
		mono.exec { ClrExecSpec execSpec ->
			execSpec.executable booc
			execSpec.args builder.arguments
		}
	}

	def monoForFrameworkVersion(String framework) {
		monoProvider.runtimeForFrameworkVersion(framework)
	}

	def boocFor(String targetFramework) {
		if (targetFramework == 'v3.5' || targetFramework == 'unity')
			return monoForFrameworkVersion('unity').lib('unity', 'booc.exe')
		if (targetFramework == 'v4.0')
			return monoForFrameworkVersion(targetFramework).lib('4.0', 'booc.exe')
		throw new IllegalArgumentException("Unsupported framework version '$targetFramework'")
	}
}
