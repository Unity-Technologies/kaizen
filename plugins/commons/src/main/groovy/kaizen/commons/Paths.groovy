package kaizen.commons

class Paths {
	static String combine(String parent, String... parts) {
		File combined = parts.inject(new File(parent), { current, part -> new File(current, part) })
		combined.canonicalPath
	}
}
