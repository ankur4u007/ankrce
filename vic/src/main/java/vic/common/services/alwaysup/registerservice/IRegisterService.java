package vic.common.services.alwaysup.registerservice;

import java.util.List;

import vic.common.bo.InstanceBO;

public interface IRegisterService {

	public final String localhost = "localhost";

	public void register();

	public List<InstanceBO> scanAndGetInstances();

	public void waitTillActiveInstanceIsShutDown();

}
