package kaizen.plugins.unity.internal

import org.gradle.process.ExecSpec

class McsCommandLineBuilder extends AbstractCompilerCommandLineBuilderBase {

	private ExecSpec execSpec

	McsCommandLineBuilder(ExecSpec execSpec) {
		this.execSpec = execSpec
	}

	@Override
	void targetFrameworkVersion(String frameworkVersion) {
		def sdkVersion = sdkVersionForFrameworkVersion(frameworkVersion)
		args("-sdk:$sdkVersion")
	}

	@Override
	void args(Object... args) {
		execSpec.args(args)
	}

	@Override
	void args(Iterable args) {
		execSpec.args(args)
	}
}
