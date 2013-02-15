package kaizen.plugins.unity.internal

class BoocCommandLineBuilder extends AbstractCompilerCommandLineBuilderBase {

	final List<Object> arguments = []

	String targetFramework

	@Override
	void targetFrameworkVersion(String frameworkVersion) {
		targetFramework = frameworkVersion
	}

	@Override
	void args(Object... args) {
		arguments.addAll(args)
	}

	@Override
	void args(Iterable args) {
		arguments.addAll(args)
	}
}
