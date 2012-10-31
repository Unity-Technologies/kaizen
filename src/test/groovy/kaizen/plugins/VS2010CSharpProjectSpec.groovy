package kaizen.plugins

import org.gradle.api.internal.TaskInternal

class VS2010CSharpProjectSpec extends PluginSpecification {

	def 'csproj is generated for c# project'() {
		given:
		def projectDir = DirectoryBuilder.tempDirWith {
			dir('Properties') {
				file('AssemblyInfo.cs')
			}
			file('Core.cs')
		}
		def project = projectBuilderWithName('Bundle.Core').withProjectDir(projectDir).build()

		when:
		project.apply plugin: 'assembly'
		project.apply plugin: 'vs2010'
		(project.tasks.vs2010Project as TaskInternal).execute()

		then:
		def projectFile = new File(projectDir, 'Bundle.Core.csproj')
		def projectXml = new XmlParser().parse(projectFile)
		projectXml.PropertyGroup[0].RootNamespace.text() == project.name
		projectXml.PropertyGroup[0].AssemblyName.text() == project.name
		projectXml.ItemGroup.Compile.@Include == ['Core.cs', 'Properties\\AssemblyInfo.cs']
	}
}
