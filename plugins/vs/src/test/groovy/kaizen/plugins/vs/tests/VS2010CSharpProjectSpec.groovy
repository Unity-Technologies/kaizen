package kaizen.plugins.vs.tests

import kaizen.plugins.assembly.AssemblyPlugin
import kaizen.plugins.vs.VS2010Plugin
import kaizen.testing.DirectoryBuilder

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
		project.plugins.apply AssemblyPlugin
		project.plugins.apply VS2010Plugin
	}

	def 'csproj is generated for c# project'() {
		when:
		def projectXml = loadProjectFileOf(project)

		then:
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

	def 'TargetFrameworkVersion honors assembly.targetFrameworkVersion'() {
		expect:
		targetFrameworkVersionFor(assemblyTargetFrameworkVersion) == projectTargetFrameworkVersion

		where:
		assemblyTargetFrameworkVersion  | projectTargetFrameworkVersion
		'v3.5' 													| 'v3.5'
		'v4.0'													| 'v4.0'
	}

	String projectOutputTypeFor(String assemblyTarget) {
		project.extensions.assembly.target = assemblyTarget
		loadProjectFileOf(project).PropertyGroup[0].OutputType.text()
	}

	String targetFrameworkVersionFor(String targetVersion) {
		project.extensions.assembly.targetFrameworkVersion = targetVersion
		loadProjectFileOf(project).PropertyGroup[0].TargetFrameworkVersion.text()
	}
}

