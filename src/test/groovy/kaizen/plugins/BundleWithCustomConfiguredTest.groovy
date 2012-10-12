package kaizen.plugins

class BundleWithCustomConfiguredTest extends BundleSpecification {

	def customTest = subProjectWithName('t').with {
		it.configurations.add(Configurations.TEST)
		it
	}

	void setup() {
		bundle.apply plugin: BundlePlugin
		triggerBundleEvaluation()
	}

	def 'custom test assembly is included in the test configuration'() {
		expect:
		projectsDependedUponBy(Configurations.TEST) == [customTest]
	}

	def 'custom test assembly is NOT included the editor configuration'() {
		expect:
		projectsDependedUponBy(Configurations.EDITOR) == []
	}

	def 'no additional configurations are added to the project'() {
		expect:
		setOf(customTest.configurations*.name) == setOf([Configurations.TEST] + BasePluginInfo.configurationsContributedByBasePlugin())
	}

	def setOf(Collection<String> strings) {
		strings.toSet()
	}
}
