package kaizen.plugins

import org.gradle.api.internal.TaskInternal

class VS2010CSharpProjectSpec extends VSProjectSpecification {

	def projectDir = DirectoryBuilder.tempDirWith {
		dir('Properties') {
			file('AssemblyInfo.cs')
		}
		file('Core.cs')
	}
	def project = projectBuilderWithName('Bundle.Core').withProjectDir(projectDir).build()

	@Override
	def setup() {
		project.apply plugin: 'assembly'
		project.apply plugin: 'vs2010'
	}

	def 'csproj is generated for c# project'() {
		when:
		executeProjectTask()

		then:
		def projectXml = parseProjectFileOf(project)
		projectXml.PropertyGroup[0].RootNamespace.text() == project.name
		projectXml.PropertyGroup[0].AssemblyName.text() == project.name
		projectXml.ItemGroup.Compile.@Include == ['Core.cs', 'Properties\\AssemblyInfo.cs']
	}

	def 'OutputType honors assembly.target'() {
		expect:
		projectOutputTypeFor(target) == outputType

		where:
		target   | outputType
		'exe'    | 'Exe'
		'winexe' | 'WinExe'
		'library'| 'Library'
	}

	String projectOutputTypeFor(String assemblyTarget) {
		project.extensions.assembly.target = assemblyTarget
		executeProjectTask()
		parseProjectFileOf(project).PropertyGroup[0].OutputType.text()
	}

	def executeProjectTask() {
		(project.tasks.vsProject as TaskInternal).execute()
	}
}

