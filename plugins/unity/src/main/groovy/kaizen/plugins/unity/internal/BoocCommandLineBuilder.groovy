package kaizen.plugins.unity.internal

class BoocCommandLineBuilder extends AbstractCompilerCommandLineBuilderBase {

	@Override
	void outputXmlDoc(File file) {
		throw new IllegalStateException("booc doesn't support outputting xmldoc files")
	}

	@Override
	void embeddedResources(Map<String, File> embeddedResources) {
		throw new IllegalStateException("booc doesn't support embedded resources")
	}
}
