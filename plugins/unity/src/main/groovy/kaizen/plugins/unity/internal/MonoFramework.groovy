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

class MonoFramework implements Clr, Mono {
	String prefix
	final OperatingSystem operatingSystem
	final ExecHandler execHandler
	private String cli

	MonoFramework(OperatingSystem operatingSystem, String prefix, ExecHandler execHandler, String cli = 'cli') {
		this.cli = cli
		this.operatingSystem = operatingSystem
		this.prefix = prefix
		this.execHandler = execHandler
	}

	String getCli() {
		script(cli)
	}

	String script(String name) {
		operatingSystem.getScriptName(bin(name))
	}

	String bin(String path) {
		Paths.combine prefix, 'bin', path
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
		execHandler.exec { ExecSpec execSpec ->
			execSpec.workingDir executable.parentFile
			execSpec.executable cli
			execSpec.args '--debug'
			execSpec.args executable
			execSpec.args clrExecSpec.allArguments
		}
	}

	def canonicalFileFor(String file) {
		new File(file).canonicalFile
	}
}

