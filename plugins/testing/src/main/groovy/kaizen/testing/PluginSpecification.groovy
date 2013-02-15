package kaizen.testing

import org.gradle.api.Task
import org.gradle.api.internal.TaskInternal
import spock.lang.Specification
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.util.ConfigureUtil
import org.gradle.api.internal.project.ProjectInternal

abstract class PluginSpecification extends Specification {

	Project projectWithName(String name) {
		projectBuilderWithName(name).build()
	}

	ProjectBuilder projectBuilderWithName(String name) {
		ProjectBuilder.builder().withName(name)
	}

	ProjectBuilder projectBuilderWithDir(File dir) {
		ProjectBuilder.builder().withProjectDir(dir)
	}

	Project projectWithDirectoryStructure(Closure structure) {
		def tempDir = DirectoryBuilder.tempDirWith(structure)
		projectBuilderWithDir(tempDir).build()
	}

	Project subProjectWithDir(Project parent, File dir) {
		projectBuilderWithDir(dir).withName(dir.name).withParent(parent).build()
	}

	def configure(Object object, Closure closure) {
		ConfigureUtil.configure(closure, object)
	}

	def evaluateProject(Project p) {
		(p as ProjectInternal).evaluate()
	}

	def executeTask(Task t) {
		(t as TaskInternal).execute()
	}
}
