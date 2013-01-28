package kaizen.plugins.nuget.resources

import org.apache.ivy.plugins.repository.Resource
import kaizen.plugins.nuget.cache.NuGetAssembly

import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class NuGetAssemblyZipResource extends LazyNuGetAssemblyResource {

	NuGetAssemblyZipResource(String name, NuGetAssembly assembly) {
		super(name, assembly)
	}

	@Override
	File initFile() {
		def tmpZipFile = File.createTempFile("$containingPackage.name-$name", "-$revision-${NuGetAssembly.DefaultConfiguration}.zip")
		writeZipFile(tmpZipFile, assembly.files)
		tmpZipFile
	}

	def getRevision() {
		containingPackage.revision
	}

	def getContainingPackage() {
		assembly.containingPackage
	}

	@Override
	Resource clone(String s) {
		return new NuGetAssemblyZipResource(name, assembly)
	}

	static void writeZipFile(File zipFile, Iterable<File> includes) {
		new ZipOutputStream(new FileOutputStream(zipFile)).withStream { ZipOutputStream zos ->
			includes.each { File file ->
				zos.putNextEntry(new ZipEntry(file.name))
				zos.write(file.bytes)
			}
		}
	}
}
