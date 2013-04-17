package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompileSpec
import org.gradle.process.ExecSpec

abstract class AbstractCompilerCommandLineBuilderBase implements ClrCompileSpec  {

	final List<Object> arguments = []

	String targetFramework

	@Override
	void targetFrameworkVersion(String targetFramework) {
		this.targetFramework = targetFramework
	}

	@Override
	void outputAssembly(File outputAssembly) {
		args("-target:${targetFor(outputAssembly)}")
		args("-out:$outputAssembly.canonicalPath")
	}

	def targetFor(File file) {
		file.name.endsWith('.exe') ? 'exe' : 'library'
	}

	@Override
	void sourceFiles(Iterable<File> sourceFiles) {
		args(sourceFiles*.canonicalPath)
	}

	def sdkVersionForFrameworkVersion(String frameworkVersion) {
		if (frameworkVersion == 'v3.5')
			return 2
		if (frameworkVersion == 'v4.0')
			return 4
		throw new IllegalArgumentException("Unsupported framework version '$frameworkVersion'")
	}

	@Override
	void references(Iterable<String> references) {
		args(references.collect { "-r:$it" })
	}

	@Override
	void defines(Iterable<String> defines) {
		args(defines.collect { "-define:$it" })
	}

	@Override
	void keyFile(File keyFile) {
		throw new IllegalStateException("kaizen.plugins.clr.ClrCompileSpec.keyFile is not implemented")
	}

	@Override
	void compilerOptions(Iterable<String> compilerOptions) {
		arguments.addAll(compilerOptions)
	}

	void args(Object... args) {
		arguments.addAll(args)
	}

	void args(Iterable args) {
		arguments.addAll(args)
	}
}
