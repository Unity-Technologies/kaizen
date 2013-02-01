package kaizen.plugins.nuget

import kaizen.plugins.nuget.cache.DefaultNuGetPackageCache
import kaizen.plugins.nuget.cache.NuGetPackageCache
import org.apache.ivy.plugins.resolver.DependencyResolver
import org.apache.ivy.plugins.resolver.RepositoryResolver

class NuGetDependencyResolverFactory {

	static DependencyResolver newNuGetDependencyResolver(NuGetPackageCache packageCache = new DefaultNuGetPackageCache()) {
		def repository = new NuGetRepository(packageCache)
		def resolver = new RepositoryResolver(name: 'kaizen.plugins.nuget', repository: repository)
		resolver.addIvyPattern("[organisation]:[module]:[revision]:[type]:[artifact]:[ext]")
		resolver.addArtifactPattern("[organisation]:[module]:[revision]:[type]:[artifact]:[ext]")
		resolver
	}

}
