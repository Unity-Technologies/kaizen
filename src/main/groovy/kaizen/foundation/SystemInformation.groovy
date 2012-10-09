package kaizen.foundation

class SystemInformation {

	static boolean isWindows() {
		lowerCaseOsName().contains('windows')
	}

	static boolean isMac() {
		lowerCaseOsName().startsWith('mac')
	}

	private static String lowerCaseOsName() {
		System.properties['os.name'].toString().toLowerCase()
	}
}