package kaizen.plugins.vs.tests

import kaizen.testing.BundleSpecification

class VSSolutionsSpec extends BundleSpecification {

	def framework = subProjectWithName('framework')
	def test = subProjectWithName('test')
	def uncategorized = subProjectWithName('uncategorized')

	@Override
	def setup() {
		configure(bundle) {
			apply plugin: 'vs2010'
			vs {
				solutions {
					Bundle {
						folder('Frameworks') {
							project(framework.path)
						}
						folder('Tests') {
							project(test.path)
						}
						project(uncategorized.path)
					}
				}
			}
		}
	}

	def 'solution folders'() {
		given:
		def solution = bundle.extensions.vs.solutions.Bundle

		expect:
		solution.folders.collect { it.name } == ['Frameworks', 'Tests']
		solution.folders.getByName('Frameworks').projects == [framework].toSet()
		solution.folders.getByName('Tests').projects == [test].toSet()
		solution.projects == [uncategorized].toSet()
		solution.allProjects.toSet() == [framework, test, uncategorized].toSet()
	}

	def 'destination file'() {
		expect:
		bundle.extensions.vs.solutions.Bundle.destinationFile == bundle.file('Bundle.sln')
	}
}
