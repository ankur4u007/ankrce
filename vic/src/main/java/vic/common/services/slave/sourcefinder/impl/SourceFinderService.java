package vic.common.services.slave.sourcefinder.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import vic.common.services.slave.sourcefinder.ISourceFinderService;

@Component
public class SourceFinderService implements ISourceFinderService {

	@Override
	public String getSourceIp() {
		String ipToReturn = null;
		try {
			final List<String> aliveNodeList = getAliveNodes();
			if (aliveNodeList != null) {
				for (final String ip : aliveNodeList) {
					if (checkIsAliveSource(ip)) {
						ipToReturn = ip;
						break;
					}
				}
			}
		} catch (final Exception e) {
			System.err.println("Error in getSourceIp:" + e.getMessage());
		}

		if (ipToReturn == null) {
			// if ip is still null, prefer internet
			if (checkIsAliveSource(Mediator)) {
				ipToReturn = Mediator;
			}
		}

		return ipToReturn;
	}

	@Override
	public boolean checkIsAliveSource(final String ip) {
		// TODO Auto-generated method stub
		return false;
	}

	private List<String> getAliveNodes() throws UnknownHostException, IOException, InterruptedException,
			ExecutionException {
		final int timeout = 5000;
		List<Future<String>> futureList = null;
		List<String> toReturnList = null;
		final String hostAddress = InetAddress.getLocalHost().getHostAddress();
		if (!StringUtils.isEmpty(hostAddress)) {
			final String subnet = hostAddress.substring(0, hostAddress.lastIndexOf('.'));
			if (!StringUtils.isEmpty(subnet)) {
				final ExecutorService exs = Executors.newCachedThreadPool();
				futureList = new ArrayList<Future<String>>();
				for (int i = 1; i < 255; i++) {
					final String host = subnet + "." + i;
					futureList.add(exs.submit(new AliveHost(host, timeout)));
				}
			}
		}
		if (futureList != null) {
			toReturnList = new ArrayList<String>();
			for (final Future<String> f : futureList) {
				final String host = f.get();
				if (host != null) {
					toReturnList.add(host);
				}
			}
		}
		return toReturnList;
	}

	private class AliveHost implements Callable<String> {
		private final String host;
		private final int timeout;

		public AliveHost(final String host, final int timeout) {
			super();
			this.host = host;
			this.timeout = timeout;
		}

		@Override
		public String call() throws Exception {
			if (InetAddress.getByName(host).isReachable(timeout)) {
				return host;
			} else {
				return null;
			}
		}
	}
}
