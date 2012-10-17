package kaizen.plugins

import org.gradle.util.ConfigureUtil

class DirectoryBuilder {

	static File tempDirWith(Closure closure) {
		configure(createTempDir(), closure)
	}

	private static File configure(File root, Closure closure) {
		def builder = new DirectoryBuilder(root)
		ConfigureUtil.configure(closure, builder)
		builder.root
	}

	private static File createTempDir() {
		File.createTempFile("kaizen", ".tmp").with { file ->
			file.delete()
			file.mkdir()
			file
		}
	}

	final File root

	DirectoryBuilder(File root) {
		this.root = root
	}

	File dir(String name, Closure children = null) {
		new File(root, name).with { dir ->
			dir.mkdirs()
			if (children)
				configure(dir, children)
		}
	}

	File file(String name, String content = '') {
		new File(root, name).with { file ->
			file.text = content
			file
		}
	}
}
