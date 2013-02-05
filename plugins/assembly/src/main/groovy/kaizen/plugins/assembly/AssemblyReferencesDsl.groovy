package kaizen.plugins.assembly

import org.gradle.api.DomainObjectSet

class AssemblyReferencesDsl {
	private final DomainObjectSet<AssemblyReference> set

	AssemblyReferencesDsl(DomainObjectSet<AssemblyReference> set) {
		this.set = set
	}

	def frameworkAssembly(String name) {
		set.add(new FrameworkAssemblyReference(name))
	}
}
