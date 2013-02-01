package kaizen.plugins.nuget.cache

class DefaultNuGetPackageCache implements NuGetPackageCache {

	final File cacheDir = new File(System.properties['user.home'], '.kaizen/nuget/packages')

	NuGetPackage queryPackage(String packageName, String revision) {
		def packageCache = cacheDirectoryFor(packageName, revision)
		packageCache.exists() ? new CachedNuGetPackage(packageCache, packageName, revision) : null
	}

	void addPackage(String packageName, String revision, File file) {
		unpackFileTo(cacheDirectoryFor(packageName, revision), file)
	}

	File cacheDirectoryFor(String packageName, String revision) {
		new File(cacheDir, "$packageName.$revision")
	}

	void unpackFileTo(File dir, File zipFile) {
		def ant = new AntBuilder()
		ant.unzip(src: zipFile, dest: dir, overwrite: true)
	}
}
