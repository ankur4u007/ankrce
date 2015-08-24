package vic.common.services.alwaysup.portservice;

import java.util.HashSet;
import java.util.Set;

import vic.common.services.alwaysup.enums.PortMappingEnum;

public abstract class AbstractPortService {

	protected Set<Integer> getPortsForEnv(final String env) {
		final Set<Integer> toReturnSet = new HashSet<Integer>();
		for (final PortMappingEnum ports : PortMappingEnum.values()) {
			if (ports.getOs().equalsIgnoreCase(env)) {
				toReturnSet.add(ports.getPort());
			}
		}
		return toReturnSet;
	}
}
