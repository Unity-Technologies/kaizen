package kaizen.plugins.vs

import java.security.MessageDigest

class GuidString {
	static def from(String s) {
		"{${uuidFrom(s).toString().toUpperCase()}}"
	}

	static def uuidFrom(String s) {
		UUID.nameUUIDFromBytes(MessageDigest.getInstance("MD5").digest(s.bytes))
	}
}
