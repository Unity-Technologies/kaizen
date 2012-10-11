package kaizen.plugins

class BundleWithCustomConfiguredTest extends BundleSpecification {

	def customTest = subProjectWithName('t').with {
		it.configurations.add(Configurations.TEST)
		it
	}

	def 'custom test assembly is included in the test configuration'() {

	}

	def 'custom test assembly is NOT included the editor configuration'() {

	}
}
