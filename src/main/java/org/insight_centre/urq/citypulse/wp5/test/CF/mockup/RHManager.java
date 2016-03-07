package org.insight_centre.urq.citypulse.wp5.test.CF.mockup;

import java.util.logging.Logger;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;
import org.insight_centre.urq.citypulse.wp5.Configuration;

import citypulse.commons.data.CoordinateParseException;

/**
 * @author Thu-Le Pham
 * @organization INSIGHT@NUIG, Galway, Ireland
 * @email thule.pham@insight-centre.org
 */
public class RHManager {

	/**
	 *
	 */
	public static Logger Log = Logger.getLogger(RHManager.class.getPackage()
			.getName());

	/**
	 * @param args
	 * @throws CoordinateParseException
	 */
	public static void main(final String[] args)
			throws CoordinateParseException {
		// subscribeEvents();
		new RHManager().startRH(Configuration.getInstance().getHostName(),
				Configuration.getInstance().getPort(), Configuration
						.getInstance().getRootPath());
	}

	/**
	 * @param hostName
	 * @param port
	 * @param rootPath
	 */
	private void startRH(final String hostName, final int port,
			final String rootPath) {

		RHManager.Log.info("started " + this.getClass().getName());

		final Server rhServer = new Server(hostName, port, rootPath, null,
				ContextualEventsRequestEndpointMockup.class);

		// try {
		try {
			rhServer.start();
			while (true) {
				Thread.sleep(1000);
			}
		} catch (final DeploymentException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			rhServer.stop();
		}

	}

}
