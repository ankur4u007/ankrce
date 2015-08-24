package vic;

import java.net.UnknownHostException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import vic.common.services.init.IInitService;
import vic.util.OsFinderUtil;

public class Launch {

	public static void main(final String[] args) throws UnknownHostException {
		loadAppropriateContext();
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("vic/spring.xml");
		final IInitService initService = ctx.getBean(IInitService.class);
		initService.init();
		ctx.close();
	}

	private static void loadAppropriateContext() {
		final String os = OsFinderUtil.findOs();
		if (OsFinderUtil.WINDOWS.equalsIgnoreCase(os)) {
			System.setProperty("spring.profiles.active", OsFinderUtil.WINDOWS.toLowerCase());
		} else if (OsFinderUtil.LINUX.equalsIgnoreCase(os)) {
			System.setProperty("spring.profiles.active", OsFinderUtil.LINUX.toLowerCase());
		} else if (OsFinderUtil.MAC.equalsIgnoreCase(os)) {
			System.setProperty("spring.profiles.active", OsFinderUtil.MAC.toLowerCase());
		} else {
			System.setProperty("spring.profiles.active", OsFinderUtil.WINDOWS.toLowerCase());
		}
	}
}
