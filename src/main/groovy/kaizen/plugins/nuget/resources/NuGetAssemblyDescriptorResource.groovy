package kaizen.plugins.nuget.resources

import groovy.xml.MarkupBuilder
import org.apache.ivy.plugins.repository.Resource
import kaizen.plugins.nuget.cache.NuGetAssembly

class NuGetAssemblyDescriptorResource extends LazyNuGetAssemblyResource {

	static final String DefaultConfiguration = "Net35"

	NuGetAssemblyDescriptorResource(String name, NuGetAssembly assembly) {
		super(name, assembly)
	}

	@Override
	File initFile() {
		File ivyFile = File.createTempFile("$packageName-$assemblyName-$revision", "-ivy.xml")
		ivyFile.withWriter {
			def markup = new MarkupBuilder(it)
			markup.'ivy-module'(version: '2.0') {
				markup.info(
					organisation: packageName,
					module: assemblyName,
					revision: revision,
					status: 'release',
					'default': 'true',
				)
				markup.configurations {
					if (DefaultConfiguration in configurations) {
						markup.conf(name: 'default', visibility: 'public')
					}
					configurations.each {
						markup.conf(name: it, visibility: 'public')
					}
				}
				markup.publications {
					configurations.each {
						def conf = it == DefaultConfiguration ? "$it,default" : it
						artifact(name: ArtifactNamer.nameForArtifact(assemblyName, it), type: "zip", ext: "zip", conf: conf)
					}
				}
				markup.dependencies {
				}
			}
		}
		ivyFile
	}

	def getPackageName() {
		containingPackage.name
	}

	def getAssemblyName() {
		assembly.name
	}

	def getRevision() {
		containingPackage.revision
	}

	def getConfigurations() {
		assembly.configurations
	}

	def getContainingPackage() {
		assembly.containingPackage
	}

	@Override
	Resource clone(String s) {
		return new NuGetAssemblyDescriptorResource(name, assembly)
	}
}
