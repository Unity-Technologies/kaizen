package kaizen.plugins

import kaizen.foundation.Paths
import kaizen.foundation.SystemInformation

interface FrameworkLocator {
	String getFrameworkPath(String frameworkName)
}

class MonoFramework {
	final FrameworkLocator locator
	String frameworkName

	MonoFramework(FrameworkLocator locator, String frameworkName) {
		this.locator = locator
		this.frameworkName = frameworkName
	}

	MonoTool getGmcs() {
		new MonoTool(this, "gmcs")
	}

	MonoTool getBooc() {
		new MonoTool(this, "booc")
	}

	String getCli() {
		monoExecutable("cli")
	}

	String monoExecutable(String name) {
		platformSpecificExecutable monoBinPath(name)
	}

	String monoLib(String relativePath) {
		Paths.combine monoPath, 'lib', 'mono', '2.0', relativePath
	}

	String platformSpecificExecutable(String executable) {
		isWindows() ? "${executable}.bat" : executable
	}

	private boolean isWindows() {
		SystemInformation.isWindows()
	}

	String monoBinPath(String path) {
		Paths.combine monoPath, "bin", path
	}

	String getMonoPath() {
		locator.getFrameworkPath(frameworkName)
	}
}

class MonoTool {
	final MonoFramework framework
	final String name

	MonoTool(MonoFramework framework, String name) {
		this.framework = framework
		this.name = name
	}

	String getExecutable() {
		framework.monoLib("${name}.exe")
	}
}
