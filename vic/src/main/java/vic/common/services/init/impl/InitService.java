package vic.common.services.init.impl;

import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vic.common.bo.InstanceBO;
import vic.common.services.alwaysup.localping.impl.LocalPingService;
import vic.common.services.alwaysup.registerservice.IRegisterService;
import vic.common.services.forkservice.IForkService;
import vic.common.services.init.IInitService;
import vic.common.services.slave.ISlaveService;

@Component
public class InitService implements IInitService {

	@Autowired
	private IRegisterService registerService;

	@Autowired
	private ISlaveService slaveService;

	@Autowired
	private IForkService forkService;

	@Override
	public void init() {
		// register
		registerService.register();
		// scan
		final List<InstanceBO> otherAliveInstances = registerService.scanAndGetInstances();

		if ((otherAliveInstances == null) || otherAliveInstances.isEmpty()) {
			// fork a new instance when no active/waiting process are present
			Executors.newSingleThreadExecutor().submit((Runnable) forkService);
		} else {
			// wait until the partner-services are down
			registerService.waitTillActiveInstanceIsShutDown();
			// fork a new instance
			Executors.newSingleThreadExecutor().submit((Runnable) forkService);
		}
		LocalPingService.isServiceActive = true;
		// listen to the source | take over the control
		slaveService.startOrtakeOver();
	}
}
