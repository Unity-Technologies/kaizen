package kaizen.plugins.vs.tests

import kaizen.plugins.assembly.AssemblyPlugin
import kaizen.plugins.vs.VS2010Plugin

class AssemblyReferencesSpec extends VSProjectSpecification {

	def project = projectWithName('p')

	@Override
	def setup() {
		project.plugins.apply AssemblyPlugin
		project.plugins.apply VS2010Plugin
	}

	def 'System and System.Core are referenced by default'() {
		expect:
		vsProjectReferences() == ['System', 'System.Core']
	}

	def 'referenced framework assemblies are added to the project'() {
		when:
		configure(project) {
			assembly {
				references {
					frameworkAssembly 'System.Runtime.Remoting'
				}
			}
		}

		then:
		vsProjectReferences() == ['System', 'System.Core', 'System.Runtime.Remoting']
	}

	Collection<String> vsProjectReferences() {
		loadProjectFileOf(project).ItemGroup.Reference*.@Include
	}
}
