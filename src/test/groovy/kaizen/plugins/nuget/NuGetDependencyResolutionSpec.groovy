package kaizen.plugins.nuget

import kaizen.plugins.DirectoryBuilder
import kaizen.plugins.PluginSpecification
import kaizen.plugins.nuget.cache.CachedNuGetPackage
import kaizen.plugins.nuget.cache.NuGetPackage
import kaizen.plugins.nuget.cache.NuGetPackageCache

import java.util.zip.ZipFile

class NuGetDependencyResolutionSpec extends PluginSpecification {

	def project = projectWithName('p')

	def 'resolves to net35 artifact by default'() {

		def packageName = 'ThePackage'
		def assemblyName = 'TheAssembly'
		def revision = '1.0.2856.0'

		def cachedPackageDir = DirectoryBuilder.tempDirWith {
			dir('lib') {
				dir('Net35') {
					file("${assemblyName}.dll")
					file("${assemblyName}.xml")
				}
			}
		}

		NuGetPackage nuGetPackage = new CachedNuGetPackage(cachedPackageDir, packageName, revision)
		NuGetPackageCache nuGetPackageCache = Mock()

		when:
		configure(project) {
			configurations {
				c
			}

			dependencies {
				c "$packageName:$assemblyName:$revision"
			}

			repositories {
				add NuGetDependencyResolverFactory.newNuGetDependencyResolver(nuGetPackageCache)
			}
		}

		def incomingFiles = project.configurations['c'].files.toList()

		then:
		1 == incomingFiles.size()
		def incomingFile = incomingFiles[0]
		incomingFile.name == "$assemblyName-Net35-${revision}.zip"
		zipEntryNamesFor(incomingFile).toSet() == ["${assemblyName}.dll", "${assemblyName}.xml"].toSet()

		_ * nuGetPackageCache.queryPackage(packageName, revision) >> nuGetPackage
		0 * _._
	}

	def zipEntryNamesFor(File zipFile) {
		def zip = new ZipFile(zipFile)
		try {
			return zip.entries().collect { it.name }.toList()
		} finally {
			zip.close()
		}
	}
}
