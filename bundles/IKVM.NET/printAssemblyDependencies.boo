import Mono.Cecil
import Boo.Lang.PatternMatching

def Main(assemblies as (string)):
  for assembly in assemblies:
    print
    PrintDependenciesOf assembly

def PrintDependenciesOf(assembly as string):
  definition = ModuleDefinition.ReadModule(assembly)
  print "project(':$(definition.Assembly.Name.Name)') {"
  print "\tdependencies {"
  for assemblyRef in definition.AssemblyReferences:
    if IsProvidedAssembly(assemblyRef.Name): continue
    print "\t\t'default' project(':$(assemblyRef.Name)')"
  print "\t}"
  print "}"

def IsProvidedAssembly(assemblyName as string):
  match assemblyName:
    case 'mscorlib' | /System.*/:
      return true
    otherwise:
      return false
