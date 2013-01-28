package kaizen.plugins.nuget.cache

class CachedNuGetAssembly implements NuGetAssembly {

	final NuGetPackage containingPackage
	final String name
	final File assemblyFile

	CachedNuGetAssembly(NuGetPackage containingPackage, String name, File file) {
		this.containingPackage = containingPackage
		this.name = name
		this.assemblyFile = file
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
	Iterable<File> getFiles() {
		def xmlDocFile = new File(assemblyFile.parentFile, "${name}.xml")
		xmlDocFile.exists() ? [assemblyFile, xmlDocFile] : [assemblyFile]
	}
}
