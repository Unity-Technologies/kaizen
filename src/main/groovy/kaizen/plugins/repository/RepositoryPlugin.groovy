package kaizen.plugins.repository

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.util.ConfigureUtil

class RepositoryPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		configure(project) {
			task('index') <<{
				DirectoryIndexer.index(project.projectDir)
			}
			defaultTasks 'index'
		}
	}

	void configure(Project project, Closure closure) {
		ConfigureUtil.configure(closure, project)
	}
}
