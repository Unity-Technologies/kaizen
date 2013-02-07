package kaizen.plugins.clr

import org.gradle.process.ExecResult

public interface Clr {
	ExecResult exec(Closure<ClrExecSpec> execSpecClosure)
}