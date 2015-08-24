package vic.common.services.alwaysup.enums;

import vic.common.services.alwaysup.portservice.IPortService;

public enum PortMappingEnum {

	PORT1(11960, IPortService.ALL, "Alpha"), PORT2(11961, IPortService.ALL, "Charlie"), PORT3(11962, IPortService.ALL,
			"Bomber");
	/**
	 * , PORT4(11963, IPortService.ALL), PORT5(11964, IPortService.ALL),
	 * PORT6(11965, IPortService.ALL), PORT7(11966, IPortService.ALL),
	 * PORT8(11967, IPortService.ALL), PORT9(11968, IPortService.ALL);
	 */

	private int port;
	private String os;
	private String name;

	private PortMappingEnum(final int port, final String os, final String name) {
		this.port = port;
		this.os = os;
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public String getOs() {
		return os;
	}

	public String getName() {
		return name;
	}

	public static PortMappingEnum getByPortNumber(final Integer port) {
		for (final PortMappingEnum portEnum : PortMappingEnum.values()) {
			if (portEnum.getPort() == port) {
				return portEnum;
			}
		}
		return null;
	}

}
