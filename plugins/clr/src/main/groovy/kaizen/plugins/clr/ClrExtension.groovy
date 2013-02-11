package kaizen.plugins.clr

import kaizen.plugins.clr.internal.DefaultClrCompilerContainer
import kaizen.plugins.clr.internal.DefaultClrProviderContainer
import org.gradle.api.Project

class ClrExtension implements ClrProvider {

	static ClrExtension forProject(Project project) {
		while (project != null) {
			def clr = project.extensions.findByType(ClrExtension)
			if (clr != null)
				return clr
			project = project.parent
		}
		null
	}

	final ClrProviderContainer providers = new DefaultClrProviderContainer()

	final ClrCompilerContainer compilers = new DefaultClrCompilerContainer()

	@Override
	Clr runtimeForFrameworkVersion(String frameworkVersion) {
		providers.runtimeForFrameworkVersion(frameworkVersion)
	}

	ClrCompiler compilerForLanguage(String language) {
		compilers.compilerForLanguage(language)
	}
}
