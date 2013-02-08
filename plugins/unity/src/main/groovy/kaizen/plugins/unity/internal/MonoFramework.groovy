package kaizen.plugins.unity.internal

import kaizen.commons.Paths
import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.ClrExecSpec
import org.apache.commons.lang.NotImplementedException
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult

class MonoFramework implements Clr {
	String prefix
	String frameworkVersion
	final OperatingSystem operatingSystem

	MonoFramework(OperatingSystem operatingSystem, String prefix, String frameworkVersion) {
		this.operatingSystem = operatingSystem
		this.prefix = prefix
		this.frameworkVersion = frameworkVersion
	}

	String getCli() {
		monoExecutable("cli")
	}

	String monoExecutable(String name) {
		platformSpecificExecutable monoBinPath(name)
	}

	String monoLib(String relativePath) {
		Paths.combine prefix, 'lib', 'mono', '2.0', relativePath
	}

	String platformSpecificExecutable(String executable) {
		operatingSystem.getScriptName(executable)
	}

	String monoBinPath(String path) {
		Paths.combine prefix, "bin", path
	}

	@Override
	ExecResult exec(Closure<ClrExecSpec> execSpecClosure) {
		throw new NotImplementedException()
	}
}

