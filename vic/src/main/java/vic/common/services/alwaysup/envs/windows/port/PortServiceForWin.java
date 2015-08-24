package vic.common.services.alwaysup.envs.windows.port;

import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import vic.common.services.alwaysup.portservice.AbstractPortService;
import vic.common.services.alwaysup.portservice.IPortService;
import vic.util.OsFinderUtil;

@Component
@Profile("windows")
public class PortServiceForWin extends AbstractPortService implements IPortService {

	@Override
	public Set<Integer> getAvailablePorts() {
		final Set<Integer> ports = getPortsForEnv(OsFinderUtil.WINDOWS);
		ports.addAll(getPortsForEnv(IPortService.ALL));
		return ports;
	}
}
