package kaizen.plugins.clr.internal

import kaizen.plugins.clr.Clr
import kaizen.plugins.clr.ClrProvider
import kaizen.plugins.clr.ClrProviderContainer

class DefaultClrProviderContainer implements ClrProviderContainer {

	private final List<ClrProvider> providers = []

	@Override
	void add(ClrProvider provider) {
		providers.add(provider)
	}

	@Override
	boolean isEmpty() {
		providers.isEmpty()
	}

	@Override
	boolean contains(ClrProvider element) {
		providers.contains(element)
	}

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
