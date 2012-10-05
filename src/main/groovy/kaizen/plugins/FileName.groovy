package kaizen.plugins

class FileName {
	static String withoutExtension(File file) {
		file.name.lastIndexOf('.').with {it != -1 ? file.name[0..<it] : file.name }
	}
}


