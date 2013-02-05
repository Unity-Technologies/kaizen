package kaizen.plugins.assembly

import kaizen.plugins.assembly.AssemblyReference

class FrameworkAssemblyReference implements AssemblyReference {
	final String name

	FrameworkAssemblyReference(String name) {
		this.name = name
	}
}
