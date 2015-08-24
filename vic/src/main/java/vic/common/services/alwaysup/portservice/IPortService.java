package vic.common.services.alwaysup.portservice;

import java.util.Set;

public interface IPortService {

	public final String ALL = "ALL";

	public Set<Integer> getAvailablePorts();

}
