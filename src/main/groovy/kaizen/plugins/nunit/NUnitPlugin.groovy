package kaizen.plugins.nunit

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

class NUnitPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		def nunit = new NUnitExtension(project)
		project.extensions.add('nunit', nunit)

		configure(project) {
			configurations {
				NUnit
			}

			afterEvaluate {
				dependencies {
					NUnit "nunit:nunit-console:${nunit.version}"
				}
			}
		}
	}

	def configure(Object delegate, Closure closure) {
		ConfigureUtil.configure(closure, delegate)
	}
}


