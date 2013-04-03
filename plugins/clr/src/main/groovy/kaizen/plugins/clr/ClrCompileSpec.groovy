package kaizen.plugins.clr

public interface ClrCompileSpec {
	void targetFrameworkVersion(String frameworkVersion)
	void sourceFiles(Iterable<File> sourceFiles)
	void outputAssembly(File outputAssembly)
	void outputXmlDoc(File file)
	void defines(Iterable<String> defines)
	void keyFile(File keyFile)
	void compilerOptions(Iterable<String> compilerOptions)
	void references(Iterable<String> references)
	void embeddedResources(Map<String, File> embeddedResources)
}
