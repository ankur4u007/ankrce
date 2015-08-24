package vic.common.services.forkservice.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vic.Launch;
import vic.common.bo.InstanceBO;
import vic.common.services.alwaysup.registerservice.IRegisterService;
import vic.common.services.forkservice.IForkService;
import vic.common.services.forkservice.shellservice.IShellService;

@Component
public class ForkService implements IForkService, Runnable {

	@Autowired
	private IRegisterService registerService;

	@Autowired
	private IShellService shellService;

	private void forkNewProcess() {
		try {
			String jarloc = Launch.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			jarloc = jarloc.substring(1);
			final String[] params = shellService.getCallingParams("javaw", "-jar", jarloc);
			System.out.println("forking:");
			Runtime.getRuntime().exec(params);
		} catch (final URISyntaxException | IOException e) {
			System.err.println("Error in Fork:" + e.getMessage());
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				final List<InstanceBO> otherAliveInstances = registerService.scanAndGetInstances();
				if ((otherAliveInstances == null) || otherAliveInstances.isEmpty()) {
					// fork a new instance when no active/waiting process are
					// present
					forkNewProcess();
				}
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
