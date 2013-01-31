package kaizen.plugins.nuget.resources

class ArtifactNamer {
	static def nameForArtifact(String assemblyName, String configuration) {
		"$assemblyName-$configuration"
	}

	static def configurationFromArtifactName(String name) {
		name.substring(name.lastIndexOf('-') + 1)
	}
}
