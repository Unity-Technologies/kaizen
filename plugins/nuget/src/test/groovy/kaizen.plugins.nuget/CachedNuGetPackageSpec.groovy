package kaizen.plugins.nuget

import kaizen.plugins.nuget.cache.CachedNuGetPackage
import kaizen.testing.DirectoryBuilder
import spock.lang.Specification

class CachedNuGetPackageSpec extends Specification {

	def 'includes configurations for all assemblies'() {
		def cachedPackageDir = DirectoryBuilder.tempDirWith {
			dir('lib') {
				dir('Net35') {
					file('Core.dll')
					file('UI.dll')
				}
				dir('Net4') {
					file('Core.dll')
				}
			}
		}
		def subject = new CachedNuGetPackage(cachedPackageDir, 'Package', '1.0')
		def core = subject.queryAssembly('Core')
		def ui = subject.queryAssembly('UI')
		expect:
		core.configurations == ['Net35', 'Net4'].toSet()
		ui.configurations == ['Net35'].toSet()
	}

}
