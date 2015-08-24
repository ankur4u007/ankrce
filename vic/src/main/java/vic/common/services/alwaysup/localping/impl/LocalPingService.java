package vic.common.services.alwaysup.localping.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

import vic.common.bo.InstanceBO;
import vic.common.services.alwaysup.localping.ILocalPingService;
import vic.common.services.alwaysup.registerservice.IRegisterService;

public class LocalPingService implements ILocalPingService, Callable<InstanceBO> {

	public static final String StatusWaiting = "WAITING";
	public static final String StatusActive = "ACTIVE";
	public static volatile boolean isServiceActive;
	private final boolean isServer;
	private final Integer portToPing;
	private final ServerSocket serverSocket;
	private final String serverName;

	public LocalPingService(final ServerSocket serverSocket, final String serverName, final boolean isServer,
			final Integer portToPing) {
		super();
		this.serverSocket = serverSocket;
		this.serverName = serverName;
		this.isServer = isServer;
		this.portToPing = portToPing;
	}

	@Override
	public InstanceBO call() throws Exception {
		InstanceBO toReturn = null;
		if (isServer) {
			replyPings();
		} else {
			toReturn = getAllInstances();
		}
		return toReturn;
	}

	private InstanceBO getAllInstances() {
		InstanceBO toReturn = null;
		try (Socket socket = new Socket(IRegisterService.localhost, portToPing)) {
			final BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			final String responses[] = inFromServer.readLine().split(":");
			toReturn = new InstanceBO(responses[0], portToPing, responses[1]);
		} catch (final IOException e) {
			System.err.println("Error in getAllInstances:" + e.getMessage());
		}
		return toReturn;
	}

	private void replyPings() {
		System.out.println("Server:" + serverName + " started!");
		while (true) {
			try {
				final Socket socket = serverSocket.accept();
				final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
				if (isServiceActive) {
					outToClient.writeBytes(serverName + ":" + StatusActive);
				} else {
					outToClient.writeBytes(serverName + ":" + StatusWaiting);
				}
			} catch (final Exception ae) {
				System.err.println("Error in replyingPings:" + ae.getMessage());
			}
		}
	}

}
