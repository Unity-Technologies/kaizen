package kaizen.plugins.clr

class ClrExtension implements ClrProvider {
	final List<ClrProvider> providers = []

	@Override
	Clr runtimeForFrameworkVersion(String frameworkVersion) {
		for (def provider in providers) {
			def clr = provider.runtimeForFrameworkVersion(frameworkVersion)
			if (clr)
				return clr
		}
		return null
	}
}
