package kaizen.plugins.vs.tests

import kaizen.plugins.vs.VS2010Plugin
import kaizen.testing.DirectoryBuilder

class BooProjectReferenceSpec extends VSProjectSpecification {

	def bundleDir = DirectoryBuilder.tempDirWith {
		dir('booProject') {
			file('f.boo')
		}
		dir('csProject')
	}

	def bundle = projectBuilderWithDir(bundleDir).build()
	def booProject = subProjectWithDir(bundle, new File(bundleDir, 'booProject'))
	def csProject = subProjectWithDir(bundle, new File(bundleDir, 'csProject'))

	@Override
	def setup() {
		configure(bundle) {
			subprojects {
				apply plugin: 'assembly'
				apply plugin: 'vs2010'
			}
			configure(csProject) {
				dependencies {
					'default' project(booProject.path)
				}
			}
		}
		bundle.allprojects { it.evaluate() }
	}

	def 'boo project reference is omitted and an assembly reference is used instead'() {
		when:
		csProject.tasks.vsProject.execute()

		then:
		def projectXml = parseProjectFileOf(csProject)
		projectXml.ItemGroup.ProjectReference == []

		def booProjectRefs = projectXml.ItemGroup.Reference.findAll { it.@Include == 'booProject' }
		booProjectRefs.size() == 1
		booProjectRefs[0].HintPath.text() == 'build\\Default\\booProject.dll'
	}
}
