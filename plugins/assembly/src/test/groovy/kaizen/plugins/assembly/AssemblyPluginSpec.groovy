package kaizen.plugins.assembly

import kaizen.testing.PluginSpecification

class AssemblyPluginSpec extends PluginSpecification {

	def project = projectWithName('C')

	@Override
	def setup() {
		project.apply plugin: AssemblyPlugin
	}

	def 'assembly name is derived from project name'() {
		expect:
		project.assembly.fileName == "C.dll"
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
		AssemblyExtension assembly = project.extensions.assembly
		assembly.references*.name =~ ['System.Runtime.Remoting', 'System.Xml.Linq']
	}
}