package kaizen.plugins.unity.internal

import kaizen.plugins.unity.UnityLocator
import org.gradle.api.Project

class ProjectPropertyUnityLocator implements UnityLocator {

	final Project project
	final UnityLocator fallback

	ProjectPropertyUnityLocator(Project project, UnityLocator fallback) {
		this.project = project
		this.fallback = fallback
	}

	@Override
	String getUnityLocation() {
		project.hasProperty('unityDir') ? project.file(project.property('unityDir')) : fallback.unityLocation
	}
}
