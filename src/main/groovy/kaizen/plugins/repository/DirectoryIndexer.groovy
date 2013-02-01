package kaizen.plugins.repository

import groovy.xml.MarkupBuilder

class DirectoryIndexer {

	static void index(File dir) {
		writeIndexFileFor(dir)
		indexSubDirsOf(dir)
	}

	static void writeIndexFileFor(File dir) {
		new File(dir, INDEX_FILENAME).withWriter { writer ->
			new MarkupBuilder(writer).html {
				body {
					h1('Directory listing')
					hr()
					pre {
						dir.eachFile {
							if (!shouldBeHidden(it))
								a(href: it.name, it.name)
						}
					}
				}
			}
		}
	}

	private static boolean shouldBeHidden(File file) {
		file.name in HIDDEN_FILES || file.name.startsWith('.') || file.name.endsWith('.gradle')
	}

	static void indexSubDirsOf(File dir) {
		dir.eachDir {
			index it
		}
	}

	static final String INDEX_FILENAME = 'index.html'

	static final Set<String> HIDDEN_FILES = [INDEX_FILENAME, 'gradle', 'gradlew', 'gradlew.bat'].toSet()

}
