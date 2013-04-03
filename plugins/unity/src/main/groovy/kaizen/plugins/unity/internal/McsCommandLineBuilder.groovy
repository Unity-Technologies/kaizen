package kaizen.plugins.unity.internal

class McsCommandLineBuilder extends AbstractCompilerCommandLineBuilderBase {

	@Override
	void targetFrameworkVersion(String targetFramework) {
		super.targetFrameworkVersion(targetFramework)
		def sdkVersion = sdkVersionForFrameworkVersion(targetFramework)
		args("-sdk:$sdkVersion")
	}

	@Override
	void outputXmlDoc(File file) {
		args("-doc:$file.canonicalPath")
	}

	@Override
	void embeddedResources(Map<String, File> embeddedResources) {
		embeddedResources.each {
			args("-resource:$it.value,$it.key")
		}
	}
}
