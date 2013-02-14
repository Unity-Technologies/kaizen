package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompileSpec
import org.gradle.process.ExecSpec

class McsCommandLineBuilder implements ClrCompileSpec {

	private ExecSpec execSpec

	McsCommandLineBuilder(ExecSpec execSpec) {
		this.execSpec = execSpec
	}

	@Override
	void outputAssembly(File outputAssembly) {
		execSpec.args("-target:${targetFor(outputAssembly)}")
		execSpec.args("-out:$outputAssembly.canonicalPath")
	}

	def targetFor(File file) {
		file.name.endsWith('.exe') ? 'exe' : 'library'
	}

	@Override
	void sourceFiles(Iterable<File> sourceFiles) {
		execSpec.args(sourceFiles*.canonicalPath)
	}

	@Override
	void targetFrameworkVersion(String frameworkVersion) {
		def sdkVersion = sdkVersionForFrameworkVersion(frameworkVersion)
		execSpec.args("-sdk:$sdkVersion")
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
		throw new IllegalStateException("kaizen.plugins.clr.ClrCompileSpec.references is not implemented")
	}

	@Override
	void defines(Iterable<String> defines) {
		throw new IllegalStateException("kaizen.plugins.clr.ClrCompileSpec.defines is not implemented")
	}

	@Override
	void keyFile(File keyFile) {
		throw new IllegalStateException("kaizen.plugins.clr.ClrCompileSpec.keyFile is not implemented")
	}

	@Override
	void compilerOptions(Iterable<String> compilerOptions) {
		throw new IllegalStateException("kaizen.plugins.clr.ClrCompileSpec.compilerOptions is not implemented")
	}
}
