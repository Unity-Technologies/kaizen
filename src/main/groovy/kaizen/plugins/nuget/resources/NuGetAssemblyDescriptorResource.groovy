package kaizen.plugins.nuget.resources

import groovy.xml.MarkupBuilder
import org.apache.ivy.plugins.repository.Resource
import kaizen.plugins.nuget.cache.NuGetAssembly

class NuGetAssemblyDescriptorResource extends LazyNuGetAssemblyResource {

	NuGetAssemblyDescriptorResource(String name, NuGetAssembly assembly) {
		super(name, assembly)
	}

	@Override
	File initFile() {
		def nuGetPackage = assembly.containingPackage
		ivyDescriptorFor(nuGetPackage.name, assembly.name, nuGetPackage.revision)
	}

	def ivyDescriptorFor(group, module, revision) {
		File ivyFile = File.createTempFile("$group-$module-$revision", "-ivy.xml")
		ivyFile.withWriter {
			def build = new MarkupBuilder(it)
			build.doubleQuotes = true
			build.'ivy-module'(version: '2.0') {
				info(
					organisation: group,
					module: module,
					revision: revision,
					status: 'release',
					'default': 'true',
				)
				build.configurations() {
					conf(name: "default", visibility: "public")
				}
				build.publications() {
					artifact(name: "$module-$NuGetAssembly.DefaultConfiguration", type: "zip", ext: "zip", conf: "default")
				}
				build.dependencies {
				}
			}
			it.close()
		}
		ivyFile
	}

	@Override
	Resource clone(String s) {
		return new NuGetAssemblyDescriptorResource(name, assembly)
	}
}
