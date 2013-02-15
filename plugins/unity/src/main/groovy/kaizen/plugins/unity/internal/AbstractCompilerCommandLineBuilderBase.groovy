package kaizen.plugins.unity.internal

import kaizen.plugins.clr.ClrCompileSpec
import org.gradle.process.ExecSpec

/**
 * Created with IntelliJ IDEA.
 * User: bamboo
 * Date: 15/02/13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractCompilerCommandLineBuilderBase implements ClrCompileSpec  {
	final ExecSpec execSpec

	AbstractCompilerCommandLineBuilderBase(ExecSpec execSpec) {
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

	def sdkVersionForFrameworkVersion(String frameworkVersion) {
		if (frameworkVersion == 'v3.5')
			return 2
		if (frameworkVersion == 'v4.0')
			return 4
		throw new IllegalArgumentException("Unsupported framework version '$frameworkVersion'")
	}

	@Override
	void references(Iterable<String> references) {
		execSpec.args(references.collect { "-r:$it" })
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
