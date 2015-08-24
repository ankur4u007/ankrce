package vic.common.services.alwaysup.registerservice.impl;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vic.common.bo.InstanceBO;
import vic.common.services.alwaysup.enums.PortMappingEnum;
import vic.common.services.alwaysup.localping.impl.LocalPingService;
import vic.common.services.alwaysup.portservice.IPortService;
import vic.common.services.alwaysup.registerservice.IRegisterService;

@Component
public class RegisterService implements IRegisterService {

	@Autowired
	private IPortService portService;

	private Integer registeredPort;

	@Override
	public void register() {
		final Set<Integer> ports = portService.getAvailablePorts();
		ServerSocket connectionSocket = null;
		for (final Integer port : ports) {
			try {
				connectionSocket = new ServerSocket(port);
				registeredPort = port;
				break;
			} catch (final Exception ae) {
				System.err.println("Error in registeringPorts:" + ae.getMessage());
			}
		}

		Executors.newSingleThreadExecutor().submit(
				new LocalPingService(connectionSocket, PortMappingEnum.getByPortNumber(registeredPort).getName(), true,
						null));
	}

	@Override
	public List<InstanceBO> scanAndGetInstances() {
		final Set<Integer> ports = portService.getAvailablePorts();
		if (registeredPort != null) {
			ports.remove(registeredPort);
		}
		final ExecutorService es = Executors.newCachedThreadPool();
		final List<Future<InstanceBO>> scanList = new ArrayList<Future<InstanceBO>>();
		for (final Integer port : ports) {
			scanList.add(es.submit(new LocalPingService(null, null, false, port)));
		}
		final List<InstanceBO> toReturnList = new ArrayList<InstanceBO>(scanList.size());
		for (final Future<InstanceBO> future : scanList) {
			try {
				final InstanceBO bo = future.get();
				if (bo != null) {
					toReturnList.add(bo);
				}
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("Error in scanAndGetInstances:" + e.getMessage());
			}
		}
		return toReturnList;
	}

	@Override
	public void waitTillActiveInstanceIsShutDown() {
		String activeServer = null;
		while (true) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				final List<InstanceBO> nodesList = scanAndGetInstances();
				boolean isAnyActive = false;
				for (final InstanceBO bo : nodesList) {
					if (LocalPingService.StatusActive.equalsIgnoreCase(bo.getStatus())) {
						isAnyActive = true;
						activeServer = bo.getName();
						break;
					}
				}
				if (!isAnyActive) {
					System.out.println("server:" + activeServer + " Shutdown Detected!");
					break;
				}
			} catch (final InterruptedException e) {
				System.err.println("Error in waitTillActiveInstanceIsShutDown:" + e.getMessage());

			}
		}
	}
}
