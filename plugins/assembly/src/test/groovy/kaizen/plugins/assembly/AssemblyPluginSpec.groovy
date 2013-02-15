package kaizen.plugins.assembly

import kaizen.plugins.assembly.model.Assembly
import kaizen.plugins.assembly.tasks.AssemblyCompile
import kaizen.testing.PluginSpecification


class AssemblyPluginSpec extends PluginSpecification {

	def project = projectWithName('C')

	@Override
	def setup() {
		project.plugins.apply AssemblyPlugin
	}

	def 'assembly name is derived from project name'() {
		expect:
		assembly.fileName == "C.dll"
	}

	def 'framework assembly references'() {
		when:
		configure(project) {
			assembly {
				references {
					frameworkAssembly 'System.Runtime.Remoting'
					frameworkAssembly 'System.Xml.Linq'
				}
			}
		}

		then:
		assembly.references*.name =~ ['System.Runtime.Remoting', 'System.Xml.Linq']
	}

	def getAssembly() {
		Assembly.forProject(project)
	}

	def file(String projectRelativePath) {
		project.file(projectRelativePath)
	}
}