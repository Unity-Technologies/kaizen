package kaizen.plugins.clr

import kaizen.plugins.clr.internal.DefaultClrCompilerContainer
import kaizen.plugins.clr.internal.DefaultClrProviderContainer
import org.gradle.api.Project

class ClrExtension implements ClrProvider {

	static ClrExtension forProject(Project project) {
		project.extensions.findByType(ClrExtension)
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
