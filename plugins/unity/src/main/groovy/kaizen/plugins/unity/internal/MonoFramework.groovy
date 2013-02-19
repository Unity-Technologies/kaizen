package kaizen.plugins.unity.internal

import kaizen.commons.Paths
import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.internal.DefaultClrExecSpec
import kaizen.plugins.unity.ExecHandler
import kaizen.plugins.unity.Mono
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.gradle.util.ConfigureUtil

abstract class MonoFramework implements Clr, Mono {

	String prefix
	final OperatingSystem operatingSystem
	final ExecHandler execHandler

	MonoFramework(OperatingSystem operatingSystem, String prefix, ExecHandler execHandler) {
		this.operatingSystem = operatingSystem
		this.prefix = prefix
		this.execHandler = execHandler
	}

	@Override
	String lib(String profile, String fileName) {
		Paths.combine prefix, 'lib', 'mono', profile, fileName
	}

	@Override
	ExecResult exec(Closure execSpecClosure) {
		def clrExecSpec = new DefaultClrExecSpec()
		ConfigureUtil.configure(execSpecClosure, clrExecSpec)
		assert clrExecSpec.executable

		def executable = canonicalFileFor(clrExecSpec.executable)
		def arguments = clrExecSpec.allArguments
		execHandler.exec { ExecSpec execSpec ->
			execSpec.executable cli
			setUpCliArguments execSpec
			execSpec.args '--debug'
			execSpec.args executable
			execSpec.args arguments
		}
	}

	void setUpCliArguments(ExecSpec execSpec) {
	}

	abstract String getCli()

	String bin(String path) {
		Paths.combine prefix, 'bin', path
	}

	def canonicalFileFor(String file) {
		new File(file).canonicalFile
	}

}
