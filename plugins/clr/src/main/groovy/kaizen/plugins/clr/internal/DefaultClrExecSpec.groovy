package kaizen.plugins.clr.internal

import kaizen.plugins.clr.ClrExecSpec
import org.gradle.process.internal.ExecHandleBuilder

class DefaultClrExecSpec extends ExecHandleBuilder implements ClrExecSpec {
	DefaultClrExecSpec() {
		super()
	}

	DefaultClrExecSpec(org.gradle.api.internal.file.FileResolver fileResolver) {
		super(fileResolver)
	}
}
