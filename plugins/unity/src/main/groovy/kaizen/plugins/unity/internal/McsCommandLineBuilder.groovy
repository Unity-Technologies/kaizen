package kaizen.plugins.unity.internal

class McsCommandLineBuilder extends AbstractCompilerCommandLineBuilderBase {

	final List<Object> arguments = []

	String targetFramework

	@Override
	void args(Object... args) {
		arguments.addAll(args)
	}

	@Override
	void args(Iterable args) {
		arguments.addAll(args)
	}

	@Override
	void targetFrameworkVersion(String frameworkVersion) {
		def sdkVersion = sdkVersionForFrameworkVersion(frameworkVersion)
		args("-sdk:$sdkVersion")
		this.targetFramework = frameworkVersion
	}
}
