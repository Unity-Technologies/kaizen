package kaizen.plugins.unity.internal

import kaizen.plugins.unity.ExecHandler
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

class LegacyMonoFramework extends MonoFramework {

	LegacyMonoFramework(OperatingSystem operatingSystem, String prefix, ExecHandler execHandler) {
		super(operatingSystem, prefix, execHandler)
	}

	@Override
	String getCli() {
		operatingSystem.getScriptName(bin('cli_unity'))
	}
}
