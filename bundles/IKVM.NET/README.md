Gradle build script to publish [IKVM](http://ikvm.net) libraries as kaizen components.

Publishing a new version requires running gradle twice:

	../../gradlew
	../../gradlew publish

The first run will take care of updating the
[assemblyDependencies.gradle](assemblyDependencies.gradle) file which describes the ikvm assembly dependencies as gradle project dependencies.
