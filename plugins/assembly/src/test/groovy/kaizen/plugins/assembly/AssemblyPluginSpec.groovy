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

	def 'a compile task per configuration'() {
		when:
		configure(project) {
			configurations {
				// 'default' // contributed by base plugin
				net40
			}
		}
		evaluateProject(project)

		then:
		def compileTasks = project.tasks.withType(AssemblyCompile)
		compileTasks*.name == ['compileDefault', 'compileNet40']
		compileTasks*.outputAssembly == [file('build/Default/C.dll'), file('build/Net40/C.dll')]
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