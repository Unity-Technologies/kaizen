package kaizen.plugins.clr

public interface ClrCompilerContainer extends ClrContainer<ClrCompiler> {
	ClrCompiler compilerForLanguage(String language)
}