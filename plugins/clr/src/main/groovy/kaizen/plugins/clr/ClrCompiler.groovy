package kaizen.plugins.clr

import org.gradle.process.ExecResult

public interface ClrCompiler {
	String getLanguage()
	ExecResult exec(Closure compileSpec)
}



