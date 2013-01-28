package kaizen.plugins.nuget.resources

import kaizen.plugins.nuget.cache.NuGetAssembly
import org.apache.ivy.plugins.repository.LazyResource
import org.apache.ivy.plugins.resolver.packager.BuiltFileResource
import org.apache.tools.ant.types.resources.URLProvider

abstract class LazyNuGetAssemblyResource extends LazyResource implements URLProvider {

	BuiltFileResource resource
	final NuGetAssembly assembly

	LazyNuGetAssemblyResource(String name, NuGetAssembly assembly) {
		super(name)
		this.assembly = assembly
	}

	URL getURL() {
		resource.file.toURL()
	}

	@Override
	protected void init() {
		resource = new BuiltFileResource(initFile())
		init(resource)
	}

	abstract File initFile()

	@Override
	InputStream openStream() {
		return resource.openStream()
	}
}
