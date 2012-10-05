package kaizen.foundation

class Paths {
	static String combine(String parent, String... parts) {
		parts.inject(new File(parent), { current, part -> new File(current, part) })
	}
}
