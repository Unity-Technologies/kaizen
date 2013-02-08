package kaizen.plugins.assembly.tasks

import kaizen.plugins.clr.ClrCompileSpec
import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrExtension
import kaizen.plugins.clr.ClrPlugin
import kaizen.testing.PluginSpecification

class AssemblyCompileSpec extends PluginSpecification {

	def 'invokes compiler from registry with the right settings'() {
		given:
		def project = projectWithName('UI')
		def compile = project.task('compile', type: AssemblyCompile) {
			outputAssembly 'UI.dll'
			inputs.source 'UI.cs'
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
		1 * compileSpec.outputAssembly(project.file('UI.dll'))
		1 * compileSpec.sourceFiles(compile.inputs.sourceFiles)
		1 * compileSpec.targetFrameworkVersion('v3.5')
		1 * compileSpec.references(['Core.dll'])
	}
}
