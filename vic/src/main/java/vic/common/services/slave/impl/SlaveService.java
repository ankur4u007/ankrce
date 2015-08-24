package vic.common.services.slave.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import vic.common.services.slave.ISlaveService;
import vic.common.services.slave.runner.ICommandRunnerService;
import vic.common.services.slave.sourcefinder.ISourceFinderService;

@Component
public class SlaveService implements ISlaveService {

	@Autowired
	private ISourceFinderService sourceFinderService;

	@Autowired
	private ICommandRunnerService commandRunnerService;

	@Override
	public void startOrtakeOver() {
		String sourceIp = null;
		while (true) {
			if ((sourceIp == null) || !sourceFinderService.checkIsAliveSource(sourceIp)) {
				while (true) {
					sourceIp = sourceFinderService.getSourceIp();
					if (StringUtils.isEmpty(sourceIp)) {
						try {
							TimeUnit.SECONDS.sleep(3);
						} catch (final InterruptedException ae) {
							System.err.println("Error in startOrtakeOver:" + ae.getMessage());
						}
					} else {
						break;
					}
				}
			}
			// ping the source IP and wait for command
			final String command = pingAndWaitForCommand(sourceIp);
			if (!StringUtils.isEmpty(command)) {
				final String response = commandRunnerService.runCommand(command);
				if (!StringUtils.isEmpty(response)) {
					while (true) {
						if (sourceFinderService.checkIsAliveSource(sourceIp)) {
							if (isResponseSentToSource(sourceIp, response)) {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		}
	}

	private String pingAndWaitForCommand(final String sourceIp) {
		return sourceIp;
	}

	private boolean isResponseSentToSource(final String sourceIp, final String response) {
		return false;
	}
}
