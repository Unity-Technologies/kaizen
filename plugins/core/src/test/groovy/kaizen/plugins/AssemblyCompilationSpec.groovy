package kaizen.plugins

class AssemblyCompilationSpec extends BundleSpecification {

	def depender = subProjectWithName('depender')
	def dependee = subProjectWithName('dependee')

	def setup() {
		configure(depender) {
			configurations {
				editor
				windowsPhone8
			}
			dependencies {
				editor project(dependee.path)
			}
		}
		bundle.apply plugin: BundlePlugin
		evaluateBundle()
	}

	def 'one compile task per configuration except archives'() {
		expect:
		setOf(depender.tasks*.name.findAll { it.startsWith('compile') }) == setOf(['compileDefault', 'compileEditor', 'compileWindowsPhone8'])
	}

	Set<String> setOf(Collection<String> strings) {
		strings.toSet()
	}
}
