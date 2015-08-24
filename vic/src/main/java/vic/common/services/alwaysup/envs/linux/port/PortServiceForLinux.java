package vic.common.services.alwaysup.envs.linux.port;

import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import vic.common.services.alwaysup.portservice.AbstractPortService;
import vic.common.services.alwaysup.portservice.IPortService;

@Component
@Profile("linux")
public class PortServiceForLinux extends AbstractPortService implements IPortService {

	@Override
	public Set<Integer> getAvailablePorts() {
		return null;
	}
}
