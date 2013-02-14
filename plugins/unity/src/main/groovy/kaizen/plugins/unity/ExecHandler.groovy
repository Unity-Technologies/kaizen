package kaizen.plugins.unity

import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

public interface ExecHandler {
	ExecResult exec(Closure<ExecSpec> execSpecConfig)
}