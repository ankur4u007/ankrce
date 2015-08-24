package vic.util;

public class OsFinderUtil {

	public final static String WINDOWS = "WINDOWS";
	public final static String LINUX = "LINUX";
	public final static String MAC = "MAC";
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static String findOs() {
		System.out.println(OS);
		if (isWindows()) {
			return WINDOWS;
		} else if (isMac()) {
			return MAC;
		} else if (isUnix() || isSolaris()) {
			return LINUX;
		}
		return null;
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return ((OS.indexOf("nix") >= 0) || (OS.indexOf("nux") >= 0) || (OS.indexOf("aix") > 0));
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}
}
