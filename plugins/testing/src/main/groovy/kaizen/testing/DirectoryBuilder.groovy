package kaizen.testing

import org.gradle.util.ConfigureUtil

class DirectoryBuilder {

	static File tempDirWith(Closure<DirectoryBuilder> structure) {
		configure(createTempDir(), structure)
	}

	private static File configure(File root, Closure<DirectoryBuilder> structure) {
		def builder = new DirectoryBuilder(root)
		ConfigureUtil.configure(structure, builder)
		builder.root
	}

	public static File createTempDir() {
		File.createTempFile("kaizen", ".tmp").with {
			it.delete()
			it.mkdir()
			it
		}
	}

	final File root

	DirectoryBuilder(File root) {
		this.root = root
	}

	File dir(String name, Closure children = null) {
		def dir = new File(root, name)
		dir.mkdirs()
		if (children)
			configure(dir, children)
		dir
	}

	File file(String name, String content = '') {
		new File(root, name).with {
			text = content
			it
		}
	}
}
