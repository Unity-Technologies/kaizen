package kaizen.foundation

class SystemInformation {

	public static final String WINDOWS = 'windows'
	public static final String MAC = 'mac'
	public static final String UNIX = 'unix'

	static String getSystemFamily() {
		def osName = lowerCaseOsName()
		if (osName.contains('windows'))
			return WINDOWS
		if (osName.startsWith('mac'))
			return MAC
		return UNIX
	}

	static boolean isWindows() {
		systemFamily == WINDOWS
	}

	static boolean isMac() {
		systemFamily == MAC
	}

	private static String lowerCaseOsName() {
		System.properties['os.name'].toString().toLowerCase()
	}
}