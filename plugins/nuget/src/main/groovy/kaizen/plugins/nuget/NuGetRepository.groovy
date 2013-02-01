package kaizen.plugins.nuget

import kaizen.plugins.nuget.cache.NuGetAssembly
import kaizen.plugins.nuget.cache.NuGetPackage
import kaizen.plugins.nuget.cache.NuGetPackageCache
import kaizen.plugins.nuget.resources.NuGetAssemblyDescriptorResource
import kaizen.plugins.nuget.resources.NuGetAssemblyZipResource
import org.apache.ivy.plugins.repository.*
import org.apache.ivy.plugins.repository.url.URLResource
import org.apache.ivy.util.FileUtil
import org.apache.tools.ant.types.resources.URLProvider

class NuGetRepository extends AbstractRepository {

	private final RepositoryCopyProgressListener progress = new RepositoryCopyProgressListener(this);
	private final Map<String, Resource> resources = [:]

	NuGetPackageCache packageCache

	NuGetRepository(NuGetPackageCache packageCache) {
		this.packageCache = packageCache
	}

	@Override
	Resource getResource(String src) {
		cachedResourceFor(src) ?: newResourceFor(src)
	}

	def cachedResourceFor(String src) {
		resources[src]
	}

	def newResourceFor(String src) {
		def resource = createResourceFor(src)
		resources.put(src, resource)
		resource
	}

	def createResourceFor(String src) {
		def (group, module, revision, type) = src.split(/:/)
		def nuGetPackage = nuGetPackageFor(group, revision)
		def assembly = nuGetPackage?.queryAssembly(module)
		assembly == null ?
			nonExistentResource(src) :
			resourceForAssembly(src, assembly, type)
	}

	NuGetPackage nuGetPackageFor(String packageId, String revision) {
		cachedNuGetPackageFor(packageId, revision) ?: downloadNuGetPackageFor(packageId, revision)
	}

	NuGetPackage downloadNuGetPackageFor(String packageId, String revision) {
		def packageUrl = new URL("https://nuget.org/api/v2/package/$packageId/$revision")
		def tmpPackageFile = File.createTempFile(packageId, "-revision.nupkg")
		transferResource(new URLResource(packageUrl), packageUrl, tmpPackageFile)
		cacheNuGetPackage(packageId, revision, tmpPackageFile)
		cachedNuGetPackageFor(packageId, revision)
	}

	def cacheNuGetPackage(String packageId, String revision, File tmpPackageFile) {
		packageCache.addPackage(packageId, revision, tmpPackageFile)
	}

	NuGetPackage cachedNuGetPackageFor(String packageId, String revision) {
		packageCache.queryPackage(packageId, revision)
	}

	def resourceForAssembly(String src, NuGetAssembly assembly, String type) {
		switch (type) {
			case 'ivy':
				return new NuGetAssemblyDescriptorResource(src, assembly)
			case 'zip':
				return new NuGetAssemblyZipResource(src, assembly)
			default:
				return nonExistentResource(src)
		}
	}

	def nonExistentResource(String src) {
		new BasicResource(src, false, 0, 0, false)
	}

	@Override
	void get(String src, File destination) {
		def resource = getResource(src)
		transferResource(resource, ((URLProvider)resource).URL, destination)
	}

	def transferResource(Resource resource, URL src, File destination) {
		fireTransferInitiated(resource, TransferEvent.REQUEST_GET)
		try {
			long totalLength = resource.getContentLength()
			if (totalLength > 0) {
				progress.setTotalLength(new Long(totalLength))
			}
			FileUtil.copy(src, destination, progress)
		} catch (IOException ex) {
			fireTransferError(ex)
			throw ex
		} catch (RuntimeException ex) {
			fireTransferError(ex)
			throw ex
		} finally {
			progress.setTotalLength(null);
		}
	}

	@Override
	List list(String parent) {
		Collections.emptyList()
	}
}
