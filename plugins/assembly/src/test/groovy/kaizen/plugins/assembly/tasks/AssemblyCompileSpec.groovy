package kaizen.plugins.assembly.tasks

import kaizen.plugins.clr.ClrCompileSpec
import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExtension
import kaizen.plugins.clr.ClrPlugin
import kaizen.testing.PluginSpecification
import spock.lang.Unroll

class AssemblyCompileSpec extends PluginSpecification {

	def project = projectWithName('P')

	@Unroll
	def 'invokes compiler from registry with the right settings for framework #targetFramework'() {
		given:
		def compile = project.task('compile', type: AssemblyCompile) {
			outputAssembly 'P.dll'
			inputs.source 'P.cs'
			references 'Core.dll'
			targetFrameworkVersion targetFramework
		}
		def compiler = Mock(ClrCompiler)
		def compileSpec = Mock(ClrCompileSpec)

		when:
		project.plugins.apply ClrPlugin
		ClrExtension.forProject(project).compilers.add compiler
		executeTask compile

		then:
		_ * compiler.language >> 'c#'
		1 * compiler.exec({ it(compileSpec); true })
		1 * compileSpec.outputAssembly(project.file('P.dll'))
		1 * compileSpec.sourceFiles(compile.inputs.sourceFiles)
		1 * compileSpec.targetFrameworkVersion(targetFramework)
		1 * compileSpec.references(['Core.dll'])

		where:
		targetFramework << ['v3.5', 'v4.0']
	}

	def 'outputAssembly is declared as task output'() {
		given:
		def compile = project.task('compile', type: AssemblyCompile) {
			outputAssembly 'P.dll'
		}

		expect:
		compile.outputs.files.singleFile == project.file('P.dll')
	}
}

