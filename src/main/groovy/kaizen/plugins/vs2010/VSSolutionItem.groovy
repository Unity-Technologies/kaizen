package kaizen.plugins.vs2010

import org.gradle.api.internal.AbstractNamedDomainObjectContainer
import org.gradle.api.Named
import org.gradle.internal.reflect.DirectInstantiator

class VSSolutionItem extends AbstractNamedDomainObjectContainer implements Named {

	final String name
	final List<String> projects = []

	protected VSSolutionItem(String name) {
		super(VSSolutionFolder, new DirectInstantiator())
		this.name = name
	}

	def project(String path) {
		projects.add(path)
	}

	@Override
	protected VSSolutionFolder doCreate(String s) {
		return new VSSolutionFolder(s)
	}
}
