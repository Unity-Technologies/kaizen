package kaizen.plugins.nuget.cache

interface NuGetAssembly {
	NuGetPackage getContainingPackage()
	String getName()
	Set<String> getConfigurations()
	Iterable<File> filesForConfiguration(String configuration)
}
