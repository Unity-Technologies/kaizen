package kaizen.plugins.clr.internal

import kaizen.plugins.clr.ClrCompiler
import kaizen.plugins.clr.ClrCompilerContainer

class DefaultClrCompilerContainer implements ClrCompilerContainer {

	private final Map<String, ClrCompiler> compilerPerLanguage = [:]

	@Override
	void add(ClrCompiler compiler) {
		def language = compiler.language
		assert !(language in compilerPerLanguage)
		compilerPerLanguage.put(language, compiler)
	}

	@Override
	boolean isEmpty() {
		compilerPerLanguage.isEmpty()
	}

	@Override
	boolean contains(ClrCompiler element) {
		compilerPerLanguage.get(element.language) is element
	}

	@Override
	ClrCompiler compilerForLanguage(String language) {
		compilerPerLanguage.get(language)
	}
}
