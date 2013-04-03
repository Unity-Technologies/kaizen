package kaizen.plugins.refactorings

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class RenameProjectTask extends DefaultTask {

	def from
	def to

	def RenameProjectTask() {
		description = "Renames a project. Required arguments: -Pfrom=oldName -Pto=newName"
		group = "Refactoring"
	}

	@TaskAction
	def rename() {
		def oldName = from ?: project.properties.from
		def newName = to ?: project.properties.to
		applyProjectRenaming oldName, newName
		def oldTestsName = oldName + '.Tests'
		if (project.findProject(":$oldTestsName")) {
			def newTestsName = newName + '.Tests'
			applyProjectRenaming oldTestsName, newTestsName
		}
	}

	def applyProjectRenaming(String oldName, String newName) {
		File oldDir = project.project(":$oldName").projectDir
		File srcDir = oldDir.parentFile
		File newDir = new File(srcDir, newName)

		// rename project directory
		println "$oldDir -> $newDir"
		oldDir.renameTo newDir

		// rename .gradle file
		new File(newDir, "${oldName}.gradle").renameTo(new File(newDir, "${newName}.gradle"))

		// update project references in every .gradle file
		def projectRefPattern = /('|"|:)${java.util.regex.Pattern.quote(oldName)}('|")/
		def replacementPattern = "\$1$newName\$2"
		srcDir.eachFileRecurse(groovy.io.FileType.FILES) { file ->
			if (!file.name.endsWith('.gradle'))
				return
			fileReplace file, projectRefPattern, replacementPattern
		}
		fileReplace project.rootProject.buildFile, projectRefPattern, replacementPattern
	}

	def fileReplace(File file, pattern, replacementPattern) {
		def oldText = file.text
		def newText = oldText.replaceAll(pattern, replacementPattern)
		if (oldText == newText)
			return
		print "Updating ${file.name}... "
		file.text = newText
		println "done."
	}

}