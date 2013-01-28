package kaizen.plugins.nuget.cache

class CachedNuGetPackage implements NuGetPackage {

	final File packageDir
	final String name
	final String revision

	CachedNuGetPackage(File packageDir, String name, String revision) {
		this.packageDir = packageDir
		this.name = name
		this.revision = revision
	}

	@Override
	String getName() {
		name
	}

	@Override
	String getRevision() {
		revision
	}

	@Override
	NuGetAssembly queryAssembly(String assemblyName, String configuration) {
		def assemblyFile = new File(packageDir, "lib/$configuration/${assemblyName}.dll")
		assemblyFile.exists() ? new CachedNuGetAssembly(this, assemblyName, assemblyFile) : null
	}
}
