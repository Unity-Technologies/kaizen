package kaizen.plugins.assembly.model

import org.gradle.api.DomainObjectSet

class AssemblyReferencesHandler {
	final DomainObjectSet<AssemblyReference> references

	AssemblyReferencesHandler(DomainObjectSet<AssemblyReference> references) {
		this.references = references
	}

	def frameworkAssembly(String name) {
		references.add(new FrameworkAssemblyReference(name))
	}
}
