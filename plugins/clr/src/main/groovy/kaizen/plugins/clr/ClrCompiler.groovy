package kaizen.plugins.clr

import org.gradle.process.ExecResult

public interface ClrCompiler<TCompileSpec extends ClrCompileSpec> {
	String getLanguage()
	ExecResult exec(Closure<TCompileSpec> compileSpec)
}



