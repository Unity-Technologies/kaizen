package kaizen.plugins.assembly.tasks

import kaizen.plugins.clr.ClrCompileSpec
import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExtension
import kaizen.plugins.clr.ClrPlugin
import kaizen.testing.PluginSpecification

class AssemblyCompileSpec extends PluginSpecification {

	def project = projectWithName('P')

	def 'invokes compiler from registry with the right settings'() {
		given:
		def compile = project.task('compile', type: AssemblyCompile) {
			outputAssembly 'P.dll'
			inputs.source 'P.cs'
			references 'Core.dll'
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
		1 * compileSpec.targetFrameworkVersion('v3.5')
		1 * compileSpec.references(['Core.dll'])
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

