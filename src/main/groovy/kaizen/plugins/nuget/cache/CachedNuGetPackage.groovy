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
	NuGetAssembly queryAssembly(String assemblyName) {
		def configurations = availableConfigurationsFor(assemblyName)
		configurations.empty ? null : new CachedNuGetAssembly(this, assemblyName, configurations)
	}

	@Override
	File queryFile(String relativePath) {
		def file = new File(packageDir, relativePath)
		file.exists() ? file : null
	}

	Set<String> availableConfigurationsFor(String assemblyName) {
		def configurations = queryFile('lib').listFiles({ File f -> f.directory } as FileFilter)
		configurations.findAll { new File(it, "${assemblyName}.dll").exists() }.collect { it.name }.toSet()
	}
}
