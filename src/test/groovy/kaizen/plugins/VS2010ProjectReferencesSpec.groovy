package kaizen.plugins

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project

class VS2010ProjectReferencesSpec extends PluginSpecification {

	def bundleDir = DirectoryBuilder.tempDirWith {
		dir('src') {
			dir('a')
			dir('b')
			dir('c')
		}
	}

	def bundle = ProjectBuilder.builder().withProjectDir(bundleDir).withName('bundle').build()
	def a = subProjectWithName('a')
	def b = subProjectWithName('b')
	def c = subProjectWithName('c')

	@Override
	def setup() {
		configure(bundle) {
			apply plugin: 'kaizen-bundle'
			subprojects {
				apply plugin: 'vs2010'
			}
			project(':a') {
			}
			project(':b') {
				dependencies {
					'default' project(':a')
				}
			}
			project(':c') {
				dependencies {
					'default' project(':a')
					'default' project(':b')
				}
			}
		}
		bundle.allprojects.each {
			it.evaluate()
		}
		bundle.subprojects.each {
			it.tasks.vs2010Project.execute()
		}
	}

	def 'project reference paths are relative'() {
		expect:
		def aProjectXml = parseProjectFileOf(a)
		aProjectXml.ItemGroup.ProjectReference == []

		def bProjectXml = parseProjectFileOf(b)
		bProjectXml.ItemGroup.ProjectReference.collect { [it.Project.text(), it.@Include] } == [
				[a.extensions.vs2010.guid, '..\\a\\a.csproj']
		]

		def cProjectXml = parseProjectFileOf(c)
		cProjectXml.ItemGroup.ProjectReference.collect { [it.Project.text(), it.@Include] } == [
				[a.extensions.vs2010.guid, '..\\a\\a.csproj'],
				[b.extensions.vs2010.guid, '..\\b\\b.csproj'],
		]
	}

	private parseProjectFileOf(Project project) {
		new XmlParser().parse(new File(project.projectDir, project.name + '.csproj'))
	}

	private subProjectWithName(String name) {
		ProjectBuilder.builder().withParent(bundle).withProjectDir(new File(bundleDir, 'src/' + name)).withName(name).build()
	}
}
