package kaizen.plugins.nuget.cache

interface NuGetPackage {
	String getName()
	String getRevision()
	NuGetAssembly queryAssembly(String assemblyName, String configuration)
}

