package kaizen.plugins

class AssemblyCompilationSpec extends BundleSpecification {

	def depender = subProjectWithName('depender')
	def dependee = subProjectWithName('dependee')

	def setup() {
		bundle.apply plugin: BundlePlugin
		configure(depender) {
			dependencies {
				editor project(path: dependee.path, configuration: 'editor')
			}
		}
		evaluateBundle()
	}

	def 'depender.compileEditor depends on dependee.assembleEditor'() {
		given:
		def dependerCompileEditor = dependee.tasks.getByName('compileEditor')
		def dependeeAssembleEditor = depender.tasks.getByName('assembleEditor')

		expect:
		dependerCompileEditor.dependsOn.contains(dependeeAssembleEditor)
	}
}
