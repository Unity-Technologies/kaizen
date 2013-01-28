package kaizen.plugins.nuget.cache

interface NuGetPackageCache {
	NuGetPackage queryPackage(String packageName, String revision)
	void addPackage(String packageName, String revision, File file)
}

