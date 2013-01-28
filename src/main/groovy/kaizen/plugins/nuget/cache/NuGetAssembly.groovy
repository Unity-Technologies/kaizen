package kaizen.plugins.nuget.cache

interface NuGetAssembly {
	static final String DefaultConfiguration = "Net35"

	NuGetPackage getContainingPackage()
	String getName()
	Iterable<File> getFiles()
}
