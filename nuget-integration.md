Nuget package dependencies can be specified just like any other module
dependency after the nuget plugin has been applied:

```groovy
apply plugin: 'nuget'

dependencies {
  net35 'Rx-Core-old:1.0.2856.0:System.Observable'
}
```

Upon dependency resolution any missing nuget packages are downloaded and
unpacked to ~/.kaizen/nuget/packages. The required assemblies are packed
into kaizen/ivy modules as needed.

Specific framework versions of the assemblies can be requested through
the configuration attribute:

```groovy
dependencies {
  net40(
    group: 'ICSharpCode.NRefactory',
    name: 'ICSharpCode.NRefactory',
    version: '5.3.0',
    configuration: 'net40') // lib/net40/ICSharpCode.NRefactory.dll
}
```
            
Transitive dependencies are not handled at this point so they have to
be explicitly specified.
