package kaizen.plugins.unity.internal

import kaizen.plugins.unity.ExecHandler
import org.gradle.internal.os.OperatingSystem
import org.gradle.process.ExecSpec

class BleedingEdgeMonoFramework extends MonoFramework {
	final String runtimeVersion

	BleedingEdgeMonoFramework(OperatingSystem operatingSystem, String prefix, ExecHandler execHandler, String runtimeVersion) {
		super(operatingSystem, prefix, execHandler)
		this.runtimeVersion = runtimeVersion
	}

	@Override
	void setUpCliArguments(ExecSpec execSpec) {
		execSpec.args "--runtime=$runtimeVersion"
	}

	String getCli() {
		operatingSystem.getExecutableName(bin('mono'))
	}
}


