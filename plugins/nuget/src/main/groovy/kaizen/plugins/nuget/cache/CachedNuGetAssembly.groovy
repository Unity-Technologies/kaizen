package kaizen.plugins.nuget.cache

class CachedNuGetAssembly implements NuGetAssembly {

	final NuGetPackage containingPackage
	final String name
	final Set<String> configurations

	CachedNuGetAssembly(NuGetPackage containingPackage, String name, Set<String> configurations) {
		this.containingPackage = containingPackage
		this.name = name
		this.configurations = configurations
	}

	@Override
	NuGetPackage getContainingPackage() {
		containingPackage
	}

	@Override
	String getName() {
		name
	}

	@Override
	Iterable<File> filesForConfiguration(String configuration) {
		def assemblyFile = assemblyFileForConfiguration(configuration)
		def xmlDocFile = new File(assemblyFile.parentFile, "${name}.xml")
		xmlDocFile.exists() ? [assemblyFile, xmlDocFile] : [assemblyFile]
	}

	File assemblyFileForConfiguration(String configuration) {
		assert configuration in configurations
		containingPackage.queryFile("lib/$configuration/${name}.dll")
	}
}
