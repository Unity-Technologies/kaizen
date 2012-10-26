package kaizen.plugins

class BundleWithTests extends BundleSpecification {

	def p1 = subProjectWithName('p1')
	def p1Tests = subProjectWithName('p1.Tests')
	def p2 = subProjectWithName('p2')
	def p2Tests = subProjectWithName('p2.Tests')

	@Override
	void setup() {
		bundle.apply plugin: BundlePlugin
		evaluateBundle()
	}
}
