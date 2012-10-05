package kaizen

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertArrayEquals
import org.gradle.api.Project
import org.junit.Before

import static org.junit.Assert.assertTrue
import kaizen.plugins.BundlePlugin

import kaizen.plugins.Unity

class BundlePluginTest {

	Project bundle

	@Before
	void setUpProject() {
		bundle = projectWithName('Bundle').build()
	}

	@Test
	void bundleDependsOnEverySubProject() {
		def component1 = projectWithName('C1').withParent(bundle).build()
		def component2 = projectWithName('C2').withParent(bundle).build()
		applyBundlePlugin()
		assertArrayEquals(
			[component1, component2].toArray(),
			bundle.configurations['editor'].dependencies.collect { it.dependencyProject }.toArray())
	}

	@Test
	void bundleImpliesUnityPlugin() {
		applyBundlePlugin()

		def unity = bundle.extensions.unity
		assertTrue unity instanceof Unity
	}

	private applyBundlePlugin() {
		bundle.apply plugin: BundlePlugin
	}

	private ProjectBuilder projectWithName(String name) {
		ProjectBuilder.builder().withName(name)
	}
}