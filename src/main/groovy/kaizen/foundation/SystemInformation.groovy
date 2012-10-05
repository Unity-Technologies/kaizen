package kaizen.foundation

class SystemInformation {

	static boolean isWindows() {
		System.properties['os.name'].toString().toLowerCase().contains('windows')
	}
}